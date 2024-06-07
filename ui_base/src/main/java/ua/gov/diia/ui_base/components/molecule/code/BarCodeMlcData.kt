package ua.gov.diia.ui_base.components.molecule.code


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.mlc.scancode.BarCodeMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.organism.document.formatNumber
import ua.gov.diia.ui_base.components.subatomic.barcode.BarcodeAdapterView
import ua.gov.diia.ui_base.components.subatomic.barcode.BarcodeType
import ua.gov.diia.ui_base.components.theme.Black

@Composable
fun BarCodeMlc(
    modifier: Modifier = Modifier,
    data: BarCodeMlcData
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        data.ean13code?.let {
            BarcodeAdapterView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5 / 2f),
                value = it.asString(),
                type = BarcodeType.EAN_13,
                width = 247.dp,
                height = 100.dp,
                blur = data.blured
            )
            Text(
                text = formatNumber(data.ean13code.asString()),
                modifier = Modifier.padding(
                    top = 16.dp
                ),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 4.sp
                ),
                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                color = Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class BarCodeMlcData(
    val actionKey: String = UIActionKeysCompose.BAR_CODE_MLC,
    val componentId: UiText? = null,
    val ean13code: UiText? = null,
    val blured: Boolean = false
)

fun BarCodeMlc.toUIModel(): BarCodeMlcData {
    return BarCodeMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        ean13code = this.barCode.toDynamicString()
    )
}

@Preview
@Composable
fun BarCodeCodeMlc_Preview_Valid() {
    val data = BarCodeMlcData(
        ean13code = UiText.DynamicString("5725556653368")
    )
    BarCodeMlc(data = data)
}

@Preview
@Composable
fun BarCodeCodeMlc_Preview_Blured() {
    val data = BarCodeMlcData(
        ean13code = UiText.DynamicString("5725556653368"),
        blured = true
    )
    BarCodeMlc(data = data)
}

