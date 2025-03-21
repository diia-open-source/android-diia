package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.atm.input.InputNumberLargeAtm
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.ComposeConst.INPUT_NUMBER_LARGE_MLC_MASK
import ua.gov.diia.ui_base.components.infrastructure.ComposeConst.INPUT_NUMBER_LARGE_MLC_MASK_SYMBOL
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.clearFocusOnKeyboardDismiss
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.White

private const val COUNT_OF_INPUTS: Int = 4
private const val LAST_INDEX: Int = COUNT_OF_INPUTS - 1


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputNumberLargeMlc(
    modifier: Modifier = Modifier,
    data: InputNumberLargeMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val localFocusManager: FocusManager = LocalFocusManager.current
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(COUNT_OF_INPUTS) { index ->
            InputNumberLargeAtm(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(White)
                    .weight(1f)
                    .onKeyEvent {
                        if (it.key == Key.Backspace) {
                            if (data.value[index] == INPUT_NUMBER_LARGE_MLC_MASK_SYMBOL) {
                                if (index != 0) {
                                    localFocusManager.moveFocus(FocusDirection.Previous)
                                } else {
                                    keyboardController?.hide()
                                }
                            }
                        }
                        false
                    }
                    .clearFocusOnKeyboardDismiss()
                    .semantics {
                        testTag = data.componentId.orEmpty()
                    },
                value = getSymbolToDisplayByIndex(data, index),
                type = data.type,
                hint = getHintByInputType(index = index, type = data.type),
                imeAction = getImeActionByIndex(index),
                localFocusManager = localFocusManager,
                keyboardController = keyboardController
            ) {
                if (!it.data.isNullOrEmpty()) {
                    if (index != LAST_INDEX) {
//                        localFocusManager.moveFocus(FocusDirection.Next) //TODO Need return back and fix the error
                    } else {
                        keyboardController?.hide()
                    }
                }
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = formatUIActionData(
                            currentValue = data.value,
                            newSymbol = it.data,
                            index = index
                        )
                    )
                )
            }
            if (index != LAST_INDEX) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

private fun getImeActionByIndex(index: Int) = if (index != LAST_INDEX) {
    ImeAction.Next
} else {
    ImeAction.Done
}

@Composable
private fun getHintByInputType(index: Int, type: CodeInputType): String {
    return when (type) {
        CodeInputType.NUMBER -> "${index + 1}"
        CodeInputType.TEXT -> {
            when (index) {
                0 -> stringResource(id = R.string.code_input_text_hint_1)
                1 -> stringResource(id = R.string.code_input_text_hint_2)
                2 -> stringResource(id = R.string.code_input_text_hint_3)
                3 -> stringResource(id = R.string.code_input_text_hint_4)
                else -> stringResource(id = R.string.code_input_text_hint_1)
            }
        }
    }
}

private fun getSymbolToDisplayByIndex(data: InputNumberLargeMlcData, index: Int): String {
    val charSequence = data.value.toCharArray()
    val result =
        if (charSequence.size > index && charSequence[index] != INPUT_NUMBER_LARGE_MLC_MASK_SYMBOL) {
            charSequence[index]
        } else {
            ""
        }
    return result.toString()
}

private fun formatUIActionData(
    currentValue: String, newSymbol: String?, index: Int
): String {
    val charSequence = currentValue.toCharArray()
    val result: String = if (charSequence.size > index) {
        charSequence[index] = if (newSymbol.isNullOrEmpty()) {
            INPUT_NUMBER_LARGE_MLC_MASK_SYMBOL
        } else {
            newSymbol.toCharArray().last()
        }
        String(charSequence)
    } else {
        INPUT_NUMBER_LARGE_MLC_MASK_SYMBOL.toString().repeat(COUNT_OF_INPUTS)
    }
    return result
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InputNumberLargeAtm(
    modifier: Modifier,
    value: String,
    type: CodeInputType,
    hint: String,
    imeAction: ImeAction,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }

    val textFieldValueState = remember {
        mutableStateOf(
            TextFieldValue(
                text = value.uppercase(),
                selection = when {
                    value.isEmpty() -> TextRange.Zero
                    else -> TextRange(value.length, value.length)
                }
            )
        )
    }

    LaunchedEffect(key1 = value) {
        textFieldValueState.value = textFieldValueState.value.copy(
            text = value,
            selection = when {
                value.isEmpty() -> TextRange.Zero
                else -> TextRange(value.length, value.length)
            }
        )
    }

    BasicTextField(modifier = modifier
        .wrapContentWidth()
        .height(104.dp)
        .onFocusChanged {
            focusState = when (focusState) {
                UIState.Focus.NeverBeenFocused -> {
                    if (it.isFocused || it.hasFocus) {
                        UIState.Focus.FirstTimeInFocus
                    } else {
                        UIState.Focus.NeverBeenFocused
                    }
                }

                UIState.Focus.FirstTimeInFocus -> UIState.Focus.OutOfFocus
                UIState.Focus.InFocus -> UIState.Focus.OutOfFocus
                UIState.Focus.OutOfFocus -> UIState.Focus.InFocus
            }
        },
        value = textFieldValueState.value,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction, keyboardType = when (type) {
                CodeInputType.NUMBER -> KeyboardType.NumberPassword
                CodeInputType.TEXT -> KeyboardType.Text
            }
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                localFocusManager.moveFocus(FocusDirection.Next)
            },
            onDone = {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }),
        onValueChange = {
            onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
                    data = it.text
                )
            )
        },
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 38.sp,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        cursorBrush = SolidColor(Black),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                HintText(value, focusState, hint)
                innerTextField()
            }
        })
}

@Composable
private fun HintText(value: String, focusState: UIState.Focus, hint: String) {
    if (value.isEmpty() && (focusState == UIState.Focus.OutOfFocus || focusState == UIState.Focus.NeverBeenFocused)) {
        Text(
            text = hint,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                fontWeight = FontWeight.Normal,
                fontSize = 38.sp,
                lineHeight = 44.sp
            ), color = BlackAlpha30
        )
    }
}

enum class CodeInputType {
    NUMBER, TEXT
}

data class InputNumberLargeMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
    val type: CodeInputType,
    var value: String,
    var placeholder: String? = null,
    var componentId: String? = null,
    var state: UIState.Interaction = UIState.Interaction.Enabled,
) : UIElementData {


    fun onValueChange(newValue: String): InputNumberLargeMlcData {
        return this.copy(value = newValue)
    }
}

fun InputNumberLargeAtm?.toUIModel(): InputNumberLargeMlcData? {
    if (this == null) return null
    return InputNumberLargeMlcData(
        type = CodeInputType.NUMBER,
        value = this.value.orEmpty(),
        placeholder = this.placeholder,
        componentId = this.componentId,
        state = if (this.state == "disabled") UIState.Interaction.Disabled else UIState.Interaction.Enabled
    )
}

fun generateInputNumberLargeAtmMockData(codeInputType: CodeInputType): InputNumberLargeMlcData {
    return when (codeInputType) {
        CodeInputType.NUMBER -> InputNumberLargeMlcData(
            actionKey = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
            type = CodeInputType.NUMBER,
            value = INPUT_NUMBER_LARGE_MLC_MASK
        )

        CodeInputType.TEXT -> InputNumberLargeMlcData(
            actionKey = UIActionKeysCompose.INPUT_NUMBER_LARGE_MLC,
            type = CodeInputType.TEXT,
            value = INPUT_NUMBER_LARGE_MLC_MASK
        )
    }
}

@Composable
@Preview
fun InputNumberLargeMlcPreview_Number() {
    var state by remember {
        mutableStateOf(generateInputNumberLargeAtmMockData(CodeInputType.NUMBER))
    }

    Column(
        modifier = Modifier
            .background(Primary)
    ) {
        InputNumberLargeMlc(
            data = state
        ) {
            state = state.onValueChange(it.data ?: INPUT_NUMBER_LARGE_MLC_MASK)
        }
    }
}

@Composable
@Preview
fun InputNumberLargeMlcPreview_Text() {
    var state by remember {
        mutableStateOf(generateInputNumberLargeAtmMockData(CodeInputType.TEXT))
    }

    Column(
        modifier = Modifier
            .background(Primary)
    ) {
        InputNumberLargeMlc(
            data = state
        ) {
            state = state.onValueChange(it.data ?: INPUT_NUMBER_LARGE_MLC_MASK)
        }
    }
}
