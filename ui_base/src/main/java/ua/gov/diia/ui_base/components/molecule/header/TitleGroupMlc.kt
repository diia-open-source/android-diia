package ua.gov.diia.ui_base.components.molecule.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TitleGroupMlc(
    modifier: Modifier = Modifier,
    data: TitleGroupMlcData,
    onUIAction: (UIAction) -> Unit,
    diiaResourceIconProvider: DiiaResourceIconProvider
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.Start
    ) {
        data.leftNavIcon?.let {
            Image(
                modifier = modifier
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, radius = 28.dp)
                    ) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = it.action
                            )
                        )
                    },
                painter = painterResource(
                    id = diiaResourceIconProvider.getResourceId(it.code)
                ),
                contentDescription = it.accessibilityDescription?.toString()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            verticalAlignment = Alignment.Top
        ) {
            val title = data.heroText?.asString()
            if (title != null) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = title,
                    style = DiiaTextStyle.hero2Text
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            data.mediumIconRight?.let {
                Image(
                    modifier = modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false, radius = 28.dp)
                        ) {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    action = it.action
                                )
                            )
                        },
                    painter = painterResource(
                        id = diiaResourceIconProvider.getResourceId(it.code)
                    ),
                    contentDescription = stringResource(
                        id = diiaResourceIconProvider.getContentDescription(
                            it.code
                        )
                    )
                )
            }
        }

        if (data.label != null) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = data.label.asString(),
                style = DiiaTextStyle.t2TextDescription,
                color = BlackAlpha50,
            )
        }
    }
}


data class TitleGroupMlcData(
    val actionKey: String = UIActionKeysCompose.TITLE_GROUP_MLC,
    val leftNavIcon: LeftNavIcon? = null,
    val heroText: UiText,
    val label: UiText? = null,
    val mediumIconRight: MediumIconRight? = null
) : UIElementData {
    data class LeftNavIcon(
        val code: String,
        val accessibilityDescription: UiText? = null,
        val action: DataActionWrapper? = null
    )

    data class MediumIconRight(
        val code: String,
        val action: DataActionWrapper? = null
    )
}

@Preview
@Composable
fun TitleGroupMlcPreview_Full() {
    TitleGroupMlc(
        data = TitleGroupMlcData(
            leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                code = CommonDiiaResourceIcon.BACK.code,
                accessibilityDescription = "123".toDynamicString(),
                action = DataActionWrapper(
                    type = "type",
                    subtype = "subtype",
                    resource = "resource"
                )
            ),
            mediumIconRight = TitleGroupMlcData.MediumIconRight(
                code = CommonDiiaResourceIcon.ELLIPSE_SETTINGS.code,
                action = DataActionWrapper(
                    type = "type",
                    subtype = "subtype",
                    resource = "resource"
                )
            ),
            heroText = UiText.DynamicString("Hero text"),
            label = UiText.DynamicString("label"),
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onUIAction = {}
    )
}


@Preview
@Composable
fun TitleGroupMlcPreview_WithoutNavigation() {
    TitleGroupMlc(
        data = TitleGroupMlcData(
            leftNavIcon = null,
            mediumIconRight = TitleGroupMlcData.MediumIconRight(
                code = CommonDiiaResourceIcon.ELLIPSE_SETTINGS.code,
                action = DataActionWrapper(
                    type = "type",
                    subtype = "subtype",
                    resource = "resource"
                )
            ),
            heroText = UiText.DynamicString("Hero text"),
            label = UiText.DynamicString("label"),
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onUIAction = {}
    )
}

@Preview
@Composable
fun TitleGroupMlcPreview_Poor() {
    TitleGroupMlc(
        data = TitleGroupMlcData(
            leftNavIcon = null,
            mediumIconRight = null,
            heroText = UiText.DynamicString("Hero text"),
            label = UiText.DynamicString("label"),
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
        onUIAction = {}
    )
}
