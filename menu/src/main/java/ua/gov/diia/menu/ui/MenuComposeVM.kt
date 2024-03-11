package ua.gov.diia.menu.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.di.actions.GlobalActionDocLoadingIndicator
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.menu.MenuContentController
import ua.gov.diia.menu.R
import ua.gov.diia.menu.ui.MenuActionsKey.COPY_DEVICE_UID
import ua.gov.diia.menu.ui.MenuActionsKey.LOGOUT
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_ABOUT_DIIA
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_APP_SESSIONS
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_DIIA_ID
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_FAQ
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_HELP
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_NOTIFICATION
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_PLAY_MARKET
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_POLICY
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_SETTINGS
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_SIGNE_HISTORY
import ua.gov.diia.menu.ui.MenuActionsKey.OPEN_SUPPORT
import ua.gov.diia.menu.ui.MenuActionsKey.SHARE_APP
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import javax.inject.Inject

@HiltViewModel
class MenuComposeVM @Inject constructor(
    @GlobalActionLogout private val actionLogout: MutableLiveData<UiEvent>,
    @GlobalActionDocLoadingIndicator val globalActionDocLoadingIndicator: MutableSharedFlow<UiDataEvent<Boolean>>,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val diiaStorage: DiiaStorage,
    private val withBuildConfig: WithBuildConfig,
    private val menuContentController: MenuContentController,
    private val notificationController: NotificationController,
) : WithErrorHandlingOnFlow by errorHandling, WithRetryLastAction by retryLastAction,
    ViewModel() {

    private val _topBarData = mutableStateListOf<UIElementData>()
    val topBarData: SnapshotStateList<UIElementData> = _topBarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _hasUnreadNotifications = MutableStateFlow(false)

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _settingsAction = MutableSharedFlow<UiDataEvent<MenuAction>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val settingsAction = _settingsAction.asSharedFlow()

    init {
        viewModelScope.launch {
            notificationController.collectUnreadNotificationCounts {
                val hasUnreadNotification = it != 0
                val previousBadgeValue = _hasUnreadNotifications.value
                _hasUnreadNotifications.value = hasUnreadNotification

                if (hasUnreadNotification != previousBadgeValue) {
                    updateMenuMessageItemData(hasUnreadNotification)
                }
            }
        }
    }

    fun logoutApprove() {
        actionLogout.value = UiEvent()
    }

    private suspend fun selectMenuAction(menuAction: MenuAction) {
        if (menuAction == MenuAction.CopyDeviceUid) {
            copyDeviceUid()
        } else {
            _settingsAction.emit(UiDataEvent(menuAction))
        }
    }

    fun onUIAction(event: UIAction) {
        executeActionOnFlow {
            when (event.actionKey) {
                UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                    _navigation.tryEmit(BaseNavigation.Back)
                }

                UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                    event.action?.type?.let {
                        when (it) {
                            OPEN_NOTIFICATION -> {
                                _navigation.tryEmit(MenuTabNavigation.NavigateToNotifications)
                            }

                            else -> {
                                getMenuActionForString(it)?.let { menuAction ->
                                    selectMenuAction(menuAction)
                                }
                            }
                        }
                    }
                }

                LOGOUT -> {
                    selectMenuAction(MenuAction.Logout)
                }
            }
        }
    }

    private fun copyDeviceUid() {
        viewModelScope.launch {
            val uid = diiaStorage.getMobileUuid()
            _settingsAction.emit(UiDataEvent(MenuAction.DoCopyDeviceUid(uid)))
        }
    }

    fun configureTopBar() {
        val toolbar = TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Меню"),
                label = UiText.StringResource(R.string.version_diia, withBuildConfig.getVersionName())
            )
        )
        _topBarData.addIfNotNull(toolbar)
    }

    private fun getMenuActionForString(
        actionKey: String
    ): MenuAction? {
        return when (actionKey) {
            OPEN_PLAY_MARKET -> MenuAction.OpenPlayMarketAction
            OPEN_HELP -> MenuAction.OpenHelpAction
            OPEN_DIIA_ID -> MenuAction.OpenDiiaId
            OPEN_SIGNE_HISTORY -> MenuAction.OpenSignHistory
            OPEN_APP_SESSIONS -> MenuAction.OpenAppSessions
            OPEN_SUPPORT -> MenuAction.OpenSupportAction
            OPEN_FAQ -> MenuAction.OpenFAQAction
            SHARE_APP -> MenuAction.ShareApp
            OPEN_SETTINGS -> MenuAction.OpenSettings
            LOGOUT -> MenuAction.Logout
            OPEN_ABOUT_DIIA -> MenuAction.AboutDiia
            OPEN_POLICY -> MenuAction.OpenPolicyLink
            COPY_DEVICE_UID -> MenuAction.CopyDeviceUid
            else -> null
        }
    }

    fun configureBody() {
        _bodyData.addAll(menuContentController.configureBody(_hasUnreadNotifications.value))
    }

    fun showDataLoadingIndicator(load: Boolean) {
        viewModelScope.launch {
            globalActionDocLoadingIndicator.emit(UiDataEvent(load))
        }
    }

    private fun updateMenuMessageItemData(hasUnreadNotification: Boolean) {
        bodyData.forEachIndexed { rootIndex, item ->
            if (item is ListItemGroupOrgData) {
                item.itemsList.forEachIndexed { indexItemList, listItem ->
                    if (listItem.id == OPEN_NOTIFICATION) {
                        updateBodyByIndex(rootIndex, indexItemList, hasUnreadNotification)
                        return
                    }
                }
            }
        }
    }

    private fun updateBodyByIndex(
        rootIndex: Int,
        indexItemList: Int,
        hasUnreadNotification: Boolean
    ) {
        (bodyData.getOrNull(rootIndex) as? ListItemGroupOrgData)?.let {
            val list = it.itemsList.toMutableList()
            val item = list[indexItemList].copy(iconLeft = UiIcon.DrawableResource(
                if (hasUnreadNotification) {
                    CommonDiiaResourceIcon.NEW_MESSAGE.code
                } else {
                    CommonDiiaResourceIcon.NOTIFICATION_MESSAGE.code
                }
            ))
            list.removeAt(indexItemList)
            list.add(indexItemList, item)
            val organism = it.copy(itemsList = SnapshotStateList<ListItemMlcData>().apply {
                addAll(list)
            })
            bodyData.removeAt(rootIndex)
            bodyData.add(rootIndex, organism)
        }
    }
}

sealed class MenuTabNavigation : NavigationPath {
    object NavigateToNotifications : MenuTabNavigation()
}