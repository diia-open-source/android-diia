package ua.gov.diia.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.activity.getActivity
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.navigation.HomeNavigation
import ua.gov.diia.feed.navigation.FeedHomeNavigation
import ua.gov.diia.ui_base.components.infrastructure.HomeScreenTab
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation

@Composable
fun FeedC(
    modifier: Modifier = Modifier,
    viewModel: FeedVM,
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    val context = LocalContext.current
    val toolbar = viewModel.toolbarData
    val body = viewModel.bodyData
    val contentLoaded = viewModel.contentLoaded.collectAsState(
        initial = Pair(
            UIActionKeysCompose.PAGE_LOADING_TRIDENT,
            true
        )
    )
    val progressIndicator =
        viewModel.progressIndicator.collectAsState(initial = Pair("", false))
    val connectivityState = viewModel.connectivity.collectAsState(true)
    val navigationBackStackEvent = navigationBackStackEventFlow.collectAsStateWithLifecycle(
        initialValue = BackStackEvent.Empty
    )

    LaunchedEffect(Unit) {
        viewModel.getFeedContent()
    }

    with(viewModel) {
        showTemplateDialog.collectAsEffect {
            homeNavigationActionFlow.tryEmit(FeedHomeNavigation.ToTemplateDialog(it.peekContent()))
        }
        navigation.collectAsEffect {
            when (it) {
                is BaseNavigation.Back -> {
                    context.getActivity()?.collapseApp()
                }

                is FeedVM.FeedNavigation.ToPublicService -> {
                    homeNavigationActionFlow.tryEmit(FeedHomeNavigation.ToPublicService(it.serviceCode))
                }

                is FeedVM.FeedNavigation.InitiateQRScanProcess -> {
                    homeNavigationActionFlow.tryEmit(FeedHomeNavigation.ToCameraRequest())
                }

                is FeedVM.FeedNavigation.EnemyTrack -> {
                    context.openLink(link = it.link, withCrashlytics = it.withCrashlytics)
                }

                is FeedVM.FeedNavigation.ToAllNotifications -> {
                    homeNavigationActionFlow.tryEmit(FeedHomeNavigation.ToNotification())
                }

                is FeedVM.FeedNavigation.OnNotificationSelected -> {
                    homeNavigationActionFlow.tryEmit(FeedHomeNavigation.OnNotificationSelected(it.notification))
                }

                is FeedVM.FeedNavigation.ToStartNewFlow -> {
                    val deeplink = DeepLinkActionStartFlow(
                        flowId = it.flowId,
                        resId = it.resId.orEmpty(),
                        resType = it.resType
                    )
                    homeNavigationActionFlow.tryEmit(
                        FeedHomeNavigation.ToStartNewFlow(
                            deeplink = deeplink
                        )
                    )
                }

            }
        }
    }

    handleNavigationResultEvent(navigationBackStackEvent.value, viewModel)

    HomeScreenTab(
        modifier = modifier,
        contentLoaded = contentLoaded.value,
        progressIndicator = progressIndicator.value,
        connectivityState = connectivityState.value,
        topBar = toolbar,
        body = body,
        onEvent = { viewModel.onUIAction(it) }
    )
}

fun handleNavigationResultEvent(
    event: BackStackEvent,
    viewModel: FeedVM,
) {
    when (event) {
        is BackStackEvent.UserActionResult -> {
            event.data.consumeEvent { action ->
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                }
            }
        }
    }
}
