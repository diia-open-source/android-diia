package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnControlAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWhiteAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWhiteAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.WhiteAlpha20

@Composable
fun BtnControlMlc(
    modifier: Modifier = Modifier,
    data: BtnControlMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val micIcon = if (data.micEnabled == UIState.Interaction.Disabled) {
        DiiaResourceIcon.MIC_OFF
    } else {
        DiiaResourceIcon.MIC_ON
    }

    BoxWithConstraints {
        if (maxWidth < 320.dp) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (data.signButtonEnabled == UIState.Interaction.Enabled) {
                    BtnStrokeWhiteAtm(
                        data = BtnStrokeWhiteAtmData(
                            id = UIActionKeysCompose.BUTTON_REGULAR,
                            title = UiText.DynamicString("Підписати"),
                            interactionState = data.signButtonEnabled,
                        ), onUIAction = onUIAction
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    BtnControlAtm(
                        Modifier,
                        data.actionKeyFlipCamera,
                        DiiaResourceIcon.FLIP_CAMERA,
                        WhiteAlpha20,
                        onUIAction
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    BtnControlAtm(Modifier, data.actionKeyMic, micIcon, WhiteAlpha20, onUIAction)
                    Spacer(modifier = Modifier.size(16.dp))
                    BtnControlAtm(
                        Modifier,
                        data.actionKeyClose,
                        DiiaResourceIcon.CLOSE,
                        Red,
                        onUIAction
                    )
                }
            }
        } else {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                BtnControlAtm(
                    Modifier,
                    data.actionKeyFlipCamera,
                    DiiaResourceIcon.FLIP_CAMERA,
                    WhiteAlpha20,
                    onUIAction
                )
                Spacer(modifier = Modifier.size(16.dp))
                BtnControlAtm(Modifier, data.actionKeyMic, micIcon, WhiteAlpha20, onUIAction)
                Spacer(modifier = Modifier.size(16.dp))
                BtnControlAtm(
                    Modifier,
                    data.actionKeyClose,
                    DiiaResourceIcon.CLOSE,
                    Red,
                    onUIAction
                )
                if (data.signButtonEnabled == UIState.Interaction.Enabled) {
                    Spacer(modifier = Modifier.size(16.dp))
                    BtnStrokeWhiteAtm(
                        data = BtnStrokeWhiteAtmData(
                            id = UIActionKeysCompose.BUTTON_REGULAR,
                            title = UiText.DynamicString("Підписати"),
                            interactionState = data.signButtonEnabled,
                        ), onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

data class BtnControlMlcData(
    val actionKeyMic: String = UIActionKeysCompose.CONTROL_BUTTON_MIC_MOLECULE,
    val actionKeyFlipCamera: String = UIActionKeysCompose.CONTROL_BUTTON_CAMERA_FLIP_MOLECULE,
    val actionKeyClose: String = UIActionKeysCompose.ITEM_HANG_UP,
    val micEnabled: UIState.Interaction = UIState.Interaction.Disabled,
    val outputEnabled: UIState.Interaction = UIState.Interaction.Disabled,
    val signButtonEnabled: UIState.Interaction = UIState.Interaction.Disabled,
)

@Composable
@Preview(widthDp = 300)
fun BtnControlMlcPreview() {
    val data = BtnControlMlcData()
    BtnControlMlc(Modifier.background(Color.Transparent), data) {}
}

@Composable
@Preview(widthDp = 300)
fun BtnControlMlcPreview_enabled() {
    val data = BtnControlMlcData(
        micEnabled = UIState.Interaction.Enabled,
        outputEnabled = UIState.Interaction.Enabled,
        signButtonEnabled = UIState.Interaction.Enabled,
    )
    BtnControlMlc(Modifier.background(Color.Transparent), data) {}
}


@Composable
@Preview(widthDp = 400)
fun BtnControlMlcPreview_wide() {
    val data = BtnControlMlcData()
    BtnControlMlc(Modifier.background(Color.Transparent), data) {}
}

@Composable
@Preview(widthDp = 400)
fun BtnControlMlcPreview_enabled_wide() {
    val data = BtnControlMlcData(
        micEnabled = UIState.Interaction.Enabled,
        outputEnabled = UIState.Interaction.Enabled,
        signButtonEnabled = UIState.Interaction.Enabled,
    )
    BtnControlMlc(Modifier.background(Color.Transparent), data) {}
}