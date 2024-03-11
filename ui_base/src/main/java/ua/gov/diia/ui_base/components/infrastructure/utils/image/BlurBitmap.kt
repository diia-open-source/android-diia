package ua.gov.diia.ui_base.components.infrastructure.utils.image

import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun LegacyBlurImage(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    blurRadius: Float
) {
    val renderScript = RenderScript.create(LocalContext.current)
    val bitmapAlloc = Allocation.createFromBitmap(renderScript, bitmap)
    ScriptIntrinsicBlur.create(renderScript, bitmapAlloc.element).apply {
        setRadius(blurRadius)
        setInput(bitmapAlloc)
        forEach(bitmapAlloc)
    }
    bitmapAlloc.copyTo(bitmap)
    renderScript.destroy()

    BlurImage(bitmap, modifier)
}

@Composable
fun BlurImage(
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun BlurBitmap(bitmap: Bitmap, width: Dp, height: Dp) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        LegacyBlurImage(
            modifier = Modifier
                .size(width = width, height = height)
                .alpha(0.05f),
            bitmap,
            5f
        )
    } else {
        BlurImage(
            bitmap,
            Modifier
                .size(width = width, height = height)
                .alpha(0.05f)
                .blur(5.dp)
                )

    }
}