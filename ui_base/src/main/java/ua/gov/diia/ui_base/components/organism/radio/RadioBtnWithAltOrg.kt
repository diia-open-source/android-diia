package ua.gov.diia.ui_base.components.organism.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.core.models.common_compose.org.radioBtn.Item
import ua.gov.diia.core.models.common_compose.org.radioBtn.RadioBtnGroupOrg
import ua.gov.diia.core.models.common_compose.org.radioBtn.RadioBtnWithAltOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.checkbox.RadioBtnMlcData
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrg
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrgData
import ua.gov.diia.ui_base.components.molecule.list.radio.toUIModel
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun RadioBtnWithAltOrg(
    modifier: Modifier = Modifier,
    data: RadioBtnWithAltOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.items.forEachIndexed { index, item ->
            if (item is RadioBtnGroupOrgData) {
                RadioBtnGroupOrg(
                    modifier = Modifier,
                    data = item
                ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = "${item.id}\\s${it.data}"
                        )
                    )
                }
            }
        }
    }
}

data class RadioBtnWithAltOrgData(
    val actionKey: String = UIActionKeysCompose.RADIO_BTN_WITH_ALT_ORG,
    val id: String? = null,
    val items: List<RadioBtnWithAltOrgItem>,
    val componentId: UiText? = null,
) : UIElementData, Cloneable {

    fun onItemClick(info: String?): RadioBtnWithAltOrgData {
        val data = this
        if (info.isNullOrEmpty()) return this
        val idList: List<String> = info.split("\\s")

        return this.copy(
            //RadioBtnGroupOrgData
            items = SnapshotStateList<RadioBtnWithAltOrgItem>().apply {
                data.items.forEach {
                    if (it is RadioBtnGroupOrgData) {
                        if (it.id == idList[0]) {
                            add(it.onItemClick(idList[1]))
                        } else {
                            when (it.mode) {
                                RadioButtonMode.SINGLE_CHOICE -> add(it.copy(items = it.items.map { radioBtnItem ->
                                    if (radioBtnItem is RadioBtnMlcData) {
                                        radioBtnItem.copy(selectionState = UIState.Selection.Unselected)
                                    } else {
                                        radioBtnItem
                                    }
                                }))

                                RadioButtonMode.MULTI_CHOICE -> add(it)
                            }
                        }
                    }
                }
            }
        )
    }

    fun dropSelections(): RadioBtnWithAltOrgData {
        val data = this
        return this.copy(
            items = SnapshotStateList<RadioBtnGroupOrgData>().apply {
                data.items.forEach { item ->
                    if (item is RadioBtnGroupOrgData) {
                        add(item.copy(
                            items = item.items.map { radioBtnItem ->
                                if (radioBtnItem is RadioBtnMlcData) {
                                    radioBtnItem.copy(selectionState = UIState.Selection.Unselected)
                                } else {
                                    radioBtnItem
                                }
                            }
                        ))
                    }
                }
            })
    }
}

fun RadioBtnWithAltOrg?.toUiModel(): RadioBtnWithAltOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val items = mutableListOf<RadioBtnWithAltOrgItem>().apply {
        (entity.items as List<Item>).forEach { list ->
            if (list.radioBtnGroupOrg is RadioBtnGroupOrg) {
                add(
                    (list.radioBtnGroupOrg as RadioBtnGroupOrg).toUIModel()
                )
            }
        }
    }
    return RadioBtnWithAltOrgData(
        items = items,
        componentId = entity.componentId.toDynamicStringOrNull()
    )
}

@Composable
@Preview
fun RadioBtnWithAltOrgPreview() {
    val source = mutableMapOf(
        "1" to "5441 **** **** 8127", "2" to "5441 **** **** 8127"
    )
    val generalList = RadioBtnGroupOrgData(id = "1k",
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
                        largeLogoRight = UiIcon.DrawableResource(DiiaResourceIcon.CARD_VISA.code)                   )
                )
            }
        })
    val altList = RadioBtnGroupOrgData(id = "2k",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnMlcData>().apply {
            add(
                RadioBtnMlcData(
                    id = "it.key",
                    label = "Відкрити картку в іншому банку",
                    mode = ua.gov.diia.ui_base.components.molecule.checkbox.RadioButtonMode.SINGLE_CHOICE,
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                )
            )
        })
    val radioBtnWithAltOrgData = RadioBtnWithAltOrgData(items = listOf(generalList, altList))

    val state = remember {
        mutableStateOf(radioBtnWithAltOrgData)
    }

    RadioBtnWithAltOrg(modifier = Modifier, data = state.value) {
        state.value = if (it.data == altList.id) {
            state.value.dropSelections()
        } else {
            state.value.onItemClick(it.data)
        }
    }
}