package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.message.AttentionMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlcData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrgDataLocal
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun SelectorOrg(
    modifier: Modifier = Modifier,
    data: SelectorOrgData,
    onUIAction: (UIAction) -> Unit
) {
    var focusState by remember { mutableStateOf<UIState.Focus>(UIState.Focus.NeverBeenFocused) }
    val bringIntoHintViewRequester = BringIntoViewRequester()


    BasicTextField(value = data.inputValue ?: "",
        enabled = false,
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
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        states = listOf(focusState),
                        optionalId = data.id
                    )
                )
            },
        onValueChange = {},
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = Black
        ),
        singleLine = true,
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (data.inputValue.isNullOrEmpty()) {
                        Text(
                            text = data.placeholder ?: "",
                            style = DiiaTextStyle.t1BigText,
                            color = BlackAlpha30
                        )
                    }
                    innerTextField()
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(R.drawable.ic_arrow_next),
                        contentDescription = null
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .height(2.dp)
                        .background(getColorForBottomLine(data = data))
                )

                AnimatedVisibility(focusState == UIState.Focus.FirstTimeInFocus) {
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
        })
}

private fun getColorForBottomLine(
    data: SelectorOrgData,
): Color {
    return if (data.inputValue.isNullOrEmpty()) BlackAlpha30 else Black
}


data class SelectorOrgData(
    val actionKey: String = UIActionKeysCompose.SELECTOR_ORG,
    val id: String? = null,
    val label: String? = null,
    val inputValue: String? = null,
    val placeholder: String? = null,
    val hintMessage: String? = null
) : UIElementData {
    fun onInputChanged(newValue: String?): SelectorOrgData {
        if (newValue == null) return this
        return this.copy(inputValue = newValue)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun SelectorOrgPreview() {

    val data = SelectorOrgData(
        id = "",
        label = LoremIpsum(6).values.joinToString(),
        inputValue = "",
        placeholder = "Placeholder",
        hintMessage = LoremIpsum(50).values.joinToString(),
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

        SelectorOrg(modifier = Modifier
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
fun SelectorOrgPreview_Prefilled() {

    val data = SelectorOrgData(
        id = "",
        label = LoremIpsum(6).values.joinToString(),
        inputValue = "123456",
        placeholder = "Placeholder",
        hintMessage = LoremIpsum(50).values.joinToString(),
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

        SelectorOrg(modifier = Modifier
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
fun SelectorOrg_Preview() {

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
                    text = UiText.DynamicString(LoremIpsum(30).values.joinToString()),
                    parameters = emptyList()
                )
            ),
            QuestionFormsOrgDataLocal(
                id = "",
                title = null,
                items = SnapshotStateList<UIElementData>().apply {
                    add(
                        SelectorOrgData(
                            id = "1",
                            label = "label",
                            placeholder = "hint",
                            inputValue = "",
                        )
                    )
                    add(
                        SelectorOrgData(
                            id = "2",
                            label = "label2",
                            placeholder = "hint2",
                            inputValue = "",
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


    PublicServiceScreen(
        toolbar = toolbarData,
        body = bodyData,
        bottom = bottomData,
        onEvent = {},
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    )

}
