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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.message.AttentionMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlcData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrgData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.White

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun TextInputMolecule(
    modifier: Modifier = Modifier,
    data: TextInputMoleculeData,
    imeAction: ImeAction = ImeAction.Default,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val bringIntoErrorViewRequester = BringIntoViewRequester()
    val bringIntoHintViewRequester = BringIntoViewRequester()
    val bringIntoInputViewRequester = BringIntoViewRequester()

    if (WindowInsets.isImeVisible) {
        LaunchedEffect(Unit) {
            if (data.validation == UIState.Validation.Failed) {
                bringIntoErrorViewRequester.bringIntoView()
            } else if (data.validation != UIState.Validation.Failed && !data.hintMessage.isNullOrEmpty()) {
                bringIntoHintViewRequester.bringIntoView()
            } else {
                bringIntoInputViewRequester.bringIntoView()
            }
        }
    }

    BasicTextField(value = data.inputValue ?: "",
        enabled = data.isEnabled,
        modifier = modifier
            .onFocusChanged {
                focusState = when (focusState) {
                    UIState.Focus.NeverBeenFocused -> {
                        if (data.inputValue.isNullOrEmpty()) {
                            if (it.isFocused || it.hasFocus) {
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
                    UIState.Focus.OutOfFocus -> UIState.Focus.InFocus
                }
            }
            .bringIntoViewRequester(bringIntoInputViewRequester),
        onValueChange = { newValue ->
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = newValue,
                    states = listOf(focusState, data.validation),
                    optionalId = data.id
                )
            )
        },
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = getColorForInput(
                data.isEnabled,
                focusState,
                data.validation
            )
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction, keyboardType = data.keyboardType
        ),
        keyboardActions = KeyboardActions(onNext = {
            localFocusManager.moveFocus(FocusDirection.Next)
        }, onDone = {
            keyboardController?.let {
                it.hide()
                localFocusManager.clearFocus()
            }
        }),
        singleLine = true,
        cursorBrush = SolidColor(
            getColorForInput(
                data.isEnabled,
                focusState,
                data.validation
            )
        ),
        decorationBox = @Composable { innerTextField ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                data.label?.let {
                    Text(
                        text = data.label,
                        style = DiiaTextStyle.t4TextSmallDescription,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    if (data.inputValue.isNullOrEmpty()) {
                        Text(
                            text = data.placeholder ?: "",
                            style = DiiaTextStyle.t1BigText,
                            color = getColorForPlaceholder(
                                focusState = focusState,
                                validationState = data.validation
                            )
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
                                inputValue = data.inputValue,
                                focusState = focusState,
                                validationState = data.validation
                            )
                        )
                )
                AnimatedVisibility(
                    data.validation == UIState.Validation.Failed && (focusState != UIState.Focus.NeverBeenFocused
                            && focusState != UIState.Focus.FirstTimeInFocus)
                ) {
                    data.errorMessage?.let { errorMsg ->
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .bringIntoViewRequester(
                                    bringIntoErrorViewRequester
                                ),
                            text = errorMsg,
                            style = DiiaTextStyle.t4TextSmallDescription,
                            color = Red
                        )
                    }
                }

                AnimatedVisibility(
                    data.validation != UIState.Validation.Failed && focusState == UIState.Focus.FirstTimeInFocus
                ) {
                    data.hintMessage?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .bringIntoViewRequester(
                                    bringIntoHintViewRequester
                                ),
                            text = data.hintMessage,
                            style = DiiaTextStyle.t4TextSmallDescription,
                            color = BlackAlpha30
                        )
                    }
                }
            }
        }
    )
}


private fun getColorForPlaceholder(
    focusState: UIState.Focus,
    validationState: UIState.Validation
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused, UIState.Focus.FirstTimeInFocus -> BlackAlpha30
        UIState.Focus.InFocus, UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> BlackAlpha30
            }

        }
    }
}

private fun getColorForBottomLine(
    inputValue: String?,
    focusState: UIState.Focus,
    validationState: UIState.Validation
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused -> BlackAlpha30
        UIState.Focus.FirstTimeInFocus -> Black
        UIState.Focus.InFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> Black
            }
        }

        UIState.Focus.OutOfFocus -> {
            when (validationState) {
                UIState.Validation.Failed -> Red
                else -> if (inputValue.isNullOrEmpty()) BlackAlpha30 else Black
            }
        }
    }
}

private fun getColorForInput(
    isEnabled: Boolean,
    focusState: UIState.Focus,
    validationState: UIState.Validation
): Color {
    return if (isEnabled) {
        when (focusState) {
            UIState.Focus.NeverBeenFocused, UIState.Focus.FirstTimeInFocus -> Black
            UIState.Focus.InFocus, UIState.Focus.OutOfFocus -> {
                when (validationState) {
                    UIState.Validation.Failed -> Red
                    else -> Black
                }
            }
        }
    } else {
        BlackAlpha30
    }
}


data class TextInputMoleculeData(
    val actionKey: String = UIActionKeysCompose.TEXT_INPUT,
    val id: String? = null,
    val label: String? = null,
    val inputValue: String? = null,
    val placeholder: String? = null,
    val hintMessage: String? = null,
    var errorMessage: String? = null,
    val validationData: List<ValidationTextItem>? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val isEnabled: Boolean = true
) : InputFormItem() {
    data class ValidationTextItem(
        val regex: String,
        val flags: List<String>,
        val errorMessage: String
    )

    fun onInputChanged(newValue: String?): TextInputMoleculeData {
        if (newValue == null) return this

        val numRegex = "^[+,0-9]"
        val newValidationValue = if (this.id == "phoneNumber") {
            newValue.filter { n -> numRegex.toRegex().matches(n.toString()) }
        } else {
            newValue
        }

        return this.copy(
            inputValue = newValidationValue,
            validation = when (this.id) {
                "phoneNumber" -> {
                    dataValidation(newValidationValue)
                }

                "email" -> {
                    dataValidationEmail(newValue)
                }

                else -> {
                    if (newValue.matches(
                            Regex(
                                this.validationData?.first()?.regex ?: ".*"
                            )
                        )
                    ) {
                        UIState.Validation.Passed
                    } else {
                        UIState.Validation.Failed
                    }
                }
            }
        )
    }

    private fun dataValidation(value: String): UIState.Validation {
        var isMatches: Boolean? = null
        this.validationData?.forEach {
            if (value.matches(Regex(it.regex))) {
                isMatches = true
            } else {
                isMatches = false
                this.errorMessage = it.errorMessage
                return@forEach
            }
        }
        return if (value.isEmpty()) {
            UIState.Validation.NeverBeenPerformed
        } else if (isMatches != null && isMatches == true) {
            UIState.Validation.Passed
        } else {
            UIState.Validation.Failed
        }
    }

    private fun dataValidationEmail(value: String): UIState.Validation {
        val regex1 = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        val regex2 = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(ru|su)\$"
        var isMatches: Boolean? = null

        if (value.matches(Regex(regex1))) {
            if (value.matches(Regex(regex2))) {
                isMatches = false
                this.errorMessage = validationData!![1].errorMessage
            } else {
                isMatches = true
            }
        } else {
            isMatches = false
            this.errorMessage = validationData!![0].errorMessage
        }

        return if (value.isEmpty()) {
            UIState.Validation.NeverBeenPerformed
        } else if (isMatches == true) {
            UIState.Validation.Passed
        } else {
            UIState.Validation.Failed
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun TextInputMoleculePreview() {

    val PHONE_NUMBER_VALIDATION_PATTERN =
        "^38(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{7}\$"

    val data = TextInputMoleculeData(
        id = "",
        label = LoremIpsum(6).values.joinToString(),
        inputValue = "",
        placeholder = "Placeholder",
        hintMessage = LoremIpsum(50).values.joinToString(),
        validationData = listOf(
            TextInputMoleculeData.ValidationTextItem(
                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                flags = listOf(),
                errorMessage = LoremIpsum(40).values.joinToString()
            )
        ),
        validation = UIState.Validation.NeverBeenPerformed
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val state = remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextInputMolecule(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                state.value = state.value.onInputChanged(it.data)
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
fun TextInputMoleculePreview_Prefilled() {

    val PHONE_NUMBER_VALIDATION_PATTERN =
        "^38(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{7}\$"

    val data = TextInputMoleculeData(
        id = "",
        label = LoremIpsum(6).values.joinToString(),
        inputValue = "123456",
        placeholder = "Placeholder",
        hintMessage = LoremIpsum(50).values.joinToString(),
        validationData = listOf(
            TextInputMoleculeData.ValidationTextItem(
                regex = PHONE_NUMBER_VALIDATION_PATTERN,
                flags = listOf(),
                errorMessage = LoremIpsum(40).values.joinToString()
            )
        ),
        keyboardType = KeyboardType.Number,
        validation = UIState.Validation.NeverBeenPerformed
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val state = remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextInputMolecule(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                state.value = state.value.onInputChanged(it.data)
            })

        Button(modifier = Modifier.padding(bottom = 16.dp), onClick = {
            focusManager.clearFocus()
        }) {
            Text("Click to remove focus from search")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun InputWithScroll_Preview() {
    val EMAIL_VALIDATION_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    val PHONE_NUMBER_VALIDATION_PATTERN =
        "^\\+?38(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{7}\$"

    val toolbarData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            TopGroupOrgData(
                navigationPanelMlcData = NavigationPanelMlcData(
                    title = UiText.DynamicString("Title"),
                    isContextMenuExist = true
                )
            )
        )

    val bodyData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            TitleLabelMlcData(label = "Some title"),
            AttentionMessageMlcData(
                icon = "",
                title = "Title",
                description = TextWithParametersData(
                    text = UiText.DynamicString(LoremIpsum(1).values.joinToString()),
                    parameters = emptyList()
                )
            ),
            QuestionFormsOrgData(
                title = null,
                items = SnapshotStateList<InputFormItem>().apply {
                    add(
                        TextInputMoleculeData(
                            id = "phone",
                            label = "label",
                            placeholder = "hint",
                            inputValue = "",
                            validationData = listOf(
                                TextInputMoleculeData.ValidationTextItem(
                                    regex = PHONE_NUMBER_VALIDATION_PATTERN,
                                    flags = listOf(),
                                    errorMessage = "error",
                                )
                            ),
                            keyboardType = KeyboardType.Number
                        )
                    )
                    add(
                        TextInputMoleculeData(
                            id = "email",
                            label = "label2",
                            placeholder = "hint2",
                            inputValue = "",
                            validationData = listOf(
                                TextInputMoleculeData.ValidationTextItem(
                                    regex = EMAIL_VALIDATION_PATTERN,
                                    flags = listOf(),
                                    errorMessage = "error",
                                )
                            ),
                            keyboardType = KeyboardType.Text
                        )
                    )
                }
            )

        )

    val bottomData: SnapshotStateList<UIElementData> =
        SnapshotStateList<UIElementData>().addAllIfNotNull(
            BottomGroupOrgData(
                primaryButton = BtnPrimaryDefaultAtmData(
                    actionKey = "123",
                    title = UiText.DynamicString("title"),
                    interactionState = UIState.Interaction.Enabled
                )
            )
        )

    fun onNewValue(id: String?, data: String?) {
        if (data == null) return
        val index = bodyData.indexOfFirst { it is QuestionFormsOrgData }

        bodyData.findAndChangeFirstByInstance<QuestionFormsOrgData> {
            it.onInputChanged(id = id, newValue = data)
        }

        bottomData.findAndChangeFirstByInstance<BottomGroupOrgData> { item ->
            if (index != -1) {
                (bodyData[index] as QuestionFormsOrgData).isFormFilledAndValid()
                    .let {
                        item.activateButtonsIgnoreCheckbox(it)
                    }
            } else {
                item
            }
        }
    }

    PublicServiceScreen(
        toolbar = toolbarData,
        body = bodyData,
        bottom = bottomData,
        onEvent = {
            onNewValue(it.optionalId, it.data)
        },
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    )
}
