package ua.gov.diia.ui_base.components.atom.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.TextLabelAtm
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TextLabelAtm(
    modifier: Modifier = Modifier,
    data: TextLabelAtmData
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1f),
            text = data.label.asString(),
            color = if (data.mode == TextLabelAtmMode.SECONDARY || (data.isEnabled != null && data.isEnabled == false)) {
                BlackAlpha30
            } else {
                Black
            },
            style = DiiaTextStyle.t3TextBody
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = data.value?.asString() ?: "",
            color = if (data.mode == TextLabelAtmMode.SECONDARY || (data.isEnabled != null && data.isEnabled == false)) {
                BlackAlpha30
            } else {
                Black
            },
            style = DiiaTextStyle.t3TextBody
        )
    }
}

data class TextLabelAtmData(
    val actionKey: String = UIActionKeysCompose.TEXT_LABEL_ATM_DATA,
    val componentId: UiText?,
    val mode: TextLabelAtmMode,
    val label: UiText,
    val value: UiText?,
    val isEnabled: Boolean?
) : UIElementData

enum class TextLabelAtmMode(val modeName: String) {
    PRIMARY("primary"),
    SECONDARY("secondary")
}

fun TextLabelAtm.toUIModel(
    isEnabled: Boolean = true,
): TextLabelAtmData {
    return TextLabelAtmData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        label = this.label.toDynamicString(),
        value = this.value.toDynamicStringOrNull(),
        isEnabled = isEnabled,
        mode = when (this.mode) {
            TextLabelAtm.Mode.PRIMARY -> TextLabelAtmMode.PRIMARY
            TextLabelAtm.Mode.SECONDARY -> TextLabelAtmMode.SECONDARY
        }
    )
}

@Preview
@Composable
fun TextLabelAtmPreview_Enabled() {
    val data = generateTextLabelAtmMockData(TextLabelAtmMockType.enabled)

    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(all = 24.dp)
        TextLabelAtm(
            modifier = modifier,
            data = data
        )
    }
}

@Preview
@Composable
fun TextLabelAtmPreview_Disabled() {
    val data = generateTextLabelAtmMockData(TextLabelAtmMockType.disabled)

    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(all = 24.dp)
        TextLabelAtm(
            modifier = modifier,
            data = data
        )
    }
}

@Preview
@Composable
fun TextLabelAtmPreview_LongLabel() {
    val data = generateTextLabelAtmMockData(TextLabelAtmMockType.long_label)

    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(all = 24.dp)
        TextLabelAtm(
            modifier = modifier,
            data = data
        )
    }
}

@Preview
@Composable
fun TextLabelAtmPreview_LongValue() {
    val data = generateTextLabelAtmMockData(TextLabelAtmMockType.long_value)

    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(all = 24.dp)
        TextLabelAtm(
            modifier = modifier,
            data = data
        )
    }
}

@Preview
@Composable
fun TextLabelAtmPreview_ValueIsNull() {
    val data = generateTextLabelAtmMockData(TextLabelAtmMockType.value_is_null)

    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(all = 24.dp)
        TextLabelAtm(
            modifier = modifier,
            data = data
        )
    }
}

@Preview
@Composable
fun TextLabelAtmPreview_FromJsonModel() {
    val jsonModel = TextLabelAtm(
        componentId = "someId",
        mode = TextLabelAtm.Mode.SECONDARY,
        label = "label",
        value = "value"
    )
    val data = jsonModel.toUIModel()

    Box(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(all = 24.dp)
        TextLabelAtm(
            modifier = modifier,
            data = data
        )
    }
}

enum class TextLabelAtmMockType {
    enabled, disabled, long_label, long_value, value_is_null
}

fun generateTextLabelAtmMockData(mockType: TextLabelAtmMockType): TextLabelAtmData {
    return when (mockType) {
        TextLabelAtmMockType.enabled -> TextLabelAtmData(
            componentId = "default".toDynamicString(),
            mode = TextLabelAtmMode.PRIMARY,
            label = "Label:".toDynamicString(),
            value = "Value".toDynamicString(),
            isEnabled = true
        )

        TextLabelAtmMockType.disabled -> TextLabelAtmData(
            componentId = "default".toDynamicString(),
            mode = TextLabelAtmMode.PRIMARY,
            label = "Label:".toDynamicString(),
            value = "Value".toDynamicString(),
            isEnabled = false
        )

        TextLabelAtmMockType.long_label -> TextLabelAtmData(
            componentId = "default".toDynamicString(),
            mode = TextLabelAtmMode.PRIMARY,
            label = "The label is very long to fill first part of the view:".toDynamicString(),
            value = "Value".toDynamicString(),
            isEnabled = true
        )

        TextLabelAtmMockType.long_value -> TextLabelAtmData(
            componentId = "default".toDynamicString(),
            mode = TextLabelAtmMode.PRIMARY,
            label = "Label".toDynamicString(),
            value = "The value is very long to fill first part of the view:".toDynamicString(),
            isEnabled = true
        )

        TextLabelAtmMockType.value_is_null -> TextLabelAtmData(
            componentId = "default".toDynamicString(),
            mode = TextLabelAtmMode.SECONDARY,
            label = "The label is very long to fill first part of the view:".toDynamicString(),
            value = null,
            isEnabled = true
        )
    }
}