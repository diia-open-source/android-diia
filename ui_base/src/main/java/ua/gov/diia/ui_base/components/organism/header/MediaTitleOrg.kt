package ua.gov.diia.ui_base.components.organism.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun MediaTitleOrg(
    modifier: Modifier = Modifier,
    data: MediaTitleOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(start = 24.dp)
                .padding(vertical = 20.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = data.secondaryLabel.asString(),
                style = DiiaTextStyle.t2TextDescription,
                color = BlackAlpha50
            )
            BtnPlainIconAtm(
                data = data.button,
                diiaResourceIconProvider = diiaResourceIconProvider,
            ) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = it.data,
                        action = it.action
                    )
                )
            }
        }
        DividerSlimAtom(
            modifier = Modifier
                .height(1.dp)
                .padding(horizontal = 24.dp),
            color = BlackAlpha10
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = data.title.asString(),
            style = DiiaTextStyle.h2MediumHeading
        )
    }
}

data class MediaTitleOrgData(
    val actionKey: String = UIActionKeysCompose.MEDIA_TITLE_ORG,
    val secondaryLabel: UiText,
    val title: UiText,
    val button: BtnPlainIconAtmData
) : UIElementData

@Preview
@Composable
fun MediaTitleOrgPreview() {
    val data = MediaTitleOrgData(
        secondaryLabel = UiText.DynamicString("secondaryLabel"),
        title = UiText.DynamicString("title"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.MENU.code)
        )
    )

    MediaTitleOrg(
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }

}