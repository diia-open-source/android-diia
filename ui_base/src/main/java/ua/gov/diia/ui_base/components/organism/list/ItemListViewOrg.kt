package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.list.ListItemsMlcV1
import ua.gov.diia.ui_base.components.molecule.list.ListItemsMlcV1Data
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ItemListViewOrg(
    modifier: Modifier = Modifier,
    data: ItemListViewOrgData,
    onUIAction: (UIAction) -> Unit

) {
    Column(modifier = modifier
        .padding(horizontal = 24.dp)
        .padding(top = 24.dp)
        .background(color = White, shape = RoundedCornerShape(8.dp))) {
        data.itemsList.forEachIndexed { index, item ->
            ListItemsMlcV1(data = item, onUIAction = onUIAction)
            if (index != data.itemsList.size - 1) {
                DividerSlimAtom()
            }
        }
    }
}

data class ItemListViewOrgData(val itemsList: SnapshotStateList<ListItemsMlcV1Data>): UIElementData

@Composable
@Preview
fun ItemListViewOrgPreview() {
    val state = ItemListViewOrgData(SnapshotStateList<ListItemsMlcV1Data>().apply {
        add(ListItemsMlcV1Data(title = "Label", endIconBase64String = PreviewBase64Icons.arrowNext))
        add(ListItemsMlcV1Data(title = "Label", endIconBase64String = PreviewBase64Icons.arrowNext))
        add(ListItemsMlcV1Data(title = "Label", endIconBase64String = PreviewBase64Icons.arrowNext))
        add(ListItemsMlcV1Data(title = "Label", endIconBase64String = PreviewBase64Icons.arrowNext))
        add(ListItemsMlcV1Data(title = "Label", endIconBase64String = PreviewBase64Icons.arrowNext))
        add(ListItemsMlcV1Data(title = "Label", endIconBase64String = PreviewBase64Icons.arrowNext))
    })
    ItemListViewOrg(data = state) {
    }
}