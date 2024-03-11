package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun StubMessageMlc(
    modifier: Modifier = Modifier,
    data: StubMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 64.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.icon.asString(),
            style = TextStyle(
                fontSize = 52.sp,
                lineHeight = 62.sp
            )
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = data.title.asString(),
            textAlign = TextAlign.Center,
            style = DiiaTextStyle.h3SmallHeading
        )
        data.description?.let {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = data.description.asString(),
                textAlign = TextAlign.Center,
                style = DiiaTextStyle.t3TextBody
            )
        }
        data.button?.let {
            ButtonStrokeAdditionalAtom(
                modifier = Modifier.padding(top = 16.dp),
                data = data.button,
                onUIAction = onUIAction
            )
        }
    }
}

data class StubMessageMlcData(
    val icon: UiText,
    val title: UiText,
    val description: UiText? = null,
    val button: ButtonStrokeAdditionalAtomData? = null
) : UIElementData

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title() {
    val data = StubMessageMlcData(
        icon = UiText.DynamicString("\uD83D\uDC4C"),
        title = UiText.DynamicString("У вас все добре"),
    )

    StubMessageMlc(
        data = data
    ) {

    }
}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title_Description() {
    val data = StubMessageMlcData(
        icon = UiText.DynamicString("\uD83D\uDC4C"),
        title = UiText.DynamicString("У вас все добре"),
        description = UiText.DynamicString("Відкритих проваджень немає")
    )

    StubMessageMlc(
        data = data
    ) {

    }
}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title_Description_Button() {
    val data = StubMessageMlcData(
        icon = UiText.DynamicString("\uD83D\uDC4C"),
        title = UiText.DynamicString("У вас все добре"),
        description = UiText.DynamicString("Відкритих проваджень немає"),
        button = ButtonStrokeAdditionalAtomData(
            actionKey = UIActionKeysCompose.BUTTON_REGULAR,
            id = "1",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        )
    )

    StubMessageMlc(
        data = data
    ) {}

}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title_Button() {
    val data = StubMessageMlcData(
        icon = UiText.DynamicString("\uD83D\uDC4C"),
        title = UiText.DynamicString("У вас все добре"),
        button = ButtonStrokeAdditionalAtomData(
            actionKey = UIActionKeysCompose.BUTTON_REGULAR,
            id = "1",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        )
    )

    StubMessageMlc(
        data = data
    ) {}

}
