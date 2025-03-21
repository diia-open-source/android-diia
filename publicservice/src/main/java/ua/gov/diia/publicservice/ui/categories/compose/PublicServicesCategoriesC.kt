package ua.gov.diia.publicservice.ui.categories.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.activity.getActivity
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.navigation.HomeNavigation
import ua.gov.diia.publicservice.navigation.PublicServiceHomeNavigation
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation

@SuppressLint("RestrictedApi")
@Composable
fun PublicServicesCategoriesC(
    modifier: Modifier = Modifier,
    viewModel: PublicServicesCategoriesComposeVM,
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val topBar = viewModel.topBarData
    val body = viewModel.bodyData
    val contentLoaded = viewModel.contentLoaded.collectAsState(
        initial = Pair(
            UIActionKeysCompose.PAGE_LOADING_TRIDENT, false
        )
    )
    val navigationBackStackEvent = navigationBackStackEventFlow.collectAsStateWithLifecycle(
        initialValue = BackStackEvent.Empty
    )

    viewModel.navigation.collectAsEffect { navigation ->
        when (navigation) {
            is BaseNavigation.Back -> {
                context.getActivity()?.collapseApp()
            }

            is PublicServicesCategoriesNavigation.NavigateToCategory -> {
                homeNavigationActionFlow.tryEmit(PublicServiceHomeNavigation.ToCategory(navigation.category))
            }

            is PublicServicesCategoriesNavigation.NavigateToService -> {
                homeNavigationActionFlow.tryEmit(PublicServiceHomeNavigation.ToService(navigation.service))
            }

            is PublicServicesCategoriesNavigation.NavigateToServiceSearch -> {
                homeNavigationActionFlow.tryEmit(
                    PublicServiceHomeNavigation.ToServiceSearch(navigation.data)
                )
            }

            is PublicServicesCategoriesNavigation.OpenEnemyTrackLink -> {
                context.openLink(link = navigation.link, withCrashlytics = navigation.crashlytics)
            }
        }
    }
    viewModel.showTemplateDialog.collectAsEffect { template ->
        homeNavigationActionFlow.tryEmit(PublicServiceHomeNavigation.ToTemplateDialog(template.peekContent()))
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.STARTED) {
            viewModel.doInit(null)
        }
    }

    handleNavigationResultEvent(navigationBackStackEvent.value, viewModel)

    PublicServicesCategoriesComposeScreen(
        modifier = modifier,
        topBar = topBar,
        body = body,
        contentLoaded = contentLoaded.value,
        onEvent = { viewModel.onUIAction(it) }
    )
}

fun handleNavigationResultEvent(
    event: BackStackEvent,
    viewModel: PublicServicesCategoriesComposeVM,
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