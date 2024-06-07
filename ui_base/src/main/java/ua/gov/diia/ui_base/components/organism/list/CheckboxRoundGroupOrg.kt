package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxRoundGroupOrg
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckIconMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckIconMlcData
import ua.gov.diia.core.models.common_compose.mlc.checkbox.CheckboxRoundMlc
import ua.gov.diia.core.models.common_compose.mlc.checkbox.CheckIconMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxRoundMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckboxRoundGroupOrg(
    modifier: Modifier = Modifier,
    data: CheckboxRoundGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember { mutableStateOf(data) }
    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    Column(
        modifier = modifier
            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(8.dp))
    ) {
        localData.value.title?.let {
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = it,
                style = DiiaTextStyle.t3TextBody
            )
            DividerSlimAtom(modifier = Modifier.fillMaxWidth(), color = BlackSqueeze)
        }
        if (localData.value.items.isNotEmpty() && localData.value.items[0] is CheckboxRoundMlcData) {
            localData.value.items.forEachIndexed { index, item ->
                when (item) {
                    is CheckboxRoundMlcData -> {
                        ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxRoundMlc(
                            modifier = Modifier,
                            data = item,
                            onUIAction = {
                                onUIAction(
                                    UIAction(
                                        actionKey = UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM,
                                        data = it.data
                                    )
                                )
                            }
                        )
                        if (index != localData.value.items.size - 1) {
                            DividerSlimAtom(
                                modifier = Modifier.fillMaxWidth(),
                                color = BlackSqueeze
                            )
                        }
                    }
                }
            }
        } else {
            PrepareCBIconList(localData.value.items, onUIAction)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PrepareCBIconList(list: SnapshotStateList<UIElementData>, onUIAction: (UIAction) -> Unit) {
    val definedList = mutableListOf<CheckIconMlcData>()
    list.forEach {
        if (it is CheckIconMlcData) definedList.add(it)
    }
    val grid = definedList.chunked(3)
    grid.forEach { CheckboxIconRow(it, onUIAction) }
}

@Composable
fun CheckboxIconRow(data: List<CheckIconMlcData>, onUIAction: (UIAction) -> Unit) {
    val rowCount = 3
    val spacesToAdd = rowCount - data.size
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.forEach {
            CheckIconMlc(data = it, onUIAction = onUIAction)
        }

        for (i in 0 until spacesToAdd) {
            Spacer(modifier = Modifier.width(64.dp))
        }
    }
}

data class CheckboxRoundGroupOrgData(
    val actionKey: String = UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM,
    val id: String?,
    val title: String? = null,
    val items: SnapshotStateList<UIElementData>
) : UIElementData {
    fun onCheckboxClick(itemId: String): CheckboxRoundGroupOrgData {
        return this.copy(items = SnapshotStateList<UIElementData>().apply {
            items.forEach {
                if (it is CheckboxRoundMlcData) {
                    if (it.id == itemId) {
                        this.add(it.onCheckboxClick())
                    } else {
                        this.add(it)
                    }
                }
            }
        }
        )
    }
}

fun CheckboxRoundGroupOrg.toUIModel(): CheckboxRoundGroupOrgData {
    val entity: CheckboxRoundGroupOrg = this
    return CheckboxRoundGroupOrgData(
        id = this.id ?: "",
        title = this.title,
        items = SnapshotStateList<UIElementData>().apply {
            entity.items.forEachIndexed { index, item ->
                item.checkboxRoundMlc?.let { cbItem ->
                    add(
                        CheckboxRoundMlcData(
                            id = cbItem.id ?: "",
                            label = cbItem.label,
                            description = cbItem.description,
                            interactionState = when (cbItem.state) {
                                CheckboxRoundMlc.CbState.REST -> UIState.Interaction.Enabled
                                CheckboxRoundMlc.CbState.SELECTED -> UIState.Interaction.Enabled
                                CheckboxRoundMlc.CbState.DISABLE -> UIState.Interaction.Disabled
                                CheckboxRoundMlc.CbState.DISABLE_SELECTED -> UIState.Interaction.Disabled
                                else -> UIState.Interaction.Enabled
                            },
                            selectionState = when (cbItem.state) {
                                CheckboxRoundMlc.CbState.REST -> UIState.Selection.Unselected
                                CheckboxRoundMlc.CbState.SELECTED -> UIState.Selection.Selected
                                CheckboxRoundMlc.CbState.DISABLE -> UIState.Selection.Unselected
                                CheckboxRoundMlc.CbState.DISABLE_SELECTED -> UIState.Selection.Selected
                                else -> UIState.Selection.Unselected
                            }
                        )
                    )
                }
                item.checkIconMlc?.let { cIcon ->
                    add(
                        CheckIconMlcData(
                            title = UiText.DynamicString(cIcon.label),
                            icon = UiIcon.DrawableResource(cIcon.icon),
                            interactionState = when (cIcon.state) {
                                CheckIconMlc.CbIconState.REST -> UIState.Interaction.Enabled
                                CheckIconMlc.CbIconState.SELECTED -> UIState.Interaction.Enabled
                                CheckIconMlc.CbIconState.DISABLE -> UIState.Interaction.Disabled
                                CheckIconMlc.CbIconState.DISABLE_SELECTED -> UIState.Interaction.Disabled
                                else -> UIState.Interaction.Enabled
                            },
                            selectionState = when (cIcon.state) {
                                CheckIconMlc.CbIconState.REST -> UIState.Selection.Unselected
                                CheckIconMlc.CbIconState.SELECTED -> UIState.Selection.Selected
                                CheckIconMlc.CbIconState.DISABLE -> UIState.Selection.Unselected
                                CheckIconMlc.CbIconState.DISABLE_SELECTED -> UIState.Selection.Selected
                                else -> UIState.Selection.Unselected
                            }
                        )
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun CheckboxRoundGroupOrg_Preview() {
    val data = CheckboxRoundGroupOrgData(
        id = "",
        title = "Title",
        items = SnapshotStateList<UIElementData>().apply {
            add(
                CheckboxRoundMlcData(
                    id = "",
                    label = "label - 1",
                    description = "description - 1",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Selected
                )
            )
            add(
                CheckboxRoundMlcData(
                    id = "",
                    label = "label - 2",
                    description = "description - 2",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxRoundMlcData(
                    id = "",
                    label = "label - 3",
                    description = "description - 3",
                    status = "status",
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxRoundMlcData(
                    id = "",
                    label = "label - 4",
                    description = "description - 4",
                    status = "status",
                    interactionState = UIState.Interaction.Disabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
        }
    )
    CheckboxRoundGroupOrg(data = data){}
}


val toolbarData: SnapshotStateList<UIElementData> =
    SnapshotStateList<UIElementData>().addAllIfNotNull(
        TopGroupOrgData(
            navigationPanelMlcData = NavigationPanelMlcData(
                title = UiText.DynamicString("Title"),
                isContextMenuExist = true
            )
        )
    )

@Composable
@Preview
fun CheckboxIconGroupOrg_Preview() {
    val listData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            CheckIconMlcData(
                id = "code",
                icon = UiIcon.DrawableResource("charging"),
                title = UiText.DynamicString("Генератор"),
                selectionState = UIState.Selection.Unselected
            ),
            CheckIconMlcData(
                id = "code2",
                icon = UiIcon.DrawableResource("heating"),
                title = UiText.DynamicString("Heating 1"),
                selectionState = UIState.Selection.Unselected
            ),
            CheckIconMlcData(
                id = "code3",
                icon = UiIcon.DrawableResource("heating"),
                title = UiText.DynamicString("Heating 2"),
                selectionState = UIState.Selection.Unselected
            ),
            CheckIconMlcData(
                id = "code4",
                icon = UiIcon.DrawableResource("heating"),
                title = UiText.DynamicString("Heating 3"),
                selectionState = UIState.Selection.Unselected
            )
        )

    val data = CheckboxRoundGroupOrgData(
        id = "",
        title = "Title",
        items = listData
    )
    CheckboxRoundGroupOrg(data = data){}
}