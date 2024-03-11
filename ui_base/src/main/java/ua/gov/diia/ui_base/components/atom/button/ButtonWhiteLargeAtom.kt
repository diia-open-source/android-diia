package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50


@Composable
fun ButtonWhiteLargeAtom(
    modifier: Modifier = Modifier,
    data: ButtonWhiteLargeAtomData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Button(
        modifier = modifier
            .defaultMinSize(minWidth = 311.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            disabledContainerColor = WhiteAlpha50
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
        }
    ) {
        AnimatedVisibility(visible = data.id == progressIndicator.first && progressIndicator.second) {
            Row {
                LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = data.title,
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.h4ExtraSmallHeading
        )
    }
}

data class ButtonWhiteLargeAtomData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String,
    val title: String,
    val interactionState: UIState.Interaction
)

@Composable
@Preview
fun ButtonWhiteLargeAtomPreview_EnabledState() {
    val buttonStateEnabled = ButtonWhiteLargeAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    ButtonWhiteLargeAtom(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun ButtonWhiteLargeAtomPreview_DisabledState() {
    val buttonStateDisabled = ButtonWhiteLargeAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    ButtonWhiteLargeAtom(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun ButtonWhiteLargeAtomPreview_LoadingState() {
    val buttonStateLoading = ButtonWhiteLargeAtomData(
        title = "Label",
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    ButtonWhiteLargeAtom(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}
