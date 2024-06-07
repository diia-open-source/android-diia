package ua.gov.diia.ui_base.components.organism.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData

@Composable
fun ToggleButtonGroup(
    modifier: Modifier = Modifier,
    data: ToggleButtonGroupData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BtnToggleMlc(
            modifier = Modifier.weight(1f),
            data = data.qr,
            onUIAction = onUIAction
        )

        BtnToggleMlc(
            modifier = Modifier.weight(1f),
            data = data.ean13,
            onUIAction = onUIAction
        )
    }
}

data class ToggleButtonGroupData(
    val qr: BtnToggleMlcData,
    val ean13: BtnToggleMlcData
) : UIElementData {
    fun onToggleClicked(toggleId: String?): ToggleButtonGroupData {
        if (toggleId == null) return this
        val data = this
        return this.copy(
            qr = data.qr.copy(
                selectionState = if (toggleId == data.qr.id) UIState.Selection.Selected else UIState.Selection.Unselected
            ),
            ean13 = data.ean13.copy(
                selectionState = if (toggleId == data.ean13.id) UIState.Selection.Selected else UIState.Selection.Unselected
            )
        )
    }
}

@Preview
@Composable
fun ToggleButtonGroupPreview() {
    val qr = BtnToggleMlcData(
        id = "1",
        label = "Label".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
        selectionState = UIState.Selection.Selected,
        action = DataActionWrapper(
            type = "qr"
        )
    )

    val ean13 = BtnToggleMlcData(
        id = "2",
        label = "Label".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
        selectionState = UIState.Selection.Unselected,
        action = DataActionWrapper(
            type = "ean"
        )
    )
    val data = ToggleButtonGroupData(qr, ean13)
    val state = remember {
        mutableStateOf(data)
    }
    ToggleButtonGroup(modifier = Modifier, state.value) {
        state.value = state.value.onToggleClicked(it.data)
    }
}
