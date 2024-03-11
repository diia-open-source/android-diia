package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R

@Composable
fun LoaderCircularEclipse23Subatomic(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = ""
    )
    Image(
        modifier = modifier
            .size(18.dp)
            .rotate(angle),
        painter = painterResource(id = R.drawable.diia_circular_progress_vector),
        contentDescription = null
    )
}

@Preview
@Composable
private fun ShowLoaderCircular(modifier: Modifier = Modifier){
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = ""
    )
    Image(
        modifier = modifier
            .size(18.dp)
            .rotate(angle),
        painter = painterResource(id = R.drawable.diia_circular_progress_vector),
        contentDescription = null
    )
}
