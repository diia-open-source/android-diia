package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.AzureishWhite
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun SearchInputV2(
    modifier: Modifier = Modifier,
    data: SearchInputV2Data,
    focused: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember {
        FocusRequester()
    }


    LaunchedEffect(Unit) {
        if (data.mode == Mode.EDITABLE.value && focused) {
            focusRequester.requestFocus()
        }
    }

    when (data.mode) {
        Mode.EDITABLE.value -> {
            BasicTextField(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp)
                    .height(height = 40.dp)
                    .focusable(true)
                    .focusRequester(focusRequester)
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
                value = data.searchFieldValue?.asString() ?: "",
                onValueChange = {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = it
                        )
                    )
                },
                enabled = true,
                readOnly = false,
                textStyle = DiiaTextStyle.t3TextBody,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                decorationBox = @Composable { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = White, shape = RoundedCornerShape(size = 16.dp))
                            .composed {
                                data.contentDescription
                                    ?.asString()
                                    ?.let { desc ->
                                        semantics(true) {
                                            contentDescription = desc
                                        }
                                    }
                                this
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val outOfFocus =
                            (focusState == UIState.Focus.OutOfFocus || focusState == UIState.Focus.NeverBeenFocused)
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 11.dp)
                                .size(width = 18.dp, height = 17.dp)
                                .alpha(if (outOfFocus) 0.3f else 1.0f),
                            painter = painterResource(id = R.drawable.diia_search_icon),
                            contentDescription = null,
                            tint = Black
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            if (data.searchFieldValue?.asString().isNullOrEmpty()) {
                                data.placeholder?.asString()?.let {
                                    Text(
                                        text = data.placeholder.asString(),
                                        style = DiiaTextStyle.t3TextBody,
                                        color = BlackAlpha30
                                    )
                                }
                            }
                            innerTextField()
                        }

                        if (!data.searchFieldValue?.asString().isNullOrEmpty()) {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .size(24.dp)
                                    .clickable {
                                        onUIAction(
                                            UIAction(
                                                actionKey = data.actionKey,
                                                data = ""
                                            )
                                        )
                                    },
                                painter = painterResource(id = R.drawable.ic_input_clear),
                                contentDescription = stringResource(R.string.clean_search_field)
                            )
                        }
                    }
                }
            )
        }

        Mode.BUTTON.value -> {
            Box(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp)
                    .height(40.dp)
                    .fillMaxWidth()
                    .background(color = White, shape = RoundedCornerShape(size = 16.dp))
                    .noRippleClickable {
                        onUIAction(UIAction(actionKey = data.actionKey))
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 11.dp)
                            .size(width = 18.dp, height = 17.dp),
                        painter = painterResource(id = R.drawable.diia_search_icon),
                        contentDescription = null,
                        tint = BlackAlpha30
                    )

                    if (data.searchFieldValue?.asString().isNullOrEmpty()) {
                        Text(
                            text = data.placeholder?.asString() ?: "",
                            style = DiiaTextStyle.t3TextBody,
                            color = BlackAlpha30,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Text(
                            text = data.searchFieldValue?.asString() ?: "",
                            style = DiiaTextStyle.t3TextBody,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }


}


data class SearchInputV2Data(
    val actionKey: String = UIActionKeysCompose.SEARCH_INPUT,
    val id: String? = null,
    val searchFieldValue: UiText? = null,
    val placeholder: UiText? = null,
    val mode: Int = Mode.EDITABLE.value,
    val contentDescription: UiText? = null,
) : UIElementData {
    fun onChange(newValue: String?): SearchInputV2Data {
        return this.copy(
            searchFieldValue = UiText.DynamicString(newValue ?: "")
        )
    }
}

@Composable
@Preview
fun SearchInputMoleculeV2PreviewEditable() {
    val data = SearchInputV2Data(
        id = "",
        searchFieldValue = UiText.DynamicString(""),
        placeholder = UiText.DynamicString("Search keyword")
    )
    val state = remember {
        mutableStateOf(data)
    }
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzureishWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputV2(modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                (UIAction(actionKey = data.actionKey))
            })
    }
}

@Composable
@Preview
fun SearchInputMoleculeV2PreviewButton() {
    val data = SearchInputV2Data(
        id = "",
        searchFieldValue = UiText.DynamicString(""),
        placeholder = UiText.DynamicString("Search keyword"),
        mode = Mode.BUTTON.value
    )
    val state = remember {
        mutableStateOf(data)
    }
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzureishWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputV2(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                state.value = state.value.onChange(it.data)
            })
    }
}

enum class Mode(val value: Int) {
    EDITABLE(0), BUTTON(1)
}
