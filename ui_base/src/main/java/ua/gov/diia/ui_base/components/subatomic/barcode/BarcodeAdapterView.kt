package ua.gov.diia.ui_base.components.subatomic.barcode


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
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
                contentScale = ContentScale.FillBounds
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