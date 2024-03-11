package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.list.ActionItemAtom
import ua.gov.diia.ui_base.components.atom.list.ActionItemAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ActionSheetMolecule(
    modifier: Modifier = Modifier,
    data: ActionSheetMoleculeData,
    onUIAction: (UIAction) -> Unit

) {
    Column(modifier = modifier.background(color = White, shape = RoundedCornerShape(16.dp))) {
        data.itemsList.forEachIndexed { index, item ->
            ActionItemAtom(data = item, onUIAction = onUIAction)
            if (index != data.itemsList.size - 1) {
                DividerSlimAtom()
            }
        }
    }
}

data class ActionSheetMoleculeData(val itemsList: SnapshotStateList<ActionItemAtomData>):
    UIElementData

@Composable
@Preview
fun ActionSheetMoleculePreview() {
    val state = ActionSheetMoleculeData(SnapshotStateList<ActionItemAtomData>().apply {
        add(ActionItemAtomData(title = "Label", startIcon = UiText.DynamicString(PreviewBase64Icons.bottle)))
        add(ActionItemAtomData(title = "Label", startIcon = UiText.DynamicString(PreviewBase64Icons.bottle)))
        add(ActionItemAtomData(title = "Label", startIcon = UiText.DynamicString(PreviewBase64Icons.bottle)))
    })
    ActionSheetMolecule(modifier = Modifier.padding(horizontal = 16.dp), data = state) {
    }
}

@Composable
@Preview
fun ActionSheetMoleculePreview_SingleAction() {
    val state = ActionSheetMoleculeData(SnapshotStateList<ActionItemAtomData>().apply {
        add(ActionItemAtomData(title = "Label", horizontalAlignment = Alignment.CenterHorizontally))
    })
    ActionSheetMolecule(modifier = Modifier.padding(horizontal = 16.dp), data = state) {
    }
}