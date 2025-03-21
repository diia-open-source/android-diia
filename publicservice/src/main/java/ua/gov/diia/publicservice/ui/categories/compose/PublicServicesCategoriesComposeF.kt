package ua.gov.diia.publicservice.ui.categories.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation

@AndroidEntryPoint
class PublicServicesCategoriesComposeF : Fragment() {

    private var composeView: ComposeView? = null
    private val args: PublicServicesCategoriesComposeFArgs by navArgs()
    private val viewModel: PublicServicesCategoriesComposeVM by viewModels()

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
            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT, false
                )
            )
            viewModel.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        activity?.collapseApp()
                    }
                    is PublicServicesCategoriesNavigation.NavigateToCategory -> {
                        viewModel.navigateToCategoryServices(
                            this@PublicServicesCategoriesComposeF,
                            navigation.category
                        )
                    }

                    is PublicServicesCategoriesNavigation.NavigateToService -> {
                        viewModel.navigateToService(
                            this@PublicServicesCategoriesComposeF,
                            navigation.service
                        )
                    }

                    is PublicServicesCategoriesNavigation.NavigateToServiceSearch -> {
                        viewModel.navigateToServiceSearch(
                            this@PublicServicesCategoriesComposeF,
                            navigation.data
                        )
                    }
                    is PublicServicesCategoriesNavigation.OpenEnemyTrackLink -> {
                        openLink(link = navigation.link, withCrashlytics = navigation.crashlytics)
                    }
                }
            }
            LaunchedEffect(key1 = Unit) {
                viewModel.doInit(args.forceOpenCategory)
            }

            PublicServicesCategoriesComposeScreen(
                topBar = topBar,
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
}