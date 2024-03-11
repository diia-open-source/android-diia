package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.icon.DoubleIconAtm
import ua.gov.diia.ui_base.components.atom.icon.DoubleIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50

@Composable
fun WhiteCardMlc(
    modifier: Modifier = Modifier,
    data: WhiteCardMlcData,
    isFirstAtBody: Boolean = false,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    val onClick = UIAction(
        actionKey = data.actionKey,
        data = data.id,
        action = data.action
    )

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = if (isFirstAtBody) 8.dp else 24.dp)
            .fillMaxWidth()
            .background(color = WhiteAlpha50, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .noRippleClickable {
                onUIAction(onClick)
            }
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.title.asString(),
            color = Black,
            style = DiiaTextStyle.h2MediumHeading
        )
        Text(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            text = data.label.asString(),
            color = Black,
            style = DiiaTextStyle.t2TextDescription
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.doubleIconAtm?.let {
                DoubleIconAtm(
                    modifier = Modifier.padding(end = 8.dp),
                    data = it,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                ) {
                    onUIAction(onClick)
                }
            }
            data.iconAtm?.let {
                IconAtm(
                    modifier = Modifier.padding(end = 8.dp),
                    data = it,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                ) {
                    onUIAction(onClick)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            data.smallIcon?.let {
                SmallIconAtm(
                    modifier = Modifier.padding(start = 8.dp),
                    data = it,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                ) {
                    onUIAction(onClick)
                }
            }
        }
    }
}

data class WhiteCardMlcData(
    val actionKey: String = UIActionKeysCompose.WHITE_CARD_MLC,
    val id: String? = null,
    val smallIcon: SmallIconAtmData? = null,
    val iconAtm: IconAtmData? = null,
    val doubleIconAtm: DoubleIconAtmData? = null,
    val title: UiText,
    val label: UiText,
    val action: DataActionWrapper? = null
) : UIElementData

@Preview
@Composable
fun WhiteCardMlc_Preview() {
    val data = WhiteCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label")
    )
    WhiteCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun WhiteCardMlc_PreviewIconNext() {
    val data = WhiteCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code)
    )
    WhiteCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun WhiteCardMlc_IconStartSingle_PreviewIconNext() {
    val data = WhiteCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        iconAtm = IconAtmData(code = CommonDiiaResourceIcon.DEFAULT.code)
    )
    WhiteCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun WhiteCardMlc_IconStartDouble_PreviewIconNext() {
    val data = WhiteCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        doubleIconAtm = DoubleIconAtmData(code = CommonDiiaResourceIcon.SAFETY_LARGE.code),

        )
    WhiteCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {
    }
}

@Preview
@Composable
fun WhiteCardMlc_BothStartIcons_PreviewIconNext() {
    val data = WhiteCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        iconAtm = IconAtmData(code = CommonDiiaResourceIcon.DEFAULT.code),
        doubleIconAtm = DoubleIconAtmData(code = CommonDiiaResourceIcon.SAFETY_LARGE.code),

        )
    WhiteCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {
    }
}