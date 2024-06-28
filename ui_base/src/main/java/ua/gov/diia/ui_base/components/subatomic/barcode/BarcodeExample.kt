package ua.gov.diia.ui_base.components.subatomic.barcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GenericBarcodeExample(
    barcodeType: BarcodeType,
    value: String,
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        if (barcodeType.isValueValid(value)) {
            BarcodeAdapterView(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .height(300.dp),
                type = barcodeType,
                value = value,
                width = 300.dp,
                height = 300.dp,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = value
            )
        }
    }
}


@Preview
@Composable
fun Ean13() {
    val BARCODE_VALUE = "123456123456"
    GenericBarcodeExample(barcodeType = BarcodeType.EAN_13, value = BARCODE_VALUE)
}

@Preview
@Composable
fun Qr() {
    val BARCODE_VALUE = "https://google.com"
    GenericBarcodeExample(barcodeType = BarcodeType.QR_CODE, value = BARCODE_VALUE)
}