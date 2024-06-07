package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.EntryPoints
import ua.gov.diia.core.models.common_compose.mlc.card.ServiceCardMlc
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50
import ua.gov.diia.ui_base.di.ServiceCardResourceEntryPointModule
import ua.gov.diia.ui_base.helper.ServiceCardResourceHelper

private lateinit var serviceCardResourceEntryPoint: ServiceCardResourceEntryPointModule.ServiceCardResourceEntryPoint

@Composable
fun requireServiceCardResourceEntryPoint(): ServiceCardResourceEntryPointModule.ServiceCardResourceEntryPoint {
    if (!::serviceCardResourceEntryPoint.isInitialized) {
        serviceCardResourceEntryPoint =
            EntryPoints.get(
                LocalContext.current.applicationContext,
                ServiceCardResourceEntryPointModule.ServiceCardResourceEntryPoint::class.java,
            )
    }
    return serviceCardResourceEntryPoint
}

@Composable
fun ServiceCardMlc(
    modifier: Modifier = Modifier,
    data: ServiceCardMlcData,
    onUIAction: (UIAction) -> Unit,
    serviceCardResourceHelper: ServiceCardResourceHelper = requireServiceCardResourceEntryPoint().serviceCardResourceHelper
) {
    Box(
        modifier = modifier
            .height(112.dp)
            .background(color = WhiteAlpha50, shape = RoundedCornerShape(16.dp))
            .clickable {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
    ) {
        Image(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .size(32.dp)
                .align(Alignment.TopStart),
            painter = painterResource(serviceCardResourceHelper.getIconResourceId(code = data.icon.code)),
            contentDescription = "serviceIcon"
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomStart),
            text = data.label,
            style = DiiaTextStyle.t3TextBody,
            color = Black
        )
    }
}

data class ServiceCardMlcData(
    val actionKey: String = UIActionKeysCompose.PS_ITEM_CLICK,
    val id: String? = "",
    val icon: UiIcon.DrawableResource,
    val label: String
) : UIElementData

fun ServiceCardMlc.toUIModel(): ServiceCardMlcData {
    return ServiceCardMlcData(
        actionKey = this.action?.type ?: UIActionKeysCompose.PS_ITEM_CLICK,
        icon = UiIcon.DrawableResource(code = this.icon),
        label = this.label
    )
}

@Composable
@Preview
fun ServiceCardMlcPreview() {
    val state = ServiceCardMlcData(
        label = "Військові облігації",
        icon = UiIcon.DrawableResource(code = "certificates")
    )
    ua.gov.diia.ui_base.components.molecule.card.ServiceCardMlc(
        modifier = Modifier,
        data = state, onUIAction = {

        }
    )
}