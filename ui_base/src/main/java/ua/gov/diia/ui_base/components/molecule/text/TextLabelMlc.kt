package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersConstants
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
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
            .padding(top = 24.dp),
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
    val parameters: List<TextParameter>? = null
) : UIElementData

@Preview
@Composable
fun TextLabelMlcPreview() {
    val linkText =
        "Міністерство цифрової трансформації розпочинають проєкт! {details}"
    val linkParameter = TextParameter(
        type = TextWithParametersConstants.TYPE_LINK,
        data = TextParameter.Data(
            name = UiText.DynamicString("details"),
            alt = UiText.DynamicString("Детальніше на сайті"),
            resource = UiText.DynamicString("https://test.test/"),
        )
    )

    val textLabelMlcData =
        TextLabelMlcData(text = linkText.toDynamicString(), parameters = listOf(linkParameter))

    TextLabelMlc(
        data = textLabelMlcData
    ) {
    }
}