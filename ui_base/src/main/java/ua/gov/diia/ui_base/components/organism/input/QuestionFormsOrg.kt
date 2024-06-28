package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.input.question_form.QuestionFormsOrg
import ua.gov.diia.core.models.common_compose.org.input.question_form.QuestionFormsOrgItem
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.input.DateInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.DateInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.InputFormItem
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMolecule
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrg
import ua.gov.diia.ui_base.components.molecule.input.SelectorOrgData
import ua.gov.diia.ui_base.components.molecule.input.TextInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.toUiModel
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QuestionFormsOrg(
    modifier: Modifier = Modifier,
    data: QuestionFormsOrgData,
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
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.title?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                text = data.title,
                style = DiiaTextStyle.t3TextBody
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

                is DateInputMoleculeData -> {
                    DateInputMolecule(
                        modifier = listItemsModifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is SelectorOrgData -> SelectorOrg(
                    modifier = listItemsModifier,
                    data = item,
                    onUIAction = onUIAction
                )

                else -> {}
            }
            if (index == data.items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class QuestionFormsOrgData(
    val id: String? = null,
    val actionKey: String = UIActionKeysCompose.INPUT_FORM,
    val componentId: UiText? = null,
    val title: String? = null,
    val items: SnapshotStateList<InputFormItem>
) : UIElementData {
    fun onInputChanged(id: String?, newValue: String?): QuestionFormsOrgData {
        val data = this
        if (newValue == null || id == null) return this
        return this.copy(items = SnapshotStateList<InputFormItem>().apply {
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

                    is DateInputMoleculeData -> {
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

                    else -> {}
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
                        if (it.validation != UIState.Validation.Passed) {
                            result = false
                        }
                    }
                }

                is TextInputMoleculeData -> {
                    if (it.validation != UIState.Validation.Passed) {
                        result = false
                    }
                }

                is DateInputMoleculeData -> {
                    if (it.validationState != UIState.Validation.Passed || it.inputValue.isNullOrBlank()) {
                        result = false
                    }
                }

                is SelectorOrgData -> {
                    if (it.inputValue.isNullOrBlank()) {
                        result = false
                    }
                }

                else -> {}
            }
        }
        return result
    }
}

fun QuestionFormsOrg.toUIModel(): QuestionFormsOrgData {
    val entity = this
    val items = SnapshotStateList<InputFormItem>()
    entity.items.forEach { formItem ->
        items.add(generateInputField(formItem))
    }
    return QuestionFormsOrgData(
        id = entity.id,
        title = entity.title,
        items = items,
    )
}

private fun generateInputField(entity: QuestionFormsOrgItem): InputFormItem {
    entity.inputTextMlc?.let { return questionFormInputFields(entity) }
    entity.inputDateMlc?.let { return it.toUiModel() }
    entity.selectorOrg?.let { return it.toUiModel() }
    return TextInputMoleculeData()
}

private fun questionFormInputFields(entity: QuestionFormsOrgItem): InputFormItem {
    entity.inputTextMlc?.let { inputText ->
        val regexp = inputText.validation?.first()?.regexp
        val predefinedValue = inputText.id
        val validationList = mutableListOf<TextInputMoleculeData.ValidationTextItem>()
        inputText.validation?.forEach {
            validationList.add(
                TextInputMoleculeData.ValidationTextItem(
                    regex = it.regexp,
                    flags = it.flags,
                    errorMessage = it.errorMessage
                )
            )
        }
        return TextInputMoleculeData(
            componentId = inputText.componentId?.let { UiText.DynamicString(it) },
            id = inputText.id ?: "phoneNumber", //PHONE_NUMBER_VALIDATION_PATTERN
            label = inputText.label,
            inputValue = inputText.value ?: "",
            placeholder = inputText.placeholder,
            validationData = validationList,
            keyboardType = when (inputText.id) {
                "phoneNumber" -> KeyboardType.Phone
                "email" -> KeyboardType.Email
                else -> KeyboardType.Text
            },
            validation = getValidationState(regexp, predefinedValue)
        )
    }
    return TextInputMoleculeData() //default
}

private fun getValidationState(regex: String?, input: String?): UIState.Validation {
    var result: UIState.Validation = UIState.Validation.NeverBeenPerformed
    if (input.isNullOrEmpty()) {
        return result
    }
    result = if (input.matches(Regex(regex ?: ".*"))) {
        UIState.Validation.Passed
    } else {
        UIState.Validation.Failed
    }
    return result
}

@Composable
@Preview
fun InputFormMoleculePreview() {
    val PHONE_NUMBER_VALIDATION_PATTERN =
        "^38(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{7}\$"
    val data =
        QuestionFormsOrgData(title = "Heading", items = SnapshotStateList<InputFormItem>().apply {
            add(
                TextInputMoleculeData(
                    id = "",
                    label = LoremIpsum(6).values.joinToString(),
                    inputValue = "",
                    placeholder = "Placeholder",
                    hintMessage = LoremIpsum(50).values.joinToString(),
                    validationData = listOf(
                        TextInputMoleculeData.ValidationTextItem(
                            regex = PHONE_NUMBER_VALIDATION_PATTERN,
                            flags = listOf(),
                            errorMessage = "error",
                        )
                    ),
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
                        validationData = listOf(
                            TextInputMoleculeData.ValidationTextItem(
                                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                                flags = listOf(),
                                errorMessage = "error",
                            )
                        ),
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
                        validationData = listOf(
                            TextInputMoleculeData.ValidationTextItem(
                                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                                flags = listOf(),
                                errorMessage = "error",
                            )
                        ),
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
                    validationData = listOf(
                        TextInputMoleculeData.ValidationTextItem(
                            regex = PHONE_NUMBER_VALIDATION_PATTERN,
                            flags = listOf(),
                            errorMessage = "error",
                        )
                    ),
                    validation = UIState.Validation.NeverBeenPerformed
                )
            )
        })
    QuestionFormsOrg(data = data, onUIAction = {})
}
