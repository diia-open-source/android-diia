package ua.gov.diia.ui_base.components.molecule.media

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ua.gov.diia.core.models.common_compose.mlc.media.ArticleVideoMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.media.player.ExoPlayerPortraitComposeView
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.FullScreenVideoOrgData
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.toUIModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleVideoMlc(
    modifier: Modifier = Modifier,
    data: ArticleVideoMlcData,
    inCarousel: Boolean = false,
    clickable: Boolean = true,
    connectivityState: Boolean,
    onUIAction: (UIAction) -> Unit
) {
    if (data.fullScreenVideoOrg != null) {
        var isLoading by remember { mutableStateOf(true) }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .testTag(data.componentId?.asString() ?: "")
                .noRippleClickable { onUIAction(UIAction(actionKey = data.actionKey)) }
                .conditional(!inCarousel) {
                    padding(horizontal = 24.dp).padding(top = 24.dp)
                },
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                model = data.thumbnail.orEmpty(),
                contentDescription = "",
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds,
                loading = placeholder(R.drawable.diia_article_placeholder),
                failure = placeholder(R.drawable.diia_article_placeholder),
                requestBuilderTransform = { requestBuilder ->
                    requestBuilder.load(data.thumbnail.orEmpty())
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
                FullScreenLoadingMolecule()
            }

            Image(
                modifier = modifier.size(64.dp),
                painter = painterResource(id = R.drawable.ic_player_btn_atm_play),
                contentDescription = null
            )
        }
    } else {
        ExoPlayerPortraitComposeView(
            modifier = modifier.testTag(data.componentId?.asString() ?: ""),
            url = data.url,
            inCarousel = inCarousel,
            clickable = clickable,
            connectivityState = connectivityState
        )
    }
}


data class ArticleVideoMlcData(
    val actionKey: String = UIActionKeysCompose.ARTICLE_VIDEO_MLC,
    val url: String? = null,
    val thumbnail: String? = null,
    val componentId: UiText? = null,
    val fullScreenVideoOrg: FullScreenVideoOrgData? = null
) : SimpleCarouselCard

fun ArticleVideoMlc.toUiModel(): ArticleVideoMlcData {
    return ArticleVideoMlcData(
        url = source,
        thumbnail = thumbnail,
        componentId = this.componentId.toDynamicStringOrNull(),
        fullScreenVideoOrg = this.fullScreenVideoOrg?.toUIModel()
    )
}

fun generateArticleVideoMlcMockData(): ArticleVideoMlcData {
    return ArticleVideoMlcData(url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4")
}

fun generateArticleVideoMlcFullScreenMockData(): ArticleVideoMlcData {
    return ArticleVideoMlcData(
        url = null,
        thumbnail = "https://picsum.photos/id/1/400/200",
        fullScreenVideoOrg = FullScreenVideoOrgData(
            source = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
        )
    )
}

@Preview
@Composable
fun ArticleVideoAtmPreview() {
    val state = remember { mutableStateOf(true) }
    ArticleVideoMlc(
        modifier = Modifier,
        data = generateArticleVideoMlcMockData(),
        connectivityState = state.value
    ) {}
}


@Preview
@Composable
fun ArticleVideoAtmPreview_full_screen() {
    val state = remember { mutableStateOf(true) }
    ArticleVideoMlc(
        modifier = Modifier,
        data = generateArticleVideoMlcFullScreenMockData(),
        connectivityState = state.value
    ) {}
}