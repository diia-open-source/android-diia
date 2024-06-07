package ua.gov.diia.ui_base.components.infrastructure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootContainer
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
    useNestedScrollBody: Boolean = false,
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
            .background(BlackSqueeze)
            .provideTestTagsAsResourceId(),
        contentLoaded = contentLoaded,
        toolbar = {
            ToolbarRootContainer(
                toolbarViews = toolbar,
                onUIAction = onEvent
            )
        },
        body = {
            if (useNestedScrollBody){
                BodyRootLazyContainer(
                    bodyViews = body,
                    displayBlockDivider = bottom.isNotEmpty(),
                    progressIndicator = progressIndicator,
                    contentLoaded = contentLoaded,
                    onUIAction = onEvent
                )
            } else {
                BodyRootContainer(
                    bodyViews = body,
                    displayBlockDivider = bottom.isNotEmpty(),
                    progressIndicator = progressIndicator,
                    contentLoaded = contentLoaded,
                    onUIAction = onEvent
                )
            }
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
