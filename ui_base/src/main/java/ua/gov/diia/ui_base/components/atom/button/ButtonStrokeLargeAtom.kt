package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent


@Composable
fun ButtonStrokeLargeAtom(
    modifier: Modifier = Modifier,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    data: ButtonStrokeLargeAtomData,
    onUIAction: (UIAction) -> Unit
) {
    Button(
        modifier = modifier
            .padding(top = 32.dp)
            .wrapContentWidth()
            .defaultMinSize(minHeight = 56.dp, minWidth = 160.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Transparent,
            disabledContainerColor = Transparent
        ),
        contentPadding = PaddingValues(),
        border = BorderStroke(
            width = 2.dp, color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            }
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
        }

    ) {
        Spacer(modifier = modifier.width(40.dp))
        Box {
            this@Button.AnimatedVisibility(
                visible = data.id == progressIndicator.first && progressIndicator.second,
                enter = fadeIn(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row {
                    LoaderCircularEclipse23Subatomic(modifier = modifier.size(18.dp))
                    Spacer(modifier = modifier.width(8.dp))
                }
            }
        }
        Text(
            modifier = modifier.padding(vertical = 16.dp),
            text = data.title.asString(), color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.h4ExtraSmallHeading
        )
        Spacer(modifier = modifier.width(40.dp))
    }
}

data class ButtonStrokeLargeAtomData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String,
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
)

@Composable
@Preview
fun ButtonStrokeLargeAtomPreview_EnabledState() {
    val buttonStateEnabled = ButtonStrokeLargeAtomData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    ButtonStrokeLargeAtom(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun ButtonStrokeLargeAtomPreview_DisabledState() {
    val buttonStateDisabled = ButtonStrokeLargeAtomData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    ButtonStrokeLargeAtom(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun ButtonStrokeLargeAtomPreview_LoadingState() {
    val buttonStateLoading = ButtonStrokeLargeAtomData(
        title = UiText.DynamicString("Label"),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    ButtonStrokeLargeAtom(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}

