package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnPlainAtm(
    modifier: Modifier = Modifier,
    data: BtnPlainAtmData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                top = 24.dp,
                bottom = 16.dp,
                start = 40.dp,
                end = 40.dp
            )
            .noRippleClickable(debounce = true) {
                if (data.interactionState == UIState.Interaction.Enabled && !(data.id == progressIndicator.first && progressIndicator.second))
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action
                        )
                    )
            }
            .testTag(data.componentId?.asString() ?: ""),
    ) {
        AnimatedVisibility(visible = data.id == progressIndicator.first && progressIndicator.second) {
            Row {
                LoaderCircularEclipse23Subatomic()
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Text(
            text = AnnotatedString(data.title.asString()),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                fontWeight = FontWeight.Normal,
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha10
                    UIState.Interaction.Enabled -> Black
                },
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
        )
    }
}


data class BtnPlainAtmData(
    val actionKey: String = UIActionKeysCompose.BTN_PLAIN_ATM,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null,
    val componentId: UiText? = null
) : UIElementData

fun BtnPlainAtm.toUIModel(
    id: String = "",
    componentIdExternal: UiText? = null
): BtnPlainAtmData {
    return BtnPlainAtmData(
        componentId = componentIdExternal ?: componentId.orEmpty().toDynamicString(),
        id = this.componentId ?: id,
        title = UiText.DynamicString(this.label),
        action = action?.toDataActionWrapper(),
        interactionState = state?.let {
            when (state) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Enabled
            }
        } ?: UIState.Interaction.Enabled,
    )
}

@Composable
@Preview
fun BtnPlainAtmPreview_EnabledState() {
    val buttonStateEnabled = BtnPlainAtmData(
        title = UiText.DynamicString("Label"),
        interactionState = UIState.Interaction.Enabled
    )
    BtnPlainAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnPlainAtmPreview_LongLabel() {
    val buttonStateEnabled = BtnPlainAtmData(
        title = UiText.DynamicString("Label very long label still long label should be centered"),
        interactionState = UIState.Interaction.Enabled
    )
    BtnPlainAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnPlainAtmPreview_DisabledState() {
    val buttonStateDisabled = BtnPlainAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnPlainAtm(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun BtnPlainAtmPreview_LoadingState() {
    val buttonStateLoading = BtnPlainAtmData(
        title = UiText.DynamicString("Label"),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    BtnPlainAtm(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}
