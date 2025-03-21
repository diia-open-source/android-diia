package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.SpacerAtmType
import ua.gov.diia.core.models.common_compose.mlc.card.AlertCardMlc
import ua.gov.diia.ui_base.components.atom.button.BtnAlertAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.BtnAlertAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun AlertCardMlc(
    modifier: Modifier = Modifier,
    data: AlertCardMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            text = data.iconText.asString(),
            color = Black,
            style = DiiaTextStyle.h1Heading
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            text = data.label.asString(),
            color = Black,
            style = DiiaTextStyle.h4ExtraSmallHeading,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            text = data.text.asString(),
            color = Black,
            style = DiiaTextStyle.t3TextBody,
            textAlign = TextAlign.Center
        )

        data.alertBtn?.let {
            BtnAlertAdditionalAtm(
                modifier = modifier.padding(top = 16.dp, bottom = 8.dp),
                data = it,
                onUIAction = onUIAction
            )
        }
        if (data.alertBtn == null) {
            SpacerAtm(data = SpacerAtmData(type = SpacerAtmType.SPACER_8))
        }
    }
}

data class AlertCardMlcData(
    val componentId: UiText? = null,
    val iconText: UiText,
    val label: UiText,
    val text: UiText,
    val alertBtn: BtnAlertAdditionalAtmData?
) : UIElementData

fun AlertCardMlc.toUiModel(): AlertCardMlcData {
    return AlertCardMlcData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        iconText = this.icon.let { UiText.DynamicString(it) },
        label = this.label.let { UiText.DynamicString(it) },
        text = this.text.let { UiText.DynamicString(it) },
        alertBtn = this.btnAlertAdditionalAtm?.toUIModel()
    )
}

enum class AlertCardMlcMockType {
    button, withoutbutton;
}

fun generateAlertCardMlcMockData(mockType: AlertCardMlcMockType): AlertCardMlcData {
    return when (mockType) {
        AlertCardMlcMockType.button -> AlertCardMlcData(
            iconText = UiText.DynamicString("⚠️"),
            label = UiText.DynamicString("Пункт зачинено в робочі години?"),
            text = UiText.DynamicString("Дайте нам знати. Передамо інформацію місцевій владі."),
            alertBtn = BtnAlertAdditionalAtmData(title = UiText.DynamicString("Сповістити"))
        )

        AlertCardMlcMockType.withoutbutton -> AlertCardMlcData(
            iconText = UiText.DynamicString("⚠️"),
            label = UiText.DynamicString("Пункт зачинено в робочі години?"),
            text = UiText.DynamicString("Дайте нам знати. Передамо інформацію місцевій владі."),
            alertBtn = null
        )
    }
}

@Composable
@Preview
fun AlertCardMlcPreview() {
    AlertCardMlc(
        data = generateAlertCardMlcMockData(AlertCardMlcMockType.button)
    ) {}
}

@Composable
@Preview
fun AlertCardMlcPreview_WithoutBtn() {
    AlertCardMlc(
        data = generateAlertCardMlcMockData(AlertCardMlcMockType.withoutbutton)
    ) {}
}