package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.ui_base.R

@Composable
fun TridentLoaderAtm(modifier: Modifier = Modifier) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    LottieAnimation(
        modifier = Modifier
            .size(width = 80.dp, height = 80.dp),
        alignment = Alignment.Center,
        composition = composition,
        progress = { progress },

    )
}

@Preview
@Composable
fun TridentLoaderAtmPreview(){
    TridentLoaderAtm(modifier = Modifier)
}