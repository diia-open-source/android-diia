package ua.gov.diia.ui_base.components.molecule.card


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun GalleryImageMolecule(
    modifier: Modifier = Modifier,
    data: GalleryImageMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    val pagerState = rememberPagerState { data.images.size }
    HorizontalPager(
            state = pagerState,
            modifier = modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            pageSize = PageSize.Fixed(93.dp),
            pageSpacing = 8.dp,
        ) { page ->
            val url = data.images.getOrNull(page)
            GlideImage(
                modifier = Modifier
                    .size(93.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onUIAction.invoke(UIAction(data.actionKey, data = page.toString())) },
                model = url,
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
    }
}

data class GalleryImageMoleculeData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_CLICK,
    val id: String? = "",
    val images: List<String>
) : TableBlockItem

@Composable
@Preview
fun GalleryImageMoleculePreview() {
    val images = listOf(
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/ce1/7fb/thumb_867_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/d01/72e/thumb_868_730_410_0_0_auto.jpg",
        "https://diia.gov.ua/img/diia-october-prod/uploads/public/652/e84/e25/thumb_870_730_410_0_0_auto.jpg",
        "https://go.diia.app/assets/img/pages/screen-phone.png",
    )
    val data = GalleryImageMoleculeData(
        id = "id",
        images = images
    )
    GalleryImageMolecule(data = data) {}
}