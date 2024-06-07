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
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMolecule
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel

@Composable
fun TableBlockPlaneOrg(
    modifier: Modifier = Modifier,
    data: TableBlockPlaneOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.headerMain?.let {
            TableHeadingMolecule(
                modifier = Modifier.padding(
                    bottom = 16.dp
                ),
                data = data.headerMain,
                onUIAction = onUIAction
            )
        }
        data.headerSecondary?.let {
            TableHeadingMolecule(
                modifier = Modifier.padding(
                    bottom = 16.dp
                ),
                data = data.headerSecondary,
                onUIAction = onUIAction
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data.items?.forEachIndexed { index, item ->
                when (item) {
                    is TableItemHorizontalMlcData -> {
                        TableItemHorizontalMlc(
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemVerticalMlcData -> {
                        TableItemVerticalMlc(
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

                    is DocTableItemHorizontalMlcData -> {
                        DocTableItemHorizontalMlc(
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    is TableItemPrimaryMlcData -> {
                        TableItemPrimaryMlc(
                            data = item,
                            onUIAction = onUIAction
                        )
                    }

                    else -> {
                        //nothing
                    }
                }
                if (index != data.items.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


data class TableBlockPlaneOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ORG,
    val headerMain: TableHeadingMoleculeData? = null,
    val headerSecondary: TableHeadingMoleculeData? = null,
    val items: List<TableBlockItem>? = null,
    val componentId: UiText? = null,
) : UIElementData

fun TableBlockPlaneOrg?.toUIModel(): TableBlockPlaneOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val tbItems = mutableListOf<TableBlockItem>().apply {
        (entity.items as List<Item>).forEach { listMlcl ->
            if (listMlcl.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                add(
                    TableItemHorizontalMlcData(
                        componentId = listMlcl.tableItemHorizontalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemHorizontalMlc?.label?.let {
                            UiText.DynamicString(
                                it
                            )
                        },
                        secondaryTitle = listMlcl.tableItemHorizontalMlc?.secondaryLabel,
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
                        secondaryTitle = listMlcl.tableItemVerticalMlc?.secondaryLabel,
                        value = listMlcl.tableItemVerticalMlc?.value,
                        secondaryValue = listMlcl.tableItemVerticalMlc?.secondaryValue,
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
    return TableBlockPlaneOrgData(
        headerMain = this?.tableMainHeadingMlc?.let {
            TableHeadingMoleculeData(
                id = null,
                title = UiText.DynamicString(it.label),
                icon = it.icon?.code?.let { it1 -> UiText.DynamicString(it1) },
                description = it.description?.let { description -> UiText.DynamicString(description) }
            )
        },
        items = tbItems
    )
}


@Preview
@Composable
fun TableBlockPlaneOrgPreview() {
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
            value = "м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16"
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
    val data = TableBlockPlaneOrgData(items = items)

    TableBlockPlaneOrg(
        modifier = Modifier,
        data = data
    ) {

    }
}

@Preview
@Composable
fun TableBlockPlaneOrgPreview_WithHeader() {
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
            value = "м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16"
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
    val data = TableBlockPlaneOrgData(
        items = items,
        headerMain = TableHeadingMoleculeData(
            title = "Header".let { UiText.DynamicString(it) }
        )
    )

    TableBlockPlaneOrg(
        modifier = Modifier,
        data = data
    ) {

    }
}