package ua.gov.diia.ui_base.components.molecule.list.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtm
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnItem
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun RadioBtnGroupOrg(
    modifier: Modifier = Modifier,
    data: RadioBtnGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
    ) {
        data.title?.let {
            Text(
                modifier = Modifier.padding(16.dp),
                text = data.title,
                style = DiiaTextStyle.t3TextBody
            )
            DividerSlimAtom(modifier = Modifier.fillMaxWidth(), color = BlackSqueeze)
        }

        val itemModifier: Modifier = Modifier.padding(16.dp)

        data.items.forEachIndexed { index, item ->
            if (item is RadioBtnAtmData) {
                RadioBtnAtm(
                    modifier = itemModifier,
                    data = item,
                ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = item.id
                        )
                    )
                }
            }
            if (index != data.items.size - 1) {
                DividerSlimAtom(
                    modifier = Modifier.fillMaxWidth(),
                    color = BlackSqueeze
                )
            }
        }
    }
}

data class RadioBtnGroupOrgData(
    val actionKey: String = UIActionKeysCompose.RADIO_BTN_GROUP_ORG,
    val id: String? = null,
    val blocker: Boolean? = null,
    val title: String? = null,
    val condition: String? = null,
    val mode: RadioButtonMode,
    val items: List<RadioBtnItem>
) : UIElementData, Cloneable {
    public override fun clone(): RadioBtnGroupOrgData {
        return super.clone() as RadioBtnGroupOrgData
    }

    fun onItemClick(clickedItemId: String?): RadioBtnGroupOrgData {
        val data = this
        if (clickedItemId == null) return this
        return this.copy(
            //RadioBtnAtmData
            items = SnapshotStateList<RadioBtnItem>().apply {
                data.items.forEach {
                    if (it is RadioBtnAtmData) {
                        if (it.id == clickedItemId) {
                            add(it.onRadioButtonClick())
                        } else {
                            when (mode) {
                                RadioButtonMode.SINGLE_CHOICE -> add(it.copy(selectionState = UIState.Selection.Unselected))
                                RadioButtonMode.MULTI_CHOICE -> add(it)
                            }
                        }
                    }
                }
            }
        )
    }

    fun dropSelections(): RadioBtnGroupOrgData {
        val data = this
        return this.copy(
            items = SnapshotStateList<RadioBtnItem>().apply {
                data.items.forEach {
                   if (it is RadioBtnAtmData) {
                       add(it.copy(selectionState = UIState.Selection.Unselected))
                   }
                }
            }
        )
    }
}

@Composable
@Preview
fun RadioMoleculePreview_RadioGeneralLogoStartStatusAtom() {
    val source = mutableMapOf(
        "1" to "Option 1", "2" to "Option 2", "3" to "Option 3", "4" to "Option 4"
    )
    val data = RadioBtnGroupOrgData(id = "",
        title = "Title",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnAtmData>().apply {
            source.map {
                add(
                    RadioBtnAtmData(
                        id = it.key,
                        label = it.value,
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected,
                        logoLeft = PreviewBase64Icons.apple,
                        status = "Status"
                    )
                )
            }
        })

    val state = remember {
        mutableStateOf(data)
    }

    RadioBtnGroupOrg(modifier = Modifier, data = state.value) {
        state.value = state.value.onItemClick(it.data)
    }
}

@Composable
@Preview
fun RadioMoleculePreview_ResetValue() {
    val source = mutableMapOf(
        "1" to "Option 1", "2" to "Option 2", "3" to "Option 3", "4" to "Option 4"
    )
    val data = RadioBtnGroupOrgData(id = "",
        title = "Title",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnAtmData>().apply {
            source.map {
                add(
                    RadioBtnAtmData(
                        id = it.key,
                        label = it.value,
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected,
                        logoLeft = PreviewBase64Icons.apple,
                        status = "Status"
                    )
                )
            }
        })
    val state = remember {
        mutableStateOf(data)
    }

    Column {
        RadioBtnGroupOrg(modifier = Modifier, data = state.value) {
            state.value = state.value.onItemClick(it.data)
        }
        Button(onClick = {
            state.value = state.value.dropSelections()
        }) {
            Text("Reset")
        }
    }

}