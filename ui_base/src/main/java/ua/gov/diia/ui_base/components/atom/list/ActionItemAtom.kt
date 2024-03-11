package ua.gov.diia.ui_base.components.atom.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ActionItemAtom(
    modifier: Modifier = Modifier,
    data: ActionItemAtomData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .noRippleClickable {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        data.startIcon?.let {
            IconWithBadge(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp),
                image = data.startIcon
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .noRippleClickable {
                    onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                },
            horizontalAlignment = data.horizontalAlignment
        ) {
            Text(
                text = data.title,
                style = DiiaTextStyle.t3TextBody,
                color = Black
            )
        }
    }
}

data class ActionItemAtomData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_CLICK,
    val id: String? = "",
    val title: String,
    val startIcon: UiText? = null,
    val horizontalAlignment: Alignment.Horizontal = Alignment.Start
) : UIElementData

@Composable
@Preview
fun ActionItemAtomIconAndTitle() {
    val state = ActionItemAtomData(
        title = "Label",
        startIcon = UiText.DynamicString(PreviewBase64Icons.apple),
        horizontalAlignment = Alignment.Start
    )
    ActionItemAtom(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ActionItemAtomTitleCenter() {
    val state = ActionItemAtomData(
        title = "Label",
        horizontalAlignment = Alignment.CenterHorizontally
    )
    ActionItemAtom(
        modifier = Modifier,
        data = state
    ) {}
}