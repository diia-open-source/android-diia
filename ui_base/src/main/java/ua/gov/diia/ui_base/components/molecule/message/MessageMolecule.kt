package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun MessageMolecule(
    modifier: Modifier = Modifier,
    data: MessageMoleculeData,
    onUIAction: (UIAction) -> Unit
) {

    Row(
        modifier = modifier
            .clickable {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            ).alpha(if (data.isRead == true) 0.5f else 1f)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .clickable {
                    onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                },
            horizontalAlignment = Alignment.Start
        ) {
            data.title?.let {
                Text(
                    text = it,
                    style = DiiaTextStyle.t2TextDescription,
                    color = Black
                )
            }
            data.shortText?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = it,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
            }
            data.creationDate?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = it,
                    style = DiiaTextStyle.t3TextBody,
                    color = BlackAlpha30
                )
            }
        }
    }
}

data class MessageMoleculeData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_CLICK,
    val id: String,
    val title: String? = null,
    val shortText: String? = null,
    val creationDate: String? = null,
    val isRead: Boolean? = false,
    val notificationId: String? = null,
    val syncAction: Any? = null
) : UIElementData

@Composable
@Preview
fun MessageMoleculePreviewUnread() {
    val state = MessageMoleculeData(
        id = "1",
        title = "Label",
        shortText = "Text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text",
        creationDate = "Date and time"
    )
    MessageMolecule(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun MessageMoleculePreviewRead() {
    val state = MessageMoleculeData(
        id = "1",
        title = "Label",
        shortText = "Text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text",
        creationDate = "Date and time",
        isRead = true
    )
    MessageMolecule(modifier = Modifier, data = state) {}
}