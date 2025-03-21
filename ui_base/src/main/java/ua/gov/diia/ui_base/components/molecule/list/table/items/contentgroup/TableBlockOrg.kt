package ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.theme.White

@Deprecated("Use ua.gov.diia.ui_base.components.organism.document.TableBlockOrg")
@Composable
fun TableBlockOrg(
    modifier: Modifier = Modifier,
    data: TableBlockOrgData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(modifier
        .fillMaxWidth()
        .background(color = White, shape = RoundedCornerShape(16.dp))) {
        data.header?.let {
            TableMainHeadingMlc(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                data = data.header,
                onUIAction = onUIAction
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 13.dp, bottom = 16.dp)) {
            data.items?.forEachIndexed { index, item ->
                when (item) {
                    is TableItemHorizontalMlcData -> {
                        TableItemHorizontalMlc(
                            modifier = Modifier.padding(vertical = 3.dp),
                            data = item,
                            onUIAction = onUIAction
                        )
                    }
                    is TableItemVerticalMlcData -> {
                        TableItemVerticalMlc(
                            modifier = Modifier.padding(vertical = 3.dp),
                            data = item,
                            onUIAction = onUIAction
                        )
                    }
                    is TableItemPrimaryMlcData -> {
                        TableItemPrimaryMlc(
                            modifier = Modifier.padding(vertical = 3.dp),
                            data = item,
                            onUIAction = onUIAction
                        )
                    }
                    else -> {
                        //nothing
                    }
                }
                if (index != data.items.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

data class TableBlockOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ORG,
    val header: TableMainHeadingMlcData? = null,
    val items: List<TableBlockItem>? = null
) : UIElementData, ContentGroupItemV2()

@Composable
@Preview
fun TableBlockMoleculeV2Preview_raw() {
    val state = TableBlockOrgData(
        header = "Heading".toDynamicString().toTableMainHeadingMlcData(),
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )
    TableBlockOrg(modifier = Modifier, data = state)
}