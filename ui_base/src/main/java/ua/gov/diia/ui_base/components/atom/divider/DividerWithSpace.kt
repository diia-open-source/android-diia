package ua.gov.diia.ui_base.components.atom.divider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.theme.Primary

@Composable
fun DividerWithSpace(
    modifier: Modifier = Modifier,
    color: Color = Primary,
    data: DividerWithSpaceData
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        SpacerAtm(data = SpacerAtmData(data.spaceAbove))
        Divider(modifier = modifier, thickness = 2.dp, color = color)
        SpacerAtm(data = SpacerAtmData(data.spaceBelow))
    }
}

data class DividerWithSpaceData(
    val spaceAbove: SpacerAtmType,
    val spaceBelow: SpacerAtmType
): UIElementData

@Preview
@Composable
fun SpaceDividerPreview() {
    DividerWithSpace(
        data = DividerWithSpaceData(
            spaceAbove = SpacerAtmType.SPACER_32,
            spaceBelow = SpacerAtmType.SPACER_32
        )
    )
}

