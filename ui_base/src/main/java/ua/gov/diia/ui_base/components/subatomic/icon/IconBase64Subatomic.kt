package ua.gov.diia.ui_base.components.subatomic.icon

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.theme.Black


@Composable
fun IconBase64Subatomic(
    modifier: Modifier,
    base64Image: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center
) {
    val bitmap = remember(base64Image) {
        val byteArray = Base64.decode(base64Image, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)?.asImageBitmap()
    }
    if (bitmap != null) {
        Image(
            modifier = modifier,
            bitmap = bitmap,
            contentScale = contentScale,
            contentDescription = contentDescription,
            alignment = alignment
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.diia_check),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Black)
        )
    }

}

@Preview
@Composable
fun IconBase64SubatomicPreview_ValidBase64() {
    val base64String =
        "iVBORw0KGgoAAAANSUhEUgAAADMAAAAzCAYAAAA6oTAqAAAAEXRFWHRTb2Z0d2FyZQBwbmdjcnVzaEB1SfMAAABQSURBVGje7dSxCQBACARB+2/ab8BEeQNhFi6WSYzYLYudDQYGBgYGBgYGBgYGBgYGBgZmcvDqYGBgmhivGQYGBgYGBgYGBgYGBgYGBgbmQw+P/eMrC5UTVAAAAABJRU5ErkJggg=="

    IconBase64Subatomic(
        modifier = Modifier.size(16.dp),
        base64Image = base64String
    )
}

@Preview
@Composable
fun IconBase64SubatomicPreview_InvalidBase64() {
    val base64String =
        "iVnjkndfjjsd"

    IconBase64Subatomic(
        modifier = Modifier.size(16.dp),
        base64Image = base64String
    )
}