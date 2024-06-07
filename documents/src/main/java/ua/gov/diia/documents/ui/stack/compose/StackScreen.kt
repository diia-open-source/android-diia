package ua.gov.diia.documents.ui.stack.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.screen.ToolbarRootContainer
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId

@Composable
fun StackScreen(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    toolbar: SnapshotStateList<UIElementData>,
    connectivityState: Boolean = true,
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>? = null,
    onEvent: (UIAction) -> Unit
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gradient_bg))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier.fillMaxSize().provideTestTagsAsResourceId(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds,
            composition = composition,
            progress = { progress },

            )
        BackHandler {
            onEvent(UIAction(actionKey = toolbar.firstOrNull {
                it is NavigationPanelMlcData
            }?.let {
                (it as NavigationPanelMlcData).backAction
            } ?: UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
        }
        ComposeRootScreen(
            modifier = modifier,
            contentLoaded = contentLoaded,
            toolbar = {
                ToolbarRootContainer(
                    toolbarViews = toolbar,
                    onUIAction = onEvent
                )
            },
            body = {
                BodyRootContainer(
                    bodyViews = body,
                    progressIndicator = progressIndicator,
                    contentLoaded = contentLoaded,
                    onUIAction = onEvent,
                    connectivityState = connectivityState
                )
            },
            bottom = {
                if (bottom != null) {
                    BottomBarRootContainer(
                        bottomViews = bottom,
                        progressIndicator = progressIndicator,
                        onUIAction = onEvent
                    )
                }
            },
            onEvent = onEvent
        )
    }
}
