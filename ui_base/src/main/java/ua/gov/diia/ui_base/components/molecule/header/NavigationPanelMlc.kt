package ua.gov.diia.ui_base.components.molecule.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.icon.IconBackArrowAtom
import ua.gov.diia.ui_base.components.atom.icon.IconEllipseMenuAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun NavigationPanelMlc(
    modifier: Modifier = Modifier,
    data: NavigationPanelMlcData,
    onUIAction: (UIAction) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(start = 24.dp, top = 32.dp, end = 24.dp, bottom = 16.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.iconAtm?.let {
            Image(
                modifier = modifier
                    .size(28.dp)
                    .noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.backAction,
                                data = it.id,
                                action = it.action
                            )
                        )
                    }
                    .semantics {
                        testTag = it.componentId
                    },
                painter = painterResource(
                    id = DiiaResourceIcon.getResourceId(it.code)
                ),
                contentDescription = it.accessibilityDescription
            )
        } ?: kotlin.run {
            IconBackArrowAtom(
                modifier = Modifier
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, radius = 28.dp)
                    ) {
                        onUIAction(UIAction(actionKey = data.backAction))
                    },
                tintColor = data.tintColor
            )
        }

        data.title?.let {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .align(alignment = Alignment.CenterVertically),
                text = data.title.asString(),
                style = DiiaTextStyle.h4ExtraSmallHeading,
                color = data.tintColor
            )
        } ?: Spacer(modifier = Modifier.weight(1f))

        if (data.isContextMenuExist) {
            IconEllipseMenuAtom(modifier = Modifier
                .size(28.dp)
                .noRippleClickable {
                    onUIAction(UIAction(actionKey = data.contextMenuAction))
                }
            )
        }
    }
}

@Preview
@Composable
fun NavigationPanelMlcPreview() {
    NavigationPanelMlc(
        data = NavigationPanelMlcData(
            title = UiText.DynamicString("Label"),
            isContextMenuExist = true
        )
    ) {

    }
}

@Preview
@Composable
fun NavigationPanelMlcPreview_With_Icon() {
    NavigationPanelMlc(
        data = NavigationPanelMlcData(
            title = UiText.DynamicString("Label"),
            isContextMenuExist = true,
            iconAtm = IconAtmData(
                code = DiiaResourceIcon.BACK.code,
            )
        )
    ) {

    }
}

@Preview
@Composable
fun NavigationBarMoleculeV2Preview_White() {
    NavigationPanelMlc(
        data = NavigationPanelMlcData(
            title = UiText.DynamicString("Label"),
            isContextMenuExist = true,
            tintColor = White
        )
    ) {

    }
}

data class NavigationPanelMlcData(
    val backAction: String = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK,
    val contextMenuAction: String = UIActionKeysCompose.TOOLBAR_CONTEXT_MENU,
    val title: UiText? = null,
    val isContextMenuExist: Boolean,
    val tintColor: Color = Black,
    val componentId: UiText? = null,
    val iconAtm: IconAtmData? = null
) : UIElementData

fun NavigationPanelMlc.toUiModel(): NavigationPanelMlcData {
    return NavigationPanelMlcData(
        title = this.label.toDynamicStringOrNull(),
        iconAtm = this.iconAtm?.toUiModel(),
        componentId = this.componentId.toDynamicStringOrNull(),
        isContextMenuExist = ellipseMenu?.isNotEmpty() ?: false
    )
}