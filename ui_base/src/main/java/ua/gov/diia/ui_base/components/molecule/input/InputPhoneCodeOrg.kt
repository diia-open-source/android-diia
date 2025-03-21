package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.input.Validation
import ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrg
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.White
import java.util.regex.Pattern

@Composable
fun InputPhoneCodeOrg(
    modifier: Modifier = Modifier,
    data: InputPhoneCodeOrgData,
    imeAction: ImeAction = ImeAction.Done,
    localFocusManager: FocusManager = LocalFocusManager.current,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onUIAction: (UIAction) -> Unit
) {
    val localDensity = LocalDensity.current
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    var justFocused by remember { mutableStateOf(false) }
    var phoneCodeWidth by remember {
        mutableStateOf(0.dp)
    }
    var inputValue = data.inputPhoneMlc.value?.asString()
    var dataValidationState by remember { mutableStateOf(data.validation) }
    val errorBlockVisible by remember {
        derivedStateOf {
            ((focusState == UIState.Focus.OutOfFocus) && dataValidationState == UIState.Validation.Failed)
                    || (focusState == UIState.Focus.InFocus && dataValidationState == UIState.Validation.Failed && justFocused)
        }
    }
    LaunchedEffect(key1 = data.validation) {
        dataValidationState = data.validation
    }

    Column(modifier = modifier) {
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
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .onGloballyPositioned { coordinates ->
                            phoneCodeWidth = with(localDensity) { coordinates.size.width.toDp() }
                        }
                        .noRippleClickable {
                            if (data.codeValueIsEditable != false) {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        action = DataActionWrapper(type = UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE)
                                    )
                                )
                            }
                        }

                ) {
                    data.codeIcon?.asString()?.let {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = "${data.codeIcon.asString()} ",
                            style = DiiaTextStyle.t1BigText
                        )
                    }
                    val codeLabel =
                        data.codes.firstOrNull { it.id == data.codeValueId }?.label ?: ""
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = codeLabel,
                        style = DiiaTextStyle.t1BigText,
                        color = Black
                    )
                    Icon(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(
                            id = R.drawable.ic_arrow_next
                        ),
                        contentDescription = stringResource(R.string.details),
                        tint = if (data.codeValueIsEditable != false) {
                            Black
                        } else {
                            BlackAlpha30
                        },
                    )
                }
                Box(
                    modifier = Modifier
                        .width(phoneCodeWidth)
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
            }
            InputPhoneMlc(
                modifier = Modifier
                    .onFocusChanged {
                        focusState = when (focusState) {
                            UIState.Focus.NeverBeenFocused -> {
                                if (inputValue == null) {
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
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 12.dp),
                disableErrorMessageDisplaying = true,
                imeAction = imeAction,
                localFocusManager = localFocusManager,
                keyboardController = keyboardController,
                data = data.inputPhoneMlc
            ) { uiAction ->
                justFocused = false
                onUIAction(uiAction)
            }
        }
        AnimatedVisibility(visible = errorBlockVisible) {
            if (!data.errorMessage?.asString().isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = data.errorMessage?.asString() ?: "",
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = Red
                )
            }
        }

        AnimatedVisibility(visible = !errorBlockVisible) {
            data.hint?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = data.hint.asString(),
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = BlackAlpha30
                )
            }
        }
    }

}

data class InputPhoneCodeOrgData(
    val actionKey: String = UIActionKeysCompose.INPUT_PHONE_CODE_ORG,
    val componentId: UiText? = null,
    val label: UiText?,
    val hint: UiText? = null,
    val mandatory: Boolean? = null,
    val inputCode: UiText? = null,
    val inputPhoneMlc: InputPhoneMlcData,
    val codeValueId: String? = null,
    val codeValue: UiText? = null,
    val codeIcon: UiText? = null,
    val codeValueIsEditable: Boolean? = null,
    val validation: UIState.Validation = UIState.Validation.NeverBeenPerformed,
    val errorMessage: UiText? = null,
    val codes: List<Code> = emptyList(),
    val isEnabled: Boolean = true
) : InputFormItem() {
    fun onInputChanged(newValue: String?): InputPhoneCodeOrgData {
        val inputPhoneMlcUpdate = this.inputPhoneMlc.onInputChanged(newValue)
        val selectedCode = this.codes.find { it.id == this.codeValueId }
        val validationRules = selectedCode?.validationRules ?: emptyList()
        val validationResult = if(newValue.isNullOrEmpty()) {
            UIState.Validation.Passed to null
        } else {
            dataValidation(
                value = (codeValue as UiText.DynamicString).value + newValue,
                validationRules = validationRules,
                previousValueIsEmpty = false
            )
        }

        return this.copy(
            validation = validationResult.first,
            inputPhoneMlc = inputPhoneMlcUpdate.copy(validation = validationResult.first),
            errorMessage = validationResult.second
        )
    }

    fun onCountryCodeChanged(newCodeId: String): InputPhoneCodeOrgData {
        val selectedCode = this.codes.find { it.id == newCodeId }
        val inputPhoneMlcUpdate = this.inputPhoneMlc.copy(
            value = UiText.DynamicString(""),
            validationRules = selectedCode?.validationRules ?: emptyList(),
            mask = selectedCode?.maskCode.toDynamicStringOrNull(),
            placeholder = selectedCode?.placeholder?.toDynamicStringOrNull(),
            validation = UIState.Validation.NeverBeenPerformed
        )
        return this.copy(
            codeValueId = newCodeId,
            codeValue = selectedCode?.value?.toDynamicStringOrNull(),
            codeIcon = selectedCode?.icon?.toDynamicStringOrNull(),
            validation = UIState.Validation.NeverBeenPerformed,
            inputPhoneMlc = inputPhoneMlcUpdate
        )
    }

    fun getFullNumber(): String {
        return "${(this.codeValue as UiText.DynamicString).value}${(this.inputPhoneMlc.value as UiText.DynamicString).value}"
    }
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

fun InputPhoneCodeOrg.toUIModel(): InputPhoneCodeOrgData {
    val selectedCodeEntity = this.codes?.firstOrNull {
        it.id == this.codeValueId
    }
    return InputPhoneCodeOrgData(
        componentId = this.componentId.orEmpty().toDynamicString(),
        label = this.label.let {
            it.toDynamicString()
        },
        hint = this.hint?.let {
            it.toDynamicString()
        },
        mandatory = this.mandatory,
        inputCode = this.inputCode?.let {
            it.toDynamicString()
        },
        inputPhoneMlc = InputPhoneMlcData(
            componentId = this.inputPhoneMlc.componentId.orEmpty().toDynamicString(),
            inputCode = this.inputPhoneMlc.inputCode?.let {
                it.toDynamicString()
            },
            label = null,
            placeholder = selectedCodeEntity?.placeholder?.toDynamicStringOrNull()
                ?: this.inputPhoneMlc.placeholder?.let {
                    it.toDynamicString()
                },
            hint = null,
            mask = selectedCodeEntity?.maskCode?.toDynamicStringOrNull(),
            //todo need change
            value = this.inputPhoneMlc.value?.let {
                if (it.length >= 9) {
                    it.take(9).toDynamicString()
                } else {
                    this.inputPhoneMlc.value.toDynamicString()
                }
            },
            mandatory = this.mandatory,
            validation = UIState.Validation.NeverBeenPerformed,
            validationRules = emptyList(),
            isEnabled = true
        ),
        codeValue = selectedCodeEntity?.value.toDynamicStringOrNull() ?: "".toDynamicString(),
        codeValueId = this.codeValueId,
        codeIcon = selectedCodeEntity?.icon?.toDynamicStringOrNull(),
        codeValueIsEditable = this.codeValueIsEditable,
        codes = this.codes?.map {
            Code(
                id = it.id,
                maskCode = it.maskCode,
                placeholder = it.placeholder,
                label = it.label,
                description = it.description,
                value = it.value,
                icon = it.icon,
                validationRules = mutableListOf<ValidationRule>().apply {
                    it.validation?.forEach { validationItem ->
                        add(
                            ValidationRule(
                                regexp = validationItem.regexp,
                                flags = validationItem.flags,
                                errorMessage = validationItem.errorMessage.toDynamicString()
                            )
                        )
                    }
                },
            )
        } ?: emptyList(),
        validation = UIState.Validation.NeverBeenPerformed,
        isEnabled = true
    )
}

data class Code(
    val id: String,
    val maskCode: String?,
    val placeholder: String?,
    val label: String? = null,
    val description: String,
    val value: String,
    val icon: String,
    val validationRules: List<ValidationRule>? = null,
)

private fun getColorForBottomLine(
    focusState: UIState.Focus,
    validationState: UIState.Validation,
    justFocused: Boolean
): Color {
    return when (focusState) {
        UIState.Focus.NeverBeenFocused -> BlackAlpha30
        UIState.Focus.FirstTimeInFocus -> BlackAlpha30
        UIState.Focus.InFocus -> {
            when (validationState) {
                UIState.Validation.Failed ->
                    if (justFocused) Red else BlackAlpha30

                else -> BlackAlpha30
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

@Preview
@Composable
fun InputPhoneCodeOrgPreview_CodeValueEditDisabled() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrg(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlc = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = "998887766",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = false,
        hint = null,
        codes = listOf(
            InputPhoneCodeOrg.Code(
                id = "UA",
                maskCode = "## ### ####",
                placeholder = "00 000 0000",
                label = "+380",
                description = "🇺🇦 Україна (+380)",
                value = "380",
                icon = "🇺🇦",
                validation = listOf(
                    Validation(
                        regexp = "^380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$",
                        flags = listOf("i", "g"),
                        errorMessage =
                        "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ."
                    )
                )
            )
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    InputPhoneCodeOrg(
        modifier = Modifier,
        data = state.value,
        localFocusManager = focusManager,
        onUIAction = {
            when (it.action?.type) {
                UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                    //navigation to search
                }

                UIActionKeysCompose.INPUT_PHONE_MLC -> {
                    it.action.let { action ->
                        state.value = state.value.onInputChanged(action.resource)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun InputPhoneCodeOrgPreview_CodeValueEmpty() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrg(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlc = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = "",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = false,
        hint = null,
        codes = listOf(
            InputPhoneCodeOrg.Code(
                id = "UA",
                maskCode = "## ### ####",
                placeholder = "00 000 0000",
                label = "+380",
                description = "🇺🇦 Україна (+380)",
                value = "380",
                icon = "🇺🇦",
                validation = listOf(
                    Validation(
                        regexp = "^380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$",
                        flags = listOf("i", "g"),
                        errorMessage =
                        "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ."
                    )
                )
            )
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    InputPhoneCodeOrg(
        modifier = Modifier,
        data = state.value,
        localFocusManager = focusManager,
        onUIAction = {
            when (it.action?.type) {
                UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                    //navigation to search
                }

                UIActionKeysCompose.INPUT_PHONE_MLC -> {
                    it.action.let { action ->
                        state.value = state.value.onInputChanged(action.resource)
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun PhoneOrg_FromBackValid() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrg(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlc = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = "998887766",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = true,
        hint = null,
        codes = listOf(
            InputPhoneCodeOrg.Code(
                id = "UA",
                maskCode = "## ### ####",
                placeholder = "00 000 0000",
                label = "+380",
                description = "🇺🇦 Україна (+380)",
                value = "380",
                icon = "🇺🇦",
                validation = listOf(
                    Validation(
                        regexp = "^380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$",
                        flags = listOf("i", "g"),
                        errorMessage =
                        "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ."
                    )
                )
            )
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    InputPhoneCodeOrg(
        modifier = Modifier,
        data = state.value,
        localFocusManager = focusManager,
        onUIAction = {
            when (it.action?.type) {
                UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                    //navigation to search
                }

                UIActionKeysCompose.INPUT_PHONE_MLC -> {
                    it.action.let { action ->
                        state.value = state.value.onInputChanged(action.resource)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PhoneOrg_FromBackValid_WithButtons() {
    val focusManager = LocalFocusManager.current
    val serverData = InputPhoneCodeOrg(
        label = "Контактний номер телефону",
        componentId = "phone_with_code",
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlc = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc(
            inputCode = null,
            componentId = "phone",
            validation = emptyList(),
            value = "998887766",
            hint = null,
            label = null,
            mandatory = null,
            mask = null,
            placeholder = null
        ),
        codeValueId = "UA",
        codeValueIsEditable = true,
        hint = null,
        codes = listOf(
            InputPhoneCodeOrg.Code(
                id = "UA",
                maskCode = "## ### ####",
                placeholder = "00 000 0000",
                label = "+380",
                description = "🇺🇦 Україна (+380)",
                value = "380",
                icon = "🇺🇦",
                validation = listOf(
                    Validation(
                        regexp = "^380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$",
                        flags = listOf("i", "g"),
                        errorMessage =
                        "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ."
                    )
                )
            ),
            InputPhoneCodeOrg.Code(
                id = "TEST",
                maskCode = "### ### ### ###",
                placeholder = "987 654 321 000",
                label = "+123",
                description = "+123 Тестовий код",
                value = "123",
                icon = "\uD83C\uDDE6\uD83C\uDDF8",
                validation = listOf(
                    Validation(
                        regexp = "^123[0-9]{12}",
                        flags = listOf("i"),
                        errorMessage = "Номер телефону має відповідати формату ### ### ### ###"
                    )
                )
            ),
        )
    )
    val mappedValue = serverData.toUIModel()
    val state = remember {
        mutableStateOf(mappedValue)
    }
    Column {
        InputPhoneCodeOrg(
            modifier = Modifier,
            data = state.value,
            localFocusManager = focusManager,
            onUIAction = {
                when (it.action?.type) {
                    UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                        //navigation to search
                    }

                    UIActionKeysCompose.INPUT_PHONE_MLC -> {
                        it.action.let { action ->
                            state.value = state.value.onInputChanged(action.resource)
                        }
                    }
                }
            }
        )

        Button(onClick = {
            state.value = state.value.onCountryCodeChanged("TEST")
        }) {
            Text(color = White, text = "Change country code id to Test")
        }
        Button(onClick = {
            state.value = state.value.onCountryCodeChanged("UA")
        }) {
            Text(color = White, text = "Change country code id to Ukraine")
        }
        Button(onClick = {
            println("Data = ${state.value.getFullNumber()}")
        }) {
            Text(color = White, text = "Get full number")
        }
    }
}

@Preview
@Composable
fun PhoneOrg_FromBackExample() {
//    "inputPhoneCodeOrg": {
//        "componentId": "phone_with_code",
//        "label": "Контактний номер телефону",
//        "mandatory": true,
//        "inputCode": "phone",
//        "inputPhoneMlc": {
//        "componentId": "phone"
//    },
//        "codeValueIsEditable": false,
//        "codeValueId": "ua",
//        "codes": [
//        {
//            "id": "ua",
//            "maskCode": "## ### ####",
//            "placeholder": "00 000 0000",
//            "label": "+380",
//            "value": "380",
//            "icon": "🇺🇦",
//            "description": "🇺🇦 Україна (+380)",
//            "validation": [
//            {
//                "regexp": "^\\380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$",
//                "flags": [
//                "i",
//                "g"
//                ],
//                "errorMessage": "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ."
//            }
//            ]
//        }
//        ]
//    }
//},
//}
//]
    val serverInputPhoneMlcData = ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc(
        componentId = "phone",
        inputCode = null,
        label = null,
        placeholder = null,
        hint = null,
        mask = null,
        value = null,
        mandatory = null,
        validation = null
    )
    val validationRule = Validation(
        errorMessage = "Упс, ви ввели неправильний номер телефону. Використовуйте формат + 38 (0ХХ) ХХХ ХХ ХХ.",
        flags = listOf("i", "g"),
        regexp = "^380(39|50|63|66|67|68|73|91|92|93|94|95|96|97|98|99)\\d{7}$"
    )
    val codes = listOf(
        ua.gov.diia.core.models.common_compose.org.input.InputPhoneCodeOrg.Code(
            id = "ua",
            maskCode = "## ### ####",
            placeholder = "00 000 0000",
            label = "+380",
            description = "380",
            value = "380",
            icon = "🇺🇦",
            validation = listOf(validationRule),
        )
    )
    val serverPhoneOrg = InputPhoneCodeOrg(
        componentId = "phone_with_code",
        label = "Контактний номер телефону",
        hint = null,
        mandatory = true,
        inputCode = "phone",
        inputPhoneMlc = serverInputPhoneMlcData,
        codeValueId = "ua",
        codeValueIsEditable = false,
        codes = codes
    )
    val mappedValue = serverPhoneOrg.toUIModel()
    val focusManager = LocalFocusManager.current
    val state = remember {
        mutableStateOf(mappedValue)
    }
    Column {
        InputPhoneCodeOrg(
            modifier = Modifier,
            data = state.value,
            localFocusManager = focusManager,
            onUIAction = {
                when (it.action?.type) {
                    UIActionKeysCompose.INPUT_PHONE_CODE_ORG_COUNTRY_CODE -> {
                        //navigation to search
                    }

                    UIActionKeysCompose.INPUT_PHONE_MLC -> {
                        it.action.let { action ->
                            state.value = state.value.onInputChanged(action.resource)
                        }
                    }
                }
            }
        )

        Button(onClick = {
            state.value = state.value.onCountryCodeChanged("TEST")
        }) {
            Text(color = White, text = "Change country code id to Test")
        }
        Button(onClick = {
            state.value = state.value.onCountryCodeChanged("UA")
        }) {
            Text(color = White, text = "Change country code id to Ukraine")
        }
        Button(onClick = {
            println("Data = ${state.value.getFullNumber()}")
        }) {
            Text(color = White, text = "Get full number")
        }
    }
}