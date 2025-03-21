package ua.gov.diia.ui_base.components.infrastructure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.BodyRootLazyContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.BottomBarRootContainer
import ua.gov.diia.ui_base.components.infrastructure.screen.ComposeRootScreen
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.GullGray

@Deprecated("Use BottomSheetScreen instead")
@Composable
fun OldBottomSheetScreen(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean> = Pair("", true),
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    toolbar: SnapshotStateList<UIElementData>,
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>,
    onEvent: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .background(GullGray)
            .fillMaxSize()
            .provideTestTagsAsResourceId()
    ) {
        BackHandler {
            onEvent(UIAction(actionKey = body.firstOrNull {
                it is NavigationPanelMlcData
            }?.let {
                (it as NavigationPanelMlcData).backAction
            } ?: UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
        }
        ComposeRootScreen(
            modifier = Modifier
                .padding(top = 44.dp)
                .background(
                    color = BlackSqueeze,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                ),
            contentLoaded = contentLoaded,
            toolbar = null,
            body = {
                BodyRootLazyContainer(
                    modifier = Modifier,
                    bodyViews = body,
                    displayBlockDivider = bottom.isNotEmpty(),
                    progressIndicator = progressIndicator,
                    contentLoaded = contentLoaded,
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
}

@Preview
@Composable
fun BottomSheetPreivew() {
    val bodyData = SnapshotStateList<UIElementData>().apply {
        add(SheetNavigationBarMoleculeData(title = UiText.DynamicString("Title")))
        add(TextLabelMlcData(text = UiText.DynamicString(LoremIpsum(300).values.joinToString())))
        add(
            CheckboxBtnOrgData(
                options = listOf(
                    CheckboxSquareMlcData(
                        id = "1",
                        title = UiText.DynamicString("Title1"),
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected
                    )
                ),
                buttonData = BtnPrimaryDefaultAtmData(
                    title = UiText.DynamicString("Next"),
                )
            )
        )
    }
    OldBottomSheetScreen(
        toolbar = SnapshotStateList<UIElementData>(),
        body = bodyData,
        bottom = SnapshotStateList<UIElementData>()
    ) {

    }
}