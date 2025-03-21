package ua.gov.diia.ui_base.components.infrastructure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootLazyContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.text.ScalingTitleMlc
import ua.gov.diia.ui_base.components.molecule.text.ScalingTitleMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlueHighlight
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Composable
fun BottomSheetScreen(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    toolbar: SnapshotStateList<UIElementData>,
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>,
    onEvent: (UIAction) -> Unit
) {
    val lazyListState = rememberLazyListState()

    BackHandler {
        onEvent(
            UIAction(
                actionKey = UIActionKeysCompose.BOTTOM_SHEET_DISMISS
            )
        )
    }

    ComposeRootScreen(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .background(
                color = BlueHighlight,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            ),
        contentLoaded = contentLoaded,
        toolbar = {
            BottomSheetToolbarContainer(
                toolbar = toolbar,
                lazyListState = lazyListState,
                onEvent = onEvent
            )
        },
        body = {
            BodyRootLazyContainer(
                bodyViews = body,
                progressIndicator = progressIndicator,
                contentLoaded = contentLoaded,
                lazyListState = lazyListState,
                onUIAction = onEvent
            )
        },
        bottom = {
            if (bottom.isNotEmpty()) {
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

@Composable
private fun BottomSheetToolbarContainer(
    toolbar: SnapshotStateList<UIElementData>,
    lazyListState: LazyListState,
    onEvent: (UIAction) -> Unit
) {
    var alpha by remember { mutableFloatStateOf(0F) }

    val scalingTitleMlcData = toolbar.firstOrNull<ScalingTitleMlcData>()

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = 16.dp,
                        end = 4.dp,
                        bottom = 16.dp
                    )
            ) {
                // Currently BottomSheetScreen supports only ScalingTitleMlc
                scalingTitleMlcData?.let { data ->
                    ScalingTitleMlc(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = data,
                        lazyListState = lazyListState,
                        alphaCallback = { newAlpha ->
                            alpha = newAlpha
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .noRippleClickable {
                        onEvent(
                            UIAction(
                                actionKey = UIActionKeysCompose.BOTTOM_SHEET_DISMISS
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Black)
                )
            }
        }
        if (scalingTitleMlcData != null) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .alpha(alpha),
                thickness = 1.dp,
                color = PeriwinkleGray
            )
        }
    }
}

@Preview
@Composable
fun BottomSheetScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        BottomSheetScreen(
            toolbar = SnapshotStateList<UIElementData>().apply {
                add(
                    ScalingTitleMlcData(
                        label = "Title"
                    )
                )
            },
            body = SnapshotStateList<UIElementData>().apply {
                add(
                    TextLabelMlcData(
                        text = UiText.DynamicString(LoremIpsum(300).values.joinToString())
                    )
                )
            },
            bottom = SnapshotStateList<UIElementData>().apply {
                add(
                    BottomGroupOrgData(
                        primaryButton = BtnPrimaryDefaultAtmData(
                            title = UiText.DynamicString("Button"),
                        )
                    )
                )
            },
            onEvent = {
                /* no-op */
            }
        )
    }
}