package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.DashboardCardMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnSemiLightAtm
import ua.gov.diia.ui_base.components.atom.button.BtnSemiLightAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.DodgerBlue
import ua.gov.diia.ui_base.components.theme.GrannySmithApple
import ua.gov.diia.ui_base.components.theme.MiddleBlueGreen
import ua.gov.diia.ui_base.components.theme.StBlue
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun DashboardCardMlc(
    modifier: Modifier = Modifier,
    data: DashboardCardMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .height(132.dp)
            .defaultMinSize(minWidth = 159.dp)
            .background(
                brush = when (data.type) {
                    DashboardCardMlcType.TRANSPARENT -> Brush.linearGradient(
                        1f to White,
                        1f to White
                    )

                    DashboardCardMlcType.BLUE -> Brush.linearGradient(
                        0f to DodgerBlue,
                        1f to StBlue
                    )

                    DashboardCardMlcType.GREEN -> Brush.linearGradient(
                        0f to MiddleBlueGreen,
                        1f to GrannySmithApple
                    )
                }, shape = RoundedCornerShape(16.dp),
                alpha = when (data.type) {
                    DashboardCardMlcType.BLUE, DashboardCardMlcType.GREEN -> 0.5f
                    else -> {
                        1f
                    }
                }
            )
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (data.icon != null && data.label != null) {
                Row {
                    UiIconWrapperSubatomic(
                        modifier = Modifier.size(20.dp),
                        icon = data.icon
                    )
                    Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = data.label.asString(),
                            modifier = Modifier.padding(start = 8.dp),
                            style = DiiaTextStyle.t4TextSmallDescription,
                            color = Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (data.valueLarge != null && data.valueSmall != null) {
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = data.valueLarge,
                        style = DiiaTextStyle.h2MediumHeading,
                        color = Black
                    )
                    Text(
                        text = data.valueSmall,
                        Modifier.padding(top = 3.dp),
                        style = DiiaTextStyle.h3SmallHeading,
                        color = Black
                    )
                }
            }
            if (data.description != null) {
                val descriptionText = data.description.asString()
                if (descriptionText.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = descriptionText,
                        style = DiiaTextStyle.t3TextBody,
                        color = BlackAlpha50
                    )
                }
            }
            if (data.btnSemiLightAtm != null) {
                BtnSemiLightAtm(
                    modifier = Modifier.padding(top = 16.dp),
                    data = data.btnSemiLightAtm, onUIAction
                )
            }
            if (data.iconCenter != null && data.descriptionCenter != null) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UiIconWrapperSubatomic(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(24.dp),
                        icon = data.iconCenter
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        text = data.descriptionCenter.asString(),
                        style = DiiaTextStyle.t3TextBody,
                        color = Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

data class DashboardCardMlcData(
    val actionKey: String = UIActionKeysCompose.DASHBOARD_CARD_MLC,
    val id: String? = null,
    val componentId: UiText? = null,
    val type: DashboardCardMlcType,
    val icon: UiIcon? = null,
    val label: UiText? = null,
    val valueLarge: String? = null,
    val valueSmall: String? = null,
    val description: UiText? = null,
    val iconCenter: UiIcon? = null,
    val descriptionCenter: UiText? = null,
    val action: DataActionWrapper? = null,
    val btnSemiLightAtm: BtnSemiLightAtmData? = null
) : UIElementData, DashboardCardTileOrgItem

fun DashboardCardMlc.toUIModel(): DashboardCardMlcData {
    return DashboardCardMlcData(
        label = label.toDynamicString(),
        componentId = componentId?.let { UiText.DynamicString(it) },
        action = action?.toDataActionWrapper(),
        type = when (type) {
            DashboardCardMlc.DashboardCardType.description -> DashboardCardMlcType.GREEN
            DashboardCardMlc.DashboardCardType.button -> DashboardCardMlcType.BLUE
            DashboardCardMlc.DashboardCardType.empty -> DashboardCardMlcType.TRANSPARENT
        },
        description = this.description?.let { UiText.DynamicString(it) },
        descriptionCenter = this.descriptionCenter?.let { UiText.DynamicString(it) },
        icon = this.icon?.let { UiIcon.DrawableResource(it) },
        iconCenter = this.iconCenter?.let { UiIcon.DrawableResource(it) },
        valueLarge = this.valueLarge,
        valueSmall = this.valueSmall,
        btnSemiLightAtm = this.btnSemiLightAtm?.toUIModel()
    )
}

enum class DashboardCardMlcType {
    GREEN,
    BLUE,
    TRANSPARENT
}

fun generateDashboardCardMlcMockData(type: DashboardCardMlcType): DashboardCardMlcData {
    return when (type) {
        DashboardCardMlcType.GREEN -> DashboardCardMlcData(
            id = "id",
            label = UiText.DynamicString("Label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
            valueLarge = "0.",
            valueSmall = "00 $",
            description = UiText.DynamicString("description"),
            type = DashboardCardMlcType.GREEN
        )

        DashboardCardMlcType.BLUE -> DashboardCardMlcData(
            id = "id",
            label = UiText.DynamicString("Label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
            valueLarge = "0.",
            valueSmall = "00 $",
            type = DashboardCardMlcType.BLUE,
            btnSemiLightAtm = BtnSemiLightAtmData(
                label = UiText.DynamicString("Label"),
                id = "",
                interactionState = UIState.Interaction.Enabled
            )
        )

        DashboardCardMlcType.TRANSPARENT -> DashboardCardMlcData(
            id = "id",
            iconCenter = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
            descriptionCenter = UiText.DynamicString(
                "description \n" +
                        "in two lines"
            ),
            type = DashboardCardMlcType.TRANSPARENT
        )
    }
}

@Composable
@Preview
fun DashboardCardMlc_Green() {
    DashboardCardMlc(data = generateDashboardCardMlcMockData(DashboardCardMlcType.GREEN)) {}
}

@Composable
@Preview
fun DashboardCardMlc_Blue() {
    DashboardCardMlc(data = generateDashboardCardMlcMockData(DashboardCardMlcType.BLUE)) {}
}

@Composable
@Preview
fun DashboardCardMlc_Transparent() {
    DashboardCardMlc(data = generateDashboardCardMlcMockData(DashboardCardMlcType.TRANSPARENT)) {}
}