package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.BlackAlpha60

@Composable
fun TridentLoaderWithUIBlocking(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean>,
) {
    if (!contentLoaded.second && (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING)) {
        Box(modifier = modifier.fillMaxSize().background(BlackAlpha60).noRippleClickable {  }, contentAlignment = Alignment.Center) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader_white))
            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

            LottieAnimation(
                modifier = Modifier
                    .size(width = 80.dp, height = 80.dp),
                alignment = Alignment.Center,
                composition = composition,
                progress = { progress },

                )
        }
    }
}

@Preview
@Composable
fun TridentLoaderWithUIBlocking_Preview() {
    TridentLoaderWithUIBlocking(contentLoaded = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING to false)
}