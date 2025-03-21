package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CardHorizontalScroll(
    modifier: Modifier = Modifier,
    data: CardHorizontalScrollData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(173.dp)
            .background(color = White, shape = RoundedCornerShape(16.dp))
    ) {
        data.itemsList.forEachIndexed { index, item ->
            ListItemMlc(data = item, onUIAction = onUIAction)
            if (index != data.itemsList.size - 1) {
                Spacer(modifier = Modifier.weight(1f))
                DividerSlimAtom()
            }
        }
    }
}

data class CardHorizontalScrollData(val itemsList: SnapshotStateList<ListItemMlcData>)

@Composable
@Preview
fun CardHorizontalScrollPreview_LongText() {
    val state = CardHorizontalScrollData(SnapshotStateList<ListItemMlcData>().apply {
        add(
            ListItemMlcData(
                label = UiText.DynamicString("label label label label label label label label label label label label label label label label label label label label"),
                description = UiText.DynamicString("label label label label label label label label label label label label label label label"),
            )
        )
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
            )
        )
    })
    CardHorizontalScroll(modifier = Modifier.padding(horizontal = 16.dp), data = state) {
    }
}

@Composable
@Preview
fun CardHorizontalScrollPreview_ShortText() {
    val state = CardHorizontalScrollData(SnapshotStateList<ListItemMlcData>().apply {
        add(
            ListItemMlcData(
                label = UiText.DynamicString("label label l labe label label label"),
                description = UiText.DynamicString("label label label label label label label"),
            )
        )
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
            )
        )
    })
    CardHorizontalScroll(modifier = Modifier.padding(horizontal = 16.dp), data = state) {
    }
}