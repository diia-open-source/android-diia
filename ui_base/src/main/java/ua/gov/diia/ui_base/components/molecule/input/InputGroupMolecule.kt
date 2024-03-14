package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputGroupMolecule(
    modifier: Modifier = Modifier,
    data: InputGroupMoleculeData,
    imeAction: ImeAction = ImeAction.Default,
    localFocusManager: FocusManager = LocalFocusManager.current,
    groupContainLastInput: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Row(modifier = modifier) {
        if (data.items.isNotEmpty()) {
            TextInputMolecule(
                modifier = Modifier.weight(1f),
                data = data.items[0],
                imeAction = imeAction,
                localFocusManager = localFocusManager,
                onUIAction = onUIAction
            )
            if (data.items.size > 1) {
                Spacer(modifier = Modifier.width(16.dp))

                TextInputMolecule(
                    modifier = Modifier.weight(1f),
                    data = data.items[1],
                    imeAction = if (groupContainLastInput) {
                        ImeAction.Done
                    } else {
                        imeAction
                    },
                    localFocusManager = localFocusManager,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

data class InputGroupMoleculeData(val items: List<TextInputMoleculeData>) : InputFormItem() {

    fun onInputChanged(id: String?, newValue: String?): InputGroupMoleculeData {
        val data = this
        if (newValue == null || id == null) return this
        return this.copy(items = SnapshotStateList<TextInputMoleculeData>().apply {
            data.items.forEach { item ->
                if (item.id == id) {
                    add(item.onInputChanged(newValue))
                } else {
                    add(item)
                }
            }
        } as List<TextInputMoleculeData>)
    }
}

@Composable
@Preview
fun InputGroupMoleculePreview() {
    val PHONE_NUMBER_VALIDATION_PATTERN = "^38(039|050|063|066|067|068|073|075|077|091|092|093|094|095|096|097|098|099)\\d{7}\$"
    val data = InputGroupMoleculeData(
        items = mutableListOf<TextInputMoleculeData>().apply {
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
        }
    )

    val state = remember {
        mutableStateOf(data)
    }
    InputGroupMolecule(data = data) {
        state.value = state.value.onInputChanged(
            id = it.optionalId, newValue = it.data
        )
    }
}
