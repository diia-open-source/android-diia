package ua.gov.diia.splash.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    dataState: State<SplashScreenData>,
    onEvent: (UIAction) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_bg))
    val progress by animateLottieCompositionAsState(composition)
    val data = dataState.value
    Box(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Text(
                modifier = modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                text = data.greetingText.asString(),
                style = DiiaTextStyle.greetingText
            )

            LottieAnimation(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 64.dp, start = 12.dp, end = 12.dp)
                    .height(62.dp),
                composition = composition,
                progress = { progress }
            )
            if (progress >= 1.0) {
                onEvent(UIAction(actionKey = data.animationActionKey))
            }
        }
    }
}

data class SplashScreenData(
    val greetingText: UiText,
    val animationActionKey: String
)

@Composable
@Preview
fun SplashScreenPreview() {
    val uiData = remember {
        mutableStateOf(
            SplashScreenData(
                UiText.DynamicString("Привіт \uD83D\uDC4B"),
                ""
            )
        )
    }
    SplashScreen(dataState = uiData, onEvent = { })
}