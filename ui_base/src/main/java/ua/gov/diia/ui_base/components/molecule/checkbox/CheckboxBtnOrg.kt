package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.border.diiaGreyBorder

@Composable
fun CheckboxBtnOrg(
    modifier: Modifier = Modifier,
    data: CheckboxBtnOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .diiaGreyBorder()
            .padding(16.dp)
    ) {
        data.options?.forEach { option ->
            CheckboxSquareMlc(
                modifier = if (data.options.last() == option) Modifier else Modifier.padding(bottom = 16.dp),
                data = option,
                onUIAction = onUIAction
            )
        }

        BtnPrimaryDefaultAtm(
            modifier = Modifier.fillMaxWidth(),
            data = data.buttonData,
            progressIndicator = progressIndicator,
            onUIAction = onUIAction
        )
    }
}

data class CheckboxBtnOrgData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_BTN_ORG,
    val id: String = "",
    val options: List<CheckboxSquareMlcData>?,
    val buttonData: BtnPrimaryDefaultAtmData
) : UIElementData {

    fun onOptionsCheckChanged(optionId: String?): CheckboxBtnOrgData {
        if (optionId == null) return this
        val current = this
        val options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            current.options?.forEach {
                if (optionId == it.id) {
                    add(it.onCheckboxClick())
                } else {
                    add(it)
                }
            }
        } as List<CheckboxSquareMlcData>
        val button = current.buttonData.copy(
            interactionState = if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                UIState.Interaction.Enabled
            } else {
                UIState.Interaction.Disabled
            }
        )
        return this.copy(options = options, buttonData = button)
    }

    //Use if button should be blocked while externalCondition == false
    fun onOptionsCheckChanged(optionId: String?, externalCondition: Boolean): CheckboxBtnOrgData {
        if (optionId == null) return this
        val current = this
        val options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            current.options?.forEach {
                if (optionId == it.id) {
                    add(it.onCheckboxClick())
                } else {
                    add(it)
                }
            }
        } as List<CheckboxSquareMlcData>
        val button = current.buttonData.copy(
            interactionState = if (externalCondition) {
                if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            } else {
                UIState.Interaction.Disabled
            }
        )
        return this.copy(options = options, buttonData = button)
    }

    fun updateButtonStateByCondition(condition: Boolean): CheckboxBtnOrgData {
        return this.copy(
            buttonData = this.buttonData.copy(
                interactionState = if (condition
                    && (this.options?.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1
                            ||
                            this.options == null)
                ) {
                    UIState.Interaction.Enabled
                } else {
                    UIState.Interaction.Disabled
                }
            )
        )
    }
}

@Preview
@Composable
fun CheckboxBtnOrgPreview() {
    val data = CheckboxBtnOrgData(
        id = "",
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            add(
                CheckboxSquareMlcData(
                    id = "1",
                    title = UiText.DynamicString("Надаю дозвіл на перевірку кредитної історії"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "2",
                    title = UiText.DynamicString("Дозволяю передати мої персональні дані банку для обробки"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "3",
                    title = UiText.DynamicString("Дозволяю банку телефонувати мені для уточнення даних"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected
                )
            )
        },
        buttonData = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Далі"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxBtnOrg(
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged(it.data)
            }
        }
    }
}
