package ua.gov.diia.notifications.ui.fragments.notifications.compose

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.di.actions.GlobalActionNetworkState
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.notification.pull.message.MessageActions
import ua.gov.diia.core.models.notification.pull.message.NotificationFull
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.pull.MessageIdentification
import ua.gov.diia.notifications.ui.compose.mapper.media.toComposeArticlePic
import ua.gov.diia.notifications.ui.compose.mapper.media.toComposeArticleVideo
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.organism.bottom.toUiModel
import ua.gov.diia.ui_base.components.organism.list.toUIModel
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.toUiModel
import javax.inject.Inject

@HiltViewModel
class NotificationFullComposeVM @Inject constructor(
    @AuthorizedClient private val apiNotifications: ApiNotifications,
    @AuthorizedClient private val apiNotificationsPublic: ApiNotificationsPublic,
    @GlobalActionNetworkState private val connectivityObserver: ConnectivityObserver,
    @GlobalActionLogout private val actionLogout: MutableLiveData<UiEvent>,
    private val notificationHelper: NotificationHelper,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val deepLinkDelegate: WithDeeplinkHandling,
) : ViewModel(), WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    WithDeeplinkHandling by deepLinkDelegate {

    private val _topBarData = mutableStateListOf<UIElementData>()
    val topBarData: SnapshotStateList<UIElementData> = _topBarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _bottomData = mutableStateListOf<UIElementData>()
    val bottomData: SnapshotStateList<UIElementData> = _bottomData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _openLink = MutableSharedFlow<String?>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val openLink = _openLink.asSharedFlow()

    var applicationId: String? = null

    private val _contentLoadedKey =
        MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION)
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

    val isNetworkEnabled = connectivityObserver.observe()

    sealed class NotificationsFullNavigation : NavigationPath {
        data class NavigateToHome(val destinationId: Int) :
            NotificationsFullNavigation()
    }

    fun loadNotification(message: MessageIdentification) {
        executeActionOnFlow(
            contentLoadedIndicator = _contentLoaded,
        ) {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION

            val notification = if (message.needAuth) {
                if (message.notificationId.isEmpty()) {
                    apiNotifications.getNotificationByMessageId(message.resourceId)
                } else {
                    apiNotifications.getPullNotification(message.notificationId)
                }
            } else {
                if (!message.resourceId.isNullOrEmpty()) {
                    apiNotificationsPublic.getMessage(message.resourceId)
                } else {
                    NotificationFull(null, null, null, null, null)
                }
            }
            notification.topGroup?.forEach {
                _topBarData.addIfNotNull(
                    it.topGroupOrg?.toUiModel()
                )
            }

            notification.body?.forEach {
                _bodyData.addAllIfNotNull(
                    it.textLabelContainerMlc?.toUiModel(),
                    it.articlePicAtm?.image.toComposeArticlePic(),
                    it.articleVideoMlc?.source.toComposeArticleVideo(),
                    it.listItemGroupOrg?.toUIModel(),
                )
            }

            notification.bottomGroup?.forEach {
                _bottomData.addAllIfNotNull(
                    it.listItemGroupOrg?.toUIModel(),
                    it.bottomGroupOrg?.toUiModel()
                )
            }
        }
    }

    fun onUIAction(event: UIAction) {
        executeActionOnFlow(
            progressIndicator = _progressIndicator
        ) {
            when (event.actionKey) {
                UIActionKeysCompose.TITLE_GROUP_MLC -> {
                    event.action?.type.let {
                        if (it == ActionsConst.ACTION_NAVIGATE_BACK) {
                            _navigation.tryEmit(BaseNavigation.Back)
                        }
                    }
                }

                UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                    _navigation.tryEmit(BaseNavigation.Back)
                }

                UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                    event.action?.let {
                        when (it.type) {
                            MessageActions.externalLink.name,
                            MessageActions.default.name,
                            MessageActions.downloadLink.name -> {
                                event.action?.resource?.let {
                                    executeActionOnFlow(
                                        progressIndicator = _progressIndicator
                                    ) {
                                        _progressIndicatorKey.tryEmit(event.data ?: "")
                                        _openLink.tryEmit(event.action?.resource)
                                    }

                                }
                            }

                            MessageActions.internalLink.name -> {
                                event.action?.resource?.let {
                                    handleInternalLink(it)
                                }
                            }

                            MessageActions.logout.name -> {
                                logout()
                            }

                            else -> {}
                        }
                    }
                }

                UIActionKeysCompose.BTN_PLAIN_ATM -> {
                    event.action?.resource?.let {
                        executeActionOnFlow(
                            progressIndicator = _progressIndicator
                        ) {
                            val resp = apiNotifications.refuseNacpDeclarantRelatives(it, false)
                            applicationId = it
                            resp.template?.let {
                                showTemplateDialog(it)
                            }
                            _progressIndicatorKey.tryEmit(event.data ?: "")
                        }
                    }
                }

                UIActionKeysCompose.BUTTON_REGULAR -> {
                    event.action?.resource?.let {
                        executeActionOnFlow(
                            progressIndicator = _progressIndicator
                        ) {
                            val resp = apiNotifications.confirmNacpDeclarantRelatives(it)
                            resp.template?.let {
                                showTemplateDialog(it)
                            }
                            _progressIndicatorKey.tryEmit(event.data ?: "")
                        }
                    }
                }

                else -> {}
            }
        }
    }

    fun confirmationRefuseNacpDeclarantRelatives() {
        executeActionOnFlow(
            progressIndicator = _progressIndicator
        ) {
            val resp =
                applicationId?.let { apiNotifications.refuseNacpDeclarantRelatives(it, true) }
            resp?.template?.let { showTemplateDialog(it) }
        }
    }

    private fun handleInternalLink(internalLink: String?) {
        viewModelScope.launch {
            Uri.parse(internalLink)?.path?.let { path ->
                emitDeeplink(UiDataEvent(buildDeepLinkAction(path)))
                _navigation.emit(
                    NotificationsFullNavigation.NavigateToHome(notificationHelper.getHomeDestinationId())
                )
            }
        }
    }

    private fun logout() {
        actionLogout.postValue(UiEvent())
    }
}