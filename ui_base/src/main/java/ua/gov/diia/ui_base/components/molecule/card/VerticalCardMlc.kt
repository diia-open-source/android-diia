package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtm
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VerticalCardMlc(
    modifier: Modifier = Modifier,
    data: VerticalCardMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(modifier = modifier
        .size(height = 290.dp, width = 223.dp)
        .clickable {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = data.id,
                    action = data.action
                )
            )
        }) {
        GlideSubcomposition(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(WhiteAlpha40),
            model = data.url
        ) {
            when (state) {
                RequestState.Failure -> {
                    VerticalCardPlaceholder()
                }

                RequestState.Loading -> {
                    VerticalCardPlaceholder()
                }

                is RequestState.Success -> Image(
                    painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .weight(1f),
                text = data.label.asString(),
                style = DiiaTextStyle.h4ExtraSmallHeading,
                color = Black
            )
            data.badge?.let {
                BadgeCounterAtm(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(20.dp)
                        .defaultMinSize(minWidth = 20.dp),
                    data = data.badge
                )
            }
        }
    }
}

@Composable
private fun VerticalCardPlaceholder() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_viewholder_dots_black))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieAnimation(
            modifier = Modifier
                .size(width = 60.dp, height = 60.dp)
                .alpha(0.2f),
            alignment = Alignment.Center,
            composition = composition,
            progress = { progress },
        )
    }
}

data class VerticalCardMlcData(
    val actionKey: String = UIActionKeysCompose.VERTICAL_CARD_MLC,
    val id: String,
    val url: String,
    val contentDescription: UiText? = null,
    val label: UiText,
    val badge: BadgeCounterAtmData,
    val action: DataActionWrapper? = null
)

@Preview
@Composable
fun VerticalCardMlcPreview() {
    val data = VerticalCardMlcData(
        id = "0",
        url = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
        label = UiText.DynamicString(LoremIpsum(10).values.joinToString()),
        badge = BadgeCounterAtmData(1)
    )

    VerticalCardMlc(data = data) {}
}