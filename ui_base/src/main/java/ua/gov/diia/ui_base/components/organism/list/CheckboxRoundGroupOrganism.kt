package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxRoundMolecule
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxRoundMoleculeData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CheckboxRoundGroupOrganism(
    modifier: Modifier = Modifier,
    data: CheckboxRoundGroupOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember { mutableStateOf(data) }
    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )

    ) {
        val dataX = localData.value
        if (dataX.title != null) {
            Text(
                modifier = modifier.padding(16.dp),
                text = dataX.title.asString(),
                style = DiiaTextStyle.t2TextDescription,
                color = Black
            )
            DividerSlimAtom(color = BlackSqueeze)
        }

        dataX.options.forEachIndexed { index, item ->
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

data class CheckboxRoundGroupOrganismData(
    val actionKey: String = UIActionKeysCompose.RADIO_BUTTON,
    val id: String = "",
    val title: UiText? = null,
    val options: List<CheckboxRoundMoleculeData>
) : UIElementData {
    fun onRadioButtonClick(id: String): CheckboxRoundGroupOrganismData {
        val result = mutableListOf<CheckboxRoundMoleculeData>()
        options.forEach {
            if (it.id == id) {
                result.add(it.onRadioButtonClick())
            } else {
                result.add(it)
            }
        }
        return this.copy(options = result)
    }

}

@Preview
@Composable
fun CheckboxRoundGroupOrganismPreview() {
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
    val data =
        CheckboxRoundGroupOrganismData("", "", UiText.DynamicString("title"), options = listData)
    CheckboxRoundGroupOrganism(data = data) {

    }
}