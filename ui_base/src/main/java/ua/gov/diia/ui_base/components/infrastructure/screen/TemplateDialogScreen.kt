package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeLargeAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeLargeAtomData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun TemplateDialogScreen(
    modifier: Modifier = Modifier,
    dataState: State<TemplateDialogScreenData>,
    onUIAction: (UIAction) -> Unit
) {
    val data = dataState.value
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val alignment = if (data.alignment == TemplateDialogScreenData.Alignment.LEFT)
                    TextAlign.Start
                else
                    TextAlign.Center
                if (data.icon != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = data.icon.asString(),
                        textAlign = alignment,
                        style = TextStyle(
                            fontSize = 32.sp,
                            lineHeight = 36.sp
                        )
                    )
                }
                if (data.titleText != null) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        text = data.titleText.asString(),
                        textAlign = alignment,
                        style = DiiaTextStyle.h2MediumHeading
                    )
                }
                if (data.descriptionText != null) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        text = data.descriptionText.asString(),
                        textAlign = alignment,
                        style = DiiaTextStyle.t2TextDescription
                    )
                }

                if (data.strokeButton != null) {
                    ButtonStrokeLargeAtom(
                        data = data.strokeButton,
                        onUIAction = onUIAction
                    )
                }

                if (data.primaryButton != null) {
                    BtnPrimaryDefaultAtm(
                        modifier = Modifier.padding(top = 24.dp),
                        data = data.primaryButton,
                        onUIAction = onUIAction
                    )
                }

                if (data.secondaryButton != null) {
                    BtnPlainAtm(
                        data = data.secondaryButton,
                        onUIAction = onUIAction
                    )
                }
            }
            if (data.isCloseable) {
                IconButton(
                    modifier = modifier
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .size(24.dp)
                        .align(Alignment.TopEnd),
                    onClick = {
                        onUIAction.invoke(UIAction(actionKey = UIActionKeysCompose.CLOSE_BUTTON))
                    }
                ) {
                    Icon(
                        modifier = modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "",
                        tint = Primary
                    )
                }
            }
        }
    }
}

data class TemplateDialogScreenData(
    val alignment: Alignment = Alignment.CENTER,
    val icon: UiText? = null,
    val titleText: UiText? = null,
    val descriptionText: UiText? = null,
    val primaryButton: BtnPrimaryDefaultAtmData? = null,
    val secondaryButton: BtnPlainAtmData? = null,
    val strokeButton: ButtonStrokeLargeAtomData? = null,
    val isCloseable: Boolean = true,
) {
    enum class Alignment {
        LEFT, CENTER
    }
}


@Composable
@Preview
fun TemplateDialogScreenPreview_stroke() {
    val data = TemplateDialogScreenData(
        icon = UiText.DynamicString("\uD83D\uDC4D"),
        titleText = UiText.DynamicString("Heading"),
        descriptionText = UiText.DynamicString("Here is the text that shortly describes the issue or statement."),
        strokeButton = ButtonStrokeLargeAtomData(
            id = "",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    val state = remember { mutableStateOf(data) }
    TemplateDialogScreen(dataState = state, onUIAction = {})
}

@Composable
@Preview
fun TemplateDialogScreenPreview_two_buttons() {
    val data = TemplateDialogScreenData(
        icon = UiText.DynamicString("\uD83D\uDC4D"),
        titleText = UiText.DynamicString("Heading"),
        descriptionText = UiText.DynamicString("Here is the text that shortly describes the issue or statement."),
        primaryButton = BtnPrimaryDefaultAtmData(
            id = "",
            title = UiText.DynamicString("Primary"),
            interactionState = UIState.Interaction.Enabled
        ),
        secondaryButton = BtnPlainAtmData(
            id = "",
            title = UiText.DynamicString("Secondary"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    val state = remember { mutableStateOf(data) }
    TemplateDialogScreen(dataState = state, onUIAction = {})
}


@Composable
@Preview
fun TemplateDialogScreenPreview_two_buttons_left() {
    val data = TemplateDialogScreenData(
        alignment = TemplateDialogScreenData.Alignment.LEFT,
        icon = UiText.DynamicString("\uD83D\uDC4D"),
        titleText = UiText.DynamicString("Heading"),
        descriptionText = UiText.DynamicString("Here is the text that describes the issue or statement.\u2028It might include multiple statements. That`s why it aligned left. This type of alert should not be used as a generic pattern. \u2028It is quite controversial approach to put long text to the modals as people do not read long text unles they are super important. Also we should mind the screen size. For a small devices this aler is already gets scrolled. We should try to avoid such long texts if possible."),
        primaryButton = BtnPrimaryDefaultAtmData(
            id = "",
            title = UiText.DynamicString("Primary"),
            interactionState = UIState.Interaction.Enabled
        ),
        secondaryButton = BtnPlainAtmData(
            id = "",
            title = UiText.DynamicString("Secondary"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    val state = remember { mutableStateOf(data) }
    TemplateDialogScreen(dataState = state, onUIAction = {})
}