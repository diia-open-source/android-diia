package ua.gov.diia.ui_base.components.molecule.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.chip.ChipTimeMlc
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ChipTimeMlc(
    modifier: Modifier = Modifier,
    data: ChipTimeMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val colorBg = if (data.selection == UIState.Selection.Selected) {
        Black
    } else {
        White
    }
    val colorText = if (data.selection == UIState.Selection.Selected) {
        White
    } else {
        Black
    }
    Box(
        modifier = modifier
            .background(colorBg, shape = RoundedCornerShape(40.dp))
            .clickable {
                onUIAction(
                    UIAction(
                        data.actionKey,
                        data.code,
                        optionalId = data.date,
                        optionalType = data.resourceId
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Text(
            text = data.title.asString(),
            color = colorText,
            style = DiiaTextStyle.t2TextDescription,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}

data class ChipTimeMlcData(
    val actionKey: String = UIActionKeysCompose.CHIP_TIME_MLC,
    val title: UiText,
    val date: String,
    val id: String? = null,
    val code: String? = null,
    val resourceId: String? = null,
    val componentId: UiText? = null,
    val selection: UIState.Selection = UIState.Selection.Unselected,
)

fun ChipTimeMlc.toUIModel(): ChipTimeMlcData {
    return ChipTimeMlcData(
        title = UiText.DynamicString(label.orEmpty()),
        id = this.id,
        date = "",
        code = this.data?.code,
        resourceId = this.data?.resourceId,
        componentId = UiText.DynamicString(componentId.orEmpty()),
        selection = if (this.active == true) UIState.Selection.Selected else UIState.Selection.Unselected,
    )
}

@Composable
@Preview
fun ChipTimeMlcPreview() {
    val data = ChipTimeMlcData(
        title = UiText.DynamicString("label"),
        selection = UIState.Selection.Selected,
        date = "",
    )
    ChipTimeMlc(Modifier, data, {})
}

@Composable
@Preview
fun ChipTimeMlcPreview_Unselected() {
    val data = ChipTimeMlcData(
        title = UiText.DynamicString("label"),
        selection = UIState.Selection.Unselected,
        date = "",
    )
    ChipTimeMlc(Modifier, data, {})
}

