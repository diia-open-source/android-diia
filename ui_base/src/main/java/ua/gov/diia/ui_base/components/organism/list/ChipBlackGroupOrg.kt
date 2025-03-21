package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.chip.ChipBlackGroupOrg
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.chip.ChipBlackMlc
import ua.gov.diia.ui_base.components.molecule.chip.ChipBlackMlcData
import ua.gov.diia.ui_base.components.molecule.chip.toUiModel
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ChipBlackGroupOrg(
    modifier: Modifier = Modifier,
    data: ChipBlackGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .background(
                color = White, shape = RoundedCornerShape(16.dp)
            )
    ) {

        data.label?.let {
            Text(
                modifier = Modifier.padding(16.dp),
                text = it,
                style = DiiaTextStyle.t3TextBody,
                color = Black
            )
            DividerSlimAtom(color = BlackSqueeze)
        }

        ChipVerticalGrid(
            spacing = 8.dp, modifier = Modifier.padding(16.dp)
        ) {
            data.items.forEach { item ->
                val state = if (data.preselectedCode?.firstOrNull { it == item.code } != null) {
                    UIState.Selection.Selected
                } else {
                    item.selectionState
                }
                val isActive = if (!data.preselectedCode.isNullOrEmpty() &&
                    data.maxCount != null
                ) {
                    if (data.preselectedCode.size >= data.maxCount && state == UIState.Selection.Unselected) {
                        false
                    } else {
                        item.active
                    }
                } else {
                    item.active
                }
                val itemUpdated = item.copy(selectionState = state, active = isActive)

                ChipBlackMlc(data = itemUpdated, onUIAction = {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = item.code,
                            optionalId = data.componentId.toString()
                        )
                    )
                })
            }
        }
    }
}

data class ChipBlackGroupOrgData(
    val actionKey: String = UIActionKeysCompose.CHIP_BLACK_GROUP_ORG,
    val componentId: String? = null,
    val id: String? = null,
    val inputCode: String? = null,
    val mandatory: Boolean? = null,
    val label: String? = null,
    val minCount: Int? = null,
    val maxCount: Int? = null,
    val items: SnapshotStateList<ChipBlackMlcData>,
    val preselectedCode: SnapshotStateList<String>? = SnapshotStateList<String>()
) : UIElementData {

    fun changeSelectedState(selectedItemsCode: List<String>): ChipBlackGroupOrgData {
        return this.copy(
            preselectedCode = SnapshotStateList<String>().apply {
                preselectedCode?.let { pc ->
                    pc.clear()
                    selectedItemsCode.forEach { newItem ->
                        this.add(newItem)
                    }
                }
            }
        )
    }
}

fun ChipBlackGroupOrg.toUiModel(): ChipBlackGroupOrgData {
    return ChipBlackGroupOrgData(
        componentId = componentId,
        id = id,
        inputCode = inputCode,
        mandatory = mandatory,
        label = label,
        minCount = minCount,
        maxCount = maxCount,
        items = SnapshotStateList<ChipBlackMlcData>().apply {
            items.forEach { item ->
                this.add(
                    item.chipBlackMlc.toUiModel()
                )
            }
        },
        preselectedCode = SnapshotStateList<String>().apply {
            preselectedCodes?.forEach { item ->
                this.add(item)
            }
        }
    )
}

@Composable
fun ChipVerticalGrid(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var currentRow = 0
        var currentOrigin = IntOffset.Zero
        val spacingValue = spacing.toPx().toInt()
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentOrigin.x > 0f && currentOrigin.x + placeable.width > constraints.maxWidth) {
                currentRow += 1
                currentOrigin =
                    currentOrigin.copy(x = 0, y = currentOrigin.y + placeable.height + spacingValue)
            }

            placeable to currentOrigin.also {
                currentOrigin = it.copy(x = it.x + placeable.width + spacingValue)
            }
        }

        layout(
            width = constraints.maxWidth,
            height = placeables.lastOrNull()?.run { first.height + second.y } ?: 0
        ) {
            placeables.forEach {
                val (placeable, origin) = it
                placeable.place(origin.x, origin.y)
            }
        }
    }
}

fun generateChipBlackGroupOrgMockData(
    minCount: Int? = null,
    maxCount: Int? = null
): ChipBlackGroupOrgData {
    return ChipBlackGroupOrgData(
        minCount = minCount,
        maxCount = maxCount,
        label = "Label",
        preselectedCode = SnapshotStateList<String>().apply {
            addAll(listOf("1", "2", "4"))
        },
        items = SnapshotStateList<ChipBlackMlcData>().apply {
            add(
                ChipBlackMlcData(
                    id = "1",
                    label = UiText.DynamicString("FPV"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "2",
                    label = UiText.DynamicString("Розвідувальний коптер"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Selected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "3",
                    label = UiText.DynamicString("Розвідувальне крило"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "4",
                    label = UiText.DynamicString("Ударне крило"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "5",
                    label = UiText.DynamicString("Нічний бомбер"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "6",
                    label = UiText.DynamicString("01"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "7",
                    label = UiText.DynamicString("02"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Selected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "8",
                    label = UiText.DynamicString("03"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "9",
                    label = UiText.DynamicString("04"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "10",
                    label = UiText.DynamicString("05"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "11",
                    label = UiText.DynamicString("Дешифрувальння та аналітика"),
                    code = "inProgress",
                    active = false,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                ChipBlackMlcData(
                    id = "12",
                    label = UiText.DynamicString("Інженерія/техніка/ремонт"),
                    code = "inProgress",
                    selectionState = UIState.Selection.Selected
                )
            )
        }
    )
}

@Composable
@Preview
fun ChipTabsPSOrgPreview() {
    ChipBlackGroupOrg(data = generateChipBlackGroupOrgMockData(maxCount = 3)) {

    }
}

@Composable
@Preview
fun ChipTabsPSOrgMax10Preview() {
    ChipBlackGroupOrg(data = generateChipBlackGroupOrgMockData(maxCount = 10)) {

    }
}