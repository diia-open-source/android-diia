package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun SmallNotificationMlc(
    modifier: Modifier = Modifier,
    data: SmallNotificationMlcData,
    clickable: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .conditional(clickable) {
                noRippleClickable {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action
                        )
                    )
                }
            }
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)

    ) {
        Text(
            text = data.label.asString(),
            style = DiiaTextStyle.t2TextDescription,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Black
        )
        SpacerAtm(data = SpacerAtmData(SpacerAtmType.SPACER_16))
        Text(
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = data.text.asString(),
            style = DiiaTextStyle.t1BigText,
            color = Black
        )
        SpacerAtm(data = SpacerAtmData(SpacerAtmType.SPACER_8))
    }
}

data class SmallNotificationMlcData(
    val actionKey: String = UIActionKeysCompose.SMALL_NOTIFICATION_MLC,
    val id: String? = "",
    val text: UiText,
    val label: UiText,
    val action: DataActionWrapper? = null
) : SimpleCarouselCard

@Preview
@Composable
fun SmallNotificationMlc_Preview() {
    val state = SmallNotificationMlcData(
        id = "1",
        label = UiText.DynamicString("Label"),
        text = UiText.DynamicString("Text text text text text text text text text text text text text text text text text text text text text"),
    )
    SmallNotificationMlc(modifier = Modifier, data = state) {}
}