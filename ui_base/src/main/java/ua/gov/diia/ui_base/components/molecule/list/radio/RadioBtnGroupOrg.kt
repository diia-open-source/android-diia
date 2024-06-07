package ua.gov.diia.ui_base.components.molecule.list.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.radioBtn.RadioBtnGroupOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.molecule.checkbox.RadioBtnMlcData
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnItem
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.RadioBtnMlc
import ua.gov.diia.ui_base.components.noRippleClickable
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
            .testTag(data.componentId?.asString() ?: "")
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
            if (item is RadioBtnMlcData) {
                RadioBtnMlc(
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

        data.button?.let { btn ->
            DividerSlimAtom(
                modifier = Modifier.height(1.dp),
                color = BlackSqueeze
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                BtnPlainIconAtm(
                    modifier = modifier.padding(vertical = 16.dp),
                    data = btn,

                    ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            action = btn.action
                        )
                    )
                }
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
    val items: List<RadioBtnItem>,
    val button: BtnPlainIconAtmData? = null,
    val componentId: UiText? = null,
    val inputCode: String? = null,
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
                    if (it is RadioBtnMlcData) {
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
                   if (it is RadioBtnMlcData) {
                       add(it.copy(selectionState = UIState.Selection.Unselected))
                   }
                }
            }
        )
    }
}

fun RadioBtnGroupOrg.toUIModel(): RadioBtnGroupOrgData {
    val entity: RadioBtnGroupOrg = this
    return RadioBtnGroupOrgData(
        id = this.id,
        title = this.title,
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnItem>().apply {
            entity.items.forEach { item ->
                item.radioBtnMlc?.let { rbItem ->
                    add(
                        RadioBtnMlcData(
                            id = rbItem.id ?: "",
                            label = rbItem.label,
                            mode = ua.gov.diia.ui_base.components.molecule.checkbox.RadioButtonMode.SINGLE_CHOICE,
                            accordionTitle = null,
                            description = rbItem.description,
                            status = rbItem.status,
                            interactionState = if (rbItem.isEnabled != null) {
                                if (rbItem.isEnabled == true) UIState.Interaction.Enabled else UIState.Interaction.Disabled
                            } else {
                                UIState.Interaction.Enabled
                            },
                            selectionState = if (rbItem.isSelected != null) {
                                if (rbItem.isSelected == true) UIState.Selection.Selected else UIState.Selection.Unselected
                            } else {
                                UIState.Selection.Unselected
                            },
                            logoLeft = rbItem.logoLeft,
                            logoRight = rbItem.logoRight,
                        )
                    )
                }
            }
        },
        button = this.btnPlainIconAtm?.toUiModel(),
        componentId = componentId?.let { UiText.DynamicString(it) },
        inputCode = this.inputCode
    )
}

@Composable
@Preview
fun RadioBtnGroupOrg_Without_title() {
    val source = mutableMapOf(
        "1" to "Title 1", "2" to "Title 2", "3" to "Title 3"
    )
    val data = RadioBtnGroupOrgData(
        id = "",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnMlcData>().apply {
            source.map {
                add(
                    RadioBtnMlcData(
                        id = it.key,
                        label = it.value,
                        description = "Description _ 02.11.2010 / І-БК 123456",
                        mode = ua.gov.diia.ui_base.components.molecule.checkbox.RadioButtonMode.SINGLE_CHOICE,
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected,
                        logoLeft = PreviewBase64Icons.apple
                    )
                )
            }
        },
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),)

    val state = remember {
        mutableStateOf(data)
    }

    RadioBtnGroupOrg(modifier = Modifier, data = state.value) {
        state.value = state.value.onItemClick(it.data)
    }
}

@Composable
@Preview
fun RadioMoleculePreview_RadioGeneralLogoStartStatusAtom() {
    val source = mutableMapOf(
        "1" to "Option 1", "2" to "Option 2", "3" to "Option 3"
    )
    val data = RadioBtnGroupOrgData(id = "",
        title = "Title",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnMlcData>().apply {
            source.map {
                add(
                    RadioBtnMlcData(
                        id = it.key,
                        label = it.value,
                        mode = ua.gov.diia.ui_base.components.molecule.checkbox.RadioButtonMode.SINGLE_CHOICE,
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
        "1" to "Option 1", "2" to "Option 2", "3" to "Option 3"
    )
    val data = RadioBtnGroupOrgData(id = "",
        title = "Title",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnMlcData>().apply {
            source.map {
                add(
                    RadioBtnMlcData(
                        id = it.key,
                        label = it.value,
                        mode = ua.gov.diia.ui_base.components.molecule.checkbox.RadioButtonMode.SINGLE_CHOICE,
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