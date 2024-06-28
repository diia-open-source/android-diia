package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.border.diiaWhiteBorder

@Composable
fun CheckboxBorderedMlc(
    modifier: Modifier = Modifier,
    data: CheckboxBorderedMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .diiaWhiteBorder()
            .padding(20.dp)
            .testTag(data.componentId?.asString() ?: "")
    )
    {
        CheckboxSquareMlc(
            modifier = Modifier,
            data = data.data,
            onUIAction = onUIAction
        )
    }
}

data class CheckboxBorderedMlcData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_BTN_ORG,
    val data: CheckboxSquareMlcData,
    val componentId: UiText? = null,
) : UIElementData {

    fun onOptionsCheckChanged(): CheckboxBorderedMlcData {
        val checkBox = data.onCheckboxClick()
        return this.copy(data = checkBox)
    }
}

@Preview
@Composable
fun CheckboxBorderedMlcPreview() {
    val data = CheckboxBorderedMlcData(
        data = CheckboxSquareMlcData(
            id = "1",
            title = UiText.DynamicString("Надаю дозвіл на перевірку кредитної історії"),
            interactionState = UIState.Interaction.Enabled,
            selectionState = UIState.Selection.Unselected
        )
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxBorderedMlc(
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged()
            }
        }
    }
}