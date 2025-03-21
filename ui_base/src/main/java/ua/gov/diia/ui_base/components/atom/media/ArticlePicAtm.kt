package ua.gov.diia.ui_base.components.atom.media

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ua.gov.diia.core.models.notification.pull.message.ArticlePicAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination

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
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)) {

        var isLoading by remember { mutableStateOf(true) }

        GlideImage(
            modifier = modifier
                .conditional(!inCarousel) {
                    padding(horizontal = 24.dp)
                        .padding(top = 24.dp)
                }
                .align(Alignment.TopCenter)
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
            model = data.url,
            contentDescription = contentDescriptor?.asString(),
            contentScale = data.contentScale,
            loading = placeholder(R.drawable.diia_article_placeholder),
            failure = placeholder(R.drawable.diia_article_placeholder),
            requestBuilderTransform = { requestBuilder ->
                requestBuilder.load(data.url)
                requestBuilder.listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        isLoading = false
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        isLoading = false
                        return false
                    }
                })
            }
        )
        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                ImageCardPlaceholder()
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

data class ArticlePicAtmData(
    val actionKey: String = UIActionKeysCompose.ARTICLE_PIC_ATM,
    override val id: String,
    val url: String? = null,
    val contentScale: ContentScale = ContentScale.Fit,
    val componentId: UiText? = null
) : SimpleCarouselCard, SimplePagination

fun ArticlePicAtm?.toUiModel(): ArticlePicAtmData? {
    if (this == null) return null
    return ArticlePicAtmData(
        url = this.image,
        componentId = this.componentId.toDynamicStringOrNull(),
        id = this.componentId ?: ""
    )
}

enum class ArticlePicAtmMockType {
    valid, invalid
}

fun generateArticlePicAtmMockData(mockType: ArticlePicAtmMockType): ArticlePicAtmData {
    return when (mockType) {
        ArticlePicAtmMockType.valid -> ArticlePicAtmData(
            url = "https://deep-image.ai/blog/content/images/2022/09/underwater-magic-world-8tyxt9yz.jpeg",
            id = "123"
        )

        ArticlePicAtmMockType.invalid -> ArticlePicAtmData(
            url = "https://business.diia.gov.ua/uplosdfsdfads/4/22881-main.jpg",
            id = "123"
        )
    }
}

@Preview
@Composable
fun ArticlePicAtmPreview() {
    ArticlePicAtm(
        modifier = Modifier, data = generateArticlePicAtmMockData(ArticlePicAtmMockType.valid)
    ) {

    }
}

@Preview
@Composable
fun ArticlePicAtmPreview_Invalid() {
    ArticlePicAtm(
        modifier = Modifier, data = generateArticlePicAtmMockData(ArticlePicAtmMockType.invalid)
    ) {

    }
}