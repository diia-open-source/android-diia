package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.BlackAlpha70


@Composable
fun MrzScannerCvAtom(
    modifier: Modifier = Modifier,
    framePosition: (Rect) -> Unit,
) {
    val scannerHeight = with(LocalDensity.current) { 72.dp.toPx() }
    val scannerPadding = with(LocalDensity.current) { 24.dp.toPx() }
    val scannerCornerRadius = with(LocalDensity.current) { 16.dp.toPx() }
    val scannerStrokeWidth = with(LocalDensity.current) { 2.dp.toPx() }
    androidx.compose.foundation.Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
            .onGloballyPositioned {
                framePosition.invoke(
                    Rect(
                        left = scannerPadding,
                        top = it.size.height / 2f - scannerHeight / 2,
                        right = it.size.width - scannerPadding,
                        bottom = it.size.height / 2f + scannerHeight / 2
                    )
                )
            },
        onDraw = {
            drawRect(
                color = BlackAlpha70,
                size = size
            )

            drawRoundRect(
                color = Color.White,
                size = Size(size.width - scannerPadding * 2, scannerHeight),
                topLeft = Offset(scannerPadding, size.height / 2f - scannerHeight / 2),
                cornerRadius = CornerRadius(scannerCornerRadius),
                style = Stroke(width = scannerStrokeWidth)
            )

            drawRoundRect(
                color = Color.Transparent,
                size = Size(size.width - scannerPadding * 2, scannerHeight),
                topLeft = Offset(scannerPadding, size.height / 2f - scannerHeight / 2),
                blendMode = BlendMode.SrcOut,
                cornerRadius = CornerRadius(scannerCornerRadius),
            )
        })
}

@Preview
@Composable
fun MrzScannerCvAtomPreview() {
    MrzScannerCvAtom {}
}