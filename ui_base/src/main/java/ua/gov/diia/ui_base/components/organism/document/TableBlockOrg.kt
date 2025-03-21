package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.card.GalleryImageMolecule
import ua.gov.diia.ui_base.components.molecule.card.GalleryImageMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlc
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData

@Composable
fun TableBlockOrg(
    modifier: Modifier = Modifier,
    data: TableBlockOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.headerMain?.let {
            TableMainHeadingMlc(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                data = data.headerMain,
                onUIAction = onUIAction
            )
        }
        data.headerSecondary?.let {
            TableSecondaryHeadingMlc(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                data = data.headerSecondary,
                onUIAction = onUIAction
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            data.items?.forEachIndexed { index, item ->
                when (item) {
                    is TableItemHorizontalMlcData -> {
                        TableItemHorizontalMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is DocTableItemHorizontalMlcData -> {
                        DocTableItemHorizontalMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is DocTableItemHorizontalLongerMlcData -> {
                        DocTableItemHorizontalLongerMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemVerticalMlcData -> {
                        TableItemVerticalMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemPrimaryMlcData -> {
                        TableItemPrimaryMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is GalleryImageMoleculeData -> {
                        GalleryImageMolecule(
                            modifier = modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is SmallEmojiPanelMlcData -> {
                        SmallEmojiPanelMlc(
                            modifier = modifier,
                            data = item,
                        )
                    }

                    else -> {
                        //nothing
                    }
                }
                if (index != data.items.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

data class TableBlockOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ORG,
    val componentId: UiText? = null,
    val headerMain: TableMainHeadingMlcData? = null,
    val headerSecondary: TableSecondaryHeadingMlcData? = null,
    val items: List<TableBlockItem>? = null
) : UIElementData

fun TableBlockOrg?.toUIModel(): TableBlockOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val tbItems = mutableListOf<TableBlockItem>().apply {
        (entity.items as List<Item>).forEach { listMlcl ->
            if (listMlcl.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                add(
                    TableItemHorizontalMlcData(
                        componentId = listMlcl.tableItemHorizontalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemHorizontalMlc?.label?.let {
                            UiText.DynamicString(it)
                        },
                        secondaryTitle = UiText.DynamicString(
                            listMlcl.tableItemHorizontalMlc?.secondaryLabel.orEmpty()
                        ),
                        value = listMlcl.tableItemHorizontalMlc?.value,
                        secondaryValue = listMlcl.tableItemHorizontalMlc?.secondaryValue,
                        supportText = listMlcl.tableItemHorizontalMlc?.supportingValue,
                        valueAsBase64String = listMlcl.tableItemHorizontalMlc?.valueImage
                    )
                )
            }

            if (listMlcl.tableItemVerticalMlc is TableItemVerticalMlc) {
                add(
                    TableItemVerticalMlcData(
                        componentId = listMlcl.tableItemVerticalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemVerticalMlc?.label?.let { UiText.DynamicString(it) },
                        secondaryTitle = listMlcl.tableItemVerticalMlc?.secondaryLabel?.let {
                            UiText.DynamicString(it)
                        },
                        value = listMlcl.tableItemVerticalMlc?.value?.let { UiText.DynamicString(it) },
                        secondaryValue = listMlcl.tableItemVerticalMlc?.secondaryValue?.let {
                            UiText.DynamicString(it)
                        },
                        supportText = listMlcl.tableItemVerticalMlc?.supportingValue,
                        valueAsBase64String = listMlcl.tableItemVerticalMlc?.valueImage
                    )
                )
            }

            if (listMlcl.tableItemPrimaryMlc is TableItemPrimaryMlc) {
                val item = (listMlcl.tableItemPrimaryMlc as TableItemPrimaryMlc).toUIModel()
                item?.let {
                    add(it)
                }
            }
        }
    }
    return TableBlockOrgData(
        headerMain = this?.tableMainHeadingMlc?.toUIModel(),
        items = tbItems
    )
}

@Preview
@Composable
fun TableBlockOrgPreview() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Тип нерухомого майна:"),
            value = "Будинок"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Частка власності:"),
            value = "1/5"
        ),
        TableItemVerticalMlcData(
            id = "3",
            title = UiText.DynamicString("Адреса:"),
            value = UiText.DynamicString("м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16")
        ),
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Номер сертифіката"),
            value = "1234567890",
            iconRight = UiText.StringResource(
                R.drawable.ic_copy
            )
        )
    )
    val data = TableBlockOrgData(items = items)

    TableBlockOrg(
        modifier = Modifier,
        data = data
    ) {

    }
}

@Preview
@Composable
fun TableBlockOrgPreview_gallery() {
    val images = listOf(
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/ce1/7fb/thumb_867_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/d01/72e/thumb_868_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/e84/e25/thumb_870_730_410_0_0_auto.jpg",
        "https://go.diia.app/assets/img/pages/screen-phone.png",
    )
    val dataGallery = GalleryImageMoleculeData(
        id = "id",
        images = images
    )

    val data = TableBlockOrgData(
        headerMain = "Фото: ".toDynamicString().toTableMainHeadingMlcData(),
        items = listOf(dataGallery)
    )

    TableBlockOrg(
        modifier = Modifier,
        data = data
    ) {}
}


@Preview
@Composable
fun TableBlockOrgPreview_small_emodji() {
    val emoji = SmallEmojiPanelMlcData(
        text = UiText.DynamicString("Booster vaccine dose"),
        icon = UiIcon.DrawableResource(
            code = DiiaResourceIcon.TRIDENT.code
        )
    )

    val data = TableBlockOrgData(
        items = listOf(emoji)
    )

    TableBlockOrg(
        modifier = Modifier,
        data = data
    ) {}
}


@Preview
@Composable
fun TableBlockOrgPreview_WithHeader() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Тип нерухомого майна:"),
            value = "Будинок"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Частка власності:"),
            value = "1/5"
        ),
        TableItemVerticalMlcData(
            id = "3",
            title = UiText.DynamicString("Адреса:"),
            value = UiText.DynamicString("м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16")
        ),
        TableItemHorizontalMlcData(
            id = "123",
            title = UiText.DynamicString("Номер сертифіката"),
            value = "1234567890",
            iconRight = UiText.StringResource(
                R.drawable.ic_copy
            )
        )
    )
    val data = TableBlockOrgData(
        items = items,
        headerMain = "Header".toDynamicString().toTableMainHeadingMlcData(),
        headerSecondary = "Secondary Header".toDynamicString().toTableSecondaryHeadingMlcData()
    )

    TableBlockOrg(
        modifier = Modifier,
        data = data
    ) {

    }
}