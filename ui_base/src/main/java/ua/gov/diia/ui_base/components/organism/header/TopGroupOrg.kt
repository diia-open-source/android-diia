package ua.gov.diia.ui_base.components.organism.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.chip.ChipMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlc
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeDataV2
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrg
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData
import ua.gov.diia.ui_base.components.organism.input.toUiModel
import ua.gov.diia.ui_base.components.theme.Primary

@Composable
fun TopGroupOrg(
    modifier: Modifier = Modifier,
    data: TopGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .testTag(data.componentId?.asString() ?: "")) {
        data.titleGroupMlcData?.let {
            TitleGroupMlc(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.navigationPanelMlcData?.let {
            NavigationPanelMlc(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.chipTabsOrgData?.let {
            ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrg(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.searchInputMlcData?.let {
            ua.gov.diia.ui_base.components.molecule.input.SearchInputMlc(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.searchBarOrgData?.let {
            SearchBarOrg(
                data = it,
                onUIAction = onUIAction
            )
        }
    }
}

data class TopGroupOrgData(
    val actionKey: String = UIActionKeysCompose.TOP_GROUP_ORG,
    val componentId: UiText? = null,
    val navigationPanelMlcData: NavigationPanelMlcData? = null,
    val chipTabsOrgData: ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrgData? = null,
    val searchInputMlcData: SearchInputMlcData? = null,
    val searchBarOrgData: SearchBarOrgData? = null,
    val titleGroupMlcData: TitleGroupMlcData? = null
) : UIElementData

@Preview
@Composable
fun TopGroupOrgPreview_NavigationPanel_and_SearchBarOrgData() {
    val searchBar = SearchBarOrgData(
        componentId = "001".toDynamicStringOrNull(),
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
    )
    val data = TopGroupOrgData(
        navigationPanelMlcData = NavigationPanelMlcData(
            title = UiText.DynamicString("Title"),
            isContextMenuExist = true
        ),
        searchBarOrgData = searchBar
    )
    TopGroupOrg(
        modifier = Modifier.background(color = Primary),
        data = data) {

    }
}

@Preview
@Composable
fun TopGroupOrgPreview_NavigationPanel_and_SearchInputMlcData() {
    val searchInput = SearchInputMlcData(
        id = "",
        searchFieldValue = UiText.DynamicString(""),
        placeholder = UiText.DynamicString("Search keyword"),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CANCEL.code),
    )
    val data = TopGroupOrgData(
        navigationPanelMlcData = NavigationPanelMlcData(
            title = UiText.DynamicString("Title"),
            isContextMenuExist = true
        ),
        searchInputMlcData = searchInput
    )
    TopGroupOrg(
        modifier = Modifier.background(color = Primary),
        data = data) {

    }
}

@Preview
@Composable
fun TopGroupOrgPreview_navigationPanel() {
    val data = TopGroupOrgData(
        navigationPanelMlcData = NavigationPanelMlcData(
            title = UiText.DynamicString("Title"),
            isContextMenuExist = true
        )
    )
    TopGroupOrg(data = data) {

    }
}

@Preview
@Composable
fun TopGroupOrgPreview_NavigationPanel_and_ChipTabBarMoleculeData() {
    val chips = ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrgData(
        chips = SnapshotStateList<ChipMlcData>().apply {
            add(
                ChipMlcData(
                    id = "cm01",
                    label = UiText.DynamicString("Label 01"),
                    code = "inProgress",
                    selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                    selectionState = UIState.Selection.Selected
                )
            )
            add(
                ChipMlcData(
                    id = "cm02",
                    label = UiText.DynamicString("Label 02"),
                    code = "inProgress",
                    selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                    selectionState = UIState.Selection.Unselected
                )
            )
        }
    )
    val data = TopGroupOrgData(
        navigationPanelMlcData = NavigationPanelMlcData(
            title = UiText.DynamicString("Title"),
            isContextMenuExist = true
        ),
        chipTabsOrgData = chips
    )
    TopGroupOrg(
        modifier = Modifier.background(color = Primary),
        data = data) {

    }
}

@Preview
@Composable
fun TopGroupOrgPreview_TitleGroupMlc() {
    val tabs = ChipTabsOrgData(tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
        add(
            ChipTabMoleculeDataV2(
                id = "1",
                title = "Label 1",
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            ChipTabMoleculeDataV2(
                id = "2",
                title = "Label 2",
                selectionState = UIState.Selection.Unselected
            )
        )

    })
    val data = TopGroupOrgData(
        navigationPanelMlcData = null,
        chipTabsOrgData = null,
        titleGroupMlcData = TitleGroupMlcData(
            leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                code = DiiaResourceIcon.MENU.code,
                accessibilityDescription = "123".toDynamicString(),
                action = DataActionWrapper(
                    type = "type",
                    subtype = "subtype",
                    resource = "resource"
                )
            ),
            mediumIconRight = TitleGroupMlcData.MediumIconRight(
                code = DiiaResourceIcon.MENU.code,
                action = DataActionWrapper(
                    type = "type",
                    subtype = "subtype",
                    resource = "resource"
                )
            ),
            heroText = UiText.DynamicString("Hero text"),
            label = UiText.DynamicString("label"),
        )
    )
    TopGroupOrg(data = data) {

    }
}

