package ua.gov.diia.ui_base.components.organism.bottom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.molecule.button.BtnIconRoundedMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnIconRoundedMlcData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

@Composable
fun BtnIconRoundedGroupOrg(
    modifier: Modifier = Modifier,
    data: BtnIconRoundedGroupOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.items.forEach {
            BtnIconRoundedMlc(
                data = it,
                diiaResourceIconProvider = diiaResourceIconProvider
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
    }
}

data class BtnIconRoundedGroupOrgData(
    val actionKey: String = UIActionKeysCompose.BTN_ICON_ROUNDED_GROUP_ORG,
    val id: String? = null,
    val items: List<BtnIconRoundedMlcData>
) : UIElementData

@Preview
@Composable
fun BtnIconRoundedGroupOrgPreview() {
    val data =
        BtnIconRoundedGroupOrgData(items = mutableListOf<BtnIconRoundedMlcData>().apply {
            repeat(3) {
                add(
                    BtnIconRoundedMlcData(
                        id = it.toString(),
                        icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code)
                    )
                )
            }
        })
    BtnIconRoundedGroupOrg(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}

@Preview
@Composable
fun BtnIconRoundedGroupOrgPreview_WithLabel() {
    val data =
        BtnIconRoundedGroupOrgData(items = mutableListOf<BtnIconRoundedMlcData>().apply {
            repeat(3) {
                add(
                    BtnIconRoundedMlcData(
                        id = it.toString(),
                        label = UiText.DynamicString("label"),
                        icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.ELLIPSE_WHITE_ARROW_RIGHT.code)
                    )
                )
            }
        })
    BtnIconRoundedGroupOrg(data = data, diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()) {

    }
}