package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.input.EditAutomaticallyDeterminedValueOrg
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.input.InputTextMultilineMlc
import ua.gov.diia.ui_base.components.molecule.input.InputTextMultilineMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUIModel
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditAutomaticallyDeterminedValueOrg(
    modifier: Modifier = Modifier,
    data: EditAutomaticallyDeterminedValueOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 16.dp)
    ) {
        data.title?.let {
            Text(
                text = data.title.asString(),
                style = DiiaTextStyle.t3TextBody,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        data.label?.let {
            Text(
                text = data.label.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        data.value?.let {
            Text(
                text = data.value.asString(),
                style = DiiaTextStyle.t1BigText,
                color = BlackAlpha30,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        DividerSlimAtom()

        data.inputData?.let {
            Spacer(modifier = Modifier.height(16.dp))
            InputTextMultilineMlc(
                data = data.inputData,
                onUIAction = onUIAction,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

data class EditAutomaticallyDeterminedValueOrgData(
    val actionKey: String = UIActionKeysCompose.TEXT_INPUT,
    val title: UiText? = null,
    val label: UiText? = null,
    val value: UiText? = null,
    val inputData: InputTextMultilineMlcData? = null,
    val componentId: UiText? = null,
) : UIElementData {

    fun onInputChanged(newValue: String?): EditAutomaticallyDeterminedValueOrgData {
        val input = this.inputData?.onInputChanged(newValue)
        return this.copy(inputData = input)
    }

    fun getValidationState(): UIState.Validation {
        return inputData?.validation ?: UIState.Validation.NeverBeenPerformed
    }
}

fun EditAutomaticallyDeterminedValueOrg.toUIModel(): EditAutomaticallyDeterminedValueOrgData {
    return EditAutomaticallyDeterminedValueOrgData(
        title = title?.let { UiText.DynamicString(it) },
        label = label?.let { UiText.DynamicString(it) },
        value = value?.let { UiText.DynamicString(it) },
        componentId = UiText.DynamicString(componentId.orEmpty()),
        inputData = inputTextMlc?.toUIModel()
    )
}

@Composable
@Preview
fun EditAutomaticallyDeterminedValueOrgPreview() {
    val input = InputTextMultilineMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        mandatory = false,
        validationRules = null
    )
    val data = EditAutomaticallyDeterminedValueOrgData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Автоматично-визначене прізвище"),
        value = UiText.DynamicString("Дія-Великий"),
        inputData = input
    )
    EditAutomaticallyDeterminedValueOrg(Modifier, data) {}
}

