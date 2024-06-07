package ua.gov.diia.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ui_base.NavSystemDialogDirections
import ua.gov.diia.core.models.SystemDialog
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.openPlayMarket
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ui_base.util.view.showCopyDeviceUuidClipedSnackBar
import ua.gov.diia.menu.NavMenuActionsDirections
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionGlobalNotificationFCompose
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionGlobalToNavFaq
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToDiiaId
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToHelpF
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToNavAppSessions
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToSettingsF
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToSignHistory
import ua.gov.diia.menu.R
import ua.gov.diia.menu.models.EventType
import ua.gov.diia.ui_base.components.infrastructure.HomeScreenTab
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.fragments.dialog.system.DiiaSystemDFVM
import ua.gov.diia.web.util.extensions.fragment.navigateToWebView
import javax.inject.Inject

@AndroidEntryPoint
class MenuFCompose : Fragment() {

    @Inject
    lateinit var withCrashlytics: WithCrashlytics
    private var composeView: ComposeView? = null
    private val viewModel: MenuComposeVM by viewModels()
    private lateinit var event: EventType
    private val dialogVM: DiiaSystemDFVM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.configureTopBar()
        viewModel.configureBody()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val topBar = viewModel.topBarData
            val body = viewModel.bodyData
            viewModel.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        activity?.collapseApp()
                    }

                    is MenuTabNavigation.NavigateToNotifications -> {
                        navigateToNotifications()
                    }
                }
            }
            viewModel.settingsAction.collectAsEffect {

                when (val action = it.getContentIfNotHandled()) {

                    MenuAction.OpenPlayMarketAction -> {
                        openPlayMarket(withCrashlytics)
                    }

                    MenuAction.OpenSupportAction -> {
                        navigate(
                            NavMenuActionsDirections.actionHomeFToSupportF(),
                            findNavControllerById(R.id.nav_host)
                        )
                    }

                    MenuAction.OpenHelpAction -> {
                        navigate(
                            actionHomeFToHelpF(true),
                            findNavControllerById(R.id.nav_host)
                        )
                    }

                    MenuAction.OpenSettings -> {
                        navigate(
                            actionHomeFToSettingsF(),
                            findNavControllerById(R.id.nav_host)
                        )
                    }

                    MenuAction.ShareApp -> {
                        shareApp()
                    }

                    MenuAction.OpenFAQAction -> {
                        navigate(
                            actionGlobalToNavFaq(),
                            findNavControllerById(R.id.nav_host)
                        )
                    }

                    MenuAction.OpenPolicyLink -> {
                        openLink(EventType.OPEN_POLICY)
                    }

                    MenuAction.Logout -> {
                        event = EventType.LOGOUT
                        navigate(
                            NavSystemDialogDirections.actionGlobalToSystemDialog(
                                SystemDialog(
                                    getString(R.string.settings_dialog_title_logout),
                                    getString(R.string.settings_dialog_logout_text),
                                    getString(R.string.settings_dialog_logout_stay),
                                    getString(R.string.settings_dialog_logout_leave),
                                )
                            ),
                            findNavControllerById(R.id.nav_host)
                        )
                    }

                    MenuAction.AboutDiia -> {
                        openLink(EventType.OPEN_ABOUT)
                    }

                    MenuAction.OpenDiiaId -> {
                        navigate(
                            actionHomeFToDiiaId(),
                            findNavControllerById(R.id.nav_host),
                        )
                    }

                    MenuAction.OpenSignHistory ->{
                        navigate(
                            actionHomeFToSignHistory(),
                            findNavControllerById(R.id.nav_host),
                        )
                    }

                    MenuAction.CopyDeviceUid -> {
                    }

                    MenuAction.OpenAppSessions -> navigate(
                        actionHomeFToNavAppSessions(),
                        findNavControllerById(R.id.nav_host)
                    )

                    else -> {
                        action?.let {
                            action as MenuAction.DoCopyDeviceUid
                            composeView?.showCopyDeviceUuidClipedSnackBar(
                                action.deviceUid,
                                topPadding = 40f,
                                bottomPadding = 0f
                            )

                        }
                    }
                }
            }

            dialogVM.action.observe(viewLifecycleOwner) {
                when (it.getContentIfNotHandled()) {
                    DiiaSystemDFVM.Action.NEGATIVE -> {
                        if (event == EventType.LOGOUT) {
                            viewModel.logoutApprove()
                        }
                    }
                    DiiaSystemDFVM.Action.POSITIVE -> {
                        when (event) {
                            EventType.OPEN_ABOUT ->
                                navigateToWebView(getString(R.string.url_about_diia))

                            EventType.OPEN_POLICY ->
                                navigateToWebView(getString(R.string.url_app_policy))

                            else -> {
                            }
                        }
                    }

                    else -> {}
                }

            }

            registerForTemplateDialogNavResult { action ->
                findNavController().popBackStack()
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                    ActionsConst.DIALOG_ACTION_CODE_CLOSE -> viewModel.showDataLoadingIndicator(false)
                }
            }

            HomeScreenTab(
                topBar = topBar,
                body = body,
                onEvent = {
                    viewModel.onUIAction(it)
                })
        }
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, getString(R.string.share_text_title) +
                        "\n${getString(R.string.share_text_description)}" +
                        "\n${getString(R.string.share_text_link)}"
            )
            type = TEXT_PLAIN
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun navigateToNotifications() {
        navigate(
            actionGlobalNotificationFCompose(),
            findNavControllerById(R.id.nav_host)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun openLink(eventType: EventType) {
        event = eventType
        navigate(
            NavSystemDialogDirections.actionGlobalToSystemDialog(
                SystemDialog(
                    getString(
                        R.string.settings_dialog_title_faq,
                        getString(R.string.browser)
                    ),
                    null,
                    getString(R.string.settings_dialog_support_open),
                    getString(R.string.settings_dialog_support_cancel),
                )
            ),
            findNavControllerById(R.id.nav_host)
        )
    }

    companion object {
        private const val TEXT_PLAIN = "text/plain"
    }
}

