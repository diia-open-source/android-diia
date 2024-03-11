package ua.gov.diia.ui_base.components.organism.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.ContentGroupMolecule
import ua.gov.diia.ui_base.components.molecule.list.table.ContentGroupMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.AccordionListMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.AccordionMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.TableBlockMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ContentTableOrganism(
    modifier: Modifier = Modifier,
    data: ContentTableOrganismData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier) {
        data.items.forEachIndexed { index, item ->
            ContentGroupMolecule(
                data = item,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
            if (index != data.items.size - 1) {
                Divider(
                    modifier = Modifier.padding(vertical = 32.dp),
                    thickness = 2.dp,
                    color = PeriwinkleGray
                )
            }
        }
    }
}

data class ContentTableOrganismData(val items: List<ContentGroupMoleculeData>) : UIElementData

@Composable
@Preview
fun ContentTableOrganismPreview_SimpleHierarchy() {
    val tableBlockState = TableBlockMoleculeData(
        header = TableHeadingMoleculeData(id = "123", title = "Heading".let { UiText.DynamicString(it) }), items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )
    val contentGroupState = ContentGroupMoleculeData(
        items = listOf(
            tableBlockState, tableBlockState, tableBlockState, tableBlockState
        )
    )
    Box(
        modifier = Modifier
            .background(White)
            .padding(16.dp)
    ) {
        ContentTableOrganism(data = ContentTableOrganismData(items = listOf(contentGroupState, contentGroupState)),
            onUIAction = {

            })
    }

}

@Composable
@Preview
fun ContentTableOrganismPreview_DeepHierarchy() {
    val tableBlock = TableBlockMoleculeData(
        header = TableHeadingMoleculeData(id = "123", title = "Heading".let { UiText.DynamicString(it) }), items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )

    val tableBlockForAccordion = TableBlockMoleculeData(
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )

    val accordionItem = AccordionMoleculeData(
        id = "1234", title = "Heading", expandState = UIState.Expand.Collapsed, items = listOf(
            tableBlockForAccordion
        )
    )

    val accordionList = AccordionListMoleculeData(listOf(accordionItem, accordionItem, accordionItem))

    val contentGroup = ContentGroupMoleculeData(
        items = listOf(
            tableBlock, accordionList, tableBlock
        )
    )

    val startState = ContentTableOrganismData(listOf(contentGroup))

    val state = remember { mutableStateOf(startState) }
    Column(
        modifier = Modifier
            .background(White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ContentTableOrganism(data = state.value,
            onUIAction = {

            })
    }
}