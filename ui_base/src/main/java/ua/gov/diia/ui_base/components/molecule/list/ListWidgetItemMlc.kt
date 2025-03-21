package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.core.models.common_compose.mlc.list.ListWidgetItemMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.disableByInteractionState
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.AzureishWhite
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ListWidgetItemMlc(
    modifier: Modifier = Modifier,
    data: ListWidgetItemMlcData,
    onUIAction: (UIAction) -> Unit
) {
    var isClickable by remember {
        mutableStateOf(data.interactionState != UIState.Interaction.Disabled)
    }

    LaunchedEffect(key1 = data.interactionState) {
        isClickable = data.interactionState != UIState.Interaction.Disabled
    }

    val onClickData = UIAction(
        actionKey = data.actionKey,
        data = data.id,
        optionalType = data.label.asString(),
        action = data.action
    )
    Column(modifier = Modifier.padding(horizontal = 24.dp)){
        Row(
            modifier = modifier
                .padding(vertical = 16.dp)
                .noRippleClickable { onUIAction(onClickData) }
                .disableByInteractionState(data.interactionState)
                .testTag(data.componentId?.asString() ?: ""),
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.iconLeft?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp),
                    painter = painterResource(
                        id = DiiaResourceIcon.getResourceId(it.code)
                    ),
                    contentDescription = data.iconLeftContentDescription?.asString()
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.label.asString(),
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
                data.description?.let {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = data.description.asString(),
                        style = DiiaTextStyle.t2TextDescription,
                        color = BlackAlpha30
                    )
                }
            }
            data.iconRight?.let {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                    painter = painterResource(
                        id = DiiaResourceIcon.getResourceId(it.code)
                    ),
                    contentDescription = data.iconRightContentDescription?.asString()
                )
            }
        }
        Divider(modifier = modifier, thickness = 2.dp, color = ColumbiaBlue)

    }
}

data class ListWidgetItemMlcData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_MLC,
    override val id: String = "",
    val label: UiText,
    val description: UiText? = null,
    val iconLeft: UiIcon.DrawableResource? = null,
    val iconLeftContentDescription: UiText? = null,
    val iconRight: UiIcon.DrawableResource? = null,
    val iconRightContentDescription: UiText? = null,
    val action: DataActionWrapper? = null, //todo ?
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val componentId: UiText? = null,
) : SimplePagination

fun ListWidgetItemMlc.toUiModel(id: String? = null): ListWidgetItemMlcData {
    return ListWidgetItemMlcData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        id = this.id ?: id ?: UIActionKeysCompose.LIST_ITEM_MLC,
        label = label.toDynamicString(),
        description = description?.toDynamicStringOrNull(),
        iconLeft = iconLeft?.code?.let { UiIcon.DrawableResource(it) },
        iconRight = iconRight?.code?.let { UiIcon.DrawableResource(it) },
        interactionState = when (state) {
            ButtonStates.enabled.name -> UIState.Interaction.Enabled
            ButtonStates.disabled.name -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        }
    )
}


@Composable
@Preview
fun ListWidgetItemMlcPreview_Full() {
    val state = ListWidgetItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Enabled
    )

    Box(modifier = Modifier.background(AzureishWhite)) {
        ListWidgetItemMlc(
            modifier = Modifier.background(Color.White),
            data = state) {}
    }
}

@Composable
@Preview
fun ListWidgetItemMlcPreview_Disabled() {
    val state = ListWidgetItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Disabled
    )
    ListWidgetItemMlc(modifier = Modifier, data = state) {
    }
}

@Composable
@Preview
fun ListWidgetItemMlcPreview_OnlyMandatoryFields() {
    val state = ListWidgetItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Disabled
    )
    ListWidgetItemMlc(modifier = Modifier, data = state) {
    }
}