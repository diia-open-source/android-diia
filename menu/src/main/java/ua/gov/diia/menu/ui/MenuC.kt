package ua.gov.diia.menu.ui

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.activity.getActivity
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.openPlayMarket
import ua.gov.diia.core.util.navigation.HomeNavigation
import ua.gov.diia.menu.R
import ua.gov.diia.menu.models.EventType
import ua.gov.diia.menu.navigation.MenuHomeNavigation
import ua.gov.diia.ui_base.components.infrastructure.HomeScreenTab
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.fragments.dialog.system.DiiaSystemDFVM
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.view.showCopyDeviceUuidClipedSnackBar

@Composable
fun MenuC(
    modifier: Modifier = Modifier,
    viewModel: MenuComposeVM,
    systemDFViewModel: DiiaSystemDFVM,
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    val event = remember { mutableStateOf<EventType?>(null) }
    val context = LocalContext.current
    val topBar = viewModel.topBarData
    val body = viewModel.bodyData
    val systemDialogAction = systemDFViewModel.action.observeAsState()
    val navigationBackStackEvent = navigationBackStackEventFlow.collectAsStateWithLifecycle(
        initialValue = BackStackEvent.Empty
    )

    val progressIndicator = viewModel.progressIndicator.collectAsState(
        initial = Pair("", true)
    )
    val contentLoaded = viewModel.contentLoaded.collectAsState(
        initial = Pair(UIActionKeysCompose.PAGE_LOADING_TRIDENT, true)
    )

    viewModel.navigation.collectAsEffect { navigation ->
        when (navigation) {
            is BaseNavigation.Back -> {
                context.getActivity()?.collapseApp()
            }

            is MenuTabNavigation.NavigateToNotifications -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToNotifications())
            }
        }
    }

    viewModel.showTemplateDialog.collectAsEffect { template ->
        homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToTemplateDialog(template.peekContent()))
    }

    viewModel.settingsAction.collectAsEffect {
        when (val action = it.getContentIfNotHandled()) {
            is MenuAction.OpenPlayMarketAction -> {
                context.openPlayMarket(action.crashlytics)
            }

            MenuAction.OpenSupportAction -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToSupport())
            }

            MenuAction.OpenHelpAction -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToHelp())
            }

            MenuAction.OpenSettings -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToSettings())
            }

            MenuAction.ShareApp -> {
                context.shareApp()
            }

            MenuAction.OpenFAQAction -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToFAQ())
            }

            MenuAction.OpenPolicyLink -> {
                event.value = EventType.OPEN_POLICY
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToLinkDialog())
            }

            MenuAction.Logout -> {
                event.value = EventType.LOGOUT
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToLogout())
            }

            MenuAction.AboutDiia -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToLinkDialog())
            }

            MenuAction.OpenDiiaId -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToDiiaID())
            }

            MenuAction.OpenSignHistory -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToSignHistory())
            }

            MenuAction.CopyDeviceUid -> {
            }

            MenuAction.OpenAppSessions -> {
                homeNavigationActionFlow.tryEmit(MenuHomeNavigation.ToAppSession())
            }

            else -> {
                val uuid = action as? MenuAction.DoCopyDeviceUid ?: return@collectAsEffect
                context.getActivity()?.window?.decorView?.showCopyDeviceUuidClipedSnackBar(
                    uuid.deviceUid,
                    topPadding = 40f,
                    bottomPadding = 0f
                )
            }
        }
    }

    LaunchedEffect(key1 = systemDialogAction.value) {
        when (systemDialogAction.value?.getContentIfNotHandled()) {
            DiiaSystemDFVM.Action.NEGATIVE -> {
                if (event.value == EventType.LOGOUT) {
                    viewModel.logoutApprove()
                }
            }

            DiiaSystemDFVM.Action.POSITIVE -> {
                when (event.value) {
                    EventType.OPEN_ABOUT -> {
                        homeNavigationActionFlow.tryEmit(
                            MenuHomeNavigation.ToWebView(context.getString(R.string.url_about_diia))
                        )
                    }

                    EventType.OPEN_POLICY -> {
                        homeNavigationActionFlow.tryEmit(
                            MenuHomeNavigation.ToWebView(context.getString(R.string.url_app_policy))
                        )
                    }

                    else -> {}
                }
            }

            else -> {}
        }
    }

    handleNavigationResultEvent(navigationBackStackEvent.value, viewModel)

    HomeScreenTab(
        modifier = modifier,
        topBar = topBar,
        body = body,
        progressIndicator = progressIndicator.value,
        contentLoaded = contentLoaded.value,
        onEvent = { viewModel.onUIAction(it) }
    )
}

private fun Context.shareApp() {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT, getString(R.string.share_text_title) +
                    "\n${getString(R.string.share_text_description)}" +
                    "\n${getString(R.string.share_text_link)}"
        )
        type = "text/plain"
    }
    startActivity(Intent.createChooser(sendIntent, null))
}

fun handleNavigationResultEvent(
    event: BackStackEvent,
    viewModel: MenuComposeVM,
) {
    when (event) {
        is BackStackEvent.UserActionResult -> {
            event.data.consumeEvent { action ->
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                    ActionsConst.DIALOG_ACTION_CODE_CLOSE -> viewModel.showDataLoadingIndicator(
                        false
                    )
                }
            }
        }
    }
}