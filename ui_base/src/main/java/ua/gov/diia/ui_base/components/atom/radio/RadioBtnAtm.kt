package ua.gov.diia.ui_base.components.atom.radio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrg
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrgData
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Mantis

@Composable
fun RadioBtnAtm(
    modifier: Modifier = Modifier,
    data: RadioBtnAtmData,
    onUIAction: (UIAction) -> Unit
) {
    var accordionExpanded by remember { mutableStateOf(false) }

    val onClick = {
        onUIAction(
            UIAction(
                actionKey = data.actionKey,
                data = data.id,
            )
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (data.interactionState == UIState.Interaction.Enabled) {
                    onClick.invoke()
                }
            }, verticalAlignment = Alignment.Top
    ) {
        RadioButton(
            modifier = Modifier.size(20.dp),
            selected = data.selectionState == UIState.Selection.Selected,
            enabled = data.interactionState == UIState.Interaction.Enabled,
            onClick = {
                onClick.invoke()
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = Mantis, unselectedColor = Black, disabledSelectedColor = BlackAlpha30, disabledUnselectedColor = BlackAlpha30
            )
        )
        data.logoLeft?.let {
            IconBase64Subatomic(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp), base64Image = data.logoLeft ?: ""
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 2.dp, start = 8.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.label,
                style = DiiaTextStyle.t3TextBody,
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha30
                    UIState.Interaction.Enabled -> Black
                }
            )
            data.accordionTitle?.let {
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        modifier = Modifier.clickable {
                            accordionExpanded = !accordionExpanded
                        },
                        text = it,
                        textDecoration = TextDecoration.Underline,
                        style = DiiaTextStyle.t3TextBody,
                    )
                    Icon(
                        modifier = Modifier
                            .padding(
                                start = 4.dp,
                                top = 2.dp
                            )
                            .size(8.dp, 8.dp),
                        painter = painterResource(
                            id = if (accordionExpanded) {
                                R.drawable.ic_arrow_show_less
                            } else {
                                R.drawable.ic_arrow_show_more
                            }
                        ),
                        contentDescription = stringResource(R.string.details),
                        tint = Black
                    )
                }
            }
            data.description?.let {
                if (data.accordionTitle == null) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        text = data.description ?: "",
                        style = DiiaTextStyle.t4TextSmallDescription,
                        color = BlackAlpha30
                    )
                } else {
                    AnimatedVisibility(visible = accordionExpanded) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clickable {
                                    accordionExpanded = !accordionExpanded
                                },
                            text = data.description ?: "",
                            style = DiiaTextStyle.t3TextBody,
                            color = BlackAlpha30
                        )
                    }
                }
            }
        }
        data.status?.let {
            Text(
                text = data.status ?: "", style = DiiaTextStyle.t3TextBody, color = BlackAlpha30
            )
        }
        data.logoRight?.let {
            if (data.status == null) {
                IconBase64Subatomic(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(30.dp, 24.dp), base64Image = data.logoRight ?: ""
                )
            }
        }
    }
}

data class RadioBtnAtmData(
    val actionKey: String = UIActionKeysCompose.RADIO_BUTTON,
    val id: String,
    val label: String,
    val mode: RadioButtonMode = RadioButtonMode.SINGLE_CHOICE,
    val accordionTitle: String? = null,
    val description: String? = null,
    val status: String? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
    val logoLeft: String? = null,
    val logoRight: String? = null
) : UIElementData, Cloneable, RadioBtnItem {
    public override fun clone(): RadioBtnAtmData {
        return super.clone() as RadioBtnAtmData
    }

    fun onRadioButtonClick(): RadioBtnAtmData {
        return this.copy(
            selectionState = when (this.mode) {
                RadioButtonMode.SINGLE_CHOICE -> UIState.Selection.Selected
                RadioButtonMode.MULTI_CHOICE -> this.selectionState.reverse()
            }
        )
    }
}

enum class RadioButtonMode {
    SINGLE_CHOICE, MULTI_CHOICE
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_AllParametersExist() {
    val data = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        description = LoremIpsum(30).values.joinToString(),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        logoLeft = PreviewBase64Icons.apple,
        logoRight = PreviewBase64Icons.mastercard, //if you want to see endLogo - comment status
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp), data = state.value
    ) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_Enabled_Selected() {
    val data = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
        logoLeft = PreviewBase64Icons.apple,
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp), data = state.value
    ) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_Enabled_Unselected() {
    val data = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        logoLeft = PreviewBase64Icons.apple, status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp), data = state.value
    ) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_Disabled_Selected() {
    val data = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Selected,
        logoLeft = PreviewBase64Icons.apple,
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp), data = state.value
    ) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_Disabled_Unselected() {
    val data = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected,
        logoLeft = PreviewBase64Icons.apple,
        status = "Status"
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp), data = state.value
    ) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_Accordion() {
    val data = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        accordionTitle = "Показати перелік",
        description = LoremIpsum(30).values.joinToString(),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
    )
    val state = remember {
        mutableStateOf(data)
    }
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp), data = state.value
    ) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_AccordionInList() {

    val state = RadioBtnGroupOrgData(id = "", title = "Title",
        mode = RadioButtonMode.SINGLE_CHOICE,
        items = SnapshotStateList<RadioBtnAtmData>().apply {
            add(
                RadioBtnAtmData(
                    id = "1",
                    label = "Label 1",
                    mode = RadioButtonMode.SINGLE_CHOICE,
                    accordionTitle = "Показати перелік",
                    description = LoremIpsum(30).values.joinToString(),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                )
            )
            add(
                RadioBtnAtmData(
                    id = "2",
                    label = "Label 2",
                    mode = RadioButtonMode.SINGLE_CHOICE,
                    accordionTitle = "Показати перелік",
                    description = LoremIpsum(30).values.joinToString(),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                )
            )
            add(
                RadioBtnAtmData(
                    id = "3",
                    label = "Label 3",
                    mode = RadioButtonMode.SINGLE_CHOICE,
                    description = LoremIpsum(30).values.joinToString(),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                )
            )
        })


    RadioBtnGroupOrg(modifier = Modifier, data = state) {

    }
}

@Composable
@Preview
fun RadioButtonAtomAtomPreview_Enabled_Selected_MultiMode() {
    val state = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
        logoLeft = PreviewBase64Icons.apple,
        status = "Status"
    )
    RadioBtnAtm(
        modifier = Modifier.padding(20.dp),
        data = state,
    ) {
    }
}