package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderBlock
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithNavigationBlock
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithUIBlocking

@Composable
fun ComposeHomeTabRoot(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean>,
    topBar: @Composable (() -> Unit)? = null,
    body: @Composable (ColumnScope.() -> Unit)? = null,
    bottom: @Composable (ColumnScope.() -> Unit)? = null,
    onEvent: (UIAction) -> Unit
) {
    Box(modifier = modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            topBar?.let {
                topBar()
            }
            body?.let {
                body()
            }
            bottom?.let {
                bottom()
            }
        }
        TridentLoaderBlock(contentLoaded)
        TridentLoaderWithNavigationBlock(
            contentLoaded = contentLoaded,
            onUIAction = onEvent
        )
        TridentLoaderWithUIBlocking(
            contentLoaded = contentLoaded
        )
    }
}