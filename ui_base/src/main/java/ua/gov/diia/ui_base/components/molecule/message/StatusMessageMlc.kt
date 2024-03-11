package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50

@Composable
fun StatusMessageMlc(
    modifier: Modifier = Modifier,
    data: StatusMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(color = WhiteAlpha50, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.wrapContentWidth()) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = data.icon ?: "☝",
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.title,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                data.description?.let{
                    TextWithParametersAtom(
                        data = data.description,
                        style = DiiaTextStyle.t3TextBody,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}


data class StatusMessageMlcData(val icon: String? = null, val title: String, val description: TextWithParametersData?): UIElementData

@Composable
@Preview
fun StatusMessageMlcPreview() {
    val state = StatusMessageMlcData(
        icon = "☝",
        title = "Завершіть заповнення заяви",
        description = TextWithParametersData(
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            parameters = emptyList()
        )
    )

    StatusMessageMlc(
        data = state
    ) { annotationContent ->
    }

}