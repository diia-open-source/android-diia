package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemDragMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemDragMlcData

@Composable
fun ListItemDragOrg(
    modifier: Modifier = Modifier,
    data: ListItemDragOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onMove: (Int, Int) -> Unit,
    onUIAction: (UIAction) -> Unit,
) {
    val state = rememberReorderableLazyListState(
        onMove = { a, b -> onMove.invoke(a.index, b.index) },
        canDragOver = { _, _ -> true },
    )

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .reorderable(state)
    ) {
        items(items = data.items, { it.id }) { item ->
            ReorderableItem(state, item.id) { dragging ->
                ListItemDragMlc(
                    modifier = Modifier,
                    data = item,
                    dragging = dragging,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                    state = state,
                    onUIAction = onUIAction,
                )
            }
        }
    }
}

data class ListItemDragOrgData(val items: SnapshotStateList<ListItemDragMlcData>) :
    UIElementData


@Composable
@Preview
fun ListItemDragOrgPreview() {
    val state =
        ListItemDragOrgData(SnapshotStateList<ListItemDragMlcData>().apply {
            add(
                ListItemDragMlcData(
                    id = "",
                    label = UiText.DynamicString("Свідоцтво про реєстрацію транспортного засобу"),
                )
            )
            add(
                ListItemDragMlcData(
                    id = "",
                    label = UiText.DynamicString("Закордонний паспорт"),
                    countOfDocGroup = 2
                )
            )
            add(
                ListItemDragMlcData(
                    id = "",
                    label = UiText.DynamicString("ХХ000000"),
                    desc = UiText.DynamicString("Дата видачі: хх.хх.хххх"),
                )
            )
        })
    ListItemDragOrg(
        data = state,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onMove = { a, b -> }) {}
}