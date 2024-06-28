package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.CodGray
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCardMlc(
    modifier: Modifier = Modifier,
    data: ImageCardMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            .height(200.dp)
            .background(color = Transparent, shape = RoundedCornerShape(16.dp))
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
    ) {
        GlideSubcomposition(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(CodGray),
            model = data.image
        ) {
            when (state) {
                RequestState.Failure -> {
                    ImageCardPlaceholder()
                }

                RequestState.Loading -> {
                    ImageCardPlaceholder()
                }

                is RequestState.Success -> Image(
                    painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = BlackAlpha50,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.title.asString(),
                    style = DiiaTextStyle.t1BigText,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                data.iconEnd?.let {
                    UiIconWrapperSubatomic(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp), icon = it
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageCardPlaceholder() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_viewholder_dots_white))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(width = 60.dp, height = 60.dp)
                .alpha(0.2f),
            alignment = Alignment.Center,
            contentScale = ContentScale.Inside,
            composition = composition,
            progress = { progress },
        )
    }
}

data class ImageCardMlcData(
    val actionKey: String = UIActionKeysCompose.IMAGE_CARD_MLC,
    val id: String? = "",
    val title: UiText,
    val iconEnd: UiIcon? = null,
    val image: String,
    val contentDescription: UiText? = null,
    val action: DataActionWrapper? = null
) : UIElementData

@Composable
@Preview
fun ImageCardMlcPreview() {
    val data = ImageCardMlcData(
        title = UiText.DynamicString("label"),
        image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
    )
    ImageCardMlc(
        modifier = Modifier,
        data = data
    ) {}
}

@Composable
@Preview
fun ImageCardMlcPreview_WithIcon() {
    val data = ImageCardMlcData(
        title = UiText.DynamicString("label label label label label label label"),
        image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
        iconEnd = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code),
    )
    ImageCardMlc(
        modifier = Modifier,
        data = data
    ) {}
}