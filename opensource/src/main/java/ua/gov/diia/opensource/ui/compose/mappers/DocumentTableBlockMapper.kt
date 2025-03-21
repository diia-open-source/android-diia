package ua.gov.diia.opensource.ui.compose.mappers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockAccordionOrg.TableBlockAccordionOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.core.util.extensions.image_processing.replaceWhiteWithTransparent
import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.organism.document.TableBlockOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockPlaneOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockTwoColumnsOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockTwoColumnsPlainOrgData
import ua.gov.diia.ui_base.components.organism.table.TableBlockAccordionOrgData
import ua.gov.diia.ui_base.util.toUiModel

private fun getIconByCode(code: String): Int {
    return when (code) {
        "copy" -> R.drawable.ic_copy_settings
        else -> {
            R.drawable.ic_copy_settings
        }
    }
}

private fun TableItemHorizontalMlc?.toComposeTableItemHorizontal(image: String?): TableItemHorizontalMlcData? {
    return this?.let {
        TableItemHorizontalMlcData(
            componentId = this.componentId.orEmpty(),
            supportText = this.supportingValue,
            title = this.label?.let { it1 -> UiText.DynamicString(it1) },
            value = this.value,
            secondaryTitle = this.secondaryLabel?.let { it1 -> UiText.DynamicString(it1) },
            secondaryValue = this.secondaryValue,
            valueAsBase64String = if (this.valueImage != null) image else null,
            iconRight = if (this.icon?.code != null) {
                this.icon?.code?.let {
                    UiText.StringResource((getIconByCode(it)))
                }
            } else {
                null
            }
        )
    }
}

private fun TableItemHorizontalMlc?.toComposeDocTableItemHorizontal(image: String?): DocTableItemHorizontalMlcData? {
    return this?.let {
        DocTableItemHorizontalMlcData(
            supportText = this.supportingValue,
            title = this.label,
            value = this.value,
            secondaryTitle = this.secondaryLabel,
            secondaryValue = this.secondaryValue,
            valueAsBase64String = if (this.valueImage != null) image else null,
            iconRight = if (this.icon?.code != null) {
                this.icon?.code?.let {
                    UiText.StringResource((getIconByCode(it)))
                }
            } else {
                null
            }
        )
    }
}

private fun TableItemHorizontalMlc?.toComposeDocTableItemLongerHorizontal(image: String?): DocTableItemHorizontalLongerMlcData? {
    return this?.let {
        DocTableItemHorizontalLongerMlcData(
            supportText = this.supportingValue,
            title = this.label,
            value = this.value,
            secondaryTitle = this.secondaryLabel,
            secondaryValue = this.secondaryValue,
            valueAsBase64String = if (this.valueImage != null) image else null,
            iconRight = if (this.icon?.code != null) {
                this.icon?.code?.let {
                    UiText.StringResource((getIconByCode(it)))
                }
            } else {
                null
            }
        )
    }
}

private fun TableItemVerticalMlc?.toComposeTableItemVertical(image: String?): TableItemVerticalMlcData? {
    return this?.let {
        TableItemVerticalMlcData(
            componentId = this.componentId.orEmpty(),
            supportText = this.supportingValue,
            title = this.label?.let { it1 -> UiText.DynamicString(it1) },
            value = this.value?.let { v -> UiText.DynamicString(v) },
            secondaryTitle = this.secondaryLabel?.let { it1 -> UiText.DynamicString(it1) },
            secondaryValue = this.secondaryValue?.let { v -> UiText.DynamicString(v) },
            valueAsBase64String = if (this.valueImage != null) image else null,
            icon = if (this.icon?.code != null) {
                this.icon?.code?.let {
                    UiText.StringResource((getIconByCode(it)))
                }
            } else {
                null
            },
            signBitmap = base64ToImageBitmap(if (this.valueImage != null) image else null)
        )
    }
}

private fun base64ToImageBitmap(base64Image: String?): ImageBitmap? {
    if (base64Image == null) {
        return null
    }
    val byteArray = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        ?.replaceWhiteWithTransparent()
        ?.asImageBitmap()
}

private fun photoBase64ToBitmap(base64Image: String?): Bitmap? {
    if (base64Image == null) {
        return null
    }
    val byteArray = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

private fun SmallEmojiPanelMlc?.toComposeEmojiPanelMlc(): SmallEmojiPanelMlcData? {
    if (this == null) return null
    val text = label ?: return null
    val code = icon?.code ?: return null
    return SmallEmojiPanelMlcData(
        text = UiText.DynamicString(text),
        icon = UiIcon.DrawableResource(code = code)
    )
}

private fun Item?.toComposeTableBlockItem(valueImage: String?): TableBlockItem? {
    return this?.let {
        when {
            this.tableItemHorizontalMlc != null ->
                tableItemHorizontalMlc.toComposeTableItemHorizontal(valueImage)

            this.docTableItemHorizontalMlc != null ->
                docTableItemHorizontalMlc.toComposeDocTableItemHorizontal(valueImage)

            this.docTableItemHorizontalLongerMlc != null ->
                docTableItemHorizontalLongerMlc.toComposeDocTableItemLongerHorizontal(valueImage)

            this.tableItemPrimaryMlc != null ->
                tableItemPrimaryMlc.toUIModel()

            this.tableItemVerticalMlc != null ->
                tableItemVerticalMlc.toComposeTableItemVertical(valueImage)

            this.smallEmojiPanelMlc != null ->
                smallEmojiPanelMlc.toComposeEmojiPanelMlc()

            else -> null
        }
    }
}

fun TableBlockTwoColumnsPlaneOrg?.toComposeTableBlockTwoColumnsPlaneOrg(
    photo: String?,
    valueImage: String?
): TableBlockTwoColumnsPlainOrgData? {
    return this?.let {
        TableBlockTwoColumnsPlainOrgData(
            componentId = this.componentId.orEmpty(),
            photo = photo,
            photoAsBitmap = photoBase64ToBitmap(photo),
            heading = this.headingWithSubtitlesMlc?.toUiModel(),
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) }
        )
    }
}

fun TableBlockTwoColumnsOrg?.toComposeTableBlockTwoColumnsOrg(
    photo: String?,
    valueImage: String?
): TableBlockTwoColumnsOrgData? {
    return this?.let {
        TableBlockTwoColumnsOrgData(
            componentId = this.componentId?.let { UiText.DynamicString(it) },
            photo = photo,
            photoAsBitmap = photoBase64ToBitmap(photo),
            heading = if (this.headingWithSubtitlesMlc != null) HeadingWithSubtitlesMlcData(
                componentId = this.headingWithSubtitlesMlc?.componentId.orEmpty(),
                value = this.headingWithSubtitlesMlc?.value,
                subtitles = this.headingWithSubtitlesMlc?.subtitles
            ) else null,
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) }
        )
    }
}

fun TableBlockOrg?.toComposeTableBlockOrg(valueImage: String?): TableBlockOrgData? {
    return this?.let {
        TableBlockOrgData(
            componentId = componentId?.let { UiText.DynamicString(it) },
            headerMain = tableMainHeadingMlc?.toUIModel(),
            headerSecondary = tableSecondaryHeadingMlc?.toUIModel(),
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) },
        )
    }
}

fun TableBlockPlaneOrg?.toComposeTableBlockPlaneOrgData(valueImage: String?): TableBlockPlaneOrgData? {
    return this?.let {
        TableBlockPlaneOrgData(
            headerMain = tableMainHeadingMlc?.toUIModel(),
            headerSecondary = tableSecondaryHeadingMlc?.toUIModel(),
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) },
            componentId = componentId?.let { UiText.DynamicString(it) }
        )
    }
}

fun TableBlockAccordionOrg?.toComposeTableBlockAccordionOrgData(valueImage: String?): TableBlockAccordionOrgData? {
    return this?.let {
        TableBlockAccordionOrgData(
            heading = this.heading,
            componentId = UiText.DynamicString(this.componentId.orEmpty()),
            expandState = if (this.isOpen == false) UIState.Expand.Collapsed else UIState.Expand.Expanded,
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) }
        )
    }
}