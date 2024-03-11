package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.components.atom.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha15

@Composable
fun DocErrorOrg(
    modifier: Modifier = Modifier,
    data: DocErrorOrgData
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.7F)
    ) {
        Box(
            modifier = modifier
                .background(color = WhiteAlpha15, shape = RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(122.dp))
                Text(
                    modifier = modifier.padding(start = 40.dp, end = 40.dp).fillMaxWidth(),
                    text = "\uD83D\uDEAB",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 36.sp,
                        lineHeight = 40.sp
                    )
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp, start = 40.dp, end = 40.dp).fillMaxWidth(),
                    text = data.title.asString(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
                Spacer(modifier = Modifier.weight(1f))
                TickerAtm(data = data.ticker, onUIAction = {})
                Spacer(modifier = Modifier.height(40.dp))

            }
        }
    }
}

data class DocErrorOrgData(
    val id: String? = "",
    val title: UiText,
    val ticker: TickerAtomData,
    val localization: Localization = Localization.ua,
) : UIElementData

@Preview
@Composable
fun DocErrorOrgPreview() {
    val tickerAtomData = TickerAtomData(
        title = "Документа з таким\u2028QR-кодом не існує ",
        type = TickerType.SMALL_NEGATIVE
    )
    val data = DocErrorOrgData(
        title = UiText.DynamicString("Документ не знайдено • Документ не знайденокумент дійсний"),
        ticker = tickerAtomData
    )
    DocErrorOrg(modifier = Modifier, data = data)
}
