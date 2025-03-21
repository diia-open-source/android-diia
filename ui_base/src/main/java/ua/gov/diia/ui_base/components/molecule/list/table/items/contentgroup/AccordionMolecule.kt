package ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.LoadActionAtomData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.PlusMinusClickableSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun AccordionMolecule(
    modifier: Modifier = Modifier,
    data: AccordionMoleculeData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit = {}
) {
    val expandState = remember { mutableStateOf(data.expandState) }

    LaunchedEffect(key1 = expandState.value) {
        onUIAction(
            UIAction(
                actionKey = data.actionKey,
                data = data.id,
                states = listOf(expandState.value)
            )
        )
    }
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .noRippleClickable {
                    expandState.value = when (expandState.value) {
                        UIState.Expand.Collapsed -> UIState.Expand.Expanded
                        UIState.Expand.Expanded -> UIState.Expand.Collapsed
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = data.title,
                color = Black,
                style = DiiaTextStyle.h4ExtraSmallHeading
            )
            PlusMinusClickableSubatomic(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(14.dp),
                expandState = expandState.value
            )
        }
        AnimatedVisibility(visible = expandState.value == UIState.Expand.Expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                data.items.forEachIndexed { index, item ->
                    TableBlockMolecule(
                        state = item,
                        progressIndicator = progressIndicator,
                        onUIAction = onUIAction
                    )

                    Spacer(
                        modifier = Modifier.height(
                            if (index != data.items.size - 1) {
                                32.dp
                            } else {
                                16.dp
                            }
                        )
                    )
                }
            }
        }
    }
}

data class AccordionMoleculeData(
    val actionKey: String = UIActionKeysCompose.ACCORDION_MOLECULE,
    val id: String,
    val title: String,
    val expandState: UIState.Expand = UIState.Expand.Collapsed,
    val items: List<TableBlockMoleculeData>
)

@Composable
@Preview
fun AccordionMoleculePreview() {
    val tableBlock1 = TableBlockMoleculeData(
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )

    val tableBlock2 = TableBlockMoleculeData(
        header = "Heading".toDynamicString().toTableMainHeadingMlcData(),
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "2", title = UiText.DynamicString("Item title 2"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("Item title 3"), value = "Value"),
            TableItemHorizontalMlcData(id = "4", title = UiText.DynamicString("Item title 4"), value = "Value"),
            TableItemHorizontalMlcData(id = "5", title = UiText.DynamicString("Item title 5"), value = "Value"),
        )
    )

    val startState = AccordionMoleculeData(
        id = "123",
        title = "Heading",
        expandState = UIState.Expand.Collapsed,
        items = listOf(
            tableBlock1, tableBlock2
        )
    )

    AccordionMolecule(
        modifier = Modifier.padding(16.dp),
        data = startState,
        onUIAction = {

        }
    )
}

@Composable
@Preview
fun AccordionMoleculePreview_WithActionLinkBlock() {
    val tableBlock = TableBlockMoleculeData(
        items = listOf(
            BtnIconPlainGroupMlcData(items = SnapshotStateList<LoadActionAtomData>().apply {
                add(
                    LoadActionAtomData(
                        id = "id1",
                        type = "Type1",
                        icon = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABfSURBVHgB7ZBBDoAgDAQH40P9iU+vJ2/WLjSNJDIJJ0inAywGMeGcJDCKJfcART4kUQRHRqIIUCTtZYh374kf328Us9NPV9U0Bd6GjYBpCsJNs4Iff9HnBUaS8oJFyAWR1y49vMiceAAAAABJRU5ErkJggg==",
                        name = "Постанова про арешт коштів",
                        actionData = "www.google1.com",
                        interactionState = UIState.Interaction.Enabled
                    )
                )
                add(
                    LoadActionAtomData(
                        id = "id2",
                        type = "Type2",
                        icon = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABfSURBVHgB7ZBBDoAgDAQH40P9iU+vJ2/WLjSNJDIJJ0inAywGMeGcJDCKJfcART4kUQRHRqIIUCTtZYh374kf328Us9NPV9U0Bd6GjYBpCsJNs4Iff9HnBUaS8oJFyAWR1y49vMiceAAAAABJRU5ErkJggg==",
                        name = "02.03.2021 11:59:11. Cума : 27 грн",
                        actionData = "www.google2.com",
                        interactionState = UIState.Interaction.Enabled
                    )
                )
            })
        )
    )

    val startState = AccordionMoleculeData(
        id = "123",
        title = "Постанови",
        expandState = UIState.Expand.Collapsed,
        items = listOf(
            tableBlock
        )
    )

    AccordionMolecule(
        modifier = Modifier.padding(16.dp),
        data = startState,
        onUIAction = {

        }
    )
}