package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelMlc
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersConstants
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TextLabelMlc(
    modifier: Modifier = Modifier,
    data: TextLabelMlcData,
    style: TextStyle = DiiaTextStyle.t3TextBody,
    onUIAction: (UIAction) -> Unit
) {
    TextWithParametersAtom(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .testTag(data.componentId?.asString() ?: ""),
        data = TextWithParametersData(
            actionKey = UIActionKeysCompose.TEXT_WITH_PARAMETERS,
            text = data.text,
            parameters = data.parameters
        ),
        style = style,
        onUIAction = onUIAction
    )
}

data class TextLabelMlcData(
    val actionKey: String = UIActionKeysCompose.TEXT_LABEL_MLC,
    val text: UiText,
    val parameters: List<TextParameter>? = null,
    val componentId: UiText? = null,
) : UIElementData

fun TextLabelMlc?.toUIModel(): TextLabelMlcData? {
    val entity = this
    if (entity?.text == null) return null
    return TextLabelMlcData(
        componentId = UiText.DynamicString(this?.componentId.orEmpty()),
        text = UiText.DynamicString(entity.text),
        parameters = if (entity.parameters != null) {
            mutableListOf<TextParameter>().apply {
                entity.parameters?.forEach {
                    add(
                        TextParameter(
                            data = ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter.Data(
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


@Preview
@Composable
fun TextLabelMlcPreview() {
    val linkText =
        "Міністерство цифрової трансформації та UNITED24 розпочинають проєкт «Армія дронів»! {details}"
    val linkParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_LINK,
        data = TextParameter.Data(
            name = UiText.DynamicString("details"),
            alt = UiText.DynamicString("Детальніше на сайті UNITED24"),
            resource = UiText.DynamicString("https://u24.gov.ua/"),
        )
    )

    val textLabelMlcData =
        TextLabelMlcData(text = linkText.toDynamicString(), parameters = listOf(linkParameter))

    TextLabelMlc(
        data = textLabelMlcData
    ) {
    }
}