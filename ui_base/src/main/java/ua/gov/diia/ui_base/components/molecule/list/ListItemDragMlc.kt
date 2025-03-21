package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40

@Composable
fun ListItemDragMlc(
    modifier: Modifier = Modifier,
    data: ListItemDragMlcData,
    dragging: Boolean,
    state: ReorderableLazyListState = rememberReorderableLazyListState(
        onMove = { _, _ -> },
        canDragOver = { _, _ -> true }
    ),
    onUIAction: (UIAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            .background(
                color = if (dragging) White else WhiteAlpha40,
                shape = RoundedCornerShape(24.dp)
            )
            .zIndex(if (dragging) 1f else 0f)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = modifier.fillMaxWidth(0.85f)) {
                Text(
                    text = data.label.asString(),
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
                if (!data.desc?.asString().isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = data.desc?.asString().orEmpty(),
                        style = DiiaTextStyle.t2TextDescription,
                        color = BlackAlpha30
                    )
                }
            }
            UiIconWrapperSubatomic(
                modifier = modifier
                    .size(24.dp)
                    .detectReorder(state),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.DRAG.code),
            )
        }

        if (data.countOfDocGroup != null && data.countOfDocGroup > 1) {
            Divider(thickness = 1.dp, color = BlackAlpha10)
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .noRippleClickable {
                        onUIAction.invoke(UIAction(actionKey = data.actionKey, data = data.id))
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UiIconWrapperSubatomic(
                    modifier = modifier.size(24.dp),
                    icon = UiIcon.DrawableResource(DiiaResourceIcon.STACK.code),
                )

                Text(
                    modifier = modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    text = stringResource(
                        id = R.string.doc_stack_order_group_count,
                        formatArgs = arrayOf(data.countOfDocGroup)
                    ),
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )

                UiIconWrapperSubatomic(
                    modifier = Modifier.size(24.dp),
                    icon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
                )
            }
        }
    }
}

data class ListItemDragMlcData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_CLICK,
    val id: String,
    val label: UiText,
    val desc: UiText? = null,
    val countOfDocGroup: Int? = null
) : UIElementData

@Composable
@Preview
fun ListItemDragMlcPreview_doc() {
    val data = ListItemDragMlcData(
        id = "",
        label = UiText.DynamicString("Свідоцтво про реєстрацію транспортного засобу"),
    )
    ListItemDragMlc(data = data, onUIAction = {}, dragging = true)
}

@Composable
@Preview
fun ListItemDragMlcPreview_doc_group() {
    val data = ListItemDragMlcData(
        id = "",
        label = UiText.DynamicString("Закордонний паспорт"),
        countOfDocGroup = 2
    )
    ListItemDragMlc(data = data, onUIAction = {}, dragging = false)
}

@Composable
@Preview
fun ListItemDragMlcPreview_doc_group_details() {
    val data = ListItemDragMlcData(
        id = "",
        label = UiText.DynamicString("ХХ000000"),
        desc = UiText.DynamicString("Дата видачі: хх.хх.хххх"),
    )
    ListItemDragMlc(data = data, onUIAction = {}, dragging = false)
}

@Composable
@Preview
fun ListItemDragMlcPreview_doc_group_details_label() {
    val data = ListItemDragMlcData(
        id = "",
        label = UiText.DynamicString("ХХ000000"),
        desc = UiText.DynamicString(""),
    )
    ListItemDragMlc(data = data, onUIAction = {}, dragging = false)
}