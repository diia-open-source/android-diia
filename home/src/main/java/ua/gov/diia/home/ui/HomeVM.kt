package ua.gov.diia.home.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.fragment.app.Fragment
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.gov.diia.core.controller.DeeplinkProcessor
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.di.actions.GlobalActionAllowAuthorizedLinks
import ua.gov.diia.core.di.actions.GlobalActionConfirmDocumentRemoval
import ua.gov.diia.core.di.actions.GlobalActionDocLoadingIndicator
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.navigation.HomeNavigation
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.ui.gallery.DocGalleryNavigationHelper
import ua.gov.diia.feed.helper.FeedHelper
import ua.gov.diia.home.R
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.menu.helper.MenuHelper
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val notificationController: NotificationController,
    private val dispatcherProvider: DispatcherProvider,
    @GlobalActionAllowAuthorizedLinks val allowAuthorizedLinksFlow: MutableSharedFlow<UiDataEvent<Boolean>>,
    @GlobalActionDocLoadingIndicator val globalActionDocLoadingIndicator: MutableSharedFlow<UiDataEvent<Boolean>>,
    @GlobalActionConfirmDocumentRemoval val globalActionConfirmDocumentRemoval: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
    private val withRetryLastAction: WithRetryLastAction,
    private val documentsDataSource: DocumentsDataRepository,
    private val itnDataSource: ItnDataRepository,
    private val errorHandlingDelegate: WithErrorHandling,
    private val deepLinkDelegate: WithDeeplinkHandling,
    private val composeMapper: HomeScreenComposeMapper,
    private val deeplinkProcessor: DeeplinkProcessor,
    private val withCrashlytics: WithCrashlytics,
    private val feedHelper: FeedHelper,
    private val publicServiceHelper: PublicServiceHelper,
    private val menuHelper: MenuHelper,
    private val documentsHelper: DocumentsHelper,
    val navigationSubscriptionHandler: DocGalleryNavigationHelper,
) : ViewModel(),
    WithErrorHandling by errorHandlingDelegate,
    WithRetryLastAction by withRetryLastAction,
    WithDeeplinkHandling by deepLinkDelegate,
    HomeScreenComposeMapper by composeMapper,
    FeedHelper by feedHelper,
    PublicServiceHelper by publicServiceHelper,
    MenuHelper by menuHelper,
    DocumentsHelper by documentsHelper {

    private val _bottomData = mutableStateListOf<UIElementData>()
    val bottomData: SnapshotStateList<UIElementData> = _bottomData

    private val _bottomBar = MutableStateFlow<TabBarOrganismData?>(null)
    val bottomBar: StateFlow<TabBarOrganismData?> = _bottomBar.asStateFlow()

    private var _processNavigation = MutableLiveData<UiDataEvent<NavDirections>>()
    val processNavigation: LiveData<UiDataEvent<NavDirections>>
        get() = _processNavigation

    private val _showTemplate = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    val showTemplate: LiveData<UiDataEvent<TemplateDialogModel>>
        get() = _showTemplate

    private val _hasUnreadNotifications = MediatorLiveData<Boolean?>(null)
    val hasUnreadNotifications: LiveData<Boolean?>
        get() = _hasUnreadNotifications

    val selectedMenuItem = globalActionSelectedMenuItem.asSharedFlow()

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

    /**
     * Flow that emits back stack navigation actions from HomeF to child tabs.
     * HomeF register all events in appropriate callback methods (for instance registerForNavigationResult)
     * and emits them into this flow
     */
    val navigationBackStackEventFlow = mutableSharedFlowOf<BackStackEvent>()

    /**
     * Flow that collects navigation actions that were emitted from child tabs (Feed, Doc, PC, Menu)
     */
    val homeNavigationActionFlow = mutableSharedFlowOf<HomeNavigation>()

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

        configureBottomTabBar()
        checkPushTokenInSync()
        viewModelScope.launch {
            itnDataSource.invalidate()
        }
    }

    private fun configureBottomTabBar() {
        val tabFeed = TabItemMoleculeData(
            label = "Стрічка",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_feed_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_feed_unselected),
            actionKey = HomeActions.HOME_FEED.toString(),
            id = HomeActions.HOME_FEED.toString(),
            componentId = UiText.StringResource(R.string.home_tab_feed_test_tag),
            selectionState = UIState.Selection.Selected
        )
        val tabDocuments = TabItemMoleculeData(
            label = "Документи",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
            actionKey = HomeActions.HOME_DOCUMENTS.toString(),
            id = HomeActions.HOME_DOCUMENTS.toString(),
            componentId = UiText.StringResource(R.string.home_tab_documents_test_tag)
        )
        val tabServices = TabItemMoleculeData(
            label = "Сервіси",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_services_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_services_unselected),
            actionKey = HomeActions.HOME_SERVICES.toString(),
            id = HomeActions.HOME_SERVICES.toString(),
            componentId = UiText.StringResource(R.string.home_tab_services_test_tag)
        )
        val tabMenu = TabItemMoleculeData(
            label = "Меню",
            iconSelected = UiText.StringResource(R.drawable.ic_tab_menu_selected),
            iconUnselected = UiText.StringResource(R.drawable.ic_tab_menu_unselected),
            iconSelectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_selected_badge),
            iconUnselectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_unselected_badge),
            actionKey = HomeActions.HOME_MENU.toString(),
            id = HomeActions.HOME_MENU.toString(),
            showBadge = hasUnreadNotifications.value ?: false,
            componentId = UiText.StringResource(R.string.home_tab_menu_test_tag)
        )

        _bottomBar.tryEmit(
            TabBarOrganismData(
                componentId = UiText.StringResource(R.string.home_tab_bar_test_tag),
                tabs = SnapshotStateList<TabItemMoleculeData>().apply {
                    add(tabFeed)
                    add(tabDocuments)
                    add(tabServices)
                    add(tabMenu)
                }
            )
        )
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.MENU_ITEM_CLICK -> {
                when (event.data) {
                    HomeActions.HOME_MENU.toString() -> setSelectedMenuItem(HomeMenuItem.MENU)
                    HomeActions.HOME_DOCUMENTS.toString() -> setSelectedMenuItem(HomeMenuItem.DOCUMENTS)
                    HomeActions.HOME_SERVICES.toString() -> setSelectedMenuItem(HomeMenuItem.SERVICES)
                    HomeActions.HOME_FEED.toString() -> setSelectedMenuItem(HomeMenuItem.FEED)
                }
            }
        }

    }

    fun setSelectedMenuItem(menuItem: HomeMenuItemConstructor) {
        globalActionSelectedMenuItem.tryEmit(UiDataEvent(menuItem))
        _bottomBar.tryEmit(_bottomBar.value?.onTabClicked(menuItem.position.toString()))
    }

    //TODO DISCUSS for what?
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
        val menuIndex =
            _bottomBar.value?.tabs?.indexOfLast { tab -> tab.id == HomeActions.HOME_MENU.toString() }
        _bottomBar.value = _bottomBar.value?.copy(
            tabs = SnapshotStateList<TabItemMoleculeData>().apply {
                _bottomBar.value?.tabs?.forEachIndexed { index, item ->
                    if (index == menuIndex) {
                        add(item.copy(showBadge = hasUnreadNotification))
                    } else {
                        add(item)
                    }
                }
            }
        )
    }

    suspend fun handleDeepLinks() {
        deeplinkFlow.collectLatest {
            it?.getContentIfNotHandled()?.let { action ->
                deeplinkProcessor.handleDeepLinkAction(action).let { route ->
                    if (route == null) {
                        setSelectedMenuItem(HomeMenuItem.DOCUMENTS)
                    } else {
                        _processNavigation.value = UiDataEvent(route)
                    }
                }
            }
        }
    }

    fun handleStartFlowDeeplink(deeplink: DeepLinkActionStartFlow) {
        viewModelScope.launch {
            deeplinkProcessor.handleDeepLinkAction(deeplink)?.let { route ->
                _processNavigation.value = UiDataEvent(route)
            }
        }
    }

    //in case it's first launch after login, we should do fetch user docs data
    fun handleNavigationFlowArgs(launchFlow: ConsumableString?) {
        launchFlow?.consumeEvent { flow ->
            if (flow == ActionsConst.ACTION_AUTH_FLOW) {
                fetchDocs()
            }
        }
    }

    fun fetchDocs() {
        documentsDataSource.invalidate()
    }

    suspend fun getNavDirectionForNotification(notification: PullNotificationItemSelection) =
        notificationController.getNavDirectionForNotification(notification)

    suspend fun markNotificationAsRead(notification: PullNotificationItemSelection) =
        notificationController.markAsRead(notification.notificationId)

    fun isMessageNotification(resourceType: String): Boolean {
        return notificationController.isMessageNotification(resourceType)
    }

    override fun navigateToNotifications(fragment: Fragment) {
        feedHelper.navigateToNotifications(fragment)
    }
}