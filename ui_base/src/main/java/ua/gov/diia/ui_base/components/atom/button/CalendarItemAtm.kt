package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.CalendarItemAtm
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CalendarItemAtm(
    modifier: Modifier = Modifier,
    data: CalendarItemAtmData,
    onUIAction: (UIAction) -> Unit
) {
    val isDisabled = data.interaction == UIState.Interaction.Disabled
    val isSelected = data.selection == UIState.Selection.Selected
    val colorBg = if (isSelected) Black else Color.Transparent

    val colorText = when {
        isSelected -> White
        isDisabled -> BlackAlpha30
        else -> Black
    }

    Box {
        Box(
            modifier = modifier
                .requiredSize(40.dp)
                .noRippleClickable {
                    if (!isDisabled && !isSelected) {
                        onUIAction(UIAction(data.actionKey, data = data.title))
                    }
                }
                .composed {
                    if (data.isToday && data.selection != UIState.Selection.Selected) {
                        border(
                            width = 1.dp,
                            color = if (isDisabled) BlackAlpha30 else Black,
                            shape = CircleShape
                        )
                    } else {
                        background(colorBg, shape = CircleShape)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data.title,
                color = colorText,
                style = DiiaTextStyle.t1BigText,
                textAlign = TextAlign.Center,
            )
        }
    }
}

data class CalendarItemAtmData(
    val actionKey: String = UIActionKeysCompose.CALENDAR_ITEM_ATM,
    val title: String,
    val interaction: UIState.Interaction = UIState.Interaction.Disabled,
    val selection: UIState.Selection = UIState.Selection.Unselected,
    val isToday: Boolean = false,
)

fun CalendarItemAtm.toUiModel(): CalendarItemAtmData {
    return CalendarItemAtmData(
        title = label.orEmpty(),
        interaction = if (isActive == true) UIState.Interaction.Enabled else UIState.Interaction.Disabled,
        selection = if (isSelected == true) UIState.Selection.Selected else UIState.Selection.Unselected,
        isToday = isToday ?: false
    )
}

@Composable
@Preview
fun CalendarItemAtmPreview_isActive() {
    val data = CalendarItemAtmData(
        title = "1",
        interaction = UIState.Interaction.Enabled
    )
    CalendarItemAtm(Modifier, data) {}
}

@Preview
@Composable
fun CalendarItemAtmPreview_disabled() {
    val data = CalendarItemAtmData(
        title = "5",
        interaction = UIState.Interaction.Disabled
    )
    CalendarItemAtm(Modifier, data) {}
}

@Composable
@Preview
fun CalendarItemAtmPreview_isToday() {
    val data = CalendarItemAtmData(
        title = "2",
        isToday = true,
        selection = UIState.Selection.Unselected
    )
    CalendarItemAtm(Modifier, data) {}
}

@Composable
@Preview
fun CalendarItemAtmPreview_isToday_enabled() {
    val data = CalendarItemAtmData(
        title = "29",
        isToday = true,
        interaction = UIState.Interaction.Enabled
    )
    CalendarItemAtm(Modifier, data) {}
}

@Composable
@Preview
fun CalendarItemAtmPreview_isSelected() {
    val data = CalendarItemAtmData(
        title = "11",
        selection = UIState.Selection.Selected
    )
    CalendarItemAtm(Modifier, data) {}
}

@Composable
@Preview
fun CalendarItemAtmPreview_isSelected2() {
    val data = CalendarItemAtmData(
        title = "31",
        selection = UIState.Selection.Selected,
        isToday = true
    )
    CalendarItemAtm(Modifier, data) {}
}

