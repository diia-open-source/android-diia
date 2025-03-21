package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc
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
import ua.gov.diia.ui_base.components.theme.White
import java.util.regex.Pattern

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun InputPhoneMlc(
    modifier: Modifier = Modifier,
    data: InputPhoneMlcData,
    imeAction: ImeAction = ImeAction.Done,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    disableErrorMessageDisplaying: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    var justFocused by remember { mutableStateOf(false) }
    val bringIntoErrorViewRequester = BringIntoViewRequester()
    val bringIntoHintViewRequester = BringIntoViewRequester()
    val bringIntoInputViewRequester = BringIntoViewRequester()
    val inputCode: String? = data.inputCode?.asString()
    val mask: String? = data.mask?.asString()
    val maskSymbolsCount: Int = mask?.count { c -> c == '#' } ?: (Int.MAX_VALUE - 1)
    var dataValidationState by remember { mutableStateOf(data.validation) }
    val errorBlockVisible by remember {
        derivedStateOf {
            ((focusState == UIState.Focus.OutOfFocus) && dataValidationState == UIState.Validation.Failed)
                    || (focusState == UIState.Focus.InFocus && dataValidationState == UIState.Validation.Failed && justFocused)
        }
    }
    val dataValue = data.value?.asString()

    LaunchedEffect(key1 = data.validation) {
        dataValidationState = data.validation
    }
    LaunchedEffect(key1 = data.mask) {
        if (justFocused) {
            justFocused = false
        }
    }

    LaunchedEffect(Unit) {
        if (!dataValue.isNullOrEmpty()) {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    action = DataActionWrapper(
                        type = data.actionKey,
                        subtype = inputCode,
                        resource = dataValue
                    ),
                    states = listOf(focusState, data.validation),
                )
            )
        }
    }

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


    BasicTextField(value = data.value?.asString() ?: "",
        enabled = data.isEnabled,
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
        onValueChange = { newValue ->
            justFocused = false
            if (data.isEnabled && newValue.length < maskSymbolsCount + 1) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        action = DataActionWrapper(
                            type = data.actionKey,
                            subtype = inputCode,
                            resource = newValue
                        ),
                        states = listOf(focusState, data.validation),
                    )
                )
            }
        },
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            color = getColorForInput(data.isEnabled)
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Phone
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
        singleLine = false,
        cursorBrush = SolidColor(getColorForInput(data.isEnabled)),
        visualTransformation = data.mask?.asString()?.let {
            PhoneVisualTransformation(it, maskSymbol = '#')
        } ?: VisualTransformation.None,
        decorationBox = @Composable { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(data.componentId?.asString() ?: ""),
            ) {
                data.label?.let {
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
                    if (data.value == null || dataValue.isNullOrEmpty()) {
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
                if (!disableErrorMessageDisplaying) {
                    AnimatedVisibility(visible = errorBlockVisible) {
                        if (!data.errorMessage?.asString().isNullOrEmpty()) {
                            Text(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .bringIntoViewRequester(bringIntoErrorViewRequester),
                                text = data.errorMessage?.asString() ?: "",
                                style = DiiaTextStyle.t4TextSmallDescription,
                                color = Red
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = !errorBlockVisible) {
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

data class InputPhoneMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_PHONE_MLC,
    val componentId: UiText? = null,
    val inputCode: UiText? = null,
    val label: UiText?,
    val placeholder: UiText? = null,
    val hint: UiText? = null,
    val mask: UiText? = null,
    val value: UiText? = null,
    val mandatory: Boolean? = null,
    val validationRules: List<ValidationRule>? = null,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val errorMessage: UiText? = null,
    val isEnabled: Boolean = true
) : UIElementData {
    fun onInputChanged(newValue: String?): InputPhoneMlcData {
        val (validationState, errorMessage) = dataValidation(
            value = newValue,
            validationRules = validationRules,
            previousValueIsEmpty = this.value == null
        )
        return this.copy(
            value = newValue.toDynamicString(),
            validation = validationState,
            errorMessage = errorMessage
        )
    }

    private fun dataValidation(
        value: String?,
        validationRules: List<ValidationRule>?,
        previousValueIsEmpty: Boolean,
    ): Pair<UIState.Validation, UiText?> {
        if (validationRules.isNullOrEmpty()) {
            UIState.Validation.Passed
        }
        if (value == null && previousValueIsEmpty) {
            UIState.Validation.NeverBeenPerformed
        }
        val validationPatterns = mutableListOf<Pair<Pattern, UiText>>().apply {
            validationRules?.forEach {
                val regexp = it.regexp
                if (it.flags.isNullOrEmpty()) {
                    add(Pattern.compile(regexp) to it.errorMessage)
                }
                it.flags?.forEach { flag ->
                    val flagKey = when (flag) {
                        "i" -> Pattern.CASE_INSENSITIVE
                        else -> null
                    }
                    if (flagKey != null) {
                        add(Pattern.compile(regexp, flagKey) to it.errorMessage)
                    } else {
                        add(Pattern.compile(regexp) to it.errorMessage)
                    }
                }
            }
        }
        validationPatterns.forEach { pattern ->
            value?.let {
                if (!it.matches(pattern.first.toRegex())) {
                    val validationMessage = validationRules?.let {
                        pattern.second
                    }
                    return UIState.Validation.Failed to validationMessage
                }
            }
        }
        return UIState.Validation.Passed to null
    }
}

fun InputPhoneMlc.toUIModel(): InputPhoneMlcData {
    val data = this
    return InputPhoneMlcData(
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
        mask = this.mask?.let {
            it.toDynamicString()
        },
        value = this.value.toDynamicString(),
        mandatory = this.mandatory,
        validation = UIState.Validation.NeverBeenPerformed,
        validationRules = mutableListOf<ValidationRule>().apply {
            data.validation?.forEach { validationItem ->
                add(
                    ValidationRule(
                        regexp = validationItem.regexp,
                        flags = validationItem.flags,
                        errorMessage = validationItem.errorMessage.toDynamicString()
                    )
                )
            }
        },
        isEnabled = true
    )
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
                UIState.Validation.Passed -> BlackAlpha30
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

class PhoneVisualTransformation(val mask: String, val maskSymbol: Char) : VisualTransformation {

    private val maxLength = mask.count { it == maskSymbol }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskSymbol) {
                    val nextDigitIndex = mask.indexOf(maskSymbol, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskSymbol))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PhoneVisualTransformation) return false
        if (mask != other.mask) return false
        if (maskSymbol != other.maskSymbol) return false
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}

private class PhoneOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount) {
            if (mask[i++] != numberChar) noneDigitCount++
        }
        return offset + noneDigitCount
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}

@Composable
@Preview
fun InputPhoneMlcPreview_SimpleNumbers() {

    val data = InputPhoneMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(10).values.joinToString().toDynamicString(),
        mandatory = false,
        mask = "### ### ### ###".toDynamicString(),
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\'+[0-9]{12}",
                    listOf("i"),
                    "Неповний номер телефону".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@Composable
@Preview
fun InputPhoneMlcPreview_NumberWithPlus() {

    val data = InputPhoneMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(10).values.joinToString().toDynamicString(),
        mandatory = false,
        mask = "### ### ### ###".toDynamicString(),
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\\+[0-9]{11}",
                    listOf("i"),
                    "Неповний номер телефону".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@Composable
@Preview
fun InputPhoneMlcPreview_Predefined_Value_Valid() {

    val data = InputPhoneMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(10).values.joinToString().toDynamicString(),
        mandatory = false,
        value = UiText.DynamicString("+38123123123"),
        mask = "### ### ### ###".toDynamicString(),
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\\+[0-9]{11}",
                    listOf("i"),
                    "Неповний номер телефону".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@Composable
@Preview
fun InputPhoneMlcPreview_Predefined_Value_Invalid() {

    val data = InputPhoneMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(10).values.joinToString().toDynamicString(),
        mandatory = false,
        value = UiText.DynamicString("+3812312313"),
        mask = "### ### ### ###".toDynamicString(),
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\\+[0-9]{11}",
                    listOf("i"),
                    "Неповний номер телефону".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@Composable
@Preview
fun InputPhoneMlcPreview_Empty_Mask() {

    val data = InputPhoneMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(10).values.joinToString().toDynamicString(),
        mandatory = false,
        value = UiText.DynamicString("+3812312313"),
        mask = null,
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\\+[0-9]{11}",
                    listOf("i"),
                    "Неповний номер телефону".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@Composable
@Preview
fun InputPhoneMlcPreview_Disabled() {
    val data = InputPhoneMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(10).values.joinToString().toDynamicString(),
        mandatory = false,
        mask = "### ### ### ###".toDynamicString(),
        isEnabled = false,
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\\+[0-9]{11}",
                    listOf("i"),
                    "Неповний номер телефону".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@Composable
@Preview
fun InputPhoneMlcPreview_No_Hint_No_Label_No_Error() {
    val data = InputPhoneMlcData(
        label = null,
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = null,
        mandatory = false,
        mask = "### ### ### ###".toDynamicString(),
        isEnabled = true,
        validationRules = mutableListOf<ValidationRule>().apply {
            add(
                ValidationRule(
                    "^\\+[0-9]{11}",
                    listOf("i"),
                    "".toDynamicString()
                )
            )
        }
    )
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val state = remember { mutableStateOf(data) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputPhoneMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                it.action?.let { action ->
                    state.value = state.value.onInputChanged(action.resource)
                }
            })
        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}