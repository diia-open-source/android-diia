package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ua.gov.diia.ui_base.components.atom.button.LoadActionAtom
import ua.gov.diia.ui_base.components.atom.button.LoadActionAtomData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Composable
fun BtnIconPlainGroupMlc(
    modifier: Modifier = Modifier,
    data: BtnIconPlainGroupMlcData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember {
        mutableStateOf(data)
    }

    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    LaunchedEffect(key1 = progressIndicator) {
        localData.value =
            localData.value.copy(items = SnapshotStateList<LoadActionAtomData>().apply {
                for (item in localData.value.items) {
                    add(
                        item.copy(
                            interactionState = if (progressIndicator.first != "" && !progressIndicator.second) {
                                UIState.Interaction.Disabled
                            } else {
                                UIState.Interaction.Enabled
                            }
                        )
                    )
                }
            })
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .border(
                color = PeriwinkleGray, width = 1.dp, shape = RoundedCornerShape(8.dp)
            )
    ) {
        data.items.forEachIndexed { index, item ->
            LoadActionAtom(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .conditional(index == data.items.size - 1) {
                        padding(bottom = 16.dp)
                    },
                data = item,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
    }


}

data class BtnIconPlainGroupMlcData(
    val items: SnapshotStateList<LoadActionAtomData>
) : UIElementData, TableBlockItem

@Composable
@Preview
fun BtnIconPlainGroupMlcPreview() {

    val loading = remember {
        mutableStateOf(Pair("", false))
    }

    LaunchedEffect(key1 = loading.value) {
        if (loading.value.first != "" && loading.value.second) {
            delay(2000)
            loading.value = Pair("", false)
        }

    }
    val data = BtnIconPlainGroupMlcData(items = SnapshotStateList<LoadActionAtomData>().apply {
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

    BtnIconPlainGroupMlc(
        progressIndicator = loading.value,
        data = data
    ) {
        loading.value = loading.value.copy(
            first = if (it.data == "www.google1.com") {
                "id1"
            } else {
                "id2"
            }, true
        )
    }
}
