package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePaginationCard
import ua.gov.diia.ui_base.components.theme.Alabaster
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.White


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HalvedCardMlc(
    modifier: Modifier = Modifier,
    data: HalvedCardMlcData,
    clickable: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .height(192.dp)
            .background(color = Transparent, shape = RoundedCornerShape(16.dp))
            .conditional(clickable) {
                clickable {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action
                        )
                    )
                }
            }
    ) {
        data.imageURL?.let {
            GlideSubcomposition(
                modifier = Modifier
                    .height(112.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Alabaster),
                model = data.imageURL
            ) {
                when (state) {
                    RequestState.Failure -> {
                        HalvedCardPlaceholder()
                    }

                    RequestState.Loading -> {
                        HalvedCardPlaceholder()
                    }

                    is RequestState.Success -> Image(
                        painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = White,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )

        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = data.label.asString(),
                maxLines = 1,
                minLines = 1,
                style = DiiaTextStyle.t4TextSmallDescription,
                color = BlackAlpha30
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                text = data.title.asString(),
                maxLines = 2,
                minLines = 2,
                style = DiiaTextStyle.t1BigText,
                color = Black
            )

        }
    }
}

@Composable
private fun HalvedCardPlaceholder() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.card_viewholder_dots_black))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .height(112.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
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

data class HalvedCardMlcData(
    val actionKey: String = UIActionKeysCompose.HALVED_CARD_MLC,
    override val id: String = "",
    val imageURL: String? = null,
    val label: UiText,
    val title: UiText,
    val action: DataActionWrapper? = null
) : SimpleCarouselCard, SimplePaginationCard

@Composable
@Preview
fun HalvedCardMlcPreview() {
    val state = HalvedCardMlcData(
        title = "єВідновлення: подавайте заяву про ремонт пошкодженого житла".toDynamicString(),
        imageURL = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
        label = "Сьогодні, 16:30".toDynamicString()
    )
    HalvedCardMlc(
        data = state
    ) {}
}