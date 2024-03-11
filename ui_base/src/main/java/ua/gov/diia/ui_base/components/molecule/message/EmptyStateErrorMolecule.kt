package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun EmptyStateErrorMolecule(
    modifier: Modifier = Modifier,
    data: EmptyStateErrorMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier) {
        data.icon?.let {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = data.icon,
                style = DiiaTextStyle.h1Heading
            )
        }
        data.text?.let {
            TextWithParametersAtom(data = data.text, onUIAction = onUIAction)
        }

        data.button?.let {
            ButtonStrokeAdditionalAtom(
                modifier = Modifier.padding(top = 32.dp),
                data = data.button,
                onUIAction = onUIAction
            )
        }
    }
}

data class EmptyStateErrorMoleculeData(val text: TextWithParametersData?, val icon: String? = null, val button: ButtonStrokeAdditionalAtomData? = null)

@Composable
@Preview
fun EmptyStateErrorMoleculePreview() {
    val data = EmptyStateErrorMoleculeData(
        text = TextWithParametersData(text = UiText.DynamicString(LoremIpsum(20).values.joinToString())),
        icon = "\uD83E\uDD37\u200D♂️",
        button = ButtonStrokeAdditionalAtomData(
            id = "",
            title = UiText.DynamicString("Next"),
            interactionState = UIState.Interaction.Enabled
        )
    )

    EmptyStateErrorMolecule(data = data) {

    }
}