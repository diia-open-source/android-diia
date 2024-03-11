package ua.gov.diia.ui_base.components.molecule.list.checkbox

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ua.gov.diia.ui_base.components.atom.checkbox.CheckboxCircleAtom
import ua.gov.diia.ui_base.components.atom.checkbox.CheckboxCircleAtomData
import ua.gov.diia.ui_base.components.atom.checkbox.CheckboxMode
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.BadgeSubatomic
import ua.gov.diia.ui_base.components.subatomic.icon.PlusMinusClickableSubatomic
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckboxMolecule(
    modifier: Modifier = Modifier,
    data: CheckboxMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    val expanded = rememberSaveable { mutableStateOf(data.expanded) }

    val localData by rememberUpdatedState(newValue = data)

    val selectedCount = remember {
        derivedStateOf {
            localData.options.sumOf(object : Function1<CheckboxCircleAtomData, Int> {
                override fun invoke(it: CheckboxCircleAtomData): Int {
                    return if (it.interactionState == UIState.Interaction.Enabled && it.selectionState == UIState.Selection.Selected) {
                        1
                    } else {
                        0
                    }
                }
            })
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        localData.title?.also { title ->
            CheckboxTitleAtom(
                title = title,
                expandable = localData.expandable,
                showBadge = localData.showBadge,
                expanded = expanded,
                selectedCount = selectedCount
            )
        }
        AnimatedVisibility(visible = expanded.value) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (localData.title != null) {
                    DividerSlimAtom(
                        modifier = Modifier.fillMaxWidth(),
                        color = BlackSqueeze
                    )
                }
                Options(
                    options = localData.options,
                    onUIAction = { action ->
                        onUIAction(
                            UIAction(
                                actionKey = localData.actionKey,
                                data = action.data,
                                states = action.states
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun Options(
    options: SnapshotStateList<CheckboxCircleAtomData>,
    onUIAction: (UIAction) -> Unit
) {
    options.forEachIndexed { index, checkboxCircleAtomData ->
        CheckboxCircleAtom(
            modifier = Modifier,
            data = checkboxCircleAtomData,
            contentPadding = PaddingValues(16.dp),
            onUIAction = onUIAction
        )
        if (index != options.lastIndex) {
            DividerSlimAtom(
                modifier = Modifier.fillMaxWidth(),
                color = BlackSqueeze
            )
        }
    }
}

@Composable
private fun Title(
    title: String,
    expandable: Boolean,
    showBadge: Boolean,
    expanded: MutableState<Boolean>,
    selectedCount: State<Int>,
) {
    ConstraintLayout(
        modifier = Modifier
            .noRippleClickable(enabled = expandable) {
                expanded.value = !expanded.value
            }
            .fillMaxWidth()
    ) {
        val (titleRef, badgeRef, buttonRef) = createRefs()
        createHorizontalChain(titleRef, badgeRef, chainStyle = ChainStyle.Packed)
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .constrainAs(titleRef) {
                    linkTo(parent.start, badgeRef.start, bias = 0f)
                    linkTo(parent.top, parent.bottom)
                    width = Dimension.preferredWrapContent
                },
            text = title,
            style = DiiaTextStyle.h4ExtraSmallHeading
        )
        if (showBadge && selectedCount.value != 0) {
            BadgeSubatomic(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
                    .constrainAs(badgeRef) {
                        linkTo(titleRef.end, buttonRef.start)
                        linkTo(parent.top, parent.bottom)

                    },
                value = selectedCount.value
            )
        }
        if (expandable) {
            PlusMinusClickableSubatomic(
                modifier = Modifier
                    .padding(end = 21.dp)
                    .size(14.dp)
                    .constrainAs(buttonRef) {
                        linkTo(badgeRef.end, parent.end, bias = 1f)
                        linkTo(parent.top, parent.bottom)
                    },
                expandState = if (expanded.value) {
                    UIState.Expand.Expanded
                } else {
                    UIState.Expand.Collapsed
                }
            )
        }
    }
}

data class CheckboxMoleculeData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_GROUP_MOLECULE,
    val id: String? = null,
    val title: String? = null,
    val expandable: Boolean = false,
    val expanded: Boolean = true,
    val showBadge: Boolean = true,
    val options: SnapshotStateList<CheckboxCircleAtomData>
) {
    fun mapSelected(mode: CheckboxMode, selectedId: String): CheckboxMoleculeData {
        return when (mode) {
            CheckboxMode.SINGLE_CHOICE -> {
                copy(
                    options = options.map { item ->
                        if (item.interactionState == UIState.Interaction.Enabled && item.id == selectedId && item.selectionState == UIState.Selection.Unselected) {
                            item.copy(selectionState = UIState.Selection.Selected)
                        } else if (item.interactionState == UIState.Interaction.Enabled && item.id != selectedId && item.selectionState == UIState.Selection.Selected) {
                            item.copy(selectionState = UIState.Selection.Unselected)
                        } else {
                            item
                        }
                    }.toMutableStateList()
                )
            }
            CheckboxMode.MULTI_CHOICE -> {
                copy(
                    options = options.map { item ->
                        if (item.interactionState == UIState.Interaction.Enabled && item.id == selectedId) {
                            item.copy(selectionState = item.selectionState.reverse())
                        } else {
                            item
                        }
                    }.toMutableStateList()
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun CheckboxMoleculePreviewExpanded() {
    CheckboxMolecule(
        data = CheckboxMoleculeData(
            title = LoremIpsum(3).values.joinToString(),
            expandable = true,
            expanded = true,
            options = mutableStateListOf(
                CheckboxCircleAtomData(
                    id = "1",
                    title = LoremIpsum(3).values.joinToString(),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Unselected,
                ),
                CheckboxCircleAtomData(
                    id = "2",
                    title = LoremIpsum(3).values.joinToString(),
                    description = LoremIpsum(1).values.joinToString(),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Selected,
                    status = LoremIpsum(1).values.joinToString()
                ),
                CheckboxCircleAtomData(
                    id = "3",
                    title = LoremIpsum(3).values.joinToString(),
                    description = LoremIpsum(2).values.joinToString(),
                    interactionState = UIState.Interaction.Disabled,
                    selectionState = UIState.Selection.Unselected,
                    status = LoremIpsum(1).values.joinToString()
                ),
                CheckboxCircleAtomData(
                    id = "3",
                    title = LoremIpsum(10).values.joinToString(),
                    description = LoremIpsum(10).values.joinToString(),
                    interactionState = UIState.Interaction.Disabled,
                    selectionState = UIState.Selection.Selected,
                    status = LoremIpsum(1).values.joinToString()
                )
            )
        ),
        onUIAction = {}
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun CheckboxMoleculePreviewCollapsed() {
    CheckboxMolecule(
        data = CheckboxMoleculeData(
            title = LoremIpsum(3).values.joinToString(),
            expandable = true,
            expanded = false,
            options = mutableStateListOf()
        ),
        onUIAction = {}
    )
}