package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxRoundMolecule
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxRoundMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.checkbox.CheckboxTitleAtom
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CheckboxRoundGroupAccordionOrganism(
    modifier: Modifier = Modifier,
    data: CheckboxRoundGroupAccordionOrganismData,
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
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        val dataX = localData.value
        CheckboxTitleAtom(
            title = dataX.title.asString(),
            expandable = true,
            showBadge = false,
            expanded = expanded,
            selectedCount = selectedCount
        )
        DividerSlimAtom(color = BlackSqueeze)

        dataX.options.forEachIndexed { index, item ->
            AnimatedVisibility(visible = expanded.value) {
                CheckboxRoundMolecule(
                    data = item,
                    onUIAction = onUIAction,
                )
                if (index != data.options.size - 1) {
                    DividerSlimAtom(color = BlackSqueeze)
                }
            }
        }
    }
}

data class CheckboxRoundGroupAccordionOrganismData(
    val actionKey: String = UIActionKeysCompose.RADIO_BUTTON,
    val id: String,
    val title: UiText,
    val expanded: Boolean = true,
    val options: List<CheckboxRoundMoleculeData>
)

@Preview
@Composable
fun CheckboxRoundGroupAccordionOrganismPreview() {
    val listData = mutableListOf<CheckboxRoundMoleculeData>()
    for (i in 0 until 10) {
        listData.add(
            CheckboxRoundMoleculeData(
                id = "id:$i",
                description = UiText.DynamicString("description"),
                title = UiText.DynamicString("lable $i"),
                status = UiText.DynamicString("status")
            )
        )
    }
    val data = CheckboxRoundGroupAccordionOrganismData(
        "",
        "",
        UiText.DynamicString("title"),
        options = listData
    )
    CheckboxRoundGroupAccordionOrganism(data = data) {}
}