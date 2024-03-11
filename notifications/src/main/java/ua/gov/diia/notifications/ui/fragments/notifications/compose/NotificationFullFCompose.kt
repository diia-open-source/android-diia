package ua.gov.diia.notifications.ui.fragments.notifications.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.ServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFullFCompose : Fragment() {

    @Inject
    lateinit var withCrashlytics: WithCrashlytics

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider

    private var composeView: ComposeView? = null
    private val args: NotificationFullFComposeArgs by navArgs()
    val viewModel: NotificationFullComposeVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadNotification(args.messageId)
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
            val bottom = viewModel.bottomData
            val progressIndicator =
                viewModel.progressIndicator.collectAsState(
                    initial = Pair(
                        "",
                        true
                    )
                )
            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_CIRCULAR, true
                )
            )
            val connectivityState =
                viewModel.isNetworkEnabled.collectAsState(initial = false)
            viewModel.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        findNavController().popBackStack()
                    }

                }
            }
            viewModel.showTemplateDialog.collectAsEffect {
                openTemplateDialog(it.peekContent())
            }

            viewModel.openLink.collectAsEffect { link ->
                link?.let {
                    openLink(it, withCrashlytics)
                }
            }
            viewModel.openInternalLink.collectAsEffect { link ->
                link?.let {
                    navByDeepLink(it)
                }
            }
            ServiceScreen(
                contentLoaded = contentLoaded.value,
                progressIndicator = progressIndicator.value,
                connectivityState = connectivityState.value,
                toolbar = topBar,
                body = body,
                bottom = bottom,
                onEvent = {
                    viewModel.onUIAction(it)
                },
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
            }
        }
    }

    private fun navByDeepLink(link: String) {
        setNavigationResult(
            key = ActionsConst.DEEP_LINK_ACTION,
            data = UiDataEvent(link)
        )
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}