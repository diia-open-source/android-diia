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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckIconMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckIconMlcData
import ua.gov.diia.ui_base.components.molecule.list.checkbox.CheckboxTitleAtom
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CheckboxRoundSeeAllOrg(
    modifier: Modifier = Modifier,
    data: CheckboxRoundSeeAllOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember { mutableStateOf(data) }
    LaunchedEffect(key1 = data) {
        localData.value = data
    }
    val expanded = rememberSaveable { mutableStateOf(data.expanded) }
    val selectedCount = remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        CheckboxTitleAtom(
            title = localData.value.title.asString(),
            expandable = false,
            showBadge = false,
            expanded = expanded,
            selectedCount = selectedCount
        )
        DividerSlimAtom(color = BlackSqueeze)

        val grid = localData.value.options.chunked(3)
        grid.forEach { IconRow(it, onUIAction) }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun IconRow(data: List<CheckIconMlcData>, onUIAction: (UIAction) -> Unit) {
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

data class CheckboxRoundSeeAllOrgData(
    val actionKey: String = UIActionKeysCompose.RADIO_BUTTON,
    val id: String = "",
    val title: UiText,
    val expanded: Boolean = true,
    val options: List<CheckIconMlcData>
) : UIElementData

@Preview
@Composable
fun CheckboxRoundSeeAllOrgPreview() {
    val listData = listOf(
        CheckIconMlcData(
            id = "code",
            icon = UiIcon.DrawableResource("charging"),
            title = UiText.DynamicString("Генератор"),
            selectionState = UIState.Selection.Unselected
        ),
        CheckIconMlcData(
            id = "code2",
            icon = UiIcon.DrawableResource("heating"),
            title = UiText.DynamicString("Heating"),
            selectionState = UIState.Selection.Unselected
        ),
        CheckIconMlcData(
            id = "code3",
            icon = UiIcon.DrawableResource("cellularConnection"),
            title = UiText.DynamicString("Cellular"),
            selectionState = UIState.Selection.Selected
        ),
        CheckIconMlcData(
            id = "code4",
            icon = UiIcon.DrawableResource("charging"),
            title = UiText.DynamicString("Аптечка"),
            selectionState = UIState.Selection.Unselected
        ),
        CheckIconMlcData(
            id = "code5",
            icon = UiIcon.DrawableResource("heating"),
            title = UiText.DynamicString("Heating"),
            selectionState = UIState.Selection.Unselected
        ),
        CheckIconMlcData(
            id = "code5",
            icon = UiIcon.DrawableResource("banking"),
            title = UiText.DynamicString("Banking"),
            selectionState = UIState.Selection.Unselected
        )
    )
    val data = CheckboxRoundSeeAllOrgData(
        "",
        "",
        UiText.DynamicString("title"),
        options = listData
    )
    CheckboxRoundSeeAllOrg(data = data) {}
}