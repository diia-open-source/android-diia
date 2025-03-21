package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.TextLabelAtm
import ua.gov.diia.core.models.common_compose.mlc.checkbox.TableItemCheckboxMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtmData
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtmMode
import ua.gov.diia.ui_base.components.atom.text.toUIModel
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.White
import java.util.UUID

@Composable
fun TableItemCheckboxMlc(
    modifier: Modifier = Modifier,
    data: TableItemCheckboxMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val id: String = data.componentId?.asString() ?: UUID.randomUUID().toString()
    val inputCode: String? = data.inputCode?.asString()
    Row(
        modifier = modifier.conditional(data.interactionState == UIState.Interaction.Enabled) {
            noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = id,
                        action = DataActionWrapper(
                            type = data.actionKey,
                            subtype = inputCode,
                            resource = id
                        ),
                        states = listOf(data.selectionState)
                    )
                )
            }
        }, verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = when (data.interactionState) {
                        UIState.Interaction.Disabled -> {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> BlackAlpha30
                                UIState.Selection.Unselected -> Transparent
                            }
                        }

                        UIState.Interaction.Enabled -> {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> Black
                                UIState.Selection.Unselected -> Transparent
                            }
                        }
                    }, shape = RoundedCornerShape(4.dp)
                )
                .conditional(data.selectionState == UIState.Selection.Unselected) {
                    border(
                        color = when (data.interactionState) {
                            UIState.Interaction.Disabled -> BlackAlpha30
                            UIState.Interaction.Enabled -> Black
                        }, width = 2.dp, shape = RoundedCornerShape(4.dp)
                    )
                }
                .size(20.dp)
                .testTag(data.componentId?.asString() ?: ""),
            contentAlignment = Alignment.Center
        ) {
            if (data.selectionState == UIState.Selection.Selected) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    painter = painterResource(id = R.drawable.diia_check),
                    contentDescription = null,
                    tint = White
                )
            }
        }

        Column(modifier = Modifier.padding(start = 12.dp)) {
            data.rows.forEach { textLabelAtmData ->
                ua.gov.diia.ui_base.components.atom.text.TextLabelAtm(
                    data = textLabelAtmData.copy(
                        isEnabled = data.interactionState != UIState.Interaction.Disabled
                    )
                )
            }
        }
    }
}

data class TableItemCheckboxMlcData(
    val actionKey: String = UIActionKeysCompose.TABLE_ITEM_CHECKBOX_MLC,
    val componentId: UiText? = null,
    val inputCode: UiText? = null,
    val rows: List<TextLabelAtmData>,
    val interactionState: UIState.Interaction,
    val selectionState: UIState.Selection,
    val mandatory: Boolean?,
) : UIElementData {

    fun onCheckboxClick(): TableItemCheckboxMlcData {
        return this.copy(selectionState = this.selectionState.reverse())
    }
}

fun TableItemCheckboxMlc?.toUIModel(
    actionKey: String = UIActionKeysCompose.TABLE_ITEM_CHECKBOX_MLC,
    interactionState: UIState.Interaction? = null,
    selectionState: UIState.Selection? = null
): TableItemCheckboxMlcData? {
    val data = this
    if (this == null) return null
    return TableItemCheckboxMlcData(
        actionKey = actionKey,
        componentId = this.componentId?.toDynamicString(),
        rows = mutableListOf<TextLabelAtmData>().apply {
            data?.items?.forEach {
                add(it.toUIModel(data.isEnabled != false))
            }
        },
        interactionState = interactionState ?: when (data?.isEnabled) {
            false -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        },
        selectionState = selectionState ?: if (data?.isSelected == true) {
            UIState.Selection.Selected
        } else {
            UIState.Selection.Unselected
        },
        mandatory = this.mandatory
    )
}

@Preview
@Composable
fun TableItemCheckboxMlc_Enabled() {
    val data = generateTableItemCheckboxMlcMockData(TableItemCheckboxMlcMockType.enabled)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        TableItemCheckboxMlc(modifier = Modifier
            .padding(24.dp)
            .background(Color.White),
            data = state,
            onUIAction = { action ->
                state = state.onCheckboxClick()
            }
        )
    }
}

@Preview
@Composable
fun TableItemCheckboxMlc_DisabledSelected() {
    val data = generateTableItemCheckboxMlcMockData(TableItemCheckboxMlcMockType.disabled_selected)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        TableItemCheckboxMlc(modifier = Modifier
            .padding(24.dp)
            .background(Color.White),
            data = state,
            onUIAction = { action ->
                state = state.onCheckboxClick()
            }
        )
    }
}

@Preview
@Composable
fun TableItemCheckboxMlc_DisabledUnselected() {
    val data =
        generateTableItemCheckboxMlcMockData(TableItemCheckboxMlcMockType.disabled_unselected)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        TableItemCheckboxMlc(modifier = Modifier
            .padding(24.dp)
            .background(Color.White),
            data = state,
            onUIAction = { action ->
                state = state.onCheckboxClick()
            }
        )
    }
}

@Preview
@Composable
fun TableItemCheckboxMlc_FromJson() {
    val data = TableItemCheckboxMlc(
        componentId = "1",
        inputCode = "inputCode",
        mandatory = true,
        items = listOf(
            TextLabelAtm(
                componentId = "someId",
                mode = TextLabelAtm.Mode.PRIMARY,
                label = "label",
                value = "value"
            )
        ),
        isSelected = true,
        isEnabled = true
    )
    var state by remember { mutableStateOf(data.toUIModel()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        state?.let {
            TableItemCheckboxMlc(modifier = Modifier
                .padding(24.dp)
                .background(Color.White),
                data = it,
                onUIAction = { action ->
                    state = it.onCheckboxClick()
                }
            )
        }
    }
}


enum class TableItemCheckboxMlcMockType {
    enabled, disabled_selected, disabled_unselected
}

fun generateTableItemCheckboxMlcMockData(mockType: TableItemCheckboxMlcMockType): TableItemCheckboxMlcData {
    val row1 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.PRIMARY,
        label = "Освіта:".toDynamicString(),
        value = "Вища".toDynamicString(),
        isEnabled = true
    )
    val row2 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.SECONDARY,
        label = "Заклад освіти:".toDynamicString(),
        value = "Академія кіно, доміно, ВЛК та нових медіа".toDynamicString(),
        isEnabled = true
    )
    val row3 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.SECONDARY,
        label = "Місто:".toDynamicString(),
        value = "Київ".toDynamicString(),
        isEnabled = true
    )

    return when (mockType) {
        TableItemCheckboxMlcMockType.enabled -> TableItemCheckboxMlcData(
            componentId = "1".toDynamicString(),
            rows = listOf(
                row1, row2, row3
            ),
            interactionState = UIState.Interaction.Enabled,
            selectionState = UIState.Selection.Selected,
            mandatory = true
        )

        TableItemCheckboxMlcMockType.disabled_selected -> TableItemCheckboxMlcData(
            componentId = "1".toDynamicString(),
            rows = listOf(
                row1, row2, row3
            ),
            interactionState = UIState.Interaction.Disabled,
            selectionState = UIState.Selection.Selected,
            mandatory = true
        )

        TableItemCheckboxMlcMockType.disabled_unselected -> TableItemCheckboxMlcData(
            componentId = "1".toDynamicString(),
            rows = listOf(
                row1, row2, row3
            ),
            interactionState = UIState.Interaction.Disabled,
            selectionState = UIState.Selection.Unselected,
            mandatory = true
        )
    }
}