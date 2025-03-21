package ua.gov.diia.ui_base.mappers

import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.models.common_compose.general.Body
import ua.gov.diia.core.models.common_compose.general.BottomGroup
import ua.gov.diia.core.models.common_compose.general.TopGroup
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.mlc.header.TitleGroupMlc
import ua.gov.diia.core.models.common_compose.org.header.TopGroupOrg
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.media.toUiModel
import ua.gov.diia.ui_base.components.atom.space.toUiModel
import ua.gov.diia.ui_base.components.atom.text.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.button.toUIModel
import ua.gov.diia.ui_base.components.molecule.card.toUIModel
import ua.gov.diia.ui_base.components.molecule.card.toUiModel
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel
import ua.gov.diia.ui_base.components.molecule.divider.toUiModel
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUIModel
import ua.gov.diia.ui_base.components.molecule.input.toUiModel
import ua.gov.diia.ui_base.components.molecule.list.radio.toUIModel
import ua.gov.diia.ui_base.components.molecule.media.toUiModel
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.toUiModel
import ua.gov.diia.ui_base.components.molecule.text.toUIModel
import ua.gov.diia.ui_base.components.molecule.text.toUiModel
import ua.gov.diia.ui_base.components.molecule.tile.toUIModel
import ua.gov.diia.ui_base.components.organism.bottom.toUiModel
import ua.gov.diia.ui_base.components.organism.calendar.toUiModel
import ua.gov.diia.ui_base.components.organism.carousel.toUiModel
import ua.gov.diia.ui_base.components.organism.checkbox.toUIModel
import ua.gov.diia.ui_base.components.organism.chip.toUiModel
import ua.gov.diia.ui_base.components.organism.document.toUIModel
import ua.gov.diia.ui_base.components.organism.document.toUiModel
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.SearchBarOrgData
import ua.gov.diia.ui_base.components.organism.input.toUIModel
import ua.gov.diia.ui_base.components.organism.input.toUiModel
import ua.gov.diia.ui_base.components.organism.list.toUIModel
import ua.gov.diia.ui_base.components.organism.list.toUiModel
import ua.gov.diia.ui_base.components.organism.photo.toUiModel
import ua.gov.diia.ui_base.components.organism.radio.toUiModel
import ua.gov.diia.ui_base.components.organism.sharing.toUIModel
import ua.gov.diia.ui_base.components.organism.table.toUIModel
import ua.gov.diia.ui_base.components.organism.tile.toUIModel
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
            chipTabsOrgData = it.chipTabsOrg?.toUiModel(),
            isContextMenuExist = isContextMenuExist,
            titleGroupMlc = it.titleGroupMlc
        )
    }
    this.searchInputMlc?.let {
        return it.toUiModel()
    }
    this.chipTabsOrg?.let {
        return it.toUiModel()
    }
    this.searchBarOrg?.let {
        return it.toUiModel()
    }
    this.mapChipTabsOrg?.let {
        return it.toUiModel()
    }
    this.scalingTitleMlc?.let {
        return it.toUiModel()
    }
    return null
}

fun TopGroupOrg?.toTopGroup(
    actionKey: String = UIActionKeysCompose.TOP_GROUP_ORG,
    navigationPanelMlc: NavigationPanelMlc?,
    chipTabsOrgData: ua.gov.diia.ui_base.components.organism.chip.ChipTabsOrgData? = null,
    searchInputMlcData: SearchInputMlcData? = null,
    searchBarOrgData: SearchBarOrgData? = null,
    isContextMenuExist: Boolean? = null,
    titleGroupMlc: TitleGroupMlc? = null
): TopGroupOrgData? {
    if (this == null) return null
    return TopGroupOrgData(
        actionKey = actionKey,
        navigationPanelMlcData = navigationPanelMlc?.toUiModel(),
        titleGroupMlcData = titleGroupMlc?.toUiModel(),
        chipTabsOrgData = chipTabsOrgData,
        searchInputMlcData = searchInputMlcData,
        searchBarOrgData = searchBarOrgData
    )
}

// DiiaResponse.Body
fun Body.mapToComposeBodyData(pagination: (() -> UIElementData)? = null): UIElementData? {
    this.textLabelContainerMlc?.let {
        return it.toUiModel() //todo import conflict
    }
    this.titleLabelMlc?.let {
        return it.toUIModel()
    }
    this.subtitleLabelMlc?.let {
        return it.toUiModel()
    }
    this.textLabelMlc?.let {
        return it.toUIModel()
    }
    this.attentionMessageMlc?.let {
        return it.toUIModel()
    }
    this.attentionIconMessageMlc?.let {
        return it.toUiModel()
    }
    this.stubMessageMlc?.let {
        return it.toUIModel()
    }
    this.statusMessageMlc?.let {
        return it.toUIModel()
    }
    this.tableBlockOrg?.let {
        return it.toUIModel()
    }
    this.tableBlockPlaneOrg?.let {
        return it.toUIModel()
    }
    this.tableBlockAccordionOrg?.let {
        return it.toUIModel()
    }
    this.fullScreenVideoOrg?.let {
        return it.toUIModel()
    }
    this.listItemGroupOrg?.let {
        return it.toUIModel()
    }
    this.cardMlc?.let {
        return it.toUIModel()
    }
    this.questionFormsOrg?.let {
        return it.toUIModel()
    }
    this.checkboxRoundGroupOrg?.let {
        return it.toUIModel()
    }
    this.radioBtnGroupOrg?.let {
        return it.toUIModel()
    }
    this.btnPrimaryDefaultAtm?.let {
        return it.toUIModel()
    }
    this.btnStrokeDefaultAtm?.let {
        return it.toUIModel()
    }
    this.btnStrokeWhiteAtm?.let {
        return it.toUIModel()
    }
    this.btnPrimaryWideAtm?.let {
        return it.toUIModel()
    }
    this.btnPlainAtm?.let {
        return it.toUIModel()
    }
    this.checkboxBtnOrg?.let {
        return it.toUIModel()
    }
    this.spacerAtm?.let {
        return it.toUiModel(it.type)
    }
    this.serviceCardTileOrg?.let {
        return it.toUIModel()
    }
    this.mediaUploadGroupOrg?.let {
        return it.toUiModel()
    }
    this.singleMediaUploadGroupOrg?.let {
        return it.toUiModel()
    }
    this.fileUploadGroupOrg?.let {
        return it.toUiModel()
    }
    this.groupFilesAddOrg?.let {
        return it.toUiModel()
    }
    this.btnLoadIconPlainGroupMlc?.let {
        return it.toUIModel()
    }
    this.dividerLineMlc?.let {
        return it.toUiModel()
    }
    this.btnIconPlainGroupMlc?.let {
        return it.toUIModel()
    }
    this.tableBlockPlaneOrg?.let {
        return it.toUIModel()
    }
    this.paymentInfoOrg?.let {
        return it.toUiModel()
    }
    this.calendarOrg?.let {
        return it.toUiModel()
    }
    this.editAutomaticallyDeterminedValueOrg?.let {
        return it.toUIModel()
    }
    this.sharingCodesOrg?.let {
        return it.toUIModel()
    }
    this.radioBtnWithAltOrg?.let {
        return it.toUiModel()
    }
    this.alertCardMlc?.let {
        return it.toUiModel()
    }
    this.dashboardCardTileOrg?.let {
        return it.toUIModel()
    }
    this.inputNumberLargeAtm?.let {
        return it.toUIModel()
    }
    this.inputNumberMlc?.let {
        return it.toUIModel()
    }
    this.articlePicAtm?.let {
        return it.toUiModel()
    }
    this.subtitleLabelMlc?.let {
        return it.toUiModel()
    }
    this.docHeadingOrg?.let {
        return it.toUiModel()
    }
    this.largeTickerAtm?.let {
        return it.toUiModel()
    }
    this.chipBlackGroupOrg?.let {
        return it.toUiModel()
    }
    this.inputPhoneCodeOrg?.let {
        return it.toUIModel()
    }
    this.paginationListWhiteOrg?.let {
        return pagination?.invoke()
    }
    this.paginationListOrg?.let {
        return pagination?.invoke()
    }
    this.checkboxBtnWhiteOrg?.let {
        return it.toUIModel()
    }
    this.smallCheckIconOrg?.let {
        return it.toUIModel()
    }
    this.photoGroupOrg?.let {
        return it.toUiModel()
    }
    this.imageCardCarouselOrg?.let {
        return it.toUiModel()
    }
    this.articleVideoMlc?.let {
        return it.toUiModel()
    }
    this.sectionTitleAtm?.let {
        return it.toUiModel()
    }
    this.cardMlcV2?.let {
        return it.toUIModel()
    }
    this.loopingVideoPlayerCardMlc?.let {
        return it.toUiModel()
    }
    return null
}

fun BottomGroup.mapToComposeBottomData(): UIElementData? {
    this.listItemGroupOrg?.let {
        return it.toUiModellistItemGroupOrg()
    }
    this.bottomGroupOrg?.let {
        return it.toUiModel()
    }
    this.tickerAtm?.let {
        return it.toUiModel()
    }
    return null
}