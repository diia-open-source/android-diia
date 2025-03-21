package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.BlackAlpha70


@Composable
fun BarcodeScannerCardAtom(
    modifier: Modifier = Modifier,
    framePosition: (Rect) -> Unit,
) {
    val cornerSize = with(LocalDensity.current) { 16.dp.toPx() }
    val scannerPadding = with(LocalDensity.current) { 48.dp.toPx() }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val frameSize = with(LocalDensity.current) { (screenWidth - 96.dp).toPx() } // Adjust frame size

    val scannerStrokeWidth = with(LocalDensity.current) { 2.dp.toPx() }

    androidx.compose.foundation.Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
            .onGloballyPositioned {
                framePosition.invoke(
                    Rect(
                        left = scannerPadding,
                        top = it.size.height / 2f - frameSize / 2,
                        right = it.size.width - scannerPadding,
                        bottom = it.size.height / 2f + frameSize / 2
                    )
                )
            },
        onDraw = {
            drawRect(
                color = BlackAlpha70,
                size = size
            )
            drawRoundRect(
                color = Color.Transparent,
                size = Size(size.width - scannerPadding * 2, frameSize),
                topLeft = Offset(scannerPadding, size.height / 2f - frameSize / 2),
                blendMode = BlendMode.SrcOut,
            )

            val frameTopLeft = Offset(scannerPadding, size.height / 2f - frameSize / 2)

            drawRect(
                color = Color.White,
                topLeft = frameTopLeft,
                size = Size(cornerSize, scannerStrokeWidth),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = frameTopLeft,
                size = Size(scannerStrokeWidth, cornerSize),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(frameTopLeft.x + frameSize - cornerSize, frameTopLeft.y),
                size = Size(cornerSize, scannerStrokeWidth),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(frameTopLeft.x + frameSize - scannerStrokeWidth, frameTopLeft.y),
                size = Size(scannerStrokeWidth, cornerSize),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(frameTopLeft.x, frameTopLeft.y + frameSize - scannerStrokeWidth),
                size = Size(cornerSize, scannerStrokeWidth),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(frameTopLeft.x, frameTopLeft.y + frameSize - cornerSize),
                size = Size(scannerStrokeWidth, cornerSize),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(frameTopLeft.x + frameSize - cornerSize, frameTopLeft.y + frameSize - scannerStrokeWidth),
                size = Size(cornerSize, scannerStrokeWidth),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(frameTopLeft.x + frameSize - scannerStrokeWidth, frameTopLeft.y + frameSize - cornerSize),
                size = Size(scannerStrokeWidth, cornerSize),
                style = Stroke(width = scannerStrokeWidth)
            )
        }
    )
}

@Preview
@Composable
fun PreviewBarcodeScannerCardAtom() {
    BarcodeScannerCardAtom{}
}
