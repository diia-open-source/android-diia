package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.card.IconCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.SmallNotificationMlcData

@Composable
fun SmallNotificationCarouselOrg(
    modifier: Modifier = Modifier,
    data: SmallNotificationCarouselOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    BaseSimpleCarouselInternal(
        modifier = modifier.padding(top = 16.dp),
        data = data,
        onUIAction = {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = it.data,
                    action = it.action
                )
            )
        },
        diiaResourceIconProvider = diiaResourceIconProvider,
    )
}

data class SmallNotificationCarouselOrgData(
    val actionKey: String = UIActionKeysCompose.SMALL_NOTIFICATION_CAROUSEL_ORG,
    val id: String? = null,
    override val items: List<SimpleCarouselCard>,
) : BaseSimpleCarouselInternalData

@Preview
@Composable
fun SmallNotificationCarouselOrgPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        val card = SmallNotificationMlcData(
            id = "1",
            label = "Нове опитування".toDynamicString(),
            text = "Оберіть ескіз воєнної марки № 3 від Укрпошти Оберіть ескіз воєнної марки № 3 від Укрпошти".toDynamicString()
        )
        val cards = listOf(card, card, card, card)
        val data = SmallNotificationCarouselOrgData(
            items = cards,
        )
        SmallNotificationCarouselOrg(
            data = data,
            diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        ) {
        }
    }
}

@Preview
@Composable
fun SmallNotificationCarouselOrgPreview_WithGoToAllCards() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        val contentCard = SmallNotificationMlcData(
            id = "1",
            label = "Нове опитування".toDynamicString(),
            text = "Оберіть ескіз воєнної марки № 3 від Укрпошти Оберіть ескіз воєнної марки № 3 від Укрпошти".toDynamicString()
        )
        val goToAllCard = IconCardMlcData(
            id = "123",
            icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.MENU.code),
            label = UiText.DynamicString("Label")
        )
        val cards = listOf(
            contentCard,
            contentCard,
            contentCard,
            contentCard,
            goToAllCard
        )
        val data = SmallNotificationCarouselOrgData(
            items = cards
        )
        SmallNotificationCarouselOrg(
            data = data,
            diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        ) {
        }
    }
}