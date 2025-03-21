package ua.gov.diia.ui_base.components.organism.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.DashboardCardMlc
import ua.gov.diia.core.models.common_compose.org.card.DashboardCardTileOrg
import ua.gov.diia.core.models.common_compose.org.card.Item
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnSemiLightAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.DashboardCardMlc
import ua.gov.diia.ui_base.components.molecule.card.DashboardCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.DashboardCardMlcType
import ua.gov.diia.ui_base.components.molecule.card.DashboardCardTileOrgItem
import ua.gov.diia.ui_base.components.molecule.card.toUIModel

@Composable
fun DashboardCardTileOrg(
    modifier: Modifier = Modifier,
    data: DashboardCardTileOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        LazyVerticalStaggeredGrid(
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = StaggeredGridCells.Adaptive(159.dp),
        ) {
            items(data.items) { item ->
                when (item) {
                    is DashboardCardMlcData -> {
                        DashboardCardMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = onUIAction
                        )
                    }
                }
            }
        }
    }
}


data class DashboardCardTileOrgData(
    val actionKey: String = UIActionKeysCompose.DASHBOARD_CARD_TILE_ORG,
    val componentId: UiText? = null,
    val items: SnapshotStateList<DashboardCardTileOrgItem>
) : UIElementData

fun LazyListScope.loadTileItems(
    item: DashboardCardTileOrgData,
    screenWidth: Int,
    onUIAction: (UIAction) -> Unit = {}
) {
    if (screenWidth > 360) {
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
                        DashboardCardMlc(
                            modifier = Modifier.weight(1f),
                            data = item.items[rowIndex * 2] as DashboardCardMlcData,
                            onUIAction = onUIAction
                        )
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        if ((rowIndex + 1) * 2 - 1 < item.items.size) {
                            DashboardCardMlc(
                                modifier = Modifier.weight(1f),
                                data = item.items[(rowIndex + 1) * 2 - 1] as DashboardCardMlcData,
                                onUIAction = onUIAction
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    } else {
        items(item.items.size) { index ->
            val dashboardItem = item.items[index] as DashboardCardMlcData
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                if (index == 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                DashboardCardMlc(
                    data = dashboardItem,
                    onUIAction = onUIAction
                )
                if (index < item.items.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

fun DashboardCardTileOrg?.toUIModel(): DashboardCardTileOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val itemList: SnapshotStateList<DashboardCardTileOrgItem> = SnapshotStateList()

    itemList.apply {
        (entity.items as List<Item>).forEach { item ->
            if (item.dashboardCardMlc is DashboardCardMlc) {
                add(
                    (item.dashboardCardMlc as DashboardCardMlc).toUIModel()
                )
            }
        }
    }
    return DashboardCardTileOrgData(
        items = itemList,
        componentId = this?.componentId?.let { UiText.DynamicString(it) },
    )
}

@Preview
@Composable
fun DashboardCardTileOrgPreview() {
    val items = SnapshotStateList<DashboardCardTileOrgItem>().apply {
        add(
            DashboardCardMlcData(
                id = "id",
                label = UiText.DynamicString("Label"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                valueLarge = "0.",
                valueSmall = "00 $",
                description = UiText.DynamicString("description"),
                type = DashboardCardMlcType.GREEN
            )
        )
        add(
            DashboardCardMlcData(
                id = "id",
                label = UiText.DynamicString("Label"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                valueLarge = "0.",
                valueSmall = "00 $",
                type = DashboardCardMlcType.BLUE,
                btnSemiLightAtm = BtnSemiLightAtmData(
                    label = UiText.DynamicString("Label"),
                    id = "",
                    interactionState = UIState.Interaction.Enabled
                )
            )
        )
    }
    val data = DashboardCardTileOrgData(items = items)

    DashboardCardTileOrg(
        modifier = Modifier,
        data = data
    ) {}
}