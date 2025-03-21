package ua.gov.diia.opensource.ui.compose.mappers

import androidx.compose.runtime.toMutableStateList
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.core.models.common_compose.org.doc.DocButtonHeadingOrg
import ua.gov.diia.core.models.common_compose.org.doc.DocHeadingOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockAccordionOrg.TableBlockAccordionOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.core.models.document.DocumentCard
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeErrorLoadResult
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeResult
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeSuccessfulLoadResult
import ua.gov.diia.core.models.document.docgroups.v2.DocCover
import ua.gov.diia.core.models.document.docgroups.v2.QrCheckStatus
import ua.gov.diia.core.models.document.docgroups.v2.SubtitleLabelMlc
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.util.CommonConst
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.doc_manual_options.models.DocManualOptions
import ua.gov.diia.documents.ui.gallery.DocActions
import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.atom.text.TickerUsage
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.molecule.card.WhiteMenuCardMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.doc.StackMlcData
import ua.gov.diia.ui_base.components.molecule.doc.toUiModel
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
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
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import ua.gov.diia.ui_base.mappers.document.ToggleId
import ua.gov.diia.ui_base.util.toDataActionWrapper
import ua.gov.diia.ui_base.util.toUiModel
import javax.inject.Inject

class DocumentComposeMapperImpl @Inject constructor() : DocumentComposeMapper {

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
        return SubtitleLabelMlcData(
            label = UiText.DynamicString(text),
            componentId = componentId?.let { UiText.DynamicString(it) }
        )
    }

    override fun toComposeDocError(status: QrCheckStatus): DocErrorOrgData? {
        return when (status) {
            QrCheckStatus.STATUS_NO_NETWORK -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_docs_no_internet_message),
                    ticker = TickerAtomData(
                        title = "Немає інтернету • Немає інтернету • Немає інтернету • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_UNKNOWN_CODE_TYPE -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.qr_code_not_registered),
                    ticker = TickerAtomData(
                        title = "Невідомий QR-код • Невідомий QR-код • Невідомий QR-код • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_DOC_NOT_LOADED_ERROR -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_error_not_found),
                    ticker = TickerAtomData(
                        title = "Документ не знайдено • Документ не знайдено • Документ не знайдено • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_QR_CODE_TIME_OUT -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_error_timeout),
                    ticker = TickerAtomData(
                        title = "Документ не знайдено • Документ не знайдено • Документ не знайдено • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_CODE_NO_REGISTRY -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_docs_no_registry_message),
                    ticker = TickerAtomData(
                        title = "Реєстр недоступний • Реєстр недоступний • Реєстр недоступний • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_CERT_VERIFICATION_INVALID -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_error_cert),
                    ticker = TickerAtomData(
                        title = "Документ не знайдено • Документ не знайдено • Документ не знайдено • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_CERT_VERIFICATION_EXPIRED -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_expired_error_cert),
                    ticker = TickerAtomData(
                        title = "Помилка валідації • Помилка валідації • Помилка валідації • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            QrCheckStatus.STATUS_CODE_TIME_OUT -> {
                DocErrorOrgData(
                    title = UiText.StringResource(R.string.check_doc_http_timeout),
                    ticker = TickerAtomData(
                        title = "Помилка валідації • Помилка валідації • Помилка валідації • ",
                        type = TickerType.NEGATIVE,
                        usage = TickerUsage.BASE
                    )
                )
            }

            else -> {
                null
            }
        }
    }

    override fun toComposeNavigationPanel(): NavigationPanelMlcData? {
        return NavigationPanelMlcData(
            title = UiText.StringResource(R.string.title_scan_qr),
            tintColor = White,
            isContextMenuExist = false,
        )
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
        docType: String,
        localization: LocalizationType,
        verificationCodesCount: Int
    ): DocCover? {
        return when (docStatus) {

            Http.HTTP_1010 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_no_photo_title else R.string.dl_no_photo_title_eng,
                    description = if (localization == LocalizationType.ua) R.string.dl_to_e_queue else R.string.dl_to_e_queue_eng,
                    buttonTitle = if (localization == LocalizationType.ua) "Записатися" else "Electronic queue",
                    actionKey = DocActions.DOC_ACTION_OPEN_WEB_VIEW,
                    verificationCodesCount = verificationCodesCount,
                    data = CommonConst.URL_DRIVER_QUEUE
                )
            }

            Http.HTTP_1011 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_outdated_title else R.string.dl_outdated_title_eng,
                    description = if (localization == LocalizationType.ua) R.string.dl_outdated_description else R.string.dl_outdated_description_eng,
                    buttonTitle = if (localization == LocalizationType.ua) "Записатися" else "Electronic queue",
                    actionKey = DocActions.DOC_ACTION_OPEN_WEB_VIEW,
                    verificationCodesCount = verificationCodesCount,
                    data = CommonConst.URL_DRIVER_QUEUE
                )
            }

            Http.HTTP_1012 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_need_verify_title else R.string.dl_need_verify_title_eng,
                    description = if (localization == LocalizationType.ua) R.string.dl_need_verify_queue else R.string.dl_need_verify_queue_eng,
                    buttonTitle = if (localization == LocalizationType.ua) "Знайти адресу" else "Open Driver's Account",
                    actionKey = DocActions.DOC_ACTION_OPEN_WEB_VIEW,
                    verificationCodesCount = verificationCodesCount,
                    data = CommonConst.URL_DRIVER_FIND_ADDRESS
                )
            }


            Http.HTTP_1016 -> {
                DocCover(
                    title = if (localization == LocalizationType.ua) R.string.dl_expiration_title else R.string.dl_expiration_title,
                    description = if (localization == LocalizationType.ua) R.string.dl_expiration_description else R.string.dl_expiration_description,
                    buttonTitle = if (localization == LocalizationType.ua) "Видалити документ" else "Delete document",
                    actionKey = DocActions.DOC_COVER_ACTION_DELETE,
                    verificationCodesCount = verificationCodesCount
                )
            }


            else -> null
        }
    }

    private fun DocCover.toComposeDocCoverMlc(isStack: Boolean, stackSize: Int): DocCoverMlcData {
        return DocCoverMlcData(
            title = UiText.StringResource(this.title),
            text = UiText.StringResource(this.description),
            button = if (this.buttonTitle != null) ButtonStrokeAdditionalAtomData(
                actionKey = this.actionKey,
                title = UiText.DynamicString(this.buttonTitle ?: ""),
                id = this.data.orEmpty(),
                interactionState = UIState.Interaction.Enabled
            ) else null,
            stackMlcData = if (isStack) StackMlcData(
                amount = stackSize,
                smallIconAtmData = SmallIconAtmData(code = "stack")
            ) else null,
            isStack = isStack,
            size = stackSize
        )
    }


    override fun TickerAtm?.toComposeTickerAtm(
        isTickerClickable: Boolean
    ): TickerAtomData? {
        return this?.let {
            TickerAtomData(
                componentId = it.componentId.orEmpty(),
                title = it.value,
                usage = TickerUsage.GRAND,
                type = when (this.type) {
                    TickerAtm.TickerType.warning -> TickerType.WARNING
                    TickerAtm.TickerType.positive -> TickerType.POSITIVE
                    TickerAtm.TickerType.neutral -> TickerType.NEUTRAL
                    TickerAtm.TickerType.informative -> TickerType.INFORMATIVE
                    TickerAtm.TickerType.negative -> TickerType.NEGATIVE
                    TickerAtm.TickerType.pink -> TickerType.PINK
                    TickerAtm.TickerType.rainbow -> TickerType.RAINBOW
                    TickerAtm.TickerType.blue -> TickerType.BLUE
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
                    componentId = this.headingWithSubtitlesMlc?.componentId.orEmpty(),
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
                    //NO photo for doc with cover
                    it.toComposeTableBlockTwoColumnsPlaneOrg(null, null)
                },
                tableBlockOrgData = tableBlockOrg?.mapNotNull {
                    it.toComposeTableBlockPlaneOrgData(valueImage)
                },
                docCover = cover?.toComposeDocCoverMlc(isStack, stackSize),
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
            back = barcodeResult?.let {
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
                id = docHeadingOrg?.docNumberCopyWhiteMlc?.value,
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
            id = docHeadingOrg?.docNumberCopyWhiteMlc?.value,
            imageUrl = url,
            position = position,
            docType = docType,
            docHeading = docHeadingOrg.toComposeDocHeadingOrg(),
            docButtonHeading = docButtonHeadingOrg.toComposeDocButtonHeadingOrg(
                isStack,
                stackSize
            ),
            chipStatusAtmData = chipStatusAtm?.toUiModel(),
            docCover = cover?.toComposeDocCoverMlc(showCover, stackSize),
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
                            doc.getItemType(),
                            doc.localization() ?: LocalizationType.ua,
                            doc.verificationCodesCount()
                        ),
                        isTickerClickable = false

                    )

                    is DocManualOptions -> toComposeAddDocOrg(
                        docType = doc.getItemType(),
                        indexOf(documentCard)
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
        tableBlockAccordionOrg: List<TableBlockAccordionOrg>?,
        photo: String?,
        valueImage: String?
    ): ContentTableOrgData {
        return ContentTableOrgData(
            tableBlockTwoColumnsOrgData = tableBlockTwoColumnsOrg?.mapNotNull {
                it.toComposeTableBlockTwoColumnsOrg(photo, valueImage)
            },
            tableBlockOrgData = tableBlockOrg?.mapNotNull {
                it.toComposeTableBlockOrg(valueImage)
            },
            tableBlockAccordionOrgData = tableBlockAccordionOrg?.mapNotNull {
                it.toComposeTableBlockAccordionOrgData(valueImage)
            }
        )
    }


}