package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.list.ActionItemAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ActionSheetMolecule
import ua.gov.diia.ui_base.components.molecule.list.ActionSheetMoleculeData

@Composable
fun ActionSheetOrganism(
    modifier: Modifier = Modifier,
    data: ActionSheetOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    val generalList = remember {
        mutableStateOf(data.generalList)
    }
    val alternativeList = remember {
        mutableStateOf(data.alternative)
    }

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp)
    ) {
        ActionSheetMolecule(data = generalList.value, onUIAction = onUIAction)
        Spacer(modifier = Modifier.height(16.dp))
        ActionSheetMolecule(
            data = alternativeList.value,
            onUIAction = onUIAction,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class ActionSheetOrganismData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_ALT_ORGANISM,
    val generalList: ActionSheetMoleculeData,
    val alternative: ActionSheetMoleculeData
) : UIElementData

@Composable
@Preview
fun ActionSheetOrganismPreview() {
    val data = ActionSheetOrganismData(
        generalList = ActionSheetMoleculeData(
            itemsList = SnapshotStateList<ActionItemAtomData>().apply {
                add(
                    ActionItemAtomData(
                        id = "ai1",
                        title = "Telegram",
                        startIcon = UiText.StringResource(R.drawable.ic_menu_notifications_action)
                    )
                )
                add(
                    ActionItemAtomData(
                        id = "ai2",
                        title = "Facebook Messenger",
                        startIcon = UiText.StringResource(R.drawable.ic_menu_notifications_action)
                    )
                )
                add(
                    ActionItemAtomData(
                        id = "ai3",
                        title = "Viber",
                        startIcon = UiText.StringResource(R.drawable.ic_menu_notifications_action)
                    )
                )
            }
        ),
        alternative = ActionSheetMoleculeData(
            itemsList = SnapshotStateList<ActionItemAtomData>().apply {
                add(
                    ActionItemAtomData(
                        id = "ai4",
                        title = "Закрити",
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                )
            })
    )
    ActionSheetOrganism(
        modifier = Modifier,
        data = data
    ) {}

}