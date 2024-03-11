package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.clickable
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
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ListItemsMlcV1(
    modifier: Modifier = Modifier,
    data: ListItemsMlcV1Data,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        data.startIconBase64String?.let {
            IconBase64Subatomic(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp),
                base64Image = data.startIconBase64String,
            )
        }
        Column(modifier = Modifier
            .weight(1f)
            .clickable { onUIAction(UIAction(actionKey = data.actionKey, data = data.id)) }) {
            Text(
                text = data.title,
                style = DiiaTextStyle.t1BigText,
                color = Black
            )
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = data.description,
                    style = DiiaTextStyle.t2TextDescription,
                    color = BlackAlpha30
                )
            }
        }
        data.endIconBase64String?.let {
            IconBase64Subatomic(
                modifier = Modifier.size(24.dp),
                base64Image = data.endIconBase64String,
            )
        }

    }
}

data class ListItemsMlcV1Data(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_CLICK,
    val id: String? = "",
    val title: String,
    val description: String? = null,
    val startIconBase64String: String? = null,
    val endIconBase64String: String? = null
) : UIElementData

@Composable
@Preview
fun ListItemsMlcV1Preview_Full() {
    val state = ListItemsMlcV1Data(
        title = "Label",
        description = "Description",
        startIconBase64String = PreviewBase64Icons.apple,
        endIconBase64String = PreviewBase64Icons.arrowNext,
    )
    ListItemsMlcV1(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ListItemsMlcV1Preview_TitleOnly() {
    val state = ListItemsMlcV1Data(
        title = "Label"
    )
    ListItemsMlcV1(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ListItemsMlcV1Preview_TitleEndIcon() {
    val state = ListItemsMlcV1Data(
        title = "Label",
        endIconBase64String = PreviewBase64Icons.arrowNext,
    )
    ListItemsMlcV1(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ListItemsMlcV1Preview_TitleDescriptionEndIcon() {
    val state = ListItemsMlcV1Data(
        title = "Label",
        description = "Description",
        endIconBase64String = PreviewBase64Icons.arrowNext,
    )
    ListItemsMlcV1(modifier = Modifier, data = state) {}
}