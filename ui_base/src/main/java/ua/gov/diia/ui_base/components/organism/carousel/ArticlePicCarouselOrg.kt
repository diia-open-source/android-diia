package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose

@Composable
fun ArticlePicCarouselOrg(
    modifier: Modifier = Modifier,
    data: ArticlePicCarouselOrgData,
    onUIAction: (UIAction) -> Unit
) {
    BaseSimpleCarouselInternal(
        modifier = modifier.padding(top = 24.dp),
        data = data,
        onUIAction = {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = it.data,
                    action = it.action
                )
            )
        }
    )
}

data class ArticlePicCarouselOrgData(
    val actionKey: String = UIActionKeysCompose.ARTICLE_PIC_CAROUSEL_ORG,
    val id: String? = null,
    override val items: List<SimpleCarouselCard>
) : BaseSimpleCarouselInternalData

@Preview
@Composable
fun ArticlePicCarouselOrgPreview() {
    Box(
        modifier = Modifier
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        val card = ArticlePicAtmData(
            id = "123",
            url = "https://deep-image.ai/blog/content/images/2022/09/underwater-magic-world-8tyxt9yz.jpeg",
        )
        val cards = listOf(card, card, card, card, card, card, card, card)
        val data = ArticlePicCarouselOrgData(
            items = cards
        )
        ArticlePicCarouselOrg(data = data) {
        }
    }
}