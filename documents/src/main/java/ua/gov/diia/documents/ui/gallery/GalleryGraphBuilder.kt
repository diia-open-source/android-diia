package ua.gov.diia.documents.ui.gallery

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.navigation.HomeNavigation

fun NavGraphBuilder.docGalleryGraphBuilder(
    withCrashlytics: WithCrashlytics,
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    composable<ScreenDocGalleryRoute> {
        val viewModel: DocGalleryVMCompose = hiltViewModel()
        DocGalleryC(
            viewModel = viewModel,
            navigationBackStackEventFlow = navigationBackStackEventFlow,
            homeNavigationActionFlow = homeNavigationActionFlow,
            withCrashlytics = withCrashlytics
        )
    }
}
