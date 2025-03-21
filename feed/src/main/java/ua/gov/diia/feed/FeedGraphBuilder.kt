package ua.gov.diia.feed

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.util.navigation.HomeNavigation

fun NavGraphBuilder.feedNavGraph(
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    composable<ScreenFeedRoute> {
        val feedViewModel: FeedVM = hiltViewModel()
        FeedC(
            viewModel = feedViewModel,
            navigationBackStackEventFlow = navigationBackStackEventFlow,
            homeNavigationActionFlow = homeNavigationActionFlow
        )
    }
}
