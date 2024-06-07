package ua.gov.diia.ui_base.components.molecule.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.card.ServiceCardTileOrg
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.ServiceCardMlc
import ua.gov.diia.ui_base.components.molecule.card.ServiceCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.toUIModel

@Composable
fun ServiceCardTileOrg(
    modifier: Modifier = Modifier,
    data: ServiceCardTileOrgData,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onUIAction: (UIAction) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(start = 24.dp, top = 8.dp, end = 24.dp)
            .fillMaxHeight()
            .testTag(data.componentId?.asString() ?: ""),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyGridState
    ) {
        items(data.items.size) { index ->
            val item = data.items[index]
            val lastIndex = data.items.size - 1
            item.let {
                ServiceCardMlc(
                    data = item,
                    onUIAction = onUIAction
                )
                if (index == lastIndex) {
                    Spacer(modifier = Modifier.height(144.dp))
                }
            }
        }
    }
}


data class ServiceCardTileOrgData(
    val items: SnapshotStateList<ServiceCardMlcData>,
    val componentId: UiText? = null
) :
    UIElementData

fun LazyListScope.loadItems(
    item: ServiceCardTileOrgData,
    onUIAction: (UIAction) -> Unit = {}
) {
    val rowsCount: Int = if (item.items.size % 2 > 0) {
        item.items.size / 2 + 1
    } else {
        item.items.size / 2
    }
    if (rowsCount > 0) {
        repeat(rowsCount) { rowIndex ->
            item(contentType = item.javaClass.canonicalName) {
                Row(
                    modifier = Modifier.padding(
                        start = 24.dp, top = if (rowIndex == 0) 16.dp else 8.dp, end = 24.dp
                    )
                ) {
                    ServiceCardMlc(
                        modifier = Modifier.weight(1f),
                        data = item.items[rowIndex * 2],
                        onUIAction = onUIAction
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    if ((rowIndex + 1) * 2 - 1 < item.items.size) {
                        ServiceCardMlc(
                            modifier = Modifier.weight(1f),
                            data = item.items[(rowIndex + 1) * 2 - 1],
                            onUIAction = onUIAction
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

fun ServiceCardTileOrg?.toUIModel(): ServiceCardTileOrgData? {
    if (this == null) return null
    val itemList: SnapshotStateList<ServiceCardMlcData> = SnapshotStateList()
    items.forEach { item ->
        itemList.add(item.toUIModel())
    }
    return ServiceCardTileOrgData(itemList, UiText.DynamicString(this.componentId.orEmpty()))
}

@Preview
@Composable
fun ServiceCardTileOrgPreview() {
    val exampleData = ServiceCardTileOrgData(
        items = SnapshotStateList<ServiceCardMlcData>().apply {
            add(
                ServiceCardMlcData(
                    id = "1",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "2",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "3",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "4",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "5",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "6",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "7",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
            add(
                ServiceCardMlcData(
                    id = "8",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = "certificates")
                )
            )
        }
    )
    ServiceCardTileOrg(modifier = Modifier, data = exampleData) {}

}