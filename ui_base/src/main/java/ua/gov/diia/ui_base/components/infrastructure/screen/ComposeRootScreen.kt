package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderBlock
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithNavigationBlock

@Composable
fun ComposeRootScreen(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean>,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    toolbar: @Composable (() -> Unit)? = null,
    body: @Composable (ColumnScope.() -> Unit)? = null,
    bottom: @Composable (() -> Unit)? = null,
    onEvent: (UIAction) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            toolbar?.let {
                toolbar()
            }
            body?.let {
                body()
            }
            bottom?.let {
                bottom()
            }
        }

        if (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_CIRCULAR && !contentLoaded.second) {
            FullScreenLoadingMolecule()
        }
        TridentLoaderBlock(contentLoaded)
        TridentLoaderWithNavigationBlock(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(), contentLoaded, diiaResourceIconProvider, onEvent
        )
    }
}
