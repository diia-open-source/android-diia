package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun BtnIconRoundedMlc(
    modifier: Modifier = Modifier,
    data: BtnIconRoundedMlcData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .conditional(data.label != null) {
                padding(horizontal = 8.dp)
                    .padding(top = 24.dp, bottom = 16.dp)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
                .noRippleClickable {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action
                        )
                    )
                }
        ) {
            UiIconWrapperSubatomic(
                modifier = Modifier.size(24.dp),
                icon = data.icon,
                diiaResourceIconProvider = diiaResourceIconProvider
            )
        }

        data.label?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.width(72.dp),
                text = it.asString(),
                color = Black,
                maxLines = 2,
                minLines = 2,
                textAlign = TextAlign.Center,
                style = DiiaTextStyle.t2TextDescription
            )
        }
    }

}

data class BtnIconRoundedMlcData(
    val actionKey: String = UIActionKeysCompose.BTN_ICON_ROUNDED_MLC,
    val id: String,
    val icon: UiIcon,
    val label: UiText? = null,
    val action: DataActionWrapper? = null
)

@Preview
@Composable
fun BtnIconRoundedAtmPreview() {
    val data = BtnIconRoundedMlcData(
        id = "1",
        icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code)
    )

    BtnIconRoundedMlc(
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    ) {

    }
}

@Preview
@Composable
fun BtnIconRoundedAtmPreview_WithLabel() {
    val data = BtnIconRoundedMlcData(
        id = "1",
        label = UiText.DynamicString("label"),
        icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code)
    )

    BtnIconRoundedMlc(
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    ) {

    }
}