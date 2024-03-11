package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun PaymentInfoOrg(
    modifier: Modifier = Modifier,
    data: PaymentInfoOrgData
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 8.dp)
            .fillMaxWidth()
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
    ) {
        data.items.forEachIndexed { index, item ->
            if (index < data.items.size - 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f, false)
                            .padding(end = 8.dp),
                        text = item.first,
                        style = DiiaTextStyle.t2TextDescription
                    )
                    Text(
                        text = item.second,
                        style = DiiaTextStyle.t2TextDescription
                    )
                }
            } else {
                if (data.items.size > 1) {
                    DividerSlimAtom(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 11.dp),
                        color = BlackSqueeze
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                            top = if (data.items.size > 1) {
                                11.dp
                            } else {
                                16.dp
                            }
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f, false)
                            .padding(end = 8.dp),
                        text = item.first,
                        style = DiiaTextStyle.t1BigText
                    )
                    Text(
                        text = item.second,
                        style = DiiaTextStyle.t1BigText
                    )
                }
            }


        }


    }
}

data class PaymentInfoOrgData(
    val items: List<Pair<String, String>>
) : UIElementData

@Composable
@Preview
fun PaymentInfoOrgPreview() {
    val data = PaymentInfoOrgData(
        items = listOf(
            "Cума до сплати:" to "1 000.00 грн",
            "Комісія:" to "1 000.00 грн",
            "Загалом:" to "1 000.00 грн"
        )
    )
    PaymentInfoOrg(
        data = data
    )
}

@Composable
@Preview
fun PaymentInfoOrgPreview_WithoutCommission() {
    val data = PaymentInfoOrgData(
        items = listOf(
            "Cума до сплати:" to "1 000.00 грн",
            "Загалом:" to "1 000.00 грн"
        )
    )
    PaymentInfoOrg(
        data = data
    )
}

@Composable
@Preview
fun PaymentInfoOrgPreview_OnlyTotal() {
    val data = PaymentInfoOrgData(
        items = listOf(
            "Загалом:" to "1 000.00 грн"
        )
    )
    PaymentInfoOrg(
        data = data
    )
}
