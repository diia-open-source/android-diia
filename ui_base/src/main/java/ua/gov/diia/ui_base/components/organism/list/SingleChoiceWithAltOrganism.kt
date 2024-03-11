package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlcl
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlclData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun SingleChoiceWithAltOrganism(
    modifier: Modifier = Modifier,
    data: SingleChoiceWithAltOrganismData,
    onUIAction: (UIAction) -> Unit
) {

    Column(modifier = modifier) {
        SingleChoiceMlcl(data = data.generalList) {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = "${data.generalList.id}\\s${it.data}"
                )
            )
        }
        data.alternative?.let {
            Spacer(modifier = Modifier.height(16.dp))
            SingleChoiceMlcl(
                data = it
            ) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = "${data.alternative.id}\\s${it.data}"
                    )
                )
            }
        }
    }
}

data class SingleChoiceWithAltOrganismData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_ALT_ORGANISM,
    val generalList: SingleChoiceMlclData,
    val alternative: SingleChoiceMlclData? = null
) : UIElementData {
    fun onItemClick(info: String?): SingleChoiceWithAltOrganismData {
        if (info.isNullOrEmpty()) return this
        val idList: List<String> = info.split("\\s")
        val data = this
        if (idList.size == 2) {
            return data.copy(generalList = if (generalList.id == idList[0]) {
                generalList.onItemClick(idList[1])
            } else {
                generalList.dropSelections()
            }, alternative = alternative?.let {
                if (alternative.id == idList[0]) {
                    alternative.onItemClick(idList[1])
                } else {
                    alternative.dropSelections()
                }
            })
        } else {
            val itemInGeneralList: Boolean =
                generalList.options.indexOfFirst { it.id == idList[0] } != -1
            val itemInAltList: Boolean = alternative?.let {
                it.options.indexOfFirst { it.id == idList[0] } != -1
            } ?: false
            return if (itemInGeneralList) {
                data.copy(
                    generalList = data.generalList.onItemClick(idList[0]),
                    alternative = data.alternative?.dropSelections(),
                )
            } else
                if (itemInAltList) {
                    data.copy(
                        generalList = data.generalList.dropSelections(),
                        alternative = data.alternative?.onItemClick(idList[0]),
                    )
                } else {
                    this
                }
        }
    }
}

@Composable
@Preview
fun SingleChoiceWithAltOrganismPreview() {
    val data = SingleChoiceWithAltOrganismData(
        generalList = SingleChoiceMlclData(title = "Виберіть картку для виплати:",
            id = "generalId",
            options = SnapshotStateList<RadioBtnAtmData>().apply {
                add(
                    RadioBtnAtmData(
                        id = "op1",
                        label = "5168 **** **** 1429",
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        description = "Приват-банк\\nUA12345678901234567890123456789",
                        logoRight = PreviewBase64Icons.visa
                    )
                )
                add(
                    RadioBtnAtmData(
                        id = "op2",
                        label = "5133 **** **** 1234",
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        description = "Приват-банк UA00000000001234567890123456789",
                        logoRight = PreviewBase64Icons.mastercard
                    )
                )
            }), alternative = SingleChoiceMlclData(
            options = SnapshotStateList<RadioBtnAtmData>().apply {
                add(
                    RadioBtnAtmData(
                        id = "op1",
                        label = "Відкрити картку в іншому банку",
                        mode = RadioButtonMode.SINGLE_CHOICE,
                    )
                )
            },
            id = "altId"
        )
    )

    val state = remember {
        mutableStateOf(data)
    }
    SingleChoiceWithAltOrganism(
        data = state.value
    ) {
        state.value = state.value.onItemClick(info = it.data)
    }
}