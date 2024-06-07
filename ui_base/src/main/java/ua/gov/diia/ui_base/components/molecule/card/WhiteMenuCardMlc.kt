package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40

@Composable
fun WhiteMenuCardMlc(
    modifier: Modifier,
    data: WhiteMenuCardMlcData,
    iconSize: Dp = 32.dp,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = WhiteAlpha40, shape = RoundedCornerShape(16.dp))
            .noRippleClickable(debounce = true) {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            data.icon?.let {
                IconWithBadge(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .size(iconSize),
                    image = it,
                    contentDescription = "",
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 32.dp)
                    .fillMaxWidth(),
                text = data.title,
                style = DiiaTextStyle.h4ExtraSmallHeading,
                color = Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class WhiteMenuCardMlcData(
    val actionKey: String = UIActionKeysCompose.WHITE_MENU_CARD_MLC,
    val id: String = "",
    val icon: UiText? = null,
    val title: String,
) : UIElementData

@Preview
@Composable
fun WhiteMenuCardMlcPreview() {
    val data = WhiteMenuCardMlcData(
        title = "Додати\nдокумент",
        icon = UiText.StringResource(R.drawable.ic_add)
    )
    WhiteMenuCardMlc(modifier = Modifier, data = data) {}

}