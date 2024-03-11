package ua.gov.diia.ui_base.components.atom.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Mantis
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckboxCircleAtom(
    modifier: Modifier = Modifier,
    data: CheckboxCircleAtomData,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onUIAction: (UIAction) -> Unit
) {
    val bgModifier = when (data.interactionState) {
        UIState.Interaction.Enabled -> {
            when (data.selectionState) {
                UIState.Selection.Selected -> Modifier.background(color = Mantis, shape = CircleShape)
                UIState.Selection.Unselected -> Modifier.border(width = 2.dp, color = Black, shape = CircleShape)
            }
        }
        UIState.Interaction.Disabled -> {
            when (data.selectionState) {
                UIState.Selection.Selected -> Modifier.background(color = BlackAlpha30, shape = CircleShape)
                UIState.Selection.Unselected -> Modifier.border(width = 2.dp, color = BlackAlpha30, shape = CircleShape)
            }
        }
    }

    Column(modifier = modifier
        .fillMaxWidth()
        .clickable(
            enabled = data.interactionState == UIState.Interaction.Enabled,
            onClick = {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        states = listOf(data.selectionState)
                    )
                )
            },
            role = Role.Checkbox,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
        .padding(contentPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .then(bgModifier),
                contentAlignment = Alignment.Center
            ) {
                if (data.selectionState == UIState.Selection.Selected) {
                    Icon(
                        modifier = Modifier.size(10.dp, 8.dp),
                        painter = painterResource(id = R.drawable.diia_check),
                        contentDescription = null,
                        tint = White
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(start = 14.dp, end = 8.dp)
                    .weight(1f),
                text = data.title,
                style = DiiaTextStyle.t3TextBody,
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha30
                    UIState.Interaction.Enabled -> Black
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            data.status?.also { status ->
                Text(
                    text = status,
                    style = DiiaTextStyle.t3TextBody,
                    color = BlackAlpha30,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        data.description?.also { description ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 34.dp, top = 2.dp),
                text = description,
                style = DiiaTextStyle.t2TextDescription,
                color = BlackAlpha30
            )
        }
    }
}

enum class CheckboxMode {
    SINGLE_CHOICE, MULTI_CHOICE
}

data class CheckboxCircleAtomData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_REGULAR,
    val id: String,
    val title: String,
    val description: String? = null,
    val status: String? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val selectionState: UIState.Selection = UIState.Selection.Unselected
)

@Preview
@Composable
fun CheckboxCircleAtomPreview() {
    CheckboxCircleAtom(
        modifier = Modifier,
        data = CheckboxCircleAtomData(
            id = "1",
            title = LoremIpsum(3).values.joinToString(),
            description = LoremIpsum(30).values.joinToString(),
            interactionState = UIState.Interaction.Enabled,
            selectionState = UIState.Selection.Selected,
            status = LoremIpsum(1).values.joinToString()
        ),
        onUIAction = {}
    )
}