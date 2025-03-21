package ua.gov.diia.ui_base.components.atom.status

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.chip.Type
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.Green
import ua.gov.diia.ui_base.components.theme.Grey
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.Yellow

@Composable
fun ChipStatusAtm(modifier: Modifier = Modifier, data: ChipStatusAtmData) {
    Box(
        modifier = modifier
            .height(18.dp)
            .background(
                color = when (data.type) {
                    StatusChipType.NEUTRAL -> Grey
                    StatusChipType.NEGATIVE -> Red
                    StatusChipType.POSITIVE -> Green
                    StatusChipType.PENDING -> Yellow
                    StatusChipType.WHITE -> White
                }, shape = RoundedCornerShape(10.dp)
            )
            .conditional(condition = data.type == StatusChipType.WHITE) {
                border(1.dp, color = BlackAlpha30, shape = RoundedCornerShape(10.dp))
            }
            .padding(horizontal = 8.dp)
            .testTag(data.componentId?.asString() ?: ""),
        contentAlignment = Alignment.Center
    ) {
        data.title?.let {
            Text(
                textAlign = TextAlign.Center,
                text = it,
                color = when (data.type) {
                    StatusChipType.NEUTRAL, StatusChipType.PENDING -> Black
                    StatusChipType.NEGATIVE, StatusChipType.POSITIVE -> White
                    StatusChipType.WHITE -> Black
                }, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    letterSpacing = TextUnit(-0.2F, TextUnitType.Sp)
                )
            )
        }
    }

}

data class ChipStatusAtmData(
    val componentId: UiText? = null,
    val type: StatusChipType,
    val title: String? = null
) : UIElementData

enum class StatusChipType {
    NEUTRAL, NEGATIVE, POSITIVE, PENDING, WHITE
}

fun ChipStatusAtm.toUiModel(): ChipStatusAtmData {
    return ChipStatusAtmData(
        title = name,
        type = when (type) {
            Type.SUCCESS.id -> StatusChipType.POSITIVE
            Type.PENDING.id -> StatusChipType.PENDING
            Type.FAIL.id -> StatusChipType.NEGATIVE
            Type.NEUTRAL.id -> StatusChipType.NEUTRAL
            Type.WHITE.id -> StatusChipType.WHITE
            else -> StatusChipType.NEUTRAL
        },
        componentId = this.componentId.toDynamicStringOrNull()
    )
}

fun generateChipStatusAtmMockData(type: StatusChipType): ChipStatusAtmData {
    return ChipStatusAtmData(
        componentId = null,
        type = type,
        title = "Status"
    )
}

@Preview
@Composable
fun StatusChipAtomState_Neutral() {
    val data = ChipStatusAtmData(type = StatusChipType.NEUTRAL, title = "STATUS")
    ChipStatusAtm(
        modifier = Modifier.padding(16.dp), data = data
    )
}

@Preview
@Composable
fun StatusChipAtomState_Positive() {
    val state = ChipStatusAtmData(type = StatusChipType.POSITIVE, title = "STATUS")
    ChipStatusAtm(
        modifier = Modifier.padding(16.dp), data = state
    )
}

@Preview
@Composable
fun StatusChipAtomState_Negative() {
    val data = ChipStatusAtmData(type = StatusChipType.NEGATIVE, title = "STATUS")
    ChipStatusAtm(
        modifier = Modifier.padding(16.dp), data = data
    )
}

@Preview
@Composable
fun StatusChipAtomState_Pending() {
    val data = ChipStatusAtmData(type = StatusChipType.PENDING, title = "STATUS")
    ChipStatusAtm(
        modifier = Modifier.padding(16.dp), data = data
    )
}

@Preview
@Composable
fun StatusChipAtomState_white() {
    val data = ChipStatusAtmData(type = StatusChipType.WHITE, title = "WHITE")
    ChipStatusAtm(
        modifier = Modifier.padding(16.dp), data = data
    )
}