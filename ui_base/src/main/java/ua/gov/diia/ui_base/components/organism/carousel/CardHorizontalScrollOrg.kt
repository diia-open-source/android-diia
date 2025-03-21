package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.carousel.CardHorizontalScrollOrg
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.CardHorizontalScroll
import ua.gov.diia.ui_base.components.molecule.card.CardHorizontalScrollData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.list.toUiModel
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun CardHorizontalScrollOrg(
    modifier: Modifier = Modifier,
    data: CardHorizontalScrollOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val pagerState = rememberPagerState { data.items.size }

    Column(modifier = modifier) {
        androidx.compose.foundation.pager.HorizontalPager(
            modifier = modifier,
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp),
            pageSpacing = 8.dp,
            state = pagerState,
        ) { page ->
            val item = data.items.getOrNull(page)
            if (item != null) {
                CardHorizontalScroll(
                    modifier = Modifier.fillMaxWidth(),
                    data = item,
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
        }
    }
}

data class CardHorizontalScrollOrgData(
    val actionKey: String = UIActionKeysCompose.CARD_HORIZONTAL_SCROLL_ORG,
    val componentId: UiText? = null,
    val items: List<CardHorizontalScrollData>
) : UIElementData

fun CardHorizontalScrollOrg?.toUIModel(): CardHorizontalScrollOrgData? {
    val entity = this ?: return null
    val itemList: SnapshotStateList<CardHorizontalScrollData> = SnapshotStateList()
    entity.cardsGroup?.forEach { card ->
        card.items?.forEach { it ->
            it.listItemMlc?.toUiModel()
        }
    }
    return CardHorizontalScrollOrgData(
        items = itemList,
        componentId = entity.componentId?.let { UiText.DynamicString(it) },
    )
}

fun generateCardHorizontalScrollOrgMockData(size: Int): CardHorizontalScrollOrgData {
    return CardHorizontalScrollOrgData(
        items = MutableList(size) {
            CardHorizontalScrollData(
                itemsList = mutableStateListOf(
                    ListItemMlcData(
                        label = UiText.DynamicString("label label label label label label label label label label label label label label label label label label label label"),
                        description = UiText.DynamicString("label label label label label label label label label label label label label label label")
                    ),
                    ListItemMlcData(
                        label = UiText.DynamicString("Label"),
                        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple)
                    )
                )
            )
        }
    )
}

@Preview
@Composable
fun CardHorizontalScrollOrgPreview() {
    val data = generateCardHorizontalScrollOrgMockData(3)
    CardHorizontalScrollOrg(data = data) {}
}
