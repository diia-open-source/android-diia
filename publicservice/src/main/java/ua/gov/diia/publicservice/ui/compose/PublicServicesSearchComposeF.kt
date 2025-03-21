package ua.gov.diia.publicservice.ui.compose

import android.annotation.SuppressLint
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
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.hideKeyboard
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.publicservice.R
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.ui_base.components.infrastructure.ServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class PublicServicesSearchComposeF : Fragment() {

    private var composeView: ComposeView? = null
    private val args: PublicServicesSearchComposeFArgs by navArgs()
    private val viewModel: PublicServicesSearchComposeVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.categories)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val toolbar = viewModel.toolBarData
            val body = viewModel.bodyData
            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT, true
                )
            )
            viewModel.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        findNavController().popBackStack()
                    }

                    is PublicServicesCategoriesSearchNavigation.NavigateToService -> {
                        navigateToService(navigation.service)
                    }

                    is PublicServicesCategoriesSearchNavigation.OpenEnemyTrackLink -> {
                        openLink(link = navigation.link, withCrashlytics = navigation.crashlytics)
                    }

                    is PublicServicesCategoriesSearchNavigation.HideKeyboard -> {
                        hideKeyboard()
                    }
                }
            }

            viewModel.showTemplateDialog.collectAsEffect { template ->
                template.getContentIfNotHandled()?.let { openTemplateDialog(it) }
            }

            registerForNavigationResult<ConsumableString>(ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY) { event ->
                if (findNavController().currentBackStack.value.last().destination.id == R.id.template_dialog) {
                    findNavController().popBackStack()
                }
                when (event.item) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                }
            }

            ServiceScreen(
                toolbar = toolbar,
                body = body,
                contentLoaded = contentLoaded.value,
                onEvent = {
                    viewModel.onUIAction(it)
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToService(service: PublicService) {
        viewModel.navigateToService(this@PublicServicesSearchComposeF, service)
    }
}