package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.input.SearchInputMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.clearFocusOnKeyboardDismiss
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.AzureishWhite
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun SearchInputMlc(
    modifier: Modifier = Modifier,
    data: SearchInputMlcData,
    focused: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember {
        FocusRequester()
    }


    LaunchedEffect(Unit) {
        if (data.mode == ModeSearchInput.EDITABLE.value && focused) {
            focusRequester.requestFocus()
        }
    }

    when (data.mode) {
        ModeSearchInput.EDITABLE.value -> {
            BasicTextField(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp)
                    .height(height = 40.dp)
                    .focusable(true)
                    .focusRequester(focusRequester)
                    .clearFocusOnKeyboardDismiss()
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
                    }
                    .testTag(data.componentId?.asString() ?: ""),
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
                        data.iconLeft?.let {
                            UiIconWrapperSubatomic(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(24.dp),
                                icon = it
                            )
                        }

                        Box(modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)) {
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
                            UiIconWrapperSubatomic(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(24.dp)
                                    .noRippleClickable {
                                        onUIAction(
                                            UIAction(
                                                actionKey = data.actionKey,
                                                data = ""
                                            )
                                        )
                                    },
                                icon = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code),
                                contentDescription = UiText.StringResource(R.string.clean_search_field),
                            )
                        }
                    }
                },
            )
        }
    }
}

data class SearchInputMlcData(
    val actionKey: String = UIActionKeysCompose.SEARCH_INPUT,
    val id: String? = null,
    val searchFieldValue: UiText? = null,
    val placeholder: UiText? = null,
    val mode: Int = ModeSearchInput.EDITABLE.value,
    val iconLeft: UiIcon? = null,
    val iconRight: UiIcon? = null,
    val contentDescription: UiText? = null,
    val componentId: UiText? = null,
) : UIElementData {

    fun onChange(newValue: String?): SearchInputMlcData {
        return this.copy(
            searchFieldValue = UiText.DynamicString(newValue ?: "")
        )
    }
}

fun SearchInputMlc.toUiModel(): SearchInputMlcData {
    return SearchInputMlcData(
        id = "searchInputMlc",
        placeholder = UiText.DynamicString(this.label),
        iconLeft = this.iconLeft?.let { icon ->
            UiIcon.DrawableResource(code = icon.code)
        },
        iconRight = this.iconRight?.let { icon ->
            UiIcon.DrawableResource(code = icon.code)
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable() {
    val data = SearchInputMlcData(
        id = "",
        searchFieldValue = UiText.DynamicString(""),
        placeholder = UiText.DynamicString("Search keyword"),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
    )
    val state = remember {
        mutableStateOf(data)
    }
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .background(AzureishWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputMlc(modifier = Modifier
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
fun SearchInputMlcPreviewEditableWithText() {
    val data = SearchInputMlcData(
        id = "",
        searchFieldValue = UiText.DynamicString("Text"),
        placeholder = UiText.DynamicString("Search keyword"),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
    )
    val state = remember {
        mutableStateOf(data)
    }
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .background(AzureishWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputMlc(modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state.value,
            onUIAction = {
                (UIAction(actionKey = data.actionKey))
            })
    }
}

enum class ModeSearchInput(val value: Int) {
    EDITABLE(0), BUTTON(1)
}
