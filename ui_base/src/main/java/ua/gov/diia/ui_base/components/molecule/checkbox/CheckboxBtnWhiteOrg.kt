package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnWhiteOrg
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun CheckboxBtnWhiteOrg(
    modifier: Modifier = Modifier,
    data: CheckboxBtnWhiteOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.options?.forEach { option ->
            CheckboxSquareMlc(
                modifier = if (data.options.last() == option)
                    Modifier else Modifier.padding(bottom = 16.dp),
                data = option,
                onUIAction = {
                    onUIAction(
                        UIAction(
                            actionKey = it.actionKey,
                            data = it.data,
                            action = DataActionWrapper(
                                type = data.actionKey
                            )
                        )
                    )
                }
            )
        }

        BtnPrimaryWideAtm(
            modifier = Modifier.fillMaxWidth(),
            data = data.buttonPrimaryData,
            progressIndicator = progressIndicator,
            onUIAction = onUIAction
        )

        data.buttonPlainAtmData?.let {
            BtnPlainAtm(
                modifier = Modifier.fillMaxWidth(),
                data = it,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
    }
}

data class CheckboxBtnWhiteOrgData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_BTN_WHITE_ORG,
    val id: String = "",
    val options: List<CheckboxSquareMlcData>?,
    val buttonPrimaryData: BtnPrimaryWideAtmData,
    val buttonPlainAtmData: BtnPlainAtmData? = null,
    val componentId: UiText? = null,
) : UIElementData {

    fun onOptionsCheckChanged(optionId: String?): CheckboxBtnWhiteOrgData {
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
        val button = current.buttonPrimaryData.copy(
            interactionState = if (options.indexOfFirst { it.selectionState == UIState.Selection.Unselected } == -1) {
                UIState.Interaction.Enabled
            } else {
                UIState.Interaction.Disabled
            }
        )
        return this.copy(options = options, buttonPrimaryData = button)
    }

    //Use if button should be blocked while externalCondition == false
    fun onOptionsCheckChanged(
        optionId: String?,
        externalCondition: Boolean
    ): CheckboxBtnWhiteOrgData {
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
        val button = current.buttonPrimaryData.copy(
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
        return this.copy(options = options, buttonPrimaryData = button)
    }

    fun updateButtonStateByCondition(condition: Boolean): CheckboxBtnWhiteOrgData {
        return this.copy(
            buttonPrimaryData = this.buttonPrimaryData.copy(
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


fun generateCheckBoxBtnWhiteOrgMockData(
    btnPrimaryState: UIState.Interaction,
    btnSecondaryPresent: Boolean,
    checkBoxSate: UIState.Selection
): CheckboxBtnWhiteOrgData {
    return CheckboxBtnWhiteOrgData(
        id = "",
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            add(
                CheckboxSquareMlcData(
                    id = "1",
                    title = UiText.DynamicString("Надаю згоду на передачу персональних даних з ЕСОЗ"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = checkBoxSate
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "2",
                    title = UiText.DynamicString("Надаю згоду на передачу персональних даних з ЕСОЗ"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = checkBoxSate
                )
            )
            add(
                CheckboxSquareMlcData(
                    id = "3",
                    title = UiText.DynamicString("Надаю згоду на передачу персональних даних з ЕСОЗ"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = checkBoxSate
                )
            )
        },
        buttonPrimaryData = BtnPrimaryWideAtmData(
            title = UiText.DynamicString("Далі"),
            id = "",
            interactionState = btnPrimaryState
        ),
        buttonPlainAtmData = if (btnSecondaryPresent) {
            BtnPlainAtmData(
                title = UiText.DynamicString("Деталі")
            )
        } else {
            null
        }
    )
}

fun CheckboxBtnWhiteOrg?.toUIModel(): CheckboxBtnWhiteOrgData {
    val checkboxBtnWhiteOrg = this
    return CheckboxBtnWhiteOrgData(
        options = SnapshotStateList<CheckboxSquareMlcData>().apply {
            checkboxBtnWhiteOrg?.items?.forEachIndexed() { index, item ->
                add(
                    CheckboxSquareMlcData(
                        id = item.checkboxSquareMlc.id ?: index.toString(),
                        title = UiText.DynamicString(item.checkboxSquareMlc.label),
                        interactionState = if (item.checkboxSquareMlc.blocker != null) {
                            if (item.checkboxSquareMlc.blocker == false) {
                                UIState.Interaction.Enabled
                            } else {
                                UIState.Interaction.Disabled
                            }
                        } else UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected
                    )
                )
            }
        },
        buttonPrimaryData = BtnPrimaryWideAtmData(
            title = UiText.DynamicString(
                checkboxBtnWhiteOrg?.btnPrimaryWideAtm?.label ?: "Далі"
            ),
            id = checkboxBtnWhiteOrg?.btnPrimaryWideAtm?.componentId ?: "",
            interactionState = when (checkboxBtnWhiteOrg?.btnPrimaryWideAtm?.state
                ?: ButtonStates.disabled) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Disabled
            },
            action = checkboxBtnWhiteOrg?.btnPrimaryWideAtm?.action?.toDataActionWrapper()
        ),
        buttonPlainAtmData = checkboxBtnWhiteOrg?.btnPlainAtm?.let { btnPlain ->
            BtnPlainAtmData(
                id = btnPlain.componentId ?: "",
                title = UiText.DynamicString(btnPlain.label),
                action = btnPlain.action?.toDataActionWrapper(),
                componentId = btnPlain.componentId?.let { UiText.DynamicString(it) }
            )
        },
        componentId = this?.componentId?.let { UiText.DynamicString(it) },
    )
}

@Preview
@Composable
fun CheckboxBtnWhiteOrgUnselectedPreview() {
    val state = remember {
        mutableStateOf(
            generateCheckBoxBtnWhiteOrgMockData(
                btnPrimaryState = UIState.Interaction.Disabled,
                btnSecondaryPresent = false,
                checkBoxSate = UIState.Selection.Unselected
            )
        )
    }

    CheckboxBtnWhiteOrg(data = state.value) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged(it.data)
            }
        }
    }
}

@Preview
@Composable
fun CheckBoxBtnWhiteSelectedPreview() {
    val state = remember {
        mutableStateOf(
            generateCheckBoxBtnWhiteOrgMockData(
                btnPrimaryState = UIState.Interaction.Enabled,
                btnSecondaryPresent = true,
                checkBoxSate = UIState.Selection.Selected
            )
        )
    }

    CheckboxBtnWhiteOrg(
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.CHECKBOX_REGULAR -> {
                state.value = state.value.onOptionsCheckChanged(it.data)
            }
        }
    }
}