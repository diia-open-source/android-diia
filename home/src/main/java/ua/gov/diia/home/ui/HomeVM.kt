package ua.gov.diia.home.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.gov.diia.core.controller.DeeplinkProcessor
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.controller.PromoController
import ua.gov.diia.core.di.actions.GlobalActionAllowAuthorizedLinks
import ua.gov.diia.core.di.actions.GlobalActionConfirmDocumentRemoval
import ua.gov.diia.core.di.actions.GlobalActionDocLoadingIndicator
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository
import ua.gov.diia.home.R
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val promoController: PromoController,
    private val notificationController: NotificationController,
    private val itnDataSource: ItnDataRepository,
    private val dispatcherProvider: DispatcherProvider,
    @GlobalActionAllowAuthorizedLinks val allowAuthorizedLinksFlow: MutableSharedFlow<UiDataEvent<Boolean>>,
    @GlobalActionDocLoadingIndicator val globalActionDocLoadingIndicator: MutableSharedFlow<UiDataEvent<Boolean>>,
    @GlobalActionConfirmDocumentRemoval val globalActionConfirmDocumentRemoval: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionFocusOnDocument val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
    private val withRetryLastAction: WithRetryLastAction,
    private val errorHandlingDelegate: WithErrorHandling,
    private val deepLinkDelegate: WithDeeplinkHandling,
    private val composeMapper: HomeScreenComposeMapper,
    private val deeplinkProcessor: DeeplinkProcessor,
    private val withCrashlytics: WithCrashlytics,
) : ViewModel(),
    WithErrorHandling by errorHandlingDelegate,
    WithRetryLastAction by withRetryLastAction,
    WithDeeplinkHandling by deepLinkDelegate,
    HomeScreenComposeMapper by composeMapper {

    private val _bottomData = mutableStateListOf<UIElementData>()
    val bottomData: SnapshotStateList<UIElementData> = _bottomData

    private var _processNavigation = MutableLiveData<UiDataEvent<NavDirections>>()
    val processNavigation: LiveData<UiDataEvent<NavDirections>>
        get() = _processNavigation

    private val _showTemplate = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    val showTemplate: LiveData<UiDataEvent<TemplateDialogModel>>
        get() = _showTemplate

    private val _processCode = MutableLiveData<Int>()

    private val _hasUnreadNotifications = MediatorLiveData<Boolean?>(null)
    val hasUnreadNotifications: LiveData<Boolean?>
        get() = _hasUnreadNotifications

    val selectedMenuItem: LiveData<UiDataEvent<HomeMenuItemConstructor>?> =
        globalActionSelectedMenuItem.asLiveData()

    val isLoadIndicatorHomeScreen: LiveData<Boolean> =
        globalActionDocLoadingIndicator.asLiveData().map {
            it.getContentIfNotHandled() ?: false
        }

    private val _nonce = MutableLiveData<String?>()
    val nonce: LiveData<String?>
        get() = _nonce

    private var selectedTabId: String? = null

    private var _notificationsRequested = MutableLiveData<UiDataEvent<Boolean>>()
    val notificationsRequested: LiveData<UiDataEvent<Boolean>>
        get() = _notificationsRequested

    init {
        viewModelScope.launch {
            notificationController.getNotificationsInitial()
            notificationController.collectUnreadNotificationCounts {
                val hasUnreadNotification = it != 0
                val previousBadgeValue = _hasUnreadNotifications.value
                _hasUnreadNotifications.postValue(hasUnreadNotification)

                if (hasUnreadNotification != previousBadgeValue) {
                    updateMenuTabItemData(hasUnreadNotification)
                }
            }
        }

        checkPromo()

        invalidateDataSource()

        //selects initial menu item
        configureBottomTabBar()
        setSelectedMenuItem(HomeMenuItem.FEED)
        checkPushTokenInSync()
    }

    private fun configureBottomTabBar() {
        val tabFeed = TabItemMoleculeData(
            label = "Стрічка",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_feed_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_feed_unselected),
            actionKey = HomeActions.HOME_FEED.toString(),
            id = HomeActions.HOME_FEED.toString()
        )
        val tabDocuments = TabItemMoleculeData(
            label = "Документи",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
            actionKey = HomeActions.HOME_DOCUMENTS.toString(),
            id = HomeActions.HOME_DOCUMENTS.toString()
        )
        val tabServices = TabItemMoleculeData(
            label = "Сервіси",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_services_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_services_unselected),
            actionKey = HomeActions.HOME_SERVICES.toString(),
            id = HomeActions.HOME_SERVICES.toString()
        )
        val tabMenu = TabItemMoleculeData(
            label = "Меню",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_menu_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_menu_unselected),
            iconSelectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_selected_badge),
            iconUnselectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_unselected_badge),
            actionKey = HomeActions.HOME_MENU.toString(),
            id = HomeActions.HOME_MENU.toString(),
            showBadge = hasUnreadNotifications.value ?: false
        )
        val tabs = listOf(tabFeed, tabDocuments, tabServices, tabMenu)
        val resultList = tabs.toComposeTabBarOrganism()

        _bottomData.addIfNotNull(resultList)
    }

    fun onUIAction(event: UIAction) {
        executeAction {
            when (event.actionKey) {
                HomeActions.HOME_MENU.toString() -> setSelectedMenuItem(HomeMenuItem.MENU)
                HomeActions.HOME_DOCUMENTS.toString() -> setSelectedMenuItem(HomeMenuItem.DOCUMENTS)
                HomeActions.HOME_SERVICES.toString() -> setSelectedMenuItem(HomeMenuItem.SERVICES)
                HomeActions.HOME_FEED.toString() -> setSelectedMenuItem(HomeMenuItem.FEED)

            }
        }
    }

    private fun onTabChoice(data: String) {
        selectedTabId = data
        val index = _bottomData.indexOfFirst {
            it is TabBarOrganismData
        }
        if (index == -1) {
            return
        } else {
            _bottomData[index] =
                (_bottomData[index] as TabBarOrganismData).onTabClicked(selectedTabId)
        }
    }

    private fun setSelectedMenuItem(menuItem: HomeMenuItemConstructor) {
        viewModelScope.launch {
            globalActionSelectedMenuItem.emit(UiDataEvent(menuItem))
            onTabChoice(menuItem.position.toString())
        }
    }

    private fun invalidateDataSource() {
        viewModelScope.launch {
            try {
                notificationController.invalidateNotificationDataSource()
                itnDataSource.invalidate()
            } catch (e: Exception) {
                withCrashlytics.sendNonFatalError(e)
            }
        }
    }
    fun confirmRemoveDocFromGallery(docName: String) {
        viewModelScope.launch {
            globalActionConfirmDocumentRemoval.emit(UiDataEvent(docName))
        }
    }

    fun showDataLoadingIndicator(load: Boolean) {
        viewModelScope.launch {
            globalActionDocLoadingIndicator.emit(UiDataEvent(load))
        }
    }

    private fun checkPushTokenInSync() {
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {

            try {
                notificationController.checkPushTokenInSync()
            } catch (exc: Exception) {
                withCrashlytics.sendNonFatalError(exc)
            }
        }
    }

    private fun checkPromo() {
        viewModelScope.launch {
            try {
                promoController.checkPromo {promoTemplate ->
                    promoTemplate.processCode.let { _processCode.value = it }
                    _showTemplate.value = UiDataEvent(promoTemplate.template)
                }
            } catch (e: Exception) {
                withCrashlytics.sendNonFatalError(e)
            }
        }
    }


    fun updatePromoProcessCode() {
        viewModelScope.launch {
            _processCode.value?.let { promoController.updatePromoProcessCode(it) }
        }
    }

    fun subscribeToBetaByCode() {
        viewModelScope.launch {
            try {
                promoController.subscribeToBetaByCode(_processCode.value)
            } catch (e: Exception) {
                withCrashlytics.sendNonFatalError(e)
            }
        }
    }

    fun allowAuthorizedDeepLinks() {
        viewModelScope.launch {
            allowAuthorizedLinksFlow.emit(UiDataEvent(true))
        }
    }

    fun checkNotificationsRequested() {
        viewModelScope.launch {
            notificationController.checkNotificationsRequested()?.let {
                _notificationsRequested.value = UiDataEvent(it)
            }
        }
    }

    fun allowNotifications() {
        viewModelScope.launch {
            notificationController.allowNotifications()
        }
    }

    fun denyNotifications() {
        viewModelScope.launch {
            notificationController.denyNotifications()
        }
    }

    private fun updateMenuTabItemData(hasUnreadNotification: Boolean) {
        _bottomData.findAndChangeFirstByInstance<TabBarOrganismData> { tabOrganism ->

            val menuItem = tabOrganism.tabs.find { it.id == HomeActions.HOME_MENU.toString()} ?: return@findAndChangeFirstByInstance tabOrganism
            val index = tabOrganism.tabs.indexOfLast { it.id ==  HomeActions.HOME_MENU.toString() }
            tabOrganism.tabs[index] = menuItem.copy(showBadge = hasUnreadNotification)
            tabOrganism
        }
    }

    fun focusOnDocumentType(documentType: String) {
        viewModelScope.launch {
            globalActionFocusOnDocument.emit(UiDataEvent(documentType))
        }
    }

    suspend fun handleDeepLinks() {
        deeplinkFlow.collectLatest {
            it?.getContentIfNotHandled()?.let { action ->
                deeplinkProcessor.handleDeepLinkAction(action)?.let {route ->
                    _processNavigation.value = UiDataEvent(route)
                }
            }
        }
    }
}