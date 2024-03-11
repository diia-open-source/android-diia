package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtm
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.getExpandStateOrNull
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.BadgeSubatomic
import ua.gov.diia.ui_base.components.subatomic.icon.PlusMinusClickableSubatomic
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun MultipleChoiceGroupOrganism(
    modifier: Modifier = Modifier,
    data: MultipleChoiceGroupOrganismDataData,
    onUIAction: (UIAction) -> Unit
) {
    val listExpandState = remember {
        mutableStateOf(data.expandState)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
    ) {
        data.title?.let {
            when (data.type) {
                MultipleChoiceGroupType.PLAIN_LIST -> {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = data.title.asString() ?: "",
                        style = DiiaTextStyle.t3TextBody
                    )
                }

                MultipleChoiceGroupType.ACCORDION -> {
                    MultipleChoiceGroupAccordionHeader(
                        title = data.title.asString(),
                        expandState = data.expandState ?: UIState.Expand.Expanded,
                        counter = data.budgeCounter ?: 0
                    ) {
                        listExpandState.value = it.getExpandStateOrNull() ?: UIState.Expand.Expanded
                    }
                }
            }
            if (listExpandState.value == UIState.Expand.Expanded) {
                DividerSlimAtom(modifier = Modifier.fillMaxWidth(), color = BlackSqueeze)
            }
        }
        AnimatedVisibility(visible = listExpandState.value == UIState.Expand.Expanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                data.options.forEachIndexed { index, item ->
                    RadioBtnAtm(
                        modifier = Modifier.padding(16.dp),
                        data = item,
                        onUIAction = {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = it.data
                                )
                            )
                        }
                    )
                    if (index != data.options.size - 1) {
                        DividerSlimAtom(
                            modifier = Modifier.fillMaxWidth(), color = BlackSqueeze
                        )
                    }
                }
            }
        }
    }
}


data class MultipleChoiceGroupOrganismDataData(
    val actionKey: String = UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM,
    val type: MultipleChoiceGroupType = MultipleChoiceGroupType.PLAIN_LIST,
    val title: UiText? = null,
    val expandState: UIState.Expand? = UIState.Expand.Expanded,
    val budgeCounter: Int? = 0,
    val options: SnapshotStateList<RadioBtnAtmData>
) : UIElementData {
    fun onItemClick(id: String?): MultipleChoiceGroupOrganismDataData {
        val data = this
        if (id == null) return this
        return this.copy(
            options = SnapshotStateList<RadioBtnAtmData>().apply {
                data.options.forEach {
                    if (id == it.id) {
                        add(it.onRadioButtonClick())
                    } else {
                        add(it)
                    }
                }
            }
        )
    }
}


@Composable
private fun MultipleChoiceGroupAccordionHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    expandState: UIState.Expand = UIState.Expand.Collapsed,
    counter: Int = 0,
    onUIAction: (UIAction) -> Unit
) {
    val localExpandState = remember { mutableStateOf(expandState) }

    LaunchedEffect(key1 = expandState) {
        localExpandState.value = expandState
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                localExpandState.value = when (localExpandState.value) {
                    UIState.Expand.Collapsed -> UIState.Expand.Expanded
                    UIState.Expand.Expanded -> UIState.Expand.Collapsed
                }
                onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM_ACCORDION_HEADER,
                        states = listOf(localExpandState.value)
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 18.dp)
                    .padding(start = 16.dp, end = 8.dp)
                    .weight(1f, fill = false),
                text = title ?: "",
                style = DiiaTextStyle.h4ExtraSmallHeading
            )
            if (counter != 0) {
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    BadgeSubatomic(value = counter)
                }
            }
        }
        PlusMinusClickableSubatomic(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(14.dp), expandState = localExpandState.value
        )
    }
}


@Composable
@Preview
fun MultipleChoiceGroupAccordionHeaderPreview() {
    MultipleChoiceGroupAccordionHeader(
        title = "Ви декларуєте місце проживання дитини вперше? Можливо ви декларуєте місце проживання дитини вдруге.",
        expandState = UIState.Expand.Collapsed,
        counter = 2,
    ) {

    }
}

@Composable
@Preview
fun MultipleChoiceGroupAccordionHeaderPreview_ShortTitle() {
    MultipleChoiceGroupAccordionHeader(
        title = "Ви декларуєте місце.",
        expandState = UIState.Expand.Collapsed,
        counter = 2,
    ) {

    }
}

@Composable
@Preview
fun MultipleChoiceGroupAccordionHeaderPreview_ShortTitle2() {
    MultipleChoiceGroupAccordionHeader(
        title = "Title", expandState = UIState.Expand.Expanded, counter = 4
    ) {

    }
}

@Composable
@Preview
fun MultipleChoiceGroupAccordionHeaderPreview_WithoutCounter() {
    MultipleChoiceGroupAccordionHeader(
        title = "Title", expandState = UIState.Expand.Expanded, counter = 0
    ) {

    }
}

@Composable
@Preview
fun MultipleChoiceGroupOrganismPreview() {
    val data = MultipleChoiceGroupOrganismDataData(
        title = UiText.DynamicString("Ви декларуєте місце проживання дитини вперше? Можливо ви декларуєте місце проживання дитини вдруге."),
        options = SnapshotStateList<RadioBtnAtmData>().apply {
            add(
                RadioBtnAtmData(
                    id = "1",
                    label = "Item 1",
                    mode = RadioButtonMode.MULTI_CHOICE
                )
            )
            add(
                RadioBtnAtmData(
                    id = "2",
                    label = "Item 2",
                    mode = RadioButtonMode.MULTI_CHOICE
                )
            )
            add(
                RadioBtnAtmData(
                    id = "3",
                    label = "Item 3",
                    mode = RadioButtonMode.MULTI_CHOICE
                )
            )
            add(
                RadioBtnAtmData(
                    id = "4",
                    label = "Item 4",
                    mode = RadioButtonMode.MULTI_CHOICE
                )
            )
        })

    val state = remember {
        mutableStateOf(data)
    }
    MultipleChoiceGroupOrganism(
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM -> {
                state.value = state.value.onItemClick(it.data)
            }

            else -> {

            }
        }
    }
}


@Composable
@Preview
fun MultipleChoiceGroupOrganismPreview_WithoutTitle() {
    val data = MultipleChoiceGroupOrganismDataData(options = SnapshotStateList<RadioBtnAtmData>().apply {
        add(
            RadioBtnAtmData(
                id = "1", label = "Item 1",
                mode = RadioButtonMode.MULTI_CHOICE
            )
        )
        add(
            RadioBtnAtmData(
                id = "2", label = "Item 2",
                mode = RadioButtonMode.MULTI_CHOICE
            )
        )
        add(
            RadioBtnAtmData(
                id = "3", label = "Item 3",
                mode = RadioButtonMode.MULTI_CHOICE
            )
        )
        add(
            RadioBtnAtmData(
                id = "4", label = "Item 4",
                mode = RadioButtonMode.MULTI_CHOICE
            )
        )
    })
    val state = remember {
        mutableStateOf(data)
    }
    MultipleChoiceGroupOrganism(
        modifier = Modifier
            .fillMaxWidth()
        ,
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM -> {
                state.value = state.value.onItemClick(it.data)
            }

            else -> {

            }
        }
    }
}

@Composable
@Preview
fun MultipleChoiceGroupOrganismPreview_Accordion() {
    val data = MultipleChoiceGroupOrganismDataData(
        options = SnapshotStateList<RadioBtnAtmData>().apply {
            add(
                RadioBtnAtmData(
                    id = "1", label = "Item 1", mode = RadioButtonMode.MULTI_CHOICE
                )
            )
            add(
                RadioBtnAtmData(
                    id = "2", label = "Item 2", mode = RadioButtonMode.MULTI_CHOICE
                )
            )
            add(
                RadioBtnAtmData(
                    id = "3", label = "Item 3", mode = RadioButtonMode.MULTI_CHOICE
                )
            )
            add(
                RadioBtnAtmData(
                    id = "4", label = "Item 4", mode = RadioButtonMode.MULTI_CHOICE
                )
            )
        },
        type = MultipleChoiceGroupType.ACCORDION,
        title = UiText.DynamicString("Ви декларуєте місце проживання дитини вперше? Можливо ви декларуєте місце проживання дитини вдруге."),
        budgeCounter = 2
    )

    val state = remember {
        mutableStateOf(data)
    }
    MultipleChoiceGroupOrganism(
        modifier = Modifier
            .fillMaxWidth()
        ,
        data = state.value
    ) {
        when (it.actionKey) {
            UIActionKeysCompose.MULTI_CHOICE_GROUP_ORGANISM -> {
                state.value = state.value.onItemClick(it.data)
            }

            else -> {

            }
        }
    }
}

enum class MultipleChoiceGroupType {
    PLAIN_LIST, ACCORDION
}