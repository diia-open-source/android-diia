package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelContainerMlc
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun TextLabelContainerMlc(
    modifier: Modifier = Modifier,
    data: TextLabelContainerMlcData,
    isFirstAtBody: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = if (isFirstAtBody) 8.dp else 24.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: ""),
    ) {
        data.data?.let {
            TextWithParametersAtom(
                modifier = Modifier
                    .padding(16.dp),
                data = it,
                onUIAction = onUIAction
            )
        }
    }
}

data class TextLabelContainerMlcData(
    val componentId: UiText? = null,
    val data: TextWithParametersData? = null
) : UIElementData

fun TextLabelContainerMlc?.toUIModel(
    actionKey: String = UIActionKeysCompose.TEXT_WITH_PARAMETERS
): TextLabelMlcData? {
    val entity = this
    if (entity?.text == null) return null
    return TextLabelMlcData(
        componentId = UiText.DynamicString(this?.componentId.orEmpty()),
        actionKey = actionKey,
        text = UiText.DynamicString(entity.text ?: ""),
        parameters = if (entity.parameters != null) {
            mutableListOf<TextParameter>().apply {
                entity.parameters?.forEach {
                    add(
                        TextParameter(
                            data = TextParameter.Data(
                                name = it.data?.name.toDynamicStringOrNull(),
                                resource = it.data?.resource.toDynamicStringOrNull(),
                                alt = it.data?.alt.toDynamicStringOrNull()
                            ),
                            type = it.type
                        )
                    )
                }
            }
        } else {
            emptyList()
        }
    )
}

fun generateTextLabelContainerMlcMockData(): TextLabelContainerMlcData {
    val phoneText =
        UiText.DynamicString("Щоб вирішити це питання, будь ласка, зверніться до ДМС України \nза номером {dmsPhoneNumber}")
    val phoneParameter = TextParameter(
        type = "phone",
        data = TextParameter.Data(
            name = UiText.DynamicString("dmsPhoneNumber"),
            alt = UiText.DynamicString("+38 (044) 278-34-02"),
            resource = UiText.DynamicString("+380442783402"),
        )
    )
    val textWithParametersData =
        TextWithParametersData(text = phoneText, parameters = listOf(phoneParameter))
    return TextLabelContainerMlcData(
        data = textWithParametersData
    )
}

@Composable
@Preview
fun TextLabelContainerMlcPreview() {
    TextLabelContainerMlc(modifier = Modifier, data = generateTextLabelContainerMlcMockData()) {}
}