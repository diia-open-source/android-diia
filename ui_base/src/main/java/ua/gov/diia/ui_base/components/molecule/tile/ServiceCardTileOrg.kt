package ua.gov.diia.ui_base.components.molecule.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.molecule.card.ServiceCardMlc
import ua.gov.diia.ui_base.components.molecule.card.ServiceCardMlcData

@Composable
fun ServiceCardTileOrg(
    modifier: Modifier = Modifier,
    data: ServiceCardTileOrgData,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(start = 24.dp, top = 8.dp, end = 24.dp)
            .fillMaxHeight(),
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
                    onUIAction = onUIAction,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                )
                if (index == lastIndex) {
                    Spacer(modifier = Modifier.height(144.dp))
                }
            }
        }
    }
}


data class ServiceCardTileOrgData(val items: SnapshotStateList<ServiceCardMlcData>) :
    UIElementData

@Preview
@Composable
fun ServiceCardTileOrgPreview() {
    val exampleData = ServiceCardTileOrgData(
        items = SnapshotStateList<ServiceCardMlcData>().apply {
            add(
                ServiceCardMlcData(
                    id = "1",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "2",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "3",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "4",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "5",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "6",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "7",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
            add(
                ServiceCardMlcData(
                    id = "8",
                    label = "Label",
                    icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
                )
            )
        }
    )
    ServiceCardTileOrg(modifier = Modifier, data = exampleData, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {}

}