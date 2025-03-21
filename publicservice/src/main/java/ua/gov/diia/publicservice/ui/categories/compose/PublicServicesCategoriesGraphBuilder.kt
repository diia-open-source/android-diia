package ua.gov.diia.publicservice.ui.categories.compose

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.util.navigation.HomeNavigation


fun NavGraphBuilder.publicServicesCategoriesNavGraph(
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    composable<PublicServicesCategoriesRoute> {
        val viewModel: PublicServicesCategoriesComposeVM = hiltViewModel()
        PublicServicesCategoriesC(
            viewModel = viewModel,
            navigationBackStackEventFlow = navigationBackStackEventFlow,
            homeNavigationActionFlow = homeNavigationActionFlow
        )
    }
}