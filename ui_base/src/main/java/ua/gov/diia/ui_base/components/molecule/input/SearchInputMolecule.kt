package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.AzureishWhite
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun SearchInputMolecule(
    modifier: Modifier = Modifier,
    data: SearchInputMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = modifier
            .height(
                height = 40.dp
            )
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
        value = data.searchFieldValue ?: "",
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
        interactionSource = interactionSource,
        visualTransformation = VisualTransformation.None,
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = White, shape = RoundedCornerShape(size = 8.dp))
                    .composed {
                        data.contentDescription?.asString()?.let { desc ->
                            semantics(true) {
                                contentDescription = desc
                            }
                        }
                        this
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                val outOfFocus = (focusState == UIState.Focus.OutOfFocus || focusState == UIState.Focus.NeverBeenFocused)
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
                    if (data.searchFieldValue.isNullOrEmpty() && outOfFocus) {
                        data.placeholder?.let {
                            Text(
                                text = data.placeholder.asString(),
                                style = DiiaTextStyle.t3TextBody,
                                color = BlackAlpha30
                            )
                        }
                    }
                    innerTextField()
                }

                if (!data.searchFieldValue.isNullOrEmpty()) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 11.dp)
                            .size(15.dp)
                            .clickable {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = ""
                                    )
                                )
                            },
                        painter = painterResource(id = R.drawable.diia_close_rounded_icon),
                        contentDescription = stringResource(R.string.clean_search_field),
                        tint = BlackAlpha30
                    )
                }
            }
        }
    )
}


data class SearchInputMoleculeData(
    val actionKey: String = UIActionKeysCompose.SEARCH_INPUT,
    val id: String? = null,
    val searchFieldValue: String? = null,
    val placeholder: UiText? = null,
    val contentDescription: UiText? = null,
) : UIElementData

@Composable
@Preview
fun SearchInputMoleculePreview() {
    val state = SearchInputMoleculeData(
        id = "",
        searchFieldValue = "",
        placeholder = UiText.DynamicString("Search keyword")
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzureishWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputMolecule(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
            data = state,
            onUIAction = {
                //
            })


        Spacer(modifier = Modifier.height(height = 20.dp))

        Button(modifier = Modifier.padding(bottom = 16.dp),
            onClick = {
                focusManager.clearFocus()
            }) {
            Text("Click to remove focus from search")
        }
    }
}
