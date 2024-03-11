package ua.gov.diia.publicservice.ui.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.components.infrastructure.ServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import javax.inject.Inject

@AndroidEntryPoint
class PublicServiceCategoryDetailsComposeF : Fragment() {

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider

    private var composeView: ComposeView? = null
    private val args: PublicServiceCategoryDetailsComposeFArgs by navArgs()
    private val viewModel: PublicServiceCategoryDetailsComposeVM by viewModels()

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
        viewModel.doInit(args.category)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val toolbar = viewModel.toolBarData
            val body = viewModel.bodyData
            viewModel.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        findNavController().popBackStack()
                    }

                    is PublicServicesCategoriesDetailsNavigation.NavigateToService -> {
                        navigateToService(navigation.service)
                    }
                }
            }

            ServiceScreen(
                toolbar = toolbar,
                body = body,
                onEvent = {
                    viewModel.onUIAction(it)
                },
            diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToService(service: PublicService) {
        viewModel.navigateToService(this@PublicServiceCategoryDetailsComposeF, service)
    }
}