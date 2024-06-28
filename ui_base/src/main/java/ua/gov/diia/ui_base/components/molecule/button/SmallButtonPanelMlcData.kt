package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun SmallButtonPanelMlc(
    modifier: Modifier = Modifier,
    data: SmallButtonPanelMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier.fillMaxWidth(0.62f)) {
            Text(
                modifier = Modifier,
                text = data.icon?.asString() + " " + data.label.asString(),
                color = Black,
                style = DiiaTextStyle.h4ExtraSmallHeading
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = data.secondaryLabel?.asString() ?: "",
                color = Black,
                style = DiiaTextStyle.t4TextSmallDescription
            )
        }

        BtnStrokeAdditionalAtm(
            data = data.buttonStrokeAdditionalAtomData,
            onUIAction = onUIAction
        )

    }
}

data class SmallButtonPanelMlcData(
    val icon: UiText?,
    val label: UiText,
    val secondaryLabel: UiText?,
    val buttonStrokeAdditionalAtomData: ButtonStrokeAdditionalAtomData
) : UIElementData


@Composable
@Preview
fun SmallButtonPanelMlcPreview() {
    val data = SmallButtonPanelMlcData(
        icon = UiText.DynamicString("⭐️"),
        label = UiText.DynamicString("Рейтинг: немає"),
        secondaryLabel = UiText.DynamicString("Усього 7 голосів"),
        buttonStrokeAdditionalAtomData = ButtonStrokeAdditionalAtomData(
            title = UiText.DynamicString("Оцінити")
        )
    )
    SmallButtonPanelMlc(data = data) {}
}