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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition01
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition02
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition03
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition04
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition05
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition06

private const val RADIUS_MULTIPLIER = 3.0f

fun Modifier.diiaRadialGradientBorder() =
    composed {

        val configuration = LocalConfiguration.current

        val screenWidth = configuration.screenWidthDp.dp.value

        border(
            2.dp, brush = Brush.radialGradient(
                center = Offset(0.0f, 100.0f),
                radius = screenWidth * RADIUS_MULTIPLIER,
                colors = listOf(
                    GradientAlternativePosition01,
                    GradientAlternativePosition02,
                    GradientAlternativePosition03,
                    GradientAlternativePosition04,
                    GradientAlternativePosition05,
                    GradientAlternativePosition06,
                ),
            ), RoundedCornerShape(8.dp)
        )
    }

@Preview
@Composable
fun DiiaRadialGradientBorderPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp, 200.dp)
                .padding(30.dp)
                .diiaRadialGradientBorder()
        )
    }
}