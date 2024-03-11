package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Mantis
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckIconMlc(
    modifier: Modifier = Modifier,
    data: CheckIconMlcData,
    onUIAction: (UIAction) -> Unit,
    diiaResourceIconProvider: DiiaResourceIconProvider
) {

    Column(modifier = modifier.width(width = 64.dp)) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    onUIAction.invoke(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id
                        )
                    )
                },
                modifier = modifier
                    .size(56.dp)
                    .background(Black, CircleShape)
                    .conditional(data.interactionState == UIState.Interaction.Disabled) {
                        noRippleClickable {}
                    },
            ) {
                Image(
                    modifier = modifier.size(24.dp),
                    painter = painterResource(diiaResourceIconProvider.getResourceId(data.icon.code)),
                    contentDescription = "contentDescription",
                    colorFilter = ColorFilter.tint(White),
                )
            }

            if (data.selectionState == UIState.Selection.Selected) {
                Box(
                    modifier = modifier
                        .size(20.dp)
                        .background(Mantis, CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = modifier.size(10.dp),
                        painter = painterResource(R.drawable.diia_check),
                        contentDescription = "contentDescription",
                        colorFilter = ColorFilter.tint(White),
                    )
                }
            }
        }

        if (data.title != null) {
            Text(
                modifier = modifier.fillMaxWidth().padding(top = 4.dp),
                text = data.title.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class CheckIconMlcData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String = "",
    val title: UiText? = null,
    val icon: UiIcon.DrawableResource,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
) : UIElementData, CheckBoxItem {

    fun onIconButtonClick(): CheckIconMlcData {
        return this.copy(selectionState = this.selectionState.reverse())
    }

}

@Composable
@Preview
fun CheckIconMlcPreview() {
    CheckIconMlc(
        data = CheckIconMlcData(
            id = "code",
            icon = UiIcon.DrawableResource("charging"),
            title = UiText.DynamicString("Генератор"),
            selectionState = UIState.Selection.Selected
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onUIAction = {}
    )
}

@Composable
@Preview
fun CheckIconMlcPreview2() {
    CheckIconMlc(
        data = CheckIconMlcData(
            id = "code",
            icon = UiIcon.DrawableResource("charging"),
            title = UiText.DynamicString("Тепло"),
            selectionState = UIState.Selection.Selected
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onUIAction = {}
    )
}