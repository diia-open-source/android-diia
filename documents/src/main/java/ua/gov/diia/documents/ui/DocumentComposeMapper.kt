package ua.gov.diia.documents.ui

import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.documents.barcode.DocumentBarcodeResult
import ua.gov.diia.documents.models.DocumentCard
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.models.docgroups.v2.DocButtonHeadingOrg
import ua.gov.diia.documents.models.docgroups.v2.DocCover
import ua.gov.diia.documents.models.docgroups.v2.DocHeadingOrg
import ua.gov.diia.documents.models.docgroups.v2.QrCheckStatus
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.documents.models.docgroups.v2.SubtitleLabelMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.molecule.text.SubtitleLabelMlcData
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.organism.document.AddDocOrgData
import ua.gov.diia.ui_base.components.organism.document.ContentTableOrgData
import ua.gov.diia.ui_base.components.organism.document.DocCodeOrgData
import ua.gov.diia.ui_base.components.organism.document.DocErrorOrgData
import ua.gov.diia.ui_base.components.organism.document.DocHeadingOrgData
import ua.gov.diia.ui_base.components.organism.document.DocOrgData
import ua.gov.diia.ui_base.components.organism.document.DocPhotoOrgData
import ua.gov.diia.ui_base.components.organism.pager.DocCardFlipData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg

interface DocumentComposeMapper {

    fun DocHeadingOrg?.toComposeDocHeadingOrg(): DocHeadingOrgData?

    fun TickerAtm?.toComposeTickerAtm(
        isTickerClickable: Boolean = false
    ): TickerAtomData?

    fun SubtitleLabelMlc?.toComposeSubtitleLabelMlc(): SubtitleLabelMlcData?

    fun SmallEmojiPanelMlc?.toComposeEmojiPanelMlc(): SmallEmojiPanelMlcData?

    fun toComposeDocError(status: QrCheckStatus): DocErrorOrgData?

    fun toComposeDocScanSubtitleLabel(status: QrCheckStatus): SubtitleLabelMlcData?

    fun toComposeDocOrgLoading(): DocPhotoOrgData

    fun toComposeAddDocOrg(docType: String, position: Int): AddDocOrgData

    fun toComposeDocPhoto(
        localisation: LocalizationType,
        photo: String?,
        valueImage: String?,
        isStack: Boolean,
        stackSize: Int,
        showCover: Boolean,
        cover: DocCover?,
        tableBlockOrg: List<TableBlockPlaneOrg>?,
        docHeadingOrg: DocHeadingOrg?,
        docButtonHeadingOrg: DocButtonHeadingOrg?,
        subtitleLabelMlc: SubtitleLabelMlc?,
        tableBlockTwoColumnsPlaneOrg: List<TableBlockTwoColumnsPlaneOrg>?,
        tickerAtm: TickerAtm?,
        isTickerClickable: Boolean = false,
        smallEmojiPanelMlc: SmallEmojiPanelMlc? = null
    ): DocPhotoOrgData

    fun toComposeDocCodeOrg(
        barcodeResult: DocumentBarcodeResult,
        localizationType: LocalizationType,
        showToggle: Boolean = true,
        isStack: Boolean = false
    ): DocCodeOrgData?

    fun toDocCardFlip(
        photo: String?,
        id: String?,
        position: Int,
        docType: String,
        barcodeResult: DocumentBarcodeResult?,
        localizationType: LocalizationType,
        valueImage: String?,
        isStack: Boolean,
        stackSize: Int,
        cover: DocCover?,
        tableBlockOrg: List<TableBlockPlaneOrg>?,
        docHeadingOrg: DocHeadingOrg?,
        docButtonHeadingOrg: DocButtonHeadingOrg?,
        subtitleLabelMlc: SubtitleLabelMlc?,
        tableBlockTwoColumnsPlaneOrg: List<TableBlockTwoColumnsPlaneOrg>?,
        tickerAtm: TickerAtm?,
        isTickerClickable: Boolean,
        smallEmojiPanelMlc: SmallEmojiPanelMlc? = null
    ): DocCardFlipData

    fun toDocOrg(
        id: String?,
        position: Int,
        docType: String,
        isStack: Boolean,
        stackSize: Int,
        cover: DocCover?,
        url: String,
        showCover: Boolean,
        docHeadingOrg: DocHeadingOrg?,
        docButtonHeadingOrg: DocButtonHeadingOrg?,
        chipStatusAtm: ChipStatusAtm?,
        placeHolder: Int
    ): DocOrgData

    fun toDocCarousel(
        cards: List<DocumentCard>,
        barcodeResult: DocumentBarcodeResult?
    ): DocCarouselOrgData

    fun toComposeContentTableOrg(
        tableBlockTwoColumnsOrg: List<TableBlockTwoColumnsOrg>?,
        tableBlockOrg: List<TableBlockOrg>?,
        photo: String?,
        valueImage: String?
    ): ContentTableOrgData
}


enum class ToggleId(val value: String) {
    qr("qr"),
    ean("ean");
}