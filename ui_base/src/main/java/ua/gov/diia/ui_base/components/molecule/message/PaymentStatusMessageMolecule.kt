package ua.gov.diia.ui_base.components.molecule.message


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun PaymentStatusMessageMolecule(
    modifier: Modifier = Modifier, data: PaymentStatusMessageMoleculeData
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
    ) {
        data.icon?.let {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp, end = 8.dp, top = 16.dp
                )
            ) {
                Text(
                    text = data.icon, style = DiiaTextStyle.h4ExtraSmallHeading
                )
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
            .conditional(data.icon == null) {
                padding(start = 16.dp)
            }) {
            Text(
                text = data.title, style = DiiaTextStyle.t1BigText
            )
            Text(
                modifier = Modifier.padding(top = 8.dp), text = data.subtitle, style = DiiaTextStyle.h1Heading
            )
            data.hiddenTextLabel?.let {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = data.hiddenTextLabel,
                    style = DiiaTextStyle.t3TextBody,
                    color = BlackAlpha30
                )
            }
            data.hiddenTextValue?.let {
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = data.hiddenTextValue,
                    style = DiiaTextStyle.h4ExtraSmallHeading,
                    color = BlackAlpha30
                )
            }
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = if (data.hiddenTextValue == null) 8.dp else 16.dp),
                    text = data.description,
                    style = DiiaTextStyle.t3TextBody
                )
            }
        }
    }
}

data class PaymentStatusMessageMoleculeData(
    val icon: String? = null,
    val title: String,
    val subtitle: String,
    val hiddenTextLabel: String? = null,
    val hiddenTextValue: String? = null,
    val description: String? = null
) : UIElementData

@Composable
@Preview
fun PaymentStatusMessageMoleculePreview() {
    val data = PaymentStatusMessageMoleculeData(
        icon = "⏳",
        title = "Чекає на зарахування",
        subtitle = "3 537.00 грн",
        hiddenTextLabel = "Сплачена сума ",
        hiddenTextValue = "9070.92 грн",
        description = "Виконавець може стягувати додаткові кошти для компенсації витрат на ведення виконавчого провадження навіть після сплати боржником повної суми свого боргу, за виконавчим провадженням згідно чинного законодавства."
    )

    PaymentStatusMessageMolecule(
        modifier = Modifier.padding(16.dp), data = data
    )
}

@Composable
@Preview
fun PaymentStatusMessageMoleculePreview_WithoutIcon() {
    val data = PaymentStatusMessageMoleculeData(
        title = "Чекає на зарахування", subtitle = "3 537.00 грн"
    )

    PaymentStatusMessageMolecule(
        modifier = Modifier.padding(16.dp), data = data
    )
}

@Composable
@Preview
fun PaymentStatusMessageMoleculePreview_WithoutHiddenText() {
    val data = PaymentStatusMessageMoleculeData(
        icon = "⏳", title = "Чекає на зарахування", subtitle = "-3 537.00 грн", description = "Перевірте суму у виконавця"
    )

    PaymentStatusMessageMolecule(
        modifier = Modifier.padding(16.dp), data = data
    )
}
