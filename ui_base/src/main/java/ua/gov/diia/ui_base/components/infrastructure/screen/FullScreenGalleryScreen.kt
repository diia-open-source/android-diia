package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.BlackAlpha80
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha20


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun FullScreenGalleryScreen(
    modifier: Modifier = Modifier,
    images: List<String>,
    selectedItemIndex: Int,
    onUIAction: (UIAction) -> Unit
) {
    val pagerState = rememberPagerState { images.size }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BlackAlpha80
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                modifier = modifier.padding(vertical = 48.dp),
                pageSize = PageSize.Fill,
                contentPadding = PaddingValues(16.dp),
                pageSpacing = 16.dp,
                state = pagerState,
            ) { page ->
                val url = images.getOrNull(page)
                GlideImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    model = url,
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                )
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
                repeat(images.size) { iteration ->
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

@Composable
@Preview
fun FullScreenGalleryScreenPreview() {
    val images = listOf(
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/ce1/7fb/thumb_867_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/d01/72e/thumb_868_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/e84/e25/thumb_870_730_410_0_0_auto.jpg",
        "https://go.diia.app/assets/img/pages/screen-phone.png",
    )
    FullScreenGalleryScreen(images = images, selectedItemIndex = 2) {}
}