package ua.gov.diia.ui_base.components.subatomic.barcode


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.gov.diia.ui_base.components.infrastructure.utils.image.BlurBitmap
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm

@Composable
fun BarcodeAdapterView(
    modifier: Modifier = Modifier,
    showProgress: Boolean = true,
    width: Dp,
    height: Dp,
    type: BarcodeType,
    value: String,
    blur: Boolean = false
) {
    val barcodeBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()
    val localDensity = LocalDensity.current

    LaunchedEffect(value) {
        scope.launch {
            withContext(Dispatchers.Default) {
                barcodeBitmap.value = try {
                    type.getImageBitmap(
                        width = with(localDensity) { width.roundToPx() },
                        height = with(localDensity) { height.roundToPx() },
                        value = value
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
    barcodeBitmap.value?.let { barcode ->
        if (blur) {
            val originalBitmap =
                barcode.asAndroidBitmap().copy(barcode.asAndroidBitmap().config, true)
            BlurBitmap(
                modifier = modifier,
                originalBitmap,
                width,
                height
            )
        } else {
            Image(
                modifier = modifier,
                painter = BitmapPainter(barcode),
                contentDescription = value,
                contentScale = ContentScale.FillWidth
            )
        }
    } ?: run {
        Box(
            modifier = modifier
                .size(width = width, height = height),
            contentAlignment = Alignment.Center
        ) {
            if (showProgress) {
                TridentLoaderAtm(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview
@Composable
fun BarcodeAdapterView_Preview() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Green)) {
        BarcodeAdapterView(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            width = 250.dp,
            height = 250.dp,
            type = BarcodeType.QR_CODE,
            value = "https://www.diia.gov.ua"
        )
    }
}