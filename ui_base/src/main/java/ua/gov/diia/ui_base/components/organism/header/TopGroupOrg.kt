package ua.gov.diia.ui_base.components.organism.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlc
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeDataV2
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrg
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData

@Composable
fun TopGroupOrg(
    modifier: Modifier = Modifier,
    data: TopGroupOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        data.titleGroupMlcData?.let {
            TitleGroupMlc(
                data = it,
                onUIAction = onUIAction,
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }
        data.navigationPanelMlcData?.let {
            NavigationPanelMlc(
                data = data.navigationPanelMlcData,
                onUIAction = onUIAction
            )
        }
        data.chipTabsOrgData?.let {
            ChipTabsOrg(
                data = it,
                onUIAction = onUIAction
            )
        }
    }

}

data class TopGroupOrgData(
    val actionKey: String = UIActionKeysCompose.TOP_GROUP_ORG,
    val navigationPanelMlcData: NavigationPanelMlcData? = null,
    val chipTabsOrgData: ChipTabsOrgData? = null,
    val titleGroupMlcData: TitleGroupMlcData? = null
) : UIElementData

@Preview
@Composable
fun TopGroupOrgPreview_navigationPanel() {
    val data = TopGroupOrgData(
        navigationPanelMlcData = NavigationPanelMlcData(
            title = UiText.DynamicString("Title"),
            isContextMenuExist = true
        )
    )
    TopGroupOrg(
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    ) {

    }
}

@Preview
@Composable
fun TopGroupOrgPreview_NavigationPanel_and_ChipTabBarMoleculeData() {
    val tabs =
        ChipTabsOrgData(tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
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
        navigationPanelMlcData = NavigationPanelMlcData(
            title = UiText.DynamicString("Title"),
            isContextMenuExist = true
        ),
        chipTabsOrgData = tabs
    )
    TopGroupOrg(
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}

@Preview
@Composable
fun TopGroupOrgPreview_TitleGroupMlc() {
    val tabs =
        ChipTabsOrgData(tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
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
                code = CommonDiiaResourceIcon.MENU.code,
                accessibilityDescription = "123".toDynamicString(),
                action = DataActionWrapper(
                    type = "type",
                    subtype = "subtype",
                    resource = "resource"
                )
            ),
            mediumIconRight = TitleGroupMlcData.MediumIconRight(
                code = CommonDiiaResourceIcon.MENU.code,
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
    TopGroupOrg(
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}

