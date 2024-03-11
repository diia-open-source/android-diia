package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.VerticalCardMlc
import ua.gov.diia.ui_base.components.molecule.card.VerticalCardMlcData

@Composable
fun VerticalCardCarouselOrg(
    modifier: Modifier = Modifier,
    data: VerticalCardCarouselOrgData,
    onUIAction: (UIAction) -> Unit
) {

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 16.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = data.items) { _, item ->
            VerticalCardMlc(
                data = item, onUIAction = {
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
    }

}

data class VerticalCardCarouselOrgData(
    val actionKey: String = UIActionKeysCompose.VERTICAL_CARD_CAROUSEL_ORG,
    val id: String? = null,
    val items: List<VerticalCardMlcData>
) : UIElementData

@Preview
@Composable
fun VerticalCardCarouselOrgPreview() {
    val data = VerticalCardCarouselOrgData(
        items = mutableListOf<VerticalCardMlcData>().apply {
            repeat(5) {
                add(
                    VerticalCardMlcData(
                        id = it.toString(),
                        url = "https://business.diia.gov.ua/uploads/4/22881-main.jpg",
                        label = UiText.DynamicString(LoremIpsum(10).values.joinToString()),
                        badge = BadgeCounterAtmData(it)
                    )
                )
            }
        }
    )
    VerticalCardCarouselOrg(data = data) {

    }
}