package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ServiceCardMlc(
    modifier: Modifier = Modifier,
    data: ServiceCardMlcData,
    onUIAction: (UIAction) -> Unit,
    diiaResourceIconProvider: DiiaResourceIconProvider
) {
    Column(
        modifier = modifier
            .height(112.dp)
            .wrapContentHeight()
            .background(color = WhiteAlpha50, shape = RoundedCornerShape(16.dp))
            .clickable {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
    ) {
        Image(
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .size(32.dp),
            painter = painterResource(diiaResourceIconProvider.getResourceId(data.icon.code)),
            contentDescription = "serviceIcon"
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
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

@Composable
@Preview
fun ServiceCardMlcPreview() {
    val state = ServiceCardMlcData(
        label = "Військові облігації",
        icon = UiIcon.DrawableResource(code = CommonDiiaResourceIcon.DEFAULT.code)
    )
    ServiceCardMlc(
        modifier = Modifier,
        data = state,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onUIAction = {}
    )
}