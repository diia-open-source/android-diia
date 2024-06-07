package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40

@Composable
fun IconCardMlc(
    modifier: Modifier = Modifier,
    data: IconCardMlcData,
    clickable: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    Box(
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
            .background(color = WhiteAlpha40, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        BtnPlainIconAtm(
            data = BtnPlainIconAtmData(
                actionKey = data.actionKey,
                id = data.id,
                label = data.label,
                icon = data.icon
            )
        ) {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = data.id,
                    action = data.action
                )
            )
        }
    }

}

data class IconCardMlcData(
    val actionKey: String = UIActionKeysCompose.ICON_CARD_MLC,
    val id: String,
    val icon: UiIcon,
    val label: UiText,
    val action: DataActionWrapper? = null,
) : SimpleCarouselCard

@Preview
@Composable
fun IconCardMlcDataPreview() {
    val data = IconCardMlcData(
        id = "someId",
        icon = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        label = UiText.DynamicString("Label")
    )
    IconCardMlc(
        data = data
    ) {
    }

}