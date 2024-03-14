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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.input.InputFormItem
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMolecule
import ua.gov.diia.ui_base.components.molecule.input.InputGroupMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.TextInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
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
                else -> {}
            }
            if (index == data.items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class QuestionFormsOrgData(
    val actionKey: String = UIActionKeysCompose.INPUT_FORM,
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
                else -> {}
            }
        }
        return result
    }
}

@Composable
@Preview
fun InputFormMoleculePreview() {
    val PHONE_NUMBER_VALIDATION_PATTERN = "^38(039|050|063|066|067|068|073|075|077|091|092|093|094|095|096|097|098|099)\\d{7}\$"
    val data = QuestionFormsOrgData(title = "Heading", items = SnapshotStateList<InputFormItem>().apply {
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
