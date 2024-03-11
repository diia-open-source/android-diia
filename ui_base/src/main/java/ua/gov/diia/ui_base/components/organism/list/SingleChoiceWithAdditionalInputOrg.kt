package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlcl
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlclData
import ua.gov.diia.ui_base.components.organism.input.RadioBtnAdditionalInputOrg
import ua.gov.diia.ui_base.components.organism.input.RadioBtnAdditionalInputOrgData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.BlackSqueeze

@Composable
fun SingleChoiceWithAdditionalInputOrg(
    modifier: Modifier = Modifier,
    data: SingleChoiceWithAdditionalInputOrgData,
    onUIAction: (UIAction) -> Unit,
) {

    Column(modifier = modifier) {
        SingleChoiceMlcl(
            data = data.generalList,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        ) {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = "${data.generalList.id}\\s${it.data}"
                )
            )
        }
        data.radioBtnAdditionalInputOrgData?.let {
            DividerSlimAtom(modifier = Modifier.fillMaxWidth(), color = BlackSqueeze)
            RadioBtnAdditionalInputOrg(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                data = it,
            ) {
                onUIAction(
                    UIAction(
                        actionKey = it.actionKey,
                        data = "${data.radioBtnAdditionalInputOrgData.radioBtnAtmData.id}\\s${it.data}"
                    )
                )
            }
        }
    }
}

data class SingleChoiceWithAdditionalInputOrgData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_ALT_ORGANISM,
    val generalList: SingleChoiceMlclData,
    val radioBtnAdditionalInputOrgData: RadioBtnAdditionalInputOrgData? = null
) : UIElementData {
    fun onItemClick(info: String?): SingleChoiceWithAdditionalInputOrgData {
        if (info.isNullOrEmpty()) return this
        val idList: List<String> = info.split("\\s")
        val data = this
            return data.copy(generalList = if (generalList.id == idList[0]) {
                generalList.onItemClick(idList[1])
            } else {
                generalList.dropSelections()
            }, radioBtnAdditionalInputOrgData = radioBtnAdditionalInputOrgData?.let {
                if (radioBtnAdditionalInputOrgData.radioBtnAtmData.id == idList[0]) {
                    radioBtnAdditionalInputOrgData.onRadioButtonClick()
                } else {
                    radioBtnAdditionalInputOrgData.dropSelection()
                }
            })
    }

    fun onTextInputChanged(newValue: String?): SingleChoiceWithAdditionalInputOrgData {
        if (newValue == null) return this
        return this.copy(
            radioBtnAdditionalInputOrgData = this.copy().radioBtnAdditionalInputOrgData?.onInputChanged(
                newValue
            )
        )
    }
}

@Composable
@Preview
fun SingleChoiceWithAdditionalInputOrganismPreviewLocal() {
    val data = SingleChoiceWithAdditionalInputOrgData(
        generalList = SingleChoiceMlclData(title = "Виберіть картку для виплати:",
            id = "generalId",
            options = SnapshotStateList<RadioBtnAtmData>().apply {
                add(
                    RadioBtnAtmData(
                        id = "op1",
                        label = "5168 **** **** 1429",
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        description = "Приват-банк\\nUA12345678901234567890123456789",
//                        endLogoBase64 = PreviewBase64Icons.visa
                    )
                )
                add(
                    RadioBtnAtmData(
                        id = "op2",
                        label = "5133 **** **** 1234",
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        description = "Приват-банк UA00000000001234567890123456789",
//                        endLogoBase64 = PreviewBase64Icons.mastercard
                    )
                )
            })
    )

    val state = remember {
        mutableStateOf(data)
    }
    SingleChoiceWithAdditionalInputOrg(
        data = state.value
    ) {
        state.value = state.value.onItemClick(info = it.data)
    }
}