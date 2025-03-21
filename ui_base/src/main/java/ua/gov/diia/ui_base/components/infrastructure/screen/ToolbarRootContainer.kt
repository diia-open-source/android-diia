package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.chip.ChipMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMolecule
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabBarMolecule
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlc
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrgData
import ua.gov.diia.ui_base.components.organism.chip.MapChipTabsOrganism
import ua.gov.diia.ui_base.components.organism.chip.MapChipTabsOrganismData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrg
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData
import ua.gov.diia.ui_base.components.theme.Primary

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

                is ChipTabsOrgData -> {
                    ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrg(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is MapChipTabsOrganismData -> {
                    MapChipTabsOrganism(
                        data = item,
                        onUIAction = onUIAction,
                        modifier = modifier.padding(top = 12.dp)
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

                is SearchInputMlcData -> {
                    SearchInputMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is SearchBarOrgData -> {
                    SearchBarOrg(
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
    ToolbarRootContainer(
        modifier = Modifier.background(color = Primary),
        toolbarViews = toolbarData) {}
}

@Preview
@Composable
fun ToolbarRootContainerPreview_SearchTab() {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData
    _toolbarData.addAllIfNotNull(
        NavigationPanelMlcData(
            title = UiText.DynamicString("Service's title"),
            isContextMenuExist = true
        ),
        SearchInputMlcData(
            id = "",
            searchFieldValue = UiText.DynamicString(""),
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CANCEL.code)
        )
    )
    ToolbarRootContainer(
        modifier = Modifier.background(color = Primary),
        toolbarViews = toolbarData) {}
}

@Preview
@Composable
fun ToolbarRootContainerPreview_SearchBar() {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData
    _toolbarData.addAllIfNotNull(
        NavigationPanelMlcData(
            title = UiText.DynamicString("Service's title"),
            isContextMenuExist = true
        ),
        SearchBarOrgData(
            componentId = UiText.DynamicString("001"),
            searchInputMlc = SearchInputMlcData(
                id = "",
                placeholder = UiText.DynamicString("Search keyword"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
                iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
            ),
            btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
                icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
                label = "Label".toDynamicString(),
                badge = BadgeCounterAtmData(1),
                interactionState = UIState.Interaction.Enabled
            )
        )
    )
    ToolbarRootContainer(
        modifier = Modifier.background(color = Primary),
        toolbarViews = toolbarData) {}
}

@Preview
@Composable
fun ToolbarRootContainerPreview_SearchBar_Chips() {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData
    _toolbarData.addAllIfNotNull(
        NavigationPanelMlcData(
            title = UiText.DynamicString("Service's title"),
            isContextMenuExist = true
        ),
        SearchBarOrgData(
            componentId = UiText.DynamicString("001"),
            searchInputMlc = SearchInputMlcData(
                id = "",
                placeholder = UiText.DynamicString("Search keyword"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
                iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
            ),
            btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
                icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
                interactionState = UIState.Interaction.Enabled
            )
        ),
        ChipTabsOrgData(chips = SnapshotStateList<ChipMlcData>().apply {
            add(
                ChipMlcData(
                    id = "1",
                    label = UiText.DynamicString("label 1"),
                    code = "inProgress",
                    selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                    selectionState = UIState.Selection.Selected
                )
            )
            add(
                ChipMlcData(
                    id = "2",
                    label = UiText.DynamicString("label 2"),
                    code = "inProgress",
                    selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipMlcData(
                    id = "3",
                    label = UiText.DynamicString("label 3"),
                    code = "inProgress",
                    selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                    selectionState = UIState.Selection.Unselected
                )
            )

        }
        )
    )
    ToolbarRootContainer(
        modifier = Modifier.background(color = Primary),
        toolbarViews = toolbarData) {}
}