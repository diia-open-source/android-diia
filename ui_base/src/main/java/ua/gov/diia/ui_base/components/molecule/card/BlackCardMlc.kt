package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
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
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha20
import ua.gov.diia.ui_base.components.theme.WhiteAlpha60

@Composable
fun BlackCardMlc(
    modifier: Modifier = Modifier,
    data: BlackCardMlcData,
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
            .background(color = Black, shape = RoundedCornerShape(16.dp))
            .noRippleClickable {
                onUIAction(onClick)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.doubleIconAtm?.let {
                DoubleIconAtm(
                    modifier = Modifier.padding(end = 8.dp),
                    data = it,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                    onUIAction = { onUIAction(onClick) }
                )
            }
            data.iconAtm?.let {
                IconAtm(
                    modifier = Modifier.padding(end = 8.dp),
                    data = it,
                    diiaResourceIconProvider = diiaResourceIconProvider
                ) {
                    onUIAction(onClick)
                }
            }
            Text(
                modifier = Modifier.weight(1f),
                text = data.title.asString(),
                color = White,
                style = DiiaTextStyle.h4ExtraSmallHeading
            )
            data.smallIcon?.let {
                SmallIconAtm(
                    modifier = Modifier.padding(start = 8.dp),
                    data = it,
                    diiaResourceIconProvider = diiaResourceIconProvider
                ) {
                    onUIAction(onClick)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        DividerSlimAtom(color = WhiteAlpha20)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            text = data.label.asString(),
            color = WhiteAlpha60,
            style = DiiaTextStyle.t1BigText
        )
    }
}

data class BlackCardMlcData(
    val actionKey: String = UIActionKeysCompose.BLACK_CARD_MLC,
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
fun BlackCardMlc_Preview() {
    val data = BlackCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label")
    )
    BlackCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun BlackCardMlc_PreviewIconNext() {
    val data = BlackCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code)
    )
    BlackCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun BlackCardMlc_IconStartSingle_PreviewIconNext() {
    val data = BlackCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code),
        iconAtm = IconAtmData(code = CommonDiiaResourceIcon.PN.code)
    )
    BlackCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun BlackCardMlc_IconStartDouble_PreviewIconNext() {
    val data = BlackCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code),
        doubleIconAtm = DoubleIconAtmData(code = CommonDiiaResourceIcon.SAFETY.code),

        )
    BlackCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {
    }
}

@Preview
@Composable
fun BlackCardMlc_BothStartIcons_PreviewIconNext() {
    val data = BlackCardMlcData(
        title = UiText.DynamicString("Title"),
        label = UiText.DynamicString("Label"),
        smallIcon = SmallIconAtmData(code = CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code),
        iconAtm = IconAtmData(code = CommonDiiaResourceIcon.PN.code),
        doubleIconAtm = DoubleIconAtmData(code = CommonDiiaResourceIcon.SAFETY.code),

        )
    BlackCardMlc(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {
    }
}