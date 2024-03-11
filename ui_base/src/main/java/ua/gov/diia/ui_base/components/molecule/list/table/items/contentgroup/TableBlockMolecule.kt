package ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMolecule
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData

@Composable
fun TableBlockMolecule(
    modifier: Modifier = Modifier,
    state: TableBlockMoleculeData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(modifier.fillMaxWidth()) {
        state.header?.let {
            TableHeadingMolecule(
                modifier = Modifier.padding(bottom = 16.dp),
                data = state.header,
                onUIAction = onUIAction
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            state.items.forEachIndexed { index, item ->
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
                    is BtnIconPlainGroupMlcData -> {
                        BtnIconPlainGroupMlc(
                            data = item,
                            progressIndicator = progressIndicator,
                            onUIAction = onUIAction
                        )
                    }
                    else -> {
                        //nothing
                    }
                }
                if (index != state.items.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

data class TableBlockMoleculeData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ORG,
    val header: TableHeadingMoleculeData? = null,
    val items: List<TableBlockItem>
) : ContentGroupItem()

@Composable
@Preview
fun TableBlockMoleculePreview() {
    val state = TableBlockMoleculeData(
        header = TableHeadingMoleculeData(id = "123", title = "Heading".let { UiText.DynamicString(it) }),
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )
    TableBlockMolecule(modifier = Modifier.padding(all = 16.dp), state = state)
}