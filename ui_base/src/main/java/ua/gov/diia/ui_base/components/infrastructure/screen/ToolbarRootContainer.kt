package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMolecule
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabBarMolecule
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

@Composable
fun ToolbarRootContainer(
    modifier: Modifier = Modifier,
    toolbarViews: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        toolbarViews.forEachIndexed { index, item ->
            when (item) {
                is NavigationPanelMlcData -> {
                    NavigationPanelMlc(
                        modifier = modifier
                            .fillMaxWidth(),
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is ChipTabBarMoleculeData -> {
                    ChipTabBarMolecule(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is SheetNavigationBarMoleculeData -> {
                    SheetNavigationBarMolecule(
                        modifier = Modifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TopGroupOrgData -> {
                    TopGroupOrg(
                        data = item,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ToolbarRootContainerPreview() {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData
    _toolbarData.add(
        NavigationPanelMlcData(
            title = UiText.DynamicString("єВиплати"),
            isContextMenuExist = true
        )
    )
    ToolbarRootContainer(toolbarViews = toolbarData) {

    }
}

@Preview
@Composable
fun ToolbarRootContainerPreview_2() {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData
    _toolbarData.addAllIfNotNull(
        NavigationPanelMlcData(
            title = UiText.DynamicString("єВиплати"),
            isContextMenuExist = true
        ),
        ChipTabBarMoleculeData(
            tabs = SnapshotStateList<ChipTabMoleculeData>().addAllIfNotNull(
                ChipTabMoleculeData(
                    id = "1",
                    title = "Label 1",
                    counter = 0,
                    selectionState = UIState.Selection.Selected
                ),
                ChipTabMoleculeData(
                    id = "2",
                    title = "Label 1",
                    counter = 0,
                    selectionState = UIState.Selection.Selected
                )
            )
        )

    )
    ToolbarRootContainer(toolbarViews = toolbarData) {

    }
}