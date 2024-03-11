package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.button.ButtonIconCircledLargeAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonWhiteLargeAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonWhiteLargeAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

@Composable
fun ActivityViewOrg(
    modifier: Modifier = Modifier,
    data: ActivityViewOrgData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        ContextMenuOrg(
            data = data.contextMenuOrg,
            diiaResourceIconProvider = diiaResourceIconProvider,
            onUIAction = onUIAction
        )
        ButtonWhiteLargeAtom(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight(),
            data = data.button,
            onUIAction = onUIAction
        )
    }
}

data class ActivityViewOrgData(
    val contextMenuOrg: ContextMenuOrgData,
    val button: ButtonWhiteLargeAtomData
) : UIElementData

@Preview
@Composable
fun ActivityViewOrgPreview() {
    val itemsList = SnapshotStateList<ListItemMlcData>().apply {
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DOC_INFO.code)
            )
        )
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DOC_INFO.code)
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
    val contextMenuOrgData =
        ContextMenuOrgData(itemsList, itemsList, null, qr, ean13, true)
    val button = ButtonWhiteLargeAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    val data = ActivityViewOrgData(
        contextMenuOrg = contextMenuOrgData,
        button = button
    )
    val state = remember {
        mutableStateOf(data)
    }
    ActivityViewOrg(
        data = state.value,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}