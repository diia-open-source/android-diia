package ua.gov.diia.ui_base.components.atom.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticlePicAtm(
    modifier: Modifier = Modifier,
    data: ArticlePicAtmData,
    inCarousel: Boolean = false,
    clickable: Boolean = true,
    contentDescriptor: UiText? = null,
    onUIAction: (UIAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        GlideImage(
            model = data.url,
            contentDescription = contentDescriptor?.asString(),
            modifier = modifier
                .conditional(!inCarousel) {
                    padding(horizontal = 24.dp)
                        .padding(top = 24.dp)
                }
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(16.dp))
                .conditional(clickable) {
                    noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = data.id
                            )
                        )
                    }
                },
            contentScale = ContentScale.Crop
        ) {
            it.error(R.drawable.diia_article_placeholder)
                .placeholder(R.drawable.diia_article_placeholder)
                .load(data.url)
        }
    }
}

data class ArticlePicAtmData(
    val actionKey: String = UIActionKeysCompose.ARTICLE_PIC_ATM,
    val id: String,
    val url: String? = null
) : SimpleCarouselCard

@Preview
@Composable
fun ArticlePicAtmPreview() {
    val iconCorrect =
        "your_url"
    ArticlePicAtm(
        modifier = Modifier, data = ArticlePicAtmData(
            url = iconCorrect,
            id = "123"
        )
    ) {

    }
}

@Preview
@Composable
fun ArticlePicAtmPreview_Invalid() {
    val iconIncorrect = "your_url"
    ArticlePicAtm(
        modifier = Modifier, data = ArticlePicAtmData(
            url = iconIncorrect,
            id = "123"
        )
    ) {

    }
}