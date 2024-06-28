package ua.gov.diia.opensource.helper.documents

import androidx.compose.runtime.toMutableStateList
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.network.Http
import ua.gov.diia.documents.barcode.DocumentBarcodeErrorLoadResult
import ua.gov.diia.documents.barcode.DocumentBarcodeResult
import ua.gov.diia.documents.barcode.DocumentBarcodeSuccessfulLoadResult
import ua.gov.diia.documents.models.DocumentCard
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.models.docgroups.v2.DocButtonHeadingOrg
import ua.gov.diia.documents.models.docgroups.v2.DocCover
import ua.gov.diia.documents.models.docgroups.v2.DocHeadingOrg
import ua.gov.diia.documents.models.docgroups.v2.QrCheckStatus
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.documents.models.docgroups.v2.SubtitleLabelMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.documents.ui.DocumentComposeMapper
import ua.gov.diia.documents.ui.ToggleId
import ua.gov.diia.documents.ui.gallery.DocActions
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.ui.compose.toComposeTableBlockOrg
import ua.gov.diia.opensource.ui.compose.toComposeTableBlockPlaneOrgData
import ua.gov.diia.opensource.ui.compose.toComposeTableBlockTwoColumnsOrg
import ua.gov.diia.opensource.ui.compose.toComposeTableBlockTwoColumnsPlaneOrg
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.molecule.card.WhiteMenuCardMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.doc.StackMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.text.SubtitleLabelMlcData
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.organism.document.AddDocOrgData
import ua.gov.diia.ui_base.components.organism.document.ContentTableOrgData
import ua.gov.diia.ui_base.components.organism.document.DocButtonHeadingOrgData
import ua.gov.diia.ui_base.components.organism.document.DocCodeOrgData
import ua.gov.diia.ui_base.components.organism.document.DocErrorOrgData
import ua.gov.diia.ui_base.components.organism.document.DocHeadingOrgData
import ua.gov.diia.ui_base.components.organism.document.DocOrgData
import ua.gov.diia.ui_base.components.organism.document.DocPhotoOrgData
import ua.gov.diia.ui_base.components.organism.document.Localization
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupData
import ua.gov.diia.ui_base.components.organism.pager.DocCardFlipData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.doc.toUiModel
import ua.gov.diia.ui_base.util.toDataActionWrapper
import ua.gov.diia.ui_base.util.toUiModel
import javax.inject.Inject

class DocumentComposeMapperImpl @Inject constructor() : DocumentComposeMapper {

    private fun getIconByCode(code: String): Int {
        return when (code) {
            "copy" -> R.drawable.ic_copy_settings
            else -> {
                R.drawable.ic_copy_settings
            }
        }
    }

    override fun SmallEmojiPanelMlc?.toComposeEmojiPanelMlc(): SmallEmojiPanelMlcData? {
        if (this == null) return null
        val text = label ?: return null
        val code = icon?.code ?: return null
        return SmallEmojiPanelMlcData(
            text = UiText.DynamicString(text),
            icon = UiIcon.DrawableResource(code = code)
        )
    }

    override fun SubtitleLabelMlc?.toComposeSubtitleLabelMlc(): SubtitleLabelMlcData? {
        val text = this?.label ?: return null
        return SubtitleLabelMlcData(label = UiText.DynamicString(text))
    }

    override fun toComposeDocError(status: QrCheckStatus): DocErrorOrgData? {
        return when (status) {
            QrCheckStatus.STATUS_NO_NETWORK -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_docs_no_internet_message),
                    ticker = TickerAtomData(
                        title = "Немає інтернету • Немає інтернету • Немає інтернету • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_UNKNOWN_CODE_TYPE -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.qr_code_not_registered),
                    ticker = TickerAtomData(
                        title = "Невідомий QR-код • Невідомий QR-код • Невідомий QR-код • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_DOC_NOT_LOADED_ERROR -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_error_not_found),
                    ticker = TickerAtomData(
                        title = "Документ не знайдено • Документ не знайдено • Документ не знайдено • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_QR_CODE_TIME_OUT -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_error_timeout),
                    ticker = TickerAtomData(
                        title = "Документ не знайдено • Документ не знайдено • Документ не знайдено • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_CODE_NO_REGISTRY -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_docs_no_registry_message),
                    ticker = TickerAtomData(
                        title = "Реєстр недоступний • Реєстр недоступний • Реєстр недоступний • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_CERT_VERIFICATION_INVALID -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_error_cert),
                    ticker = TickerAtomData(
                        title = "Документ не знайдено • Документ не знайдено • Документ не знайдено • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_CERT_VERIFICATION_EXPIRED -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_expired_error_cert),
                    ticker = TickerAtomData(
                        title = "Помилка валідації • Помилка валідації • Помилка валідації • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            QrCheckStatus.STATUS_CODE_TIME_OUT -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_http_timeout),
                    ticker = TickerAtomData(
                        title = "Помилка валідації • Помилка валідації • Помилка валідації • ",
                        type = TickerType.SMALL_NEGATIVE
                    )
                )
            }

            else -> {
                null
            }
        }
    }

    override fun toComposeDocScanSubtitleLabel(status: QrCheckStatus): SubtitleLabelMlcData {
        return when (status) {
            else -> {
                SubtitleLabelMlcData(
                    label = UiText.StringResource(R.string.qr_check_subtitle_checking_docs)
                )
            }
        }
    }

    override fun toComposeDocOrgLoading(): DocPhotoOrgData {
        return DocPhotoOrgData(
            docHeading = null,
            docButtonHeading = null,
            tickerAtomData = null,
            tableBlockOrgData = null
        )
    }

    override fun toComposeAddDocOrg(
        docType: String,
        position: Int
    ): AddDocOrgData {
        return AddDocOrgData(
            addDoc = WhiteMenuCardMlcData(
                title = "Додати документ",
                icon = UiText.StringResource(R.drawable.ic_add)
            ),
            changePosition = WhiteMenuCardMlcData(
                actionKey = UIActionKeysCompose.CHANGE_DOC_ORDER,
                title = "Змінити порядок\nдокументів",
                icon = UiText.StringResource(R.drawable.ic_doc_reorder)
            ),
            docType = docType,
            position = position
        )
    }

    private fun toComposeToggleButtonOrg(localizationType: LocalizationType): ToggleButtonGroupData {
        return ToggleButtonGroupData(
            qr = BtnToggleMlcData(
                id = ToggleId.qr.value,
                label = (if (localizationType == LocalizationType.ua) "QR-код" else "QR-code").toDynamicString(),
                iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
                iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
                selectionState = UIState.Selection.Selected,
                action = DataActionWrapper(
                    type = ToggleId.qr.value
                )
            ),
            ean13 = BtnToggleMlcData(
                id = ToggleId.ean.value,
                label = (if (localizationType == LocalizationType.ua) "Штрихкод" else "Barcode").toDynamicString(),
                iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
                iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
                selectionState = UIState.Selection.Unselected,
                action = DataActionWrapper(
                    type = ToggleId.ean.value
                )
            )
        )
    }

    private fun generateDocCoverData(
        docStatus: Int,
        localization: LocalizationType,
        verificationCodesCount: Int
    ): DocCover? {
        return when (docStatus) {
            Http.HTTP_1010 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_no_photo_title else R.string.dl_no_photo_title_eng,
                    description = if (localization == LocalizationType.ua) R.string.dl_to_e_queue else R.string.dl_to_e_queue_eng,
                    buttonTitle = if (localization == LocalizationType.ua) "Записатися" else "Electronic queue",
                    actionKey = DocActions.DOC_ACTION_IN_LINE,
                    verificationCodesCount = verificationCodesCount
                )
            }

            Http.HTTP_1011 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_outdated_title else R.string.dl_outdated_title_eng,
                    description = if (localization == LocalizationType.ua) R.string.dl_outdated_description else R.string.dl_outdated_description_eng,
                    buttonTitle = if (localization == LocalizationType.ua) "Записатися" else "Electronic queue",
                    actionKey = DocActions.DOC_ACTION_IN_LINE,
                    verificationCodesCount = verificationCodesCount
                )
            }

            Http.HTTP_1012 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_need_verify_title else R.string.dl_need_verify_title_eng,
                    description = if (localization == LocalizationType.ua) R.string.dl_need_verify_queue else R.string.dl_need_verify_queue_eng,
                    buttonTitle = if (localization == LocalizationType.ua) "Знайти адресу" else "Open Driver's Account",
                    actionKey = DocActions.DOC_ACTION_TO_DRIVER_ACCOUNT,
                    verificationCodesCount = verificationCodesCount
                )
            }

            Http.HTTP_1016 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_expiration_title else R.string.dl_expiration_title,
                    description = if (localization == LocalizationType.ua) R.string.dl_expiration_description else R.string.dl_expiration_description,
                    buttonTitle = if (localization == LocalizationType.ua) "Видалити документ" else "Delete document",
                    actionKey = "deleteDocument",
                    verificationCodesCount = verificationCodesCount
                )
            }

            else -> null
        }
    }

    private fun DocCover.toComposeDocCoverMlc(): DocCoverMlcData {
        return DocCoverMlcData(
            title = UiText.StringResource(this.title),
            description = UiText.StringResource(this.description),
            button = if (this.buttonTitle != null) ButtonStrokeAdditionalAtomData(
                actionKey = this.actionKey,
                title = UiText.DynamicString(this.buttonTitle ?: ""),
                id = "",
                interactionState = UIState.Interaction.Enabled
            ) else null
        )
    }


    override fun TickerAtm?.toComposeTickerAtm(
        isTickerClickable: Boolean
    ): TickerAtomData? {
        return this?.let {
            TickerAtomData(
                componentId = this.componentId.orEmpty(),
                title = it.value,
                type = when (this.type) {
                    TickerAtm.TickerType.warning -> TickerType.BIG_WARNING
                    TickerAtm.TickerType.positive -> TickerType.BIG_POSITIVE

                    else -> {
                        TickerType.BIG_WARNING
                    }
                },
                clickable = isTickerClickable
            )
        }
    }

    private fun DocButtonHeadingOrg?.toComposeDocButtonHeadingOrg(
        isStack: Boolean,
        stackSize: Int
    ): DocButtonHeadingOrgData? {
        return this?.let {
            DocButtonHeadingOrgData(
                componentId = this.componentId.orEmpty(),
                heading = if (this.headingWithSubtitlesMlc != null) HeadingWithSubtitlesMlcData(
                    componentId = this.headingWithSubtitlesMlc?.componentId.orEmpty(),
                    value = this.headingWithSubtitlesMlc?.value,
                    subtitles = if (!this.headingWithSubtitlesMlc?.subtitles.isNullOrEmpty()) this.headingWithSubtitlesMlc?.subtitles else null
                ) else null,
                headingWhite = if (this.headingWithSubtitleWhiteMlc != null) HeadingWithSubtitlesWhiteMlcData(
                    value = this.headingWithSubtitleWhiteMlc?.value,
                    subtitles = if (!this.headingWithSubtitleWhiteMlc?.subtitles.isNullOrEmpty()) this.headingWithSubtitleWhiteMlc?.subtitles else null
                ) else null,
                docNumberCopy = this.docNumberCopyMlc?.toUiModel(),
                docNumberCopyWhite = this.docNumberCopyWhiteMlc?.let {
                    DocNumberCopyWhiteMlcData(
                        value = this.docNumberCopyWhiteMlc?.value.toDynamicString(),
                        icon = this.docNumberCopyWhiteMlc?.icon?.let {
                            IconAtmData(
                                code = it.code,
                                action = it.action?.toDataActionWrapper()
                            )
                        })
                },
                iconAtmData = if (this.iconAtm != null) iconAtm?.toUiModel() else null,
                stackMlcData =
                if (this.docNumberCopyWhiteMlc != null || this.headingWithSubtitleWhiteMlc != null)
                    StackMlcData(
                        amount = stackSize,
                        smallIconAtmData = SmallIconAtmData(code = "stackWhite"),
                        isWhite = true
                    ) else
                    StackMlcData(
                        amount = stackSize,
                        smallIconAtmData = SmallIconAtmData(code = "stack")
                    ),
                isStack = isStack,
                size = stackSize
            )
        }
    }

    override fun DocHeadingOrg?.toComposeDocHeadingOrg(): DocHeadingOrgData? {
        return this?.let {
            DocHeadingOrgData(
                componentId = this.componentId.orEmpty(),
                heading = if (this.headingWithSubtitlesMlc != null) HeadingWithSubtitlesMlcData(
                    value = this.headingWithSubtitlesMlc?.value,
                    subtitles = if (!this.headingWithSubtitlesMlc?.subtitles.isNullOrEmpty()) this.headingWithSubtitlesMlc?.subtitles else null
                ) else null,
                headingWhite = if (this.headingWithSubtitleWhiteMlc != null) HeadingWithSubtitlesWhiteMlcData(
                    value = this.headingWithSubtitleWhiteMlc?.value,
                    subtitles = if (!this.headingWithSubtitleWhiteMlc?.subtitles.isNullOrEmpty()) this.headingWithSubtitleWhiteMlc?.subtitles else null
                ) else null,
                docNumber = this.docNumberCopyMlc?.toUiModel(),
                docNumberCopyWhite = this.docNumberCopyWhiteMlc?.let {
                    DocNumberCopyWhiteMlcData(
                        value = this.docNumberCopyWhiteMlc?.value.toDynamicString(),
                        icon = this.docNumberCopyWhiteMlc?.icon?.let {
                            IconAtmData(
                                code = it.code,
                                action = it.action?.toDataActionWrapper()
                            )
                        })
                }
            )
        }
    }

    override fun toComposeDocPhoto(
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
        isTickerClickable: Boolean,
        smallEmojiPanelMlc: SmallEmojiPanelMlc?
    ): DocPhotoOrgData {
        val doc = if (!showCover) {

            DocPhotoOrgData(
                docHeading = docHeadingOrg.toComposeDocHeadingOrg(),
                tickerAtomData = tickerAtm.toComposeTickerAtm(isTickerClickable),
                docButtonHeading = docButtonHeadingOrg.toComposeDocButtonHeadingOrg(
                    isStack,
                    stackSize
                ),
                tableBlockTwoColumns = tableBlockTwoColumnsPlaneOrg?.mapNotNull {
                    it.toComposeTableBlockTwoColumnsPlaneOrg(photo, valueImage)
                },
                tableBlockOrgData = tableBlockOrg?.mapNotNull {
                    it.toComposeTableBlockPlaneOrgData(valueImage)
                },
                subtitleLabelMlc = subtitleLabelMlc.toComposeSubtitleLabelMlc(),
                smallEmojiPanelMlcData = smallEmojiPanelMlc.toComposeEmojiPanelMlc()
            )
        } else {
            DocPhotoOrgData(
                docHeading = docHeadingOrg.toComposeDocHeadingOrg(),
                tickerAtomData = tickerAtm.toComposeTickerAtm(),
                docButtonHeading = docButtonHeadingOrg.toComposeDocButtonHeadingOrg(
                    isStack,
                    stackSize
                ),
                tableBlockTwoColumns = tableBlockTwoColumnsPlaneOrg?.mapNotNull {
                    it.toComposeTableBlockTwoColumnsPlaneOrg(photo, valueImage)
                },
                tableBlockOrgData = tableBlockOrg?.mapNotNull {
                    it.toComposeTableBlockPlaneOrgData(valueImage)
                },
                docCover = cover?.toComposeDocCoverMlc(),
                subtitleLabelMlc = subtitleLabelMlc.toComposeSubtitleLabelMlc(),
                smallEmojiPanelMlcData = smallEmojiPanelMlc.toComposeEmojiPanelMlc()
            )
        }
        return doc
    }


    override fun toComposeDocCodeOrg(
        barcodeResult: DocumentBarcodeResult,
        localizationType: LocalizationType,
        showToggle: Boolean,
        isStack: Boolean
    ): DocCodeOrgData {
        with(barcodeResult) {
            return when (this) {
                is DocumentBarcodeSuccessfulLoadResult -> DocCodeOrgData(
                    qrBitmap = this.shareQr.data.toAndroidBitmap(),
                    ean13Bitmap = this.shareEan13?.data?.toAndroidBitmap(),
                    eanCode = this.shareEanCode,
                    toggle = toComposeToggleButtonOrg(localizationType),
                    localization = when (localizationType) {
                        LocalizationType.ua -> Localization.ua
                        LocalizationType.eng -> Localization.eng
                    },
                    timerText = this.timerText,
                    showToggle = showToggle,
                    isStack = isStack
                )

                is DocumentBarcodeErrorLoadResult -> DocCodeOrgData(
                    qrBitmap = null,
                    eanCode = null,
                    ean13Bitmap = null,
                    toggle = toComposeToggleButtonOrg(localizationType),
                    localization = when (localizationType) {
                        LocalizationType.ua -> Localization.ua
                        LocalizationType.eng -> Localization.eng
                    },
                    exception = this.exception,
                    showToggle = showToggle,
                    isStack = isStack,
                    noRegistry = code
                )

                else -> {
                    DocCodeOrgData(
                        qrBitmap = null,
                        eanCode = null,
                        ean13Bitmap = null,
                        toggle = toComposeToggleButtonOrg(localizationType),
                        localization = when (localizationType) {
                            LocalizationType.ua -> Localization.ua
                            LocalizationType.eng -> Localization.eng
                        },
                        exception = null,
                        showToggle = showToggle,
                        isStack = isStack

                    )
                }
            }
        }
    }

    override fun toDocCardFlip(
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
        smallEmojiPanelMlc: SmallEmojiPanelMlc?
    ): DocCardFlipData {
        return DocCardFlipData(
            id = id,
            docType = docType,
            front = toComposeDocPhoto(
                localizationType,
                photo,
                valueImage,
                isStack,
                stackSize,
                cover != null,
                cover,
                tableBlockOrg,
                docHeadingOrg,
                docButtonHeadingOrg,
                subtitleLabelMlc,
                tableBlockTwoColumnsPlaneOrg,
                tickerAtm,
                isTickerClickable,
                smallEmojiPanelMlc
            ),
            back = barcodeResult?.let{
                toComposeDocCodeOrg(
                    it,
                    localizationType,
                    isStack = isStack
                )
            },
            position = position,
            enableFlip = cover == null
        )
    }

    override fun toDocOrg(
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
    ): DocOrgData {

        val docData = if (!showCover) {
            DocOrgData(
                imageUrl = url,
                position = position,
                docType = docType,
                docHeading = docHeadingOrg.toComposeDocHeadingOrg(),
                docButtonHeading = docButtonHeadingOrg.toComposeDocButtonHeadingOrg(
                    isStack,
                    stackSize
                ),
                chipStatusAtmData = chipStatusAtm?.toUiModel(),
                placeHolder = placeHolder


            )
        } else DocOrgData(
            imageUrl = url,
            position = position,
            docType = docType,
            docHeading = docHeadingOrg.toComposeDocHeadingOrg(),
            docButtonHeading = docButtonHeadingOrg.toComposeDocButtonHeadingOrg(
                isStack,
                stackSize
            ),
            chipStatusAtmData = chipStatusAtm?.toUiModel(),
            docCover = cover?.toComposeDocCoverMlc(),
            placeHolder = placeHolder
        )
        return docData
    }

    override fun toDocCarousel(
        cards: List<DocumentCard>,
        barcodeResult: DocumentBarcodeResult?
    ): DocCarouselOrgData {
        with(cards) {
            val listDocCards = this.mapNotNull { documentCard ->
                when (val doc = documentCard.doc.diiaDocument) {
                    is DriverLicenseV2.Data -> toDocCardFlip(
                        tickerAtm = doc.getTicker(),
                        tableBlockOrg = doc.getTableBlockPlaneOrg(),
                        docHeadingOrg = doc.getDocHeading(),
                        docButtonHeadingOrg = doc.getDocButtonHeading(),
                        subtitleLabelMlc = doc.getSubtitleLabel(),
                        tableBlockTwoColumnsPlaneOrg = doc.getTableBlockTwoColumnsPlane(),
                        barcodeResult = barcodeResult,
                        localizationType = doc.localization()
                            ?: LocalizationType.ua,
                        photo = doc.photo?.image,
                        id = doc.id,
                        docType = doc.getItemType(),
                        position = indexOf(documentCard),
                        valueImage = null,
                        isStack = documentCard.docCount > 1,
                        stackSize = documentCard.docCount,
                        cover = generateDocCoverData(
                            doc.getStatus(),
                            doc.localization() ?: LocalizationType.ua,
                            doc.verificationCodesCount()
                        ),
                        isTickerClickable = false

                    )

                    else -> null
                }
            }

            return DocCarouselOrgData(data = listDocCards.toMutableStateList())
        }
    }

    override fun toComposeContentTableOrg(
        tableBlockTwoColumnsOrg: List<TableBlockTwoColumnsOrg>?,
        tableBlockOrg: List<TableBlockOrg>?,
        photo: String?,
        valueImage: String?
    ): ContentTableOrgData {
        return ContentTableOrgData(
            tableBlockTwoColumnsOrgData = tableBlockTwoColumnsOrg?.mapNotNull {
                it.toComposeTableBlockTwoColumnsOrg(photo, valueImage)
            },
            tableBlockOrgData = tableBlockOrg?.mapNotNull {
                it.toComposeTableBlockOrg(valueImage)
            }
        )
    }
}
