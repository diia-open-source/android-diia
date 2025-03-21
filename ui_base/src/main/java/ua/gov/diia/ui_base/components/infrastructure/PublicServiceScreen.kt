package ua.gov.diia.ui_base.components.infrastructure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootLazyContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.screen.ToolbarRootContainer
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.theme.BlackSqueeze

@Composable
fun PublicServiceScreen(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    toolbar: SnapshotStateList<UIElementData>,
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>,
    useGradientBg: Boolean = false,
    onEvent: (UIAction) -> Unit
) {
    BackHandler {
        onEvent(UIAction(actionKey = toolbar.firstOrNull {
            it is NavigationPanelMlcData
        }?.let {
            (it as NavigationPanelMlcData).backAction
        } ?: UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
    }
    ComposeRootScreen(
        modifier = modifier
            .conditional(!useGradientBg) {
                background(BlackSqueeze)
            }
            .conditional(useGradientBg) {
                paint(
                    painterResource(id = R.drawable.bg_blue_yellow_gradient),
                    contentScale = ContentScale.FillBounds
                )
            }
            .provideTestTagsAsResourceId(),
        contentLoaded = contentLoaded,
        progressIndicator = progressIndicator,
        toolbar = {
            ToolbarRootContainer(
                toolbarViews = toolbar,
                onUIAction = onEvent
            )
        },
        body = {
            BodyRootLazyContainer(
                bodyViews = body,
                displayBlockDivider = bottom.isNotEmpty(),
                progressIndicator = progressIndicator,
                contentLoaded = contentLoaded,
                useGradientBg = useGradientBg,
                onUIAction = onEvent
            )
        },
        bottom = {
            BottomBarRootContainer(
                bottomViews = bottom,
                progressIndicator = progressIndicator,
                onUIAction = onEvent
            )
        },
        onEvent = onEvent
    )
}
