package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.checkbox.SmallCheckIconMlc
import ua.gov.diia.core.util.type_enum.TypeEnum
import ua.gov.diia.core.util.type_enum.getEnumTypeValue
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.SmallCheckIconMlcData.IconType
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BrightGray
import ua.gov.diia.ui_base.components.theme.GrannySmithApple
import ua.gov.diia.ui_base.components.theme.Mantis
import ua.gov.diia.ui_base.components.theme.MiddleBlueGreen
import ua.gov.diia.ui_base.components.theme.WarningYellow
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.gradientBluePosition01
import ua.gov.diia.ui_base.components.theme.gradientBluePosition02
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition01
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition02
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition03
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition04
import ua.gov.diia.ui_base.components.theme.gradientPinkPosition01
import ua.gov.diia.ui_base.components.theme.gradientPinkPosition02
import ua.gov.diia.ui_base.util.gradient.AngleLinearGradient

@Composable
fun SmallCheckIconMlc(
    modifier: Modifier = Modifier,
    data: SmallCheckIconMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val gradient = when (data.icon) {

        IconType.RAINBOW -> AngleLinearGradient(
            colors = listOf(
                gradientColorRainbowPosition01,
                gradientColorRainbowPosition02,
                gradientColorRainbowPosition03,
                gradientColorRainbowPosition04,
            ),
            angleInDegrees = 310f
        )

        IconType.PINK -> AngleLinearGradient(
            colors = listOf(gradientPinkPosition01, gradientPinkPosition02),
            angleInDegrees = 270f,
        )

        IconType.INFORMATIVE -> AngleLinearGradient(
            colors = listOf(BrightGray, BrightGray),
            angleInDegrees = 270f
        )

        IconType.WARNING -> AngleLinearGradient(listOf(WarningYellow, WarningYellow))

        IconType.BLUE -> AngleLinearGradient(
            colors = listOf(gradientBluePosition01, gradientBluePosition02),
            angleInDegrees = 270f
        )

        else -> AngleLinearGradient(
            colors = listOf(GrannySmithApple, MiddleBlueGreen),
            angleInDegrees = 270f
        )
    }
    Box(
        modifier = modifier
            .testTag(data.componentId?.asString() ?: "")
            .padding(top = 6.dp, bottom = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                onUIAction.invoke(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.icon.type
                    )
                )
            },
            modifier = modifier
                .background(brush = gradient, shape = CircleShape)
                .size(44.dp)
                .background(BlackAlpha10, CircleShape)
                .padding(1.dp)
                .background(brush = gradient, shape = CircleShape)
                .size(44.dp)
        ) {}
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
}

data class SmallCheckIconMlcData(
    val id: String? = null,
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.SMALL_CHECK_ICON_MLC,
    val code: String? = null,
    val icon: IconType,
    val label: String,
    val selectionState: UIState.Selection?
) {


    enum class State(override val type: String) : TypeEnum {
        SELECTED("selected"),
        REST("rest")
    }

    enum class IconType(override val type: String): TypeEnum {
        RAINBOW("rainbow"),
        PINK("pink"),
        INFORMATIVE("informative"),
        POSITIVE("positive"),
        WARNING("warning"),
        BLUE("blue")
    }
}

fun SmallCheckIconMlc.toUIModel(selectionState: UIState.Selection? = null): SmallCheckIconMlcData {
    return SmallCheckIconMlcData(
        id = "",
        componentId = UiText.DynamicString(componentId.orEmpty()),
        code = code,
        icon = mapIcon(icon),
        label = label,
        selectionState = selectionState ?: mapState(state)
    )
}

private fun mapState(state: String?): UIState.Selection {
    val stateType = state?.let { getEnumTypeValue<SmallCheckIconMlcData.State>(it) }
    return when (stateType) {
        SmallCheckIconMlcData.State.SELECTED -> UIState.Selection.Selected
        else -> UIState.Selection.Unselected
    }
}

private fun mapIcon(icon: String): IconType {
    return getEnumTypeValue<IconType>(icon)
        ?: IconType.POSITIVE
}

fun generateSmallCheckIconMlcMockData(
    icon: IconType,
    selectionState: UIState.Selection
): SmallCheckIconMlcData {
    return SmallCheckIconMlcData(icon = icon, label = "label", selectionState = selectionState)
}

@Composable
@Preview
fun SmallCheckIconMlcRainbowPreview() {
    SmallCheckIconMlc(
        data = generateSmallCheckIconMlcMockData(
            icon = IconType.RAINBOW,
            selectionState = UIState.Selection.Selected
        )
    ) { }
}


@Composable
@Preview
fun SmallCheckIconMlcPinkPreview() {
    SmallCheckIconMlc(
        data = generateSmallCheckIconMlcMockData(
            icon = IconType.PINK,
            selectionState = UIState.Selection.Selected
        )
    ) { }
}

@Composable
@Preview
fun SmallCheckIconMlcInformativePreview() {
    SmallCheckIconMlc(
        data = generateSmallCheckIconMlcMockData(
            icon = IconType.INFORMATIVE,
            selectionState = UIState.Selection.Selected
        )
    ) { }
}

@Composable
@Preview
fun SmallCheckIconMlcWarningPreview() {
    SmallCheckIconMlc(
        data = generateSmallCheckIconMlcMockData(
            icon = IconType.WARNING,
            selectionState = UIState.Selection.Selected
        )
    ) { }
}


@Composable
@Preview
fun SmallCheckIconMlcBluePreview() {
    SmallCheckIconMlc(
        data = generateSmallCheckIconMlcMockData(
            icon = IconType.BLUE,
            selectionState = UIState.Selection.Unselected
        )
    ) { }
}

@Composable
@Preview
fun SmallCheckIconMlcPositivePreview() {
    SmallCheckIconMlc(
        data = generateSmallCheckIconMlcMockData(
            icon = IconType.POSITIVE,
            selectionState = UIState.Selection.Selected
        )
    ) { }
}