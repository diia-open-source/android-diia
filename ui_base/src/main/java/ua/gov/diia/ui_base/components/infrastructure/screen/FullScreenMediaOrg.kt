package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha20

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullScreenMediaOrg(
    modifier: Modifier = Modifier,
    photoLinks: List<String>?,
    videoLinks: List<String>?,
    selectedItemIndex: Int,
    connectivityState: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    val mediaItems = combineMediaItems(photoLinks, videoLinks)

    val pagerState = rememberPagerState { mediaItems.size }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                modifier = modifier.padding(vertical = 48.dp),
                pageSize = PageSize.Fill,
                pageSpacing = 16.dp,
                state = pagerState,
            ) { page ->
                when (val mediaItem = mediaItems.getOrNull(page)) {
                    is MediaItem.Photo -> {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            model = mediaItem.url,
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                        )
                    }

                    is MediaItem.Video -> {
                        FullScreenVideoPlayer(
                            url = mediaItem.url,
                            connectivityState = connectivityState
                        )
                    }

                    null -> TODO()
                }
            }

            LaunchedEffect(key1 = selectedItemIndex, block = {
                pagerState.scrollToPage(selectedItemIndex)
            })

            Icon(
                modifier = Modifier
                    .padding(top = 32.dp, end = 24.dp)
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                    .noRippleClickable {
                        onUIAction(UIAction(actionKey = UIActionKeysCompose.BUTTON_REGULAR))
                    },
                painter = painterResource(id = R.drawable.diia_close_rounded_plain),
                contentDescription = stringResource(id = R.string.close),
                tint = White
            )
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(mediaItems.size) { iteration ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(if (pagerState.currentPage == iteration) White else WhiteAlpha20)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}

sealed class MediaItem {
    data class Photo(val url: String) : MediaItem()
    data class Video(val url: String) : MediaItem()
}

fun combineMediaItems(photoLinks: List<String>?, videoLinks: List<String>?): List<MediaItem> {
    val photoItems = photoLinks
        ?.takeIf { it.isNotEmpty() }
        ?.map { MediaItem.Photo(it) } ?: emptyList()

    val videoItems = videoLinks
        ?.takeIf { it.isNotEmpty() }
        ?.map { MediaItem.Video(it) } ?: emptyList()

    return photoItems + videoItems
}