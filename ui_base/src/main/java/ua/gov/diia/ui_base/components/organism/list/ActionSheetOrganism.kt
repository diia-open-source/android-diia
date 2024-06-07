package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeAtm
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeAtmData
import ua.gov.diia.ui_base.components.atom.list.ActionItemAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.ActionSheetMolecule
import ua.gov.diia.ui_base.components.molecule.list.ActionSheetMoleculeData

@Composable
fun ActionSheetOrganism(
    modifier: Modifier = Modifier,
    data: ActionSheetOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    val actionsSheet = remember {
        mutableStateOf(data.actions)
    }

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp)
            .testTag(data.componentId?.asString() ?: ""),
    ) {
        ActionSheetMolecule(data = actionsSheet.value, onUIAction = onUIAction)
        BtnWhiteLargeAtm(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight(),
            data = data.button,
            onUIAction = onUIAction
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class ActionSheetOrganismData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_ALT_ORGANISM,
    val actions: ActionSheetMoleculeData,
    val button: BtnWhiteLargeAtmData,
    val componentId: UiText? = null
) : UIElementData

@Composable
@Preview
fun ActionSheetOrganismPreview() {
    val data = ActionSheetOrganismData(
        actions = ActionSheetMoleculeData(
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
        button = BtnWhiteLargeAtmData(
            title = "Label".toDynamicString(),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    ActionSheetOrganism(
        modifier = Modifier,
        data = data
    ) {}

}