package ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Composable
fun AccordionListMolecule(
    modifier: Modifier = Modifier,
    data: AccordionListMoleculeData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier) {
        data.itemsList.forEachIndexed { index, item ->
            AccordionMolecule(
                data = item,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
            if (index != data.itemsList.size - 1) {
                Divider(thickness = 2.dp, color = PeriwinkleGray)
            }
        }
    }
}

data class AccordionListMoleculeData(val itemsList: List<AccordionMoleculeData>) : ContentGroupItem()

@Composable
@Preview
fun AccordionListMoleculePreview() {
    val tableBlock1 = TableBlockMoleculeData(
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )

    val accordionState = AccordionMoleculeData(
        id = "123",
        title = "Heading",
        expandState = UIState.Expand.Collapsed,
        items = listOf(
            tableBlock1, tableBlock1, tableBlock1
        )
    )

    val accordion2State = AccordionMoleculeData(
        id = "456",
        title = "Heading",
        expandState = UIState.Expand.Collapsed,
        items = listOf(
            tableBlock1, tableBlock1, tableBlock1
        )
    )

    val accordion3State = AccordionMoleculeData(
        id = "789",
        title = "Heading",
        expandState = UIState.Expand.Collapsed,
        items = listOf(
            tableBlock1, tableBlock1, tableBlock1
        )
    )

    val startState = AccordionListMoleculeData(listOf(accordionState, accordion2State, accordion3State))

    val state = remember { mutableStateOf(startState) }

    AccordionListMolecule(data = state.value) {

    }
}
