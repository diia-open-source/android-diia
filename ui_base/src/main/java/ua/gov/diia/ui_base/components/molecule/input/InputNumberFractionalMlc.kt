package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.mlc.input.InputNumberFractionalMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

private const val SeparatorDecimal = ','

private const val DEFAULT_DECIMAL_PART_SIZE = 2

private val decimalInputFilter by lazy { Regex("^(\\d*,?)+\$") }

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun InputNumberFractionalMlc(
    modifier: Modifier = Modifier,
    data: InputNumberFractionalMlcData,
    imeAction: ImeAction = ImeAction.Default,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    var justFocused by remember { mutableStateOf(false) }
    val bringIntoErrorViewRequester = BringIntoViewRequester()
    val bringIntoHintViewRequester = BringIntoViewRequester()
    val bringIntoInputViewRequester = BringIntoViewRequester()
    val inputCode: String? = data.inputCode?.asString()

    val textFieldValue = remember { mutableStateOf(TextFieldValue(data.value ?: "")) }

    if (WindowInsets.isImeVisible) {
        LaunchedEffect(Unit) {
            if (data.validation == UIState.Validation.Failed) {
                bringIntoErrorViewRequester.bringIntoView()
            } else if (data.validation != UIState.Validation.Failed && data.hint != null) {
                bringIntoHintViewRequester.bringIntoView()
            } else {
                bringIntoInputViewRequester.bringIntoView()
            }
        }
    }

    LaunchedEffect(key1 = data.value) {
        textFieldValue.value = textFieldValue.value.copy(
            text = data.value ?: ""
        )
    }

    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                focusState = when (focusState) {
                    UIState.Focus.NeverBeenFocused -> {
                        if (data.value == null) {
                            if (it.isFocused || it.hasFocus) {
                                justFocused = true
                                UIState.Focus.FirstTimeInFocus
                            } else {
                                UIState.Focus.NeverBeenFocused
                            }
                        } else {
                            UIState.Focus.OutOfFocus
                        }
                    }

                    UIState.Focus.FirstTimeInFocus -> {
                        UIState.Focus.OutOfFocus
                    }

                    UIState.Focus.InFocus -> UIState.Focus.OutOfFocus
                    UIState.Focus.OutOfFocus -> {
                        justFocused = true
                        UIState.Focus.InFocus
                    }
                }
            }
            .bringIntoViewRequester(bringIntoInputViewRequester),
        value = filteredDecimal(textFieldValue.value, data.decimalCount ?: DEFAULT_DECIMAL_PART_SIZE),
        enabled = data.isEnabled,
        onValueChange = {
            val newValue = it.copy(text = it.text.replace(".",","))
            justFocused = false
            if (newValue.text.contains(decimalInputFilter)) {
                val result = filteredDecimal(newValue, data.decimalCount ?: DEFAULT_DECIMAL_PART_SIZE)
                textFieldValue.value = result
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        action = DataActionWrapper(
                            type = data.actionKey,
                            subtype = inputCode,
                            resource = result.text
                        ),
                    )
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = imeAction,
            keyboardType = KeyboardType.Number,
        ),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = getColorForInput(data.isEnabled)
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                localFocusManager.moveFocus(FocusDirection.Next)
            }, onDone = {
                keyboardController?.let {
                    it.hide()
                    localFocusManager.clearFocus()
                }
            }),
        singleLine = true,
        cursorBrush = SolidColor(getColorForInput(data.isEnabled)),
        interactionSource = interactionSource,
        visualTransformation = FractionalInputTransformation(data.decimalCount ?: 3),
        decorationBox = @Composable { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(data.componentId?.asString() ?: ""),
            ) {
                data.label.let {
                    Text(
                        text = data.label.asString(),
                        style = DiiaTextStyle.t4TextSmallDescription,
                        color = getColorForLabel(
                            focusState = focusState,
                            validationState = data.validation,
                            justFocused = justFocused
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    if (data.value == null) {
                        Text(
                            text = data.placeholder?.asString() ?: "",
                            style = DiiaTextStyle.t1BigText,
                            color = BlackAlpha30
                        )
                    }
                    innerTextField()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .height(2.dp)
                        .background(
                            getColorForBottomLine(
                                focusState = focusState,
                                validationState = data.validation,
                                justFocused = justFocused
                            )
                        )
                )

                AnimatedVisibility(
                    data.validation == UIState.Validation.Failed && (focusState == UIState.Focus.OutOfFocus || justFocused)
                ) {
                    data.errorMessage?.let { errorMsg ->
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .bringIntoViewRequester(bringIntoErrorViewRequester),
                            text = errorMsg.asString(),
                            style = DiiaTextStyle.t4TextSmallDescription,
                            color = Red
                        )
                    }
                }

                AnimatedVisibility(
                    !(data.validation == UIState.Validation.Failed && (focusState == UIState.Focus.OutOfFocus || justFocused))
                ) {
                    data.hint?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .bringIntoViewRequester(bringIntoHintViewRequester),
                            text = data.hint.asString(),
                            style = DiiaTextStyle.t4TextSmallDescription,
                            color = BlackAlpha30
                        )
                    }
                }
            }
        }
    )
}


data class InputNumberFractionalMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_NUMBER_MLC,
    val componentId: UiText? = null,
    val inputCode: UiText? = null,
    val label: UiText,
    val placeholder: UiText? = null,
    val hint: UiText? = null,
    val value: String? = null,
    val maxValue: Double? = null,
    val minValue: Double? = null,
    val mandatory: Boolean? = null,
    val errorMessage: UiText? = null,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val isEnabled: Boolean = true,
    val decimalCount: Int? = null,
    val action: DataActionWrapper? = null
) : UIElementData {
    fun onInputChanged(newValue: String?): InputNumberFractionalMlcData {

        return this.copy(
            value = newValue,
            validation = dataValidation(newValue, this.value.isNullOrEmpty())
        )
    }

    private fun dataValidation(
        value: String?,
        previousValueIsEmpty: Boolean,
    ): UIState.Validation {
        if (minValue == null && maxValue == null) {
            return UIState.Validation.Passed
        }
        if (value == null && minValue != null) {
            return UIState.Validation.Failed
        }
        if (value == null && previousValueIsEmpty) {
            return UIState.Validation.NeverBeenPerformed
        }
        if (!value.isNullOrEmpty() && value[value.length - 1] == ',') {
            return UIState.Validation.Failed
        }
        if (value != null) {
            val valueToCheck = if (value.isNotEmpty() && value[value.length - 1] == ',') {
                "${value}0"
            } else {
                value.replace(",",".")
            }
            val valueAsDouble: Double
            try {
                valueAsDouble = valueToCheck.toDouble()
            } catch (e: Exception) {
                return UIState.Validation.Failed
            }
            val isMatches =
                (minValue == null && maxValue != null && (valueAsDouble <= maxValue))
                        ||
                        (minValue != null && maxValue == null && (valueAsDouble >= minValue))
                        ||
                        (minValue != null && maxValue != null && (valueAsDouble >= minValue && valueAsDouble <= maxValue))

            return if (isMatches) {
                UIState.Validation.Passed
            } else {
                UIState.Validation.Failed
            }
        } else {
            return UIState.Validation.Failed
        }
    }
}

fun InputNumberFractionalMlc.toUIModel(): InputNumberFractionalMlcData {
    return InputNumberFractionalMlcData(
        componentId = this.componentId.orEmpty().toDynamicString(),
        inputCode = this.inputCode?.let {
            it.toDynamicString()
        },
        label = this.label.let {
            it.toDynamicString()
        },
        placeholder = this.placeholder?.let {
            it.toDynamicString()
        },
        hint = this.hint?.let {
            it.toDynamicString()
        },
        value = this.value?.let {
            "$it"
        },
        maxValue = this.maxValue,
        minValue = this.minValue,
        mandatory = this.mandatory,
        errorMessage = this.errorMessage?.let {
            it.toDynamicString()
        },
        decimalCount = this.decimalCount,
        validation = UIState.Validation.NeverBeenPerformed,
        isEnabled = true
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputNumberFractionalPreview() {

    val data = InputNumberFractionalMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        minValue = 5.0,
        maxValue = 10.0,
        mandatory = false,
        errorMessage = "error".toDynamicString(),
    )

    val state = remember { mutableStateOf(data) }
    Box {
        InputNumberFractionalMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.action?.resource ?: "stub")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputNumberFractionalPreview_Without_limit() {

    val data = InputNumberFractionalMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        mandatory = false,
        errorMessage = "error".toDynamicString(),
    )

    val state = remember { mutableStateOf(data) }
    Box {
        InputNumberFractionalMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.action?.resource ?: "stub")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputNumberFractionalPreview_Only_Max() {

    val data = InputNumberFractionalMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        mandatory = false,
        maxValue = 222.2,
        errorMessage = "error".toDynamicString(),
    )

    val state = remember { mutableStateOf(data) }
    Box {
        InputNumberFractionalMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.action?.resource ?: "stub")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputNumberFractionalPreview_Default_Value() {

    val data = InputNumberFractionalMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        mandatory = false,
        maxValue = 222.2,
        value = "111.11",
        errorMessage = "error".toDynamicString(),
    )

    val state = remember { mutableStateOf(data) }
    Box {
        InputNumberFractionalMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.action?.resource ?: "stub")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputNumberFractionalPreview_Disabled() {

    val data = InputNumberFractionalMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        mandatory = false,
        isEnabled = false,
        maxValue = 222.2,
        value = "111.11",
        errorMessage = "error".toDynamicString(),
    )

    val state = remember { mutableStateOf(data) }
    Box {
        InputNumberFractionalMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.action?.resource ?: "stub")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputNumberFractionalPreview_2_after_dot() {

    val data = InputNumberFractionalMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        mandatory = false,
        isEnabled = true,
        maxValue = 222.2,
        decimalCount = 0,
        value = "111.11",
        errorMessage = "error".toDynamicString(),
    )

    val state = remember { mutableStateOf(data) }
    Box {
        InputNumberFractionalMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.action?.resource ?: "stub")
        }
    }
}

private class FractionalInputTransformation(val maxSymbolsAfterDot: Int) : VisualTransformation {
    val groupSize = 3
    val separatorGroup = ' '

    val numberFormatter: DecimalFormat = DecimalFormat("#,###.##").apply {
        maximumFractionDigits = maxSymbolsAfterDot
        minimumFractionDigits = 0
        isGroupingUsed = true
        groupingSize = 3
        roundingMode = RoundingMode.HALF_UP

        decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = separatorGroup
            decimalSeparator = SeparatorDecimal
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text.text)

        return TransformedText(
            AnnotatedString(transformation.formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return transformation.originalToTransformed[offset]
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return transformation.transformedToOriginal[offset]
                }
            },
        )
    }

    private fun reformat(original: String): Transformation2 {
        val parts = original.split(SeparatorDecimal)
        check(parts.size < groupSize) { "original text must have only one dot" }

        val hasEndDot = original.endsWith(SeparatorDecimal)
        val formatted = when {
            original.isNotEmpty() && parts.size == 1 -> {
                numberFormatter.format(parts[0].toBigDecimalOrNull() ?: 0).let {
                    if (hasEndDot) it + SeparatorDecimal else it
                }
            }

            parts.size == 2 -> {
                val numberPart = numberFormatter.format(parts[0].toBigDecimalOrNull() ?: 0)
                val decimalPart = parts[1]

                "$numberPart${SeparatorDecimal}$decimalPart"
            }

            else -> original
        }

        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0

        formatted.forEachIndexed { index, char ->
            if (separatorGroup == char) {
                specialCharsCount++
            } else {
                originalToTransformed.add(index)
            }
            transformedToOriginal.add(index - specialCharsCount)
        }
        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation2(formatted, originalToTransformed, transformedToOriginal)
    }
}

private fun getColorForInput(
    isEnabled: Boolean,
): Color {
    return if (isEnabled) {
        Black
    } else {
        BlackAlpha30
    }
}

private fun getColorForBottomLine(
    focusState: UIState.Focus,
    validationState: UIState.Validation,
    justFocused: Boolean
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused -> BlackAlpha30
        UIState.Focus.FirstTimeInFocus -> Black
        UIState.Focus.InFocus -> {
            when (validationState) {
                UIState.Validation.Failed ->
                    if (justFocused) Red else Black

                else -> Black
            }
        }

        UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> BlackAlpha30
            }
        }
    }
}

private fun getColorForLabel(
    focusState: UIState.Focus,
    validationState: UIState.Validation,
    justFocused: Boolean
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused -> BlackAlpha30
        UIState.Focus.FirstTimeInFocus -> Black
        UIState.Focus.InFocus -> {
            when (validationState) {
                UIState.Validation.Failed ->
                    if (justFocused) Red else Black

                else -> Black
            }
        }

        UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> Black
            }
        }
    }
}

private fun filteredDecimal(input: TextFieldValue, decimalPartSize: Int): TextFieldValue {
    var inputText = input.text.replaceFirst(regex = Regex("^0+(?!$)"), "")
    val startsWithDot = input.text.startsWith(SeparatorDecimal)

    var selectionStart = input.selection.start
    var selectionEnd = input.selection.end

    if (startsWithDot) {
        inputText = "0$inputText"

        if (selectionStart == selectionEnd) {
            selectionStart++
            selectionEnd++
        } else {
            selectionEnd++
        }
    }

    val parts = inputText.split(SeparatorDecimal)
    val text = if (parts.size > 1) {
        parts[0] + SeparatorDecimal + parts.subList(1, parts.size)
            .joinToString("").take(decimalPartSize)
    } else {
        parts.joinToString("")
    }
    return TextFieldValue(
        if (text.startsWith(SeparatorDecimal)) "0$text" else text,
        TextRange(selectionStart, selectionEnd)
    )
}

private data class Transformation2(
    val formatted: String,
    val originalToTransformed: List<Int>,
    val transformedToOriginal: List<Int>,
)