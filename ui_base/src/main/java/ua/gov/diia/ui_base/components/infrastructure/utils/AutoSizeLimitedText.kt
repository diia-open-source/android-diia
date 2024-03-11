package ua.gov.diia.ui_base.components.infrastructure.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun AutoSizeLimitedText(
    text: String,
    style: TextStyle = DiiaTextStyle.h2MediumHeading,
    maxLines: Int,
    modifier: Modifier,
    color: Color = Black
) {
    var resizedStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defStyle = DiiaTextStyle.h2MediumHeading.fontSize

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        style = resizedStyle,
        onTextLayout = { result ->
            if (result.didOverflowHeight) {
                if (style.fontSize.isUnspecified) {
                    resizedStyle = resizedStyle.copy(
                        fontSize = defStyle
                    )
                }
                resizedStyle = resizedStyle.copy(
                    fontSize = resizedStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        }
    )
}