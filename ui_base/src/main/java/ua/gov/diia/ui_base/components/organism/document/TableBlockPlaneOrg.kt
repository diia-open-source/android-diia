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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalLongerMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.DocTableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMolecule
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData

@Composable
fun TableBlockPlaneOrg(
    modifier: Modifier = Modifier,
    data: TableBlockPlaneOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
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
                            diiaResourceIconProvider = diiaResourceIconProvider,
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
    val items: List<TableBlockItem>? = null
) : UIElementData

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
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
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
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}