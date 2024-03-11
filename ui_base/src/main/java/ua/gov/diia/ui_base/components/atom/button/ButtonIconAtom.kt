package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ButtonIconAtom(
    modifier: Modifier = Modifier,
    data: ButtonIconAtomData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .noRippleClickable(
                enabled = data.interactionState == UIState.Interaction.Enabled,
                onClick = {
                    onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
                }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = data.title,
            tint = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.title,
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.t3TextBody,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class ButtonIconAtomData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String,
    val title: String,
    val interactionState: UIState.Interaction
) : UIElementData

@Preview
@Composable
private fun ButtonIconPlainPreview_EnabledState() {
    ButtonIconAtom(
        data = ButtonIconAtomData(
            title = LoremIpsum(6).values.joinToString(),
            id = "",
            interactionState = UIState.Interaction.Enabled
        ),
        onUIAction = {}
    )
}

@Preview
@Composable
private fun ButtonIconAtomPreview_DisabledState() {
    ButtonIconAtom(
        data = ButtonIconAtomData(
            title = LoremIpsum(6).values.joinToString(),
            id = "",
            interactionState = UIState.Interaction.Disabled
        ),
        onUIAction = {}
    )
}