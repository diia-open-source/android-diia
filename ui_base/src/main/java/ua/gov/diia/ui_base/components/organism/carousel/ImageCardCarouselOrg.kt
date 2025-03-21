package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.carousel.ImageCardCarouselOrg
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.toUiModel

@Composable
fun ImageCardCarouselOrg(
    modifier: Modifier = Modifier,
    data: ImageCardCarouselOrgData,
    onUIAction: (UIAction) -> Unit
) {
    BaseSimpleCarouselInternal(
        modifier = modifier
            .padding(top = 16.dp)
            .testTag(data.componentId?.asString() ?: ""),
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

data class ImageCardCarouselOrgData(
    val actionKey: String = UIActionKeysCompose.IMAGE_CARD_CAROUSEL_ORG,
    val id: String? = null,
    override val items: List<SimpleCarouselCard>,
    val componentId: UiText? = null
) : BaseSimpleCarouselInternalData

fun ImageCardCarouselOrg.toUiModel(id: String? = null): ImageCardCarouselOrgData {
    return ImageCardCarouselOrgData(
        id = id,
        componentId = componentId?.let { UiText.DynamicString(it) },
        items = mutableListOf<SimpleCarouselCard>().apply {
            items.forEach { item ->
                item.imageCardMlc?.let {
                    add(it.toUiModel())
                }
            }
        } as List<SimpleCarouselCard>
    )
}

fun generateImageCardCarouselOrgMockData(): ImageCardCarouselOrgData {
    val image = ImageCardMlcData(
        id = "",
        title = UiText.DynamicString("label"),
        image = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
    )
    val items = listOf(image, image, image, image)

    return ImageCardCarouselOrgData(
        items = items,
    )
}

@Preview
@Composable
fun ImageCardCarouselOrgPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        val data = generateImageCardCarouselOrgMockData()
        ImageCardCarouselOrg(data = data) {
        }
    }
}