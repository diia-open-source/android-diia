package ua.gov.diia.ui_base.components.molecule.list.table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.AccordionListMolecule
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.AccordionListMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.AccordionMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.ContentGroupItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.TableBlockMolecule
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.TableBlockMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData

@Composable
fun ContentGroupMolecule(
    modifier: Modifier = Modifier,
    data: ContentGroupMoleculeData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .conditional(data.usePadding) {
            padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        }) {
        data.items.forEachIndexed { index, item ->
            when (item) {
                is AccordionListMoleculeData -> {
                    AccordionListMolecule(
                        data = item,
                        progressIndicator = progressIndicator,
                        onUIAction = onUIAction
                    )
                }

                is TableBlockMoleculeData -> {
                    TableBlockMolecule(
                        state = item,
                        progressIndicator = progressIndicator,
                        onUIAction = onUIAction
                    )
                }

                else -> {}
            }

            if (index != data.items.size - 1) {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

data class ContentGroupMoleculeData(
    val items: List<ContentGroupItem>,
    val usePadding: Boolean = false
) : UIElementData

@Composable
@Preview
fun ContentGroupMoleculePreview() {
    val tableBlockState = TableBlockMoleculeData(
        header = TableMainHeadingMlcData(
            title = "Heading".let { UiText.DynamicString(it) }), items = listOf(
            TableItemHorizontalMlcData(
                id = "1",
                title = UiText.DynamicString("Item title"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "2",
                title = UiText.DynamicString("Item title 2"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "3",
                title = UiText.DynamicString("Item title 3"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "4",
                title = UiText.DynamicString("Item title 4"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "5",
                title = UiText.DynamicString("Item title 5"),
                value = "Value"
            ),
        )
    )

    val tableBlockForAccordion = TableBlockMoleculeData(
        items = listOf(
            TableItemHorizontalMlcData(
                id = "1",
                title = UiText.DynamicString("Item title"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "2",
                title = UiText.DynamicString("Item title 2"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "3",
                title = UiText.DynamicString("Item title 3"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "4",
                title = UiText.DynamicString("Item title 4"),
                value = "Value"
            ),
            TableItemHorizontalMlcData(
                id = "5",
                title = UiText.DynamicString("Item title 5"),
                value = "Value"
            ),
        )
    )

    val accordionItem = AccordionMoleculeData(
        id = "1234",
        title = "Heading",
        expandState = UIState.Expand.Collapsed,
        items = listOf(
            tableBlockForAccordion
        )
    )

    val accordionListState =
        AccordionListMoleculeData(listOf(accordionItem, accordionItem, accordionItem))

    val startState = ContentGroupMoleculeData(
        items = listOf(
            tableBlockState, accordionListState, tableBlockState
        )
    )

    val state = remember { mutableStateOf(startState) }


    ContentGroupMolecule(modifier = Modifier.padding(16.dp), data = state.value,
        onUIAction = {

        })
}