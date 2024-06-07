package ua.gov.diia.ui_base.mappers

import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.models.common_compose.general.Body
import ua.gov.diia.core.models.common_compose.general.BottomGroup
import ua.gov.diia.core.models.common_compose.general.TopGroup
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.org.header.TopGroupOrg
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.space.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.button.toUIModel
import ua.gov.diia.ui_base.components.molecule.card.toUIModel
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel
import ua.gov.diia.ui_base.components.molecule.divider.toUiModel
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.list.radio.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.molecule.text.toUIModel
import ua.gov.diia.ui_base.components.molecule.tile.toUIModel
import ua.gov.diia.ui_base.components.organism.bottom.toUiModel
import ua.gov.diia.ui_base.components.organism.document.toUIModel
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.toUIModel
import ua.gov.diia.ui_base.components.organism.list.toUIModel
import ua.gov.diia.ui_base.components.organism.sharing.toUIModel
import ua.gov.diia.ui_base.components.organism.table.toUIModel
import ua.gov.diia.ui_base.components.organism.toUIModel
import ua.gov.diia.ui_base.components.organism.toUiModel
import ua.gov.diia.ui_base.util.toUiModel
import ua.gov.diia.ui_base.util.toUiModellistItemGroupOrg

fun List<TopGroup>.getEllipseMenu(): List<ContextMenuItem>? {
    return this.firstOrNull {
        it.topGroupOrg != null
    }?.topGroupOrg?.navigationPanelMlc?.ellipseMenu
}

fun TopGroup.mapToComposeTopData(isContextMenuExist: Boolean): UIElementData? {
    this.topGroupOrg?.let {
        return it.toTopGroup(
            navigationPanelMlc = it.navigationPanelMlc,
            isContextMenuExist = isContextMenuExist
        ).toUIElement()
    }
    return null
}

fun TopGroupOrg?.toTopGroup(
    actionKey: String = UIActionKeysCompose.TOP_GROUP_ORG,
    navigationPanelMlc: NavigationPanelMlc?,
    chipTabsOrgData: ChipTabsOrgData? = null,
    isContextMenuExist: Boolean? = null,
): TopGroupOrgData? {
    if (this == null) return null
    return TopGroupOrgData(
        actionKey = actionKey,
        navigationPanelMlcData = NavigationPanelMlcData(
            title = navigationPanelMlc?.label.toDynamicStringOrNull(),
            isContextMenuExist = isContextMenuExist
                ?: !navigationPanelMlc?.ellipseMenu.isNullOrEmpty(),
            componentId = navigationPanelMlc?.componentId.toDynamicStringOrNull(),
        ),
        chipTabsOrgData = chipTabsOrgData
    )
}

// DiiaResponse.Body
fun Body.mapToComposeBodyData(): UIElementData? {
    this.textLabelContainerMlc?.let {
        return it.toUiModel().toUIElement() //todo import conflict
    }
    this.titleLabelMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.textLabelMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.attentionMessageMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.stubMessageMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.statusMessageMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.tableBlockOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.tableBlockPlaneOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.tableBlockAccordionOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.fullScreenVideoOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.listItemGroupOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.cardMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.questionFormsOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.checkboxRoundGroupOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.radioBtnGroupOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.btnPrimaryDefaultAtm?.let {
        return it.toUIModel().toUIElement()
    }
    this.btnStrokeDefaultAtm?.let {
        return it.toUIModel().toUIElement()
    }
    this.btnStrokeWhiteAtm?.let {
        return it.toUIModel()
    }
    this.btnPrimaryWideAtm?.let {
        return it.toUIModel()
    }
    this.btnPlainAtm?.let {
        return it.toUIModel().toUIElement()
    }
    this.checkboxBtnOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.spacerAtm?.let {
        return it.toUiModel(it.type).toUIElement()
    }
    this.serviceCardTileOrg?.let {
        return it.toUIModel().toUIElement()
    }
    this.mediaUploadGroupOrg?.let {
        return it.toUiModel().toUIElement()
    }
    this.fileUploadGroupOrg?.let {
        return it.toUiModel().toUIElement()
    }
    this.groupFilesAddOrg?.let {
        return it.toUiModel().toUIElement()
    }
    this.btnLoadIconPlainGroupMlc?.let {
        return it.toUIModel().toUIElement()
    }
    this.dividerLineMlc?.let {
        return it.toUiModel().toUIElement()
    }
    this.sharingCodesOrg?.let {
        return it.toUIModel()
    }
    return null
}

fun BottomGroup.mapToComposeBottomData(): UIElementData? {
    this.listItemGroupOrg?.let {
        return it.toUiModellistItemGroupOrg().toUIElement() //todo temp in mapper
    }
    this.bottomGroupOrg?.let {
        return it.toUiModel().toUIElement()
    }
    return null
}

private fun UIElementData?.toUIElement(): UIElementData? {
    if (this == null) return null
    return this
}