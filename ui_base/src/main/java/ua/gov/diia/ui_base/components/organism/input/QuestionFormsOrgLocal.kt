package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.input.DateInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.DateInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMolecule
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrg
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgData
import ua.gov.diia.ui_base.components.molecule.input.TextInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QuestionFormsOrgLocal(
    modifier: Modifier = Modifier,
    data: QuestionFormsOrgDataLocal,
    onUIAction: (UIAction) -> Unit
) {
    val localFocusManager: FocusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
    ) {
        data.title?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                text = data.title,
                style = DiiaTextStyle.t3TextBody
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), thickness = 1.dp, color = ColumbiaBlue
            )
        } ?: Spacer(modifier = Modifier.height(8.dp))
        val listItemsModifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
        data.items.forEachIndexed { index, item ->
            when (item) {
                is InputGroupMoleculeData -> InputGroupMolecule(
                    modifier = listItemsModifier,
                    localFocusManager = localFocusManager,
                    groupContainLastInput = index == data.items.size - 1,
                    imeAction = ImeAction.Next,
                    data = item,
                    onUIAction = onUIAction
                )

                is TextInputMoleculeData -> TextInputMolecule(
                    modifier = listItemsModifier,
                    localFocusManager = localFocusManager,
                    imeAction = if (index == data.items.size - 1) {
                        ImeAction.Done
                    } else {
                        ImeAction.Next
                    },
                    data = item,
                    onUIAction = onUIAction
                )

                is SelectorOrgData -> SelectorOrg(
                    modifier = listItemsModifier,
                    data = item,
                    onUIAction = onUIAction
                )

                is DateInputMoleculeData -> {
                    DateInputMolecule(
                        modifier = listItemsModifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is CheckboxSquareMlcData -> {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp), thickness = 1.dp, color = ColumbiaBlue
                    )
                    CheckboxSquareMlc(
                        modifier = listItemsModifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }


                else -> {}
            }
            if (index == data.items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class QuestionFormsOrgDataLocal(
    val id : String?,
    val actionKey: String = UIActionKeysCompose.INPUT_FORM,
    val title: String? = null,
    val items: SnapshotStateList<UIElementData>
) : UIElementData {
    fun onInputChanged(id: String?, newValue: String?): QuestionFormsOrgDataLocal {
        val data = this
        if (newValue == null || id == null) return this
        return this.copy(items = SnapshotStateList<UIElementData>().apply {
            data.items.forEach { item ->
                when (item) {
                    is InputGroupMoleculeData ->
                        item.items.forEach {
                            if (it.id == id) {
                                add(it.onInputChanged(newValue))
                            } else {
                                add(it)
                            }
                        }

                    is TextInputMoleculeData -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }

                    is SelectorOrgData -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }

                    is CheckboxSquareMlcData -> {
                        if (item.id == id) {
                            add(item.onCheckboxClick())
                        } else {
                            add(item)
                        }
                    }

                    is DateInputMoleculeData -> {
                        if (item.id == id) {
                            add(item.onInputChanged(newValue))
                        } else {
                            add(item)
                        }
                    }
                }
            }
        })
    }

    fun isFormFilledAndValid(): Boolean {
        var result = true
        this.items.forEach {
            when (it) {
                is InputGroupMoleculeData -> {
                    it.items.forEach {
                        if (it.validation != UIState.Validation.Passed || it.inputValue.isNullOrBlank()) {
                            result = false
                        }
                    }
                }

                is TextInputMoleculeData -> {
                    if (it.isEnabled && (it.validation != UIState.Validation.Passed || it.inputValue.isNullOrBlank())) {
                        result = false
                    }
                }

                is SelectorOrgData -> {
                    if (it.inputValue.isNullOrBlank()) {
                        result = false
                    }
                }


                is DateInputMoleculeData -> {
                    if (it.validationState != UIState.Validation.Passed || it.inputValue.isNullOrBlank()) {
                        result = false
                    }
                }
            }
        }
        return result
    }
}

@Composable
@Preview
fun InputFormMoleculePreviewLocal() {
    val PHONE_NUMBER_VALIDATION_PATTERN =
        "^38(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{7}\$"
    val data = QuestionFormsOrgDataLocal(
        id ="",
        title = "Heading",
        items = SnapshotStateList<UIElementData>().apply {
            add(
                TextInputMoleculeData(
                    id = "",
                    label = LoremIpsum(6).values.joinToString(),
                    inputValue = "",
                    placeholder = "Placeholder",
                    hintMessage = LoremIpsum(50).values.joinToString(),
                    errorMessage = LoremIpsum(40).values.joinToString(),
//                    regex = PHONE_NUMBER_VALIDATION_PATTERN,
                    validation = UIState.Validation.NeverBeenPerformed
                )
            )
            add(InputGroupMoleculeData(items = mutableListOf<TextInputMoleculeData>().apply {
                add(
                    TextInputMoleculeData(
                        id = "",
                        label = LoremIpsum(6).values.joinToString(),
                        inputValue = "",
                        placeholder = "Placeholder",
                        hintMessage = LoremIpsum(50).values.joinToString(),
                        errorMessage = LoremIpsum(40).values.joinToString(),
//                        regex = PHONE_NUMBER_VALIDATION_PATTERN,
                        validation = UIState.Validation.NeverBeenPerformed
                    )
                )
                add(
                    TextInputMoleculeData(
                        id = "",
                        label = LoremIpsum(6).values.joinToString(),
                        inputValue = "",
                        placeholder = "Placeholder",
                        hintMessage = LoremIpsum(50).values.joinToString(),
                        errorMessage = LoremIpsum(40).values.joinToString(),
//                        regex = PHONE_NUMBER_VALIDATION_PATTERN,
                        validation = UIState.Validation.NeverBeenPerformed
                    )
                )
            }))
            add(
                TextInputMoleculeData(
                    id = "",
                    label = LoremIpsum(6).values.joinToString(),
                    inputValue = "",
                    placeholder = "Placeholder",
                    hintMessage = LoremIpsum(50).values.joinToString(),
                    errorMessage = LoremIpsum(40).values.joinToString(),
//                    regex = PHONE_NUMBER_VALIDATION_PATTERN,
                    validation = UIState.Validation.NeverBeenPerformed
                )
            )
            add(
                SelectorOrgData(
                    id = "",
                    label = LoremIpsum(6).values.joinToString(),
                    inputValue = "",
                    placeholder = "Placeholder",
                    hintMessage = LoremIpsum(50).values.joinToString(),
                )
            )
            add(
                CheckboxSquareMlcData(
                    title = UiText.DynamicString("Lorem ipsum"),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Selected
                )
            )
        })
    QuestionFormsOrgLocal(data = data, onUIAction = {})
    Spacer(modifier = Modifier.height(15.dp))
    QuestionFormsOrgLocal(data = QuestionFormsOrgDataLocal(
        id ="",
        title = "Heading",
        items = SnapshotStateList<UIElementData>().apply {
            DateInputMolecule(data = DateInputMoleculeData(), onUIAction = {})
        },
    ),
        onUIAction = {})
}
