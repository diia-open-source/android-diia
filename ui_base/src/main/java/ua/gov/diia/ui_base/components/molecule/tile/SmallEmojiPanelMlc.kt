package ua.gov.diia.ui_base.components.molecule.tile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle


@Composable
fun SmallEmojiPanelMlc(
    modifier: Modifier = Modifier,
    data: SmallEmojiPanelMlcData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        UiIconWrapperSubatomic(
            modifier = Modifier.size(16.dp),
            icon = data.icon,
            diiaResourceIconProvider = diiaResourceIconProvider,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.text.asString(),
            style = DiiaTextStyle.t2TextDescription,
            color = Black
        )
    }
}


data class SmallEmojiPanelMlcData(
    val text: UiText,
    val icon: UiIcon,
) : TableBlockItem

@Preview
@Composable
fun SmallEmojiPanelMlcPreview() {
    SmallEmojiPanelMlc(
        modifier = Modifier,
        data = SmallEmojiPanelMlcData(
            text = UiText.DynamicString("Booster vaccine dose"),
            icon = UiIcon.DrawableResource(
                code = CommonDiiaResourceIcon.DEFAULT.code
            )
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    )
}