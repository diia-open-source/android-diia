package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnControlAtm
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.WhiteAlpha20

@Composable
fun BtnControlMlc(
    modifier: Modifier = Modifier,
    data: BtnControlMlc,
    onUIAction: (UIAction) -> Unit
) {
    val micIcon = if (data.micEnabled == UIState.Interaction.Disabled) {
        DiiaResourceIcon.MIC_OFF
    } else {
        DiiaResourceIcon.MIC_ON
    }
    val outputIcon = if (data.outputEnabled == UIState.Interaction.Disabled) {
        DiiaResourceIcon.SPEAKER_OFF
    } else {
        DiiaResourceIcon.SPEAKER_ON
    }

    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        BtnControlAtm(data.actionKeyMic, micIcon, WhiteAlpha20, onUIAction)
        Spacer(modifier = Modifier.size(8.dp))
        BtnControlAtm(data.actionKeyOutput, outputIcon, WhiteAlpha20, onUIAction)
        Spacer(modifier = Modifier.size(8.dp))
        BtnControlAtm(data.actionKeyClose, DiiaResourceIcon.CLOSE, Red, onUIAction)
    }
}

data class BtnControlMlc(
    val actionKeyMic: String = UIActionKeysCompose.CONTROL_BUTTON_MIC_MOLECULE,
    val actionKeyOutput: String = UIActionKeysCompose.CONTROL_BUTTON_OUTPUT_MOLECULE,
    val actionKeyClose: String = UIActionKeysCompose.CLOSE_BUTTON,
    val micEnabled: UIState.Interaction = UIState.Interaction.Disabled,
    val outputEnabled: UIState.Interaction = UIState.Interaction.Disabled,
)

@Composable
@Preview
fun BtnControlMlcPreview() {
    val data = BtnControlMlc()
    BtnControlMlc(Modifier.background(Color.Black), data) {}
}

@Composable
@Preview
fun BtnControlMlcPreview_enabled() {
    val data = BtnControlMlc(
        micEnabled = UIState.Interaction.Enabled,
        outputEnabled = UIState.Interaction.Enabled,
    )
    BtnControlMlc(Modifier.background(Color.Black), data) {}
}