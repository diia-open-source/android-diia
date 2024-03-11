package ua.gov.diia.ui_base.components.infrastructure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeHomeTabRoot
import ua.gov.diia.ui_base.components.infrastructure.screen.TabBodyRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.TopBarRootContainer
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId

@Composable
fun HomeScreenTab(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    connectivityState: Boolean = true,
    topBar: SnapshotStateList<UIElementData>? = null,
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>? = null,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onEvent: (UIAction) -> Unit
) {
    BackHandler {
        onEvent(UIAction(actionKey = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
    }
    ComposeHomeTabRoot(
        modifier = modifier
            .background(Color.Transparent)
            .provideTestTagsAsResourceId(),
        contentLoaded = contentLoaded,
        topBar = {
            if (topBar != null) {
                TopBarRootContainer(
                    modifier = Modifier.statusBarsPadding(),
                    topBarViews = topBar,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                    onUIAction = onEvent
                )
            }
        },
        body = {
            TabBodyRootContainer(
                modifier = Modifier.conditional(topBar == null){
                    statusBarsPadding()
                },
                bodyViews = body,
                contentLoaded = contentLoaded,
                progressIndicator = progressIndicator,
                onUIAction = onEvent,
                connectivityState = connectivityState,
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        },
        bottom = {
            if (bottom != null) {
                BottomBarRootContainer(
                    bottomViews = bottom,
                    progressIndicator = progressIndicator,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                    onUIAction = onEvent
                )
            }
        },
        onEvent = onEvent,
        diiaResourceIconProvider = diiaResourceIconProvider,
    )
}