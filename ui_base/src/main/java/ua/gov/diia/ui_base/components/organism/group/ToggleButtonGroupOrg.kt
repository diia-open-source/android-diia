package ua.gov.diia.ui_base.components.organism.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.group.ToggleButtonGroupOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.molecule.button.toUIModel

@Composable
fun ToggleButtonGroupOrg(
    modifier: Modifier = Modifier,
    data: ToggleButtonGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .padding(top = 32.dp, bottom = 40.dp)
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .testTag(data.componentId?.asString() ?: ""),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            data.items.forEach {
                BtnToggleMlc(
                    modifier = Modifier.weight(1f),
                    data = it,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = it.data,
                                action = it.action
                            )
                        )
                    }
                )
            }
        }
    }
}

data class ToggleButtonGroupOrgData(
    val actionKey: String = UIActionKeysCompose.TOGGLE_BUTTON_GROUP_ORG,
    val componentId: UiText? = null,
    val items: List<BtnToggleMlcData>,
    val preselected: String
) : UIElementData {
    fun onToggleClicked(toggleCode: String): ToggleButtonGroupOrgData {
        items.firstOrNull { it.id == toggleCode } ?: return this
        return this.copy(
            items = items.map {
                it.copy(
                    selectionState = if (toggleCode == it.id) UIState.Selection.Selected else UIState.Selection.Unselected
                )
            }
        )
    }
}

fun ToggleButtonGroupOrg.toUIModel(): ToggleButtonGroupOrgData {
    val data = this
    return ToggleButtonGroupOrgData(
        componentId = data.componentId.toDynamicStringOrNull(),
        items = mutableListOf<BtnToggleMlcData>().apply {
            data.items.forEach {
                add(it.btnToggleMlc.toUIModel(selectionState = if (it.btnToggleMlc.code == data.preselected) UIState.Selection.Selected else UIState.Selection.Unselected) as BtnToggleMlcData)
            }
        } as List<BtnToggleMlcData>,
        preselected = data.preselected
    )
}

@Preview
@Composable
fun ToggleButtonGroupOrg_Preview() {
    val qrButton = BtnToggleMlcData(
        id = "qr",
        label = "QR-код".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
        action = DataActionWrapper(
            type = "qr",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Selected
    )
    val barcodeButton = BtnToggleMlcData(
        id = "barcode",
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
        label = "Штрихкод".toDynamicString(),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
        action = DataActionWrapper(
            type = "barcode",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Unselected
    )
    val data = remember {
        mutableStateOf(
            ToggleButtonGroupOrgData(
                componentId = "componentId".toDynamicString(),
                items = listOf(qrButton, barcodeButton),
                preselected = "qr"
            )
        )
    }
    ToggleButtonGroupOrg(
        data = data.value
    ) {
        it.action?.let {
            data.value = data.value.onToggleClicked(it.type)
        }
    }
}

@Preview
@Composable
fun ToggleButtonGroupOrg_Preview_3_Buttons() {
    val qrButton = BtnToggleMlcData(
        id = "qr",
        label = "QR-код".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
        action = DataActionWrapper(
            type = "qr",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Selected
    )
    val barcodeButton = BtnToggleMlcData(
        id = "barcode",
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
        label = "Штрихкод".toDynamicString(),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
        action = DataActionWrapper(
            type = "barcode",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Unselected
    )
    val copyButton = BtnToggleMlcData(
        id = "copy",
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.COPY_WHITE.code),
        label = "Копіювати посилання".toDynamicString(),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.COPY.code),
        action = DataActionWrapper(
            type = "copy",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Unselected
    )
    val data = remember {
        mutableStateOf(
            ToggleButtonGroupOrgData(
                componentId = "componentId".toDynamicString(),
                items = listOf(qrButton, barcodeButton, copyButton),
                preselected = "qr"
            )
        )
    }
    ToggleButtonGroupOrg(
        data = data.value
    ) {
        it.action?.let {
            data.value = data.value.onToggleClicked(it.type)
        }
    }
}