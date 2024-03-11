package ua.gov.diia.opensource.ui.compose

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.Size
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.organism.document.TableBlockOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockPlaneOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockTwoColumnsOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockTwoColumnsPlainOrgData
import ua.gov.diia.ui_base.components.subatomic.icon.replaceWhiteWithTransparent

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
            supportText = this.supportingValue,
            title = this.label?.let { it1 -> UiText.DynamicString(it1) },
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
            value = this.value,
            secondaryTitle = this.secondaryLabel,
            secondaryValue = this.secondaryValue,
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

private fun TableItemPrimaryMlc?.toComposeTableItemPrimary(): TableItemPrimaryMlcData? {
    return this?.let {
        TableItemPrimaryMlcData(
            componentId = this.componentId.orEmpty(),
            title = this.label.let { UiText.DynamicString(it) },
            value = this.value,
            icon = this.icon?.let {
                UiIcon.DrawableResource(code = it.code)
            }
        )
    }
}

private fun Item?.toComposeTableBlockItem(valueImage: String?): TableBlockItem? {
    return this?.let {
        when {
            this.tableItemHorizontalMlc != null -> tableItemHorizontalMlc.toComposeTableItemHorizontal(
                valueImage
            )

            this.docTableItemHorizontalMlc != null -> docTableItemHorizontalMlc.toComposeDocTableItemHorizontal(
                valueImage
            )

            this.docTableItemHorizontalLongerMlc != null -> docTableItemHorizontalLongerMlc.toComposeDocTableItemLongerHorizontal(
                valueImage
            )

            this.tableItemPrimaryMlc != null -> tableItemPrimaryMlc.toComposeTableItemPrimary()
            this.tableItemVerticalMlc != null -> tableItemVerticalMlc.toComposeTableItemVertical(
                valueImage
            )

            this.smallEmojiPanelMlc != null -> smallEmojiPanelMlc.toComposeEmojiPanelMlc()
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
            componentId = this.headingWithSubtitlesMlc?.componentId.orEmpty(),
            photo = photo,
            photoAsBitmap = photoBase64ToBitmap(photo),
            heading = if (this.headingWithSubtitlesMlc != null) HeadingWithSubtitlesMlcData(
                value = this.headingWithSubtitlesMlc?.value,
                subtitles = this.headingWithSubtitlesMlc?.subtitles
            ) else null,
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
            componentId = this.headingWithSubtitlesMlc?.componentId.orEmpty(),
            photo = photo,
            photoAsBitmap = photoBase64ToBitmap(photo),
            heading = if (this.headingWithSubtitlesMlc != null) HeadingWithSubtitlesMlcData(
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
            componentId = this.componentId.orEmpty(),
            headerMain = if (this.tableMainHeadingMlc != null) TableHeadingMoleculeData(
                title = this.tableMainHeadingMlc?.label?.let { UiText.DynamicString(it) },
                icon = if (this.tableMainHeadingMlc?.icon?.code != null) {
                    this.tableMainHeadingMlc?.icon?.code?.let {
                        UiText.StringResource((getIconByCode(it)))
                    }
                } else {
                    null
                },
                description = this.tableMainHeadingMlc?.description?.let { description ->
                    UiText.DynamicString(
                        description
                    )
                }
            ) else null,
            headerSecondary = if (this.tableSecondaryHeadingMlc != null) TableHeadingMoleculeData(
                title = this.tableSecondaryHeadingMlc?.label?.let { UiText.DynamicString(it) },
                icon = if (this.tableSecondaryHeadingMlc?.icon?.code != null) {
                    this.tableSecondaryHeadingMlc?.icon?.code?.let {
                        UiText.StringResource((getIconByCode(it)))
                    }
                } else {
                    null
                },
                size = Size.secondary,
                description = this.tableSecondaryHeadingMlc?.description?.let { description ->
                    UiText.DynamicString(
                        description
                    )
                }
            ) else null,
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) }
        )

    }
}

fun TableBlockPlaneOrg?.toComposeTableBlockPlaneOrgData(valueImage: String?): TableBlockPlaneOrgData? {
    return this?.let {
        TableBlockPlaneOrgData(
            headerMain = if (this.tableMainHeadingMlc != null) TableHeadingMoleculeData(
                title = this.tableMainHeadingMlc?.label?.let { UiText.DynamicString(it) },
                icon = if (this.tableMainHeadingMlc?.icon?.code != null) {
                    this.tableMainHeadingMlc?.icon?.code?.let {
                        UiText.StringResource((getIconByCode(it)))
                    }
                } else {
                    null
                },
                description = this.tableMainHeadingMlc?.description?.let { description ->
                    UiText.DynamicString(
                        description
                    )
                }
            ) else null,
            headerSecondary = if (this.tableSecondaryHeadingMlc != null) TableHeadingMoleculeData(
                title = this.tableSecondaryHeadingMlc?.label?.let { UiText.DynamicString(it) },
                icon = if (this.tableSecondaryHeadingMlc?.icon?.code != null) {
                    this.tableSecondaryHeadingMlc?.icon?.code?.let {
                        UiText.StringResource((getIconByCode(it)))
                    }
                } else {
                    null
                },
                size = Size.secondary,
                description = this.tableSecondaryHeadingMlc?.description?.let { description ->
                    UiText.DynamicString(
                        description
                    )
                }
            ) else null,
            items = items?.mapNotNull { it.toComposeTableBlockItem(valueImage) }
        )

    }
}
