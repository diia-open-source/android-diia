package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.UiBaseConst
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.ButtonIconCircledLargeAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonIconCircledLargeAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.theme.BlackAlpha7
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ColumnScope.ContextIconMenuOrg(
    modifier: Modifier = Modifier,
    data: ContextIconMenuOrgData,
    onUIAction: (UIAction) -> Unit
) {

    Column(
        modifier = modifier
            .background(color = White, shape = RoundedCornerShape(24.dp))
            .conditional(data.displayItems == null) {
                padding(start = 16.dp, end = 16.dp, top = 8.dp)
            }
            .verticalScroll(rememberScrollState())
            .weight(weight = 1f, fill = false)
    ) {
        data.displayItems?.forEachIndexed { index, item ->
            ListItemMlc(
                modifier = Modifier.padding(horizontal = 16.dp),
                data = item,
                onUIAction = onUIAction,
            )

            if (index != data.displayItems.size - 1) {
                DividerSlimAtom(color = BlackAlpha7, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
        data.docActions?.forEachIndexed { index, item ->
            ListItemMlc(
                data = item,
                onUIAction = onUIAction,
            )

            if (index == data.docActions.size - 1) {
                DividerSlimAtom(color = BlackAlpha7)
            }
        }
        data.manualActions?.forEachIndexed { index, item ->
            ListItemMlc(
                data = item,
                onUIAction = onUIAction,
            )
            if (data.showDividerForManualActions && index < data.manualActions.size - 1) {
                DividerSlimAtom(color = BlackAlpha7)
            }
            if (index == data.manualActions.size - 1) {
                DividerSlimAtom(color = BlackAlpha7)
            }
        }

        data.generalActions?.forEachIndexed { index, item ->
            ListItemMlc(
                data = item,
                onUIAction = onUIAction,
            )
            if (index == data.generalActions.size - 1) {
                DividerSlimAtom(color = BlackAlpha7)
            }
        }
        if (data.showButtons) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.qr?.let {
                    ButtonIconCircledLargeAtm(
                        modifier = Modifier.weight(1f),
                        data = it,
                        onUIAction = onUIAction
                    )
                }

                data.ean13?.let {
                    ButtonIconCircledLargeAtm(
                        modifier = Modifier.weight(1f),
                        data = it,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

data class ContextIconMenuOrgData(
    val displayItems: List<ListItemMlcData>? = null,
    val docActions: List<ListItemMlcData>? = null,
    val generalActions: List<ListItemMlcData>? = null,
    val manualActions: List<ListItemMlcData>? = null,
    val qr: ButtonIconCircledLargeAtmData?,
    val ean13: ButtonIconCircledLargeAtmData?,
    val showButtons: Boolean,
    val showDividerForManualActions: Boolean = false
) : UIElementData

@Preview
@Composable
fun ContextMenuOrgPreview() {
    val itemsList = SnapshotStateList<ListItemMlcData>().apply {
        add(
            ListItemMlcData(
                id = UiBaseConst.FULL_DOC_MENU_ITEM,
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code)
            )
        )
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code)
            )
        )
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code),
            )
        )
    }
    val qr = ButtonIconCircledLargeAtmData(
        id = "qr",
        label = "Label",
        icon = UiText.StringResource(R.drawable.ic_doc_qr_selected)
    )
    val ean13 = ButtonIconCircledLargeAtmData(
        id = "ean",
        label = "Label",
        icon = UiText.StringResource(R.drawable.ic_doc_ean13_selected),
    )

    val data = ContextIconMenuOrgData(null, itemsList, itemsList, itemsList, qr, ean13, true, true)
    val state = remember {
        mutableStateOf(data)
    }
    Column {
        ContextIconMenuOrg(modifier = Modifier, state.value) {
        }
    }

}