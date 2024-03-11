package ua.gov.diia.ui_base.components.subatomic.border

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition1
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition2
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition3
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition4
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

fun Modifier.diiaLinearGradientBorder() =
    composed {
        border(
            2.dp, brush = Brush.linearGradient(
                colors = listOf(
                    GradientAlternativePosition1,
                    GradientAlternativePosition2,
                    GradientAlternativePosition3,
                    GradientAlternativePosition4,
                    PeriwinkleGray,
                )
            ), RoundedCornerShape(8.dp)
        )
    }

@Preview
@Composable
fun DiiaLinearGradientBorderPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp, 200.dp)
                .padding(30.dp)
                .diiaLinearGradientBorder()
        )
    }
}