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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.mlc.input.InputNumberMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.divider.DividerLineMlc
import ua.gov.diia.ui_base.components.molecule.divider.DividerLineMlcData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.White

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun InputNumberMlc(
    modifier: Modifier = Modifier,
    data: InputNumberMlcData,
    imeAction: ImeAction = ImeAction.Default,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val bringIntoErrorViewRequester = BringIntoViewRequester()
    val bringIntoHintViewRequester = BringIntoViewRequester()
    val bringIntoInputViewRequester = BringIntoViewRequester()
    val inputCode: String? = data.inputCode?.asString()
    var justFocused by remember { mutableStateOf(false) }

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

    LaunchedEffect(key1 = data) {
        if (data.value != null) {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    action = DataActionWrapper(
                        type = data.actionKey,
                        subtype = inputCode,
                        resource = data.value.toString()
                    ),
                    states = listOf(focusState, data.validation),
                )
            )
        }
    }

    BasicTextField(value = data.value?.toString() ?: "",
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
            if (data.isEnabled) {
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
            lineHeight = 17.sp,
            color = getColorForInput(data.isEnabled)
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.NumberPassword
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


data class InputNumberMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_NUMBER_MLC,
    val componentId: UiText? = null,
    val inputCode: UiText? = null,
    val label: UiText,
    val placeholder: UiText? = null,
    val hint: UiText? = null,
    val value: Int? = null,
    val maxValue: Int? = null,
    val minValue: Int? = null,
    val mandatory: Boolean? = null,
    val errorMessage: UiText? = null,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val isEnabled: Boolean = true,
    val action: DataActionWrapper? = null
) : UIElementData {
    fun onInputChanged(newValue: String?): InputNumberMlcData {
        val newValueAsInt: Int?
        try {
            newValueAsInt = newValue?.toIntOrNull()
        } catch (nfe: NumberFormatException) {
            return this
        }
        return this.copy(
            value = newValueAsInt,
            validation = dataValidation(
                value = newValueAsInt,
                previousValueIsEmpty = this.value == null
            )
        )
    }

    private fun dataValidation(
        value: Int?,
        previousValueIsEmpty: Boolean,
    ): UIState.Validation {
        if (minValue == null && maxValue == null) {
            return UIState.Validation.Passed
        }
        if (value == null && minValue != null) {
            return UIState.Validation.Failed
        }
        val isMatches =
            (minValue == null && maxValue != null && (value != null && value <= maxValue))
                    ||
                    (minValue != null && maxValue == null && (value != null && value >= minValue))
                    ||
                    (minValue != null && maxValue != null && (value != null && value >= minValue && value <= maxValue))

        return if (value == null && previousValueIsEmpty) {
            UIState.Validation.NeverBeenPerformed
        } else if (isMatches) {
            UIState.Validation.Passed
        } else {
            UIState.Validation.Failed
        }
    }
}

fun InputNumberMlc.toUIModel(): InputNumberMlcData {
    return InputNumberMlcData(
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
        value = this.value,
        maxValue = this.maxValue,
        minValue = this.minValue,
        mandatory = this.mandatory,
        errorMessage = this.errorMessage?.let {
            it.toDynamicString()
        },
        validation = UIState.Validation.NeverBeenPerformed,
        isEnabled = true
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun InputNumberMlcPreview() {

    val data = InputNumberMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        minValue = 5,
        maxValue = 10,
        mandatory = false,
        errorMessage = "error".toDynamicString(),
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val state = remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputNumberMlc(modifier = Modifier
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun InputNumberMlcPreview_Invalid_From_Server() {

    val data = InputNumberMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        value = 20,
        maxValue = 10,
        mandatory = false,
        errorMessage = "error".toDynamicString(),
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val state = remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputNumberMlc(modifier = Modifier
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun InputNumberMlcPreview_Disabled() {

    val data = InputNumberMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(50).values.joinToString().toDynamicString(),
        minValue = 5,
        maxValue = 10,
        mandatory = false,
        isEnabled = false,
        errorMessage = "error".toDynamicString(),
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val state = remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputNumberMlc(modifier = Modifier
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun InputNumberMlcPreview_Poor() {

    val data = InputNumberMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        mandatory = false,
        isEnabled = true,
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val state = remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputNumberMlc(modifier = Modifier
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun InputNumberMlcPreview_Several() {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val inputField1 = InputNumberMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(5).values.joinToString().toDynamicString(),
        minValue = 5,
        maxValue = 10,
        mandatory = false,
        errorMessage = "error".toDynamicString(),
    )

    val state1 = remember {
        mutableStateOf(inputField1)
    }

    val inputField2 = InputNumberMlcData(
        label = "label".toDynamicString(),
        inputCode = "123456".toDynamicString(),
        placeholder = "Placeholder".toDynamicString(),
        hint = LoremIpsum(5).values.joinToString().toDynamicString(),
        minValue = 5,
        maxValue = 10,
        mandatory = false,
        errorMessage = "error".toDynamicString(),
    )

    val state2 = remember {
        mutableStateOf(inputField2)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputNumberMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state1.value,
            onUIAction = {
                it.action?.let { action ->
                    state1.value = state1.value.onInputChanged(action.resource)
                }
            })

        DividerLineMlc(
            data = DividerLineMlcData(type = "default")
        )

        InputNumberMlc(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state2.value,
            onUIAction = {
                it.action?.let { action ->
                    state2.value = state2.value.onInputChanged(action.resource)
                }
            })

        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}