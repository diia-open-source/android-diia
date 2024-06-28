package ua.gov.diia.ui_base.components.infrastructure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDetailsScreen(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>? = null,
    onEvent: (UIAction) -> Unit
) {

    var openBottomSheet by remember {
        mutableStateOf(true)
    }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            containerColor = BlackSqueeze,
            scrimColor = Color.Transparent,
            onDismissRequest = {
                onEvent(
                    UIAction(
                        actionKey = UIActionKeysCompose.BOTTOM_SHEET_DISMISS
                    )
                )
                openBottomSheet = false
            },
            dragHandle = {
                Spacer(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        Gray, RoundedCornerShape(4.dp)
                    )
            )
            },
            content = {
                BodyRootContainer(
                    bodyViews = body,
                    displayBlockDivider = bottom != null && bottom.size != 0,
                    progressIndicator = progressIndicator,
                    contentLoaded = contentLoaded,
                    onUIAction = onEvent
                )

                if (bottom != null && bottom.size != 0) {
                    BottomBarRootContainer(
                        bottomViews = bottom,
                        progressIndicator = progressIndicator,
                        onUIAction = onEvent
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun BottomSheetDetailsPreview() {
    val bodyData = SnapshotStateList<UIElementData>().apply {
        add(SheetNavigationBarMoleculeData(title = UiText.DynamicString("Title")))
        add(TextLabelMlcData(text = UiText.DynamicString(LoremIpsum(300).values.joinToString())))
    }
    val bottomData = SnapshotStateList<UIElementData>().apply {
        add(
            BottomGroupOrgData(
                BtnPrimaryDefaultAtmData(
                    title = UiText.DynamicString("Застосувати фільтр"),
                ),
                BtnPlainAtmData(
                    title = UiText.DynamicString("Очистити фільтр"),
                    actionKey = "",
                )
            )
        )
    }
    BottomSheetDetailsScreen(
        body = bodyData,
        bottom = bottomData
    ) {}
}
