package ua.gov.diia.notifications.ui.fragments.home.notifications.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.LoadingState
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsActionKey.OPEN_NOTIFICATION_SETTINGS
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsActionKey.REMOVE_NOTIFICATION
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsActionKey.SELECT_NOTIFICATION
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.PAGE_LOADING_LINEAR_PAGINATION
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.PAGE_LOADING_TRIDENT
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.message.MessageMoleculeData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.MessageListOrganismData
import ua.gov.diia.ui_base.navigation.BaseNavigation
import javax.inject.Inject

@HiltViewModel
class NotificationComposeVM @Inject constructor(
    private val notificationsDataSource: NotificationDataRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val composeMapper: NotificationsMapperCompose,
    private val retryLastAction: WithRetryLastAction,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val notificationHelper: NotificationHelper
) : WithErrorHandlingOnFlow by errorHandling, WithRetryLastAction by retryLastAction,
    NotificationsMapperCompose by composeMapper, ViewModel() {

    private val _topBarData = mutableStateListOf<UIElementData>()
    val topBarData: SnapshotStateList<UIElementData> = _topBarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _navigateTo = MutableSharedFlow<NavDirections>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigateTo = _navigateTo.asSharedFlow()
    private val _contentLoadedKey = MutableStateFlow("")
    private val _contentLoaded = MutableStateFlow(false)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to value
        }

    private val _progressIndicatorKey = MutableStateFlow("")
    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator: Flow<Pair<String, Boolean>> =
        _progressIndicator.combine(_progressIndicatorKey) { value, key ->
            key to value
        }

    private var _onMessageNotificationSelected =
        MutableSharedFlow<UiDataEvent<PullNotificationItemSelection>>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val onMessageNotificationSelected = _onMessageNotificationSelected.asSharedFlow()

    private var _openResource = MutableSharedFlow<UiDataEvent<PullNotificationItemSelection>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val openResource = _openResource.asSharedFlow()

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TITLE_GROUP_MLC -> {
                event.action?.let {
                    when (it.type) {
                        UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                            _navigation.tryEmit(BaseNavigation.Back)
                        }

                        OPEN_NOTIFICATION_SETTINGS -> {
                            _navigation.tryEmit(NotificationsNavigation.NavigateToNotificationsSettings)
                        }

                        else -> {}
                    }
                }
            }

            REMOVE_NOTIFICATION -> {
                event.data?.let {
                    removeNotification(it)
                }
            }

            SELECT_NOTIFICATION -> {
                executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
                    _contentLoadedKey.tryEmit(UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION)
                }) {
                    val notification = event.data?.let {
                        notificationsDataSource.getPullNotificationById(it)
                    }
                    val notificationItemSelection =
                        notification?.pullNotificationMessage?.action?.type?.let {
                            PullNotificationItemSelection(
                                notificationId = notification.notificationId,
                                resourceId = notification.pullNotificationMessage.action.resourceId,
                                resourceType = it,
                                resourceSubtype = notification.pullNotificationMessage.action.subtype
                            )
                        }
                    notificationItemSelection?.let {
                        onNotificationSelected(it)
                    }
                }
            }
        }
    }

    fun configureTopBar() {
        val topGroupOrgData = TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = "Повідомлення".toDynamicString(),
                mediumIconRight = TitleGroupMlcData.MediumIconRight(
                    code = DiiaResourceIcon.ELLIPSE_SETTINGS.code,
                    action = DataActionWrapper(
                        type = OPEN_NOTIFICATION_SETTINGS,
                        subtype = null,
                        resource = null
                    )
                ),
                leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                    code = DiiaResourceIcon.BACK.code,
                    action = DataActionWrapper(
                        type = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK,
                        subtype = null,
                        resource = null
                    )
                )
            ),
            componentId = UiText.StringResource(R.string.notification_text_tag)
        )
        _topBarData.addIfNotNull(topGroupOrgData)
    }

    fun configureBody() {
        _bodyData.apply {
            clear()
            add(
                MessageListOrganismData(
                    items = createPager(),
                    emptyData = StubMessageMlcData(
                        icon = UiText.StringResource(R.string.error_message_notifications_icon),
                        title = UiText.StringResource(R.string.error_message_notifications_empty)
                    )
                )
            )
        }
    }

    private fun removeNotification(notificationId: String) {
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            notificationsDataSource.removeNotification(notificationId)
        }
    }

    private fun onNotificationSelected(pullNotificationItemSelection: PullNotificationItemSelection) {
        viewModelScope.launch {
            pullNotificationItemSelection.notificationId?.let {
                notificationsDataSource.markNotificationAsRead(it)
            }

            if (notificationHelper.isMessageNotification(pullNotificationItemSelection.resourceType)) {
                _onMessageNotificationSelected.emit(
                    UiDataEvent(pullNotificationItemSelection)
                )
            } else {
                _openResource.emit(UiDataEvent(pullNotificationItemSelection))
            }

        }
    }

    private fun createPager(): Flow<PagingData<MessageMoleculeData>> {
        val pagingConfig = PagingConfig(
            pageSize = 5,
            prefetchDistance = 4,
            enablePlaceholders = false,
            initialLoadSize = 5
        )
        return Pager(
            config = pagingConfig,
            initialKey = 0,
            pagingSourceFactory = {
                NotificationPagingSourceCompose(
                    notificationDataSource = notificationsDataSource,
                    onContentLoadedStateChanged = {
                        when (it) {
                            LoadingState.FIRST_PAGE_LOADING -> {
                                _contentLoaded.value = false
                                _contentLoadedKey.value = PAGE_LOADING_TRIDENT
                            }

                            LoadingState.ADDITIONAL_PAGE_LOADING -> {
                                _contentLoaded.value = false
                                _contentLoadedKey.value = PAGE_LOADING_LINEAR_PAGINATION
                            }

                            LoadingState.NOT_LOADING -> {
                                _contentLoaded.value = true
                            }

                        }
                    },
                    composeMapper = this
                )
            }
        ).flow.cachedIn(viewModelScope)
    }


    fun navigateToDirection(item: PullNotificationItemSelection) {
        viewModelScope.launch {
            val navigateToDoc = notificationHelper.navigateToDocument(item)
            navigateToDoc?.let { _navigateTo.tryEmit(it) }
        }
    }

}

sealed class NotificationsNavigation : NavigationPath {
    object NavigateToNotificationsSettings : NotificationsNavigation()
}