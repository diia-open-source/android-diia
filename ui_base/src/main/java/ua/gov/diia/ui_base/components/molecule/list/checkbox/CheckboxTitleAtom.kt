package ua.gov.diia.ui_base.components.molecule.list.checkbox

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.BadgeSubatomic
import ua.gov.diia.ui_base.components.subatomic.icon.PlusMinusClickableSubatomic
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun CheckboxTitleAtom(
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

@Preview
@Composable
fun CheckboxTitleAtomPreview() {
    val expanded = rememberSaveable { mutableStateOf(true) }
    val selectedCount = remember { mutableStateOf(4) }
    CheckboxTitleAtom(
        title = "Title",
        expandable = true,
        showBadge = false,
        expanded = expanded,
        selectedCount = selectedCount
    )
}