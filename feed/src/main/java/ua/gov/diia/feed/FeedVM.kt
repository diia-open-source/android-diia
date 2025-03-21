package ua.gov.diia.feed

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionNetworkState
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.common_compose.atm.SpacerAtmType
import ua.gov.diia.core.models.common_compose.general.DiiaResponse
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_DEBUG
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_STAGE
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository
import ua.gov.diia.feed.FeedConst.ACTION_TYPE_DEBUG_SCREEN
import ua.gov.diia.feed.helper.FeedOfflineScreenContentProvider
import ua.gov.diia.feed.network.ApiFeed
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.text.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.molecule.card.toUiModel
import ua.gov.diia.ui_base.components.organism.list.toUIModel
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.toUiModel
import javax.inject.Inject

@HiltViewModel
class FeedVM @Inject constructor(
    private val itnDataSource: ItnDataRepository,
    @AuthorizedClient private val api: ApiFeed,
    @GlobalActionNetworkState private val connectivityObserver: ConnectivityObserver,
    private val feedOfflineScreenContentProvider: FeedOfflineScreenContentProvider,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val withCrashlytics: WithCrashlytics,
    private val withBuildConfig: WithBuildConfig
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    WithCrashlytics by withCrashlytics {

    private val _contentLoadedKey =
        MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL)
    private val _contentLoaded = MutableStateFlow(true)
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

    private val _toolbarData = mutableStateListOf<UIElementData>()
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    val connectivity = MutableStateFlow(connectivityObserver.isAvailable)

    private val _navigation = mutableSharedFlowOf<NavigationPath>()
    val navigation = _navigation.asSharedFlow()

    private var firstName: String? = null

    private var lastFeedData: DiiaResponse? = null

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { value ->
                if (!connectivity.value && value) {
                    connectivity.emit(value)
                    getFeedContent()
                } else {
                    connectivity.emit(value)
                }
            }
        }
        viewModelScope.launch {
            itnDataSource.data.collect {
                firstName = it.data?.fName
            }
        }
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.IMAGE_CARD_MLC,
            UIActionKeysCompose.VERTICAL_CARD_CAROUSEL_ORG,
            UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
            UIActionKeysCompose.LOOPING_VIDEO_PLAYER_CARD_MLC-> {
                handleAction(event)
            }

            UIActionKeysCompose.SMALL_NOTIFICATION_CAROUSEL_ORG -> {
                handleNotificationOrg(event)
            }

            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TITLE_GROUP_MLC -> {
                if (withBuildConfig.getBuildType() == BUILD_TYPE_DEBUG || withBuildConfig.getBuildType() == BUILD_TYPE_STAGE) {
                    startNewFlowByUIAction(event.copy(action = DataActionWrapper(type = ACTION_TYPE_DEBUG_SCREEN)))
                }
            }

            UIActionKeysCompose.HALVED_CARD_CAROUSEL_ORG,
            UIActionKeysCompose.WHITE_CARD_MLC,
            UIActionKeysCompose.BTN_ICON_ROUNDED_GROUP_ORG -> {
                startNewFlowByUIAction(event)
            }
        }
    }

    sealed class FeedNavigation : NavigationPath {
        data class ToStartNewFlow(
            val flowId: String,
            val resId: String? = null,
            val resType: String? = null
        ) : FeedNavigation()

        object ToAllNotifications : FeedNavigation()
        object InitiateQRScanProcess : NavigationPath
        data class EnemyTrack(val link: String, val withCrashlytics: WithCrashlytics) :
            NavigationPath

        data class ToPublicService(val serviceCode: String) : NavigationPath
        data class OnNotificationSelected(val notification: PullNotificationItemSelection) :
            FeedNavigation()

    }

    fun getFeedContent() {
        if (connectivity.value) {
            executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING
            }) {
                with(api.getFeed()) {
                    if (lastFeedData != this) {
                        clearScreen()
                        topGroup?.forEach {
                            _toolbarData.addIfNotNull(it.topGroupOrg?.toUiModel())
                        }
                        body?.forEach {
                            _bodyData.addAllIfNotNull(
                                it.sectionTitleAtm?.toUiModel(),
                                it.smallNotificationCarouselOrg?.toUiModel(),
                                it.whiteCardMlc?.toUiModel(),
                                it.btnIconRoundedGroupOrg?.toUiModel(),
                                it.imageCardMlc?.toUiModel(),
                                it.halvedCardCarouselOrg?.toUiModel(),
                                it.listItemGroupOrg?.toUIModel(),
                                it.verticalCardCarouselOrg?.toUiModel(),
                                it.loopingVideoPlayerCardMlc?.toUiModel()
                            )
                        }
                        _bodyData.add(SpacerAtmData(SpacerAtmType.SPACER_24))
                        lastFeedData = this
                    }
                    template?.let {
                        showTemplateDialog(it)
                    }
                }
            }
        } else {
            if (bodyData.isEmpty()) {
                _bodyData.addAllIfNotNull(*getOfflineBody().toTypedArray())
            }
            if (toolbarData.isEmpty()) {
                _toolbarData.addAllIfNotNull(*getOfflineToolbar().toTypedArray())
            }
            _contentLoaded.tryEmit(true)
        }
    }

    private fun handleAction(event: UIAction) {
        event.action?.let {
            when (it.type) {
                FeedConst.ACTION_TYPE_ENEMY_TRACK -> {
                    loadFindEnemyShareLink()
                }

                else -> navigateToPublicServiceByCode(it.type)
            }
        }
    }

    private fun handleNotificationOrg(event: UIAction) {
        if (event.action?.type == FeedConst.ACTION_TYPE_ALL_MESSAGES) {
            _navigation.tryEmit(FeedNavigation.ToAllNotifications)
        } else {
            executeActionOnFlow {
                val notificationItemSelection =
                    PullNotificationItemSelection(
                        notificationId = event.data,
                        resourceId = event.action?.resource,
                        resourceType = event.action?.type ?: "",
                        resourceSubtype = event.action?.subtype
                    )
                _navigation.tryEmit(FeedNavigation.OnNotificationSelected(notificationItemSelection))
            }
        }
    }

    private fun loadFindEnemyShareLink() {
        executeActionOnFlow {
            with(api.getRedirectLink()) {
                link?.let {
                    _navigation.tryEmit(
                        FeedNavigation.EnemyTrack(
                            link = it,
                            withCrashlytics = withCrashlytics
                        )
                    )
                }
                template?.let {
                    showTemplateDialog(it)
                }
            }
        }
    }

    private fun navigateToPublicServiceByCode(code: String) {
        _navigation.tryEmit(
            FeedNavigation.ToPublicService(serviceCode = code)
        )
    }

    private fun startNewFlowByUIAction(event: UIAction) {
        event.action?.let {
            _navigation.tryEmit(
                FeedNavigation.ToStartNewFlow(
                    flowId = it.type,
                    resId = it.resource,
                    resType = it.subtype
                )
            )
        }
    }

    private fun getOfflineBody(): List<UIElementData> {
        return feedOfflineScreenContentProvider.getOfflineBody()
    }

    private fun getOfflineToolbar(): List<UIElementData> {
        return feedOfflineScreenContentProvider.getOfflineToolbar(firstName)

    }

    private fun clearScreen() {
        _toolbarData.clear()
        _bodyData.clear()
    }
}