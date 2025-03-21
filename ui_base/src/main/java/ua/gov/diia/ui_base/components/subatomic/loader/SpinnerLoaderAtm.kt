package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination

@Composable
fun LoaderSpinnerLoaderAtm(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = ""
    )
    Column {
        Image(
            modifier = modifier.padding(top=16.dp, bottom = 16.dp).fillMaxWidth()
                .size(24.dp)
                .rotate(angle),
            painter = painterResource(id = R.drawable.pagination_circular_progress_loader),
            contentDescription = null
        )
    }
}

data class LoaderSpinnerLoaderAtmData(override val id: String) : SimplePagination

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
    Column {
    Image(
        modifier = modifier
            .size(18.dp)
            .rotate(angle),
        painter = painterResource(id = R.drawable.pagination_circular_progress_loader),
        contentDescription = null
    )}
}
