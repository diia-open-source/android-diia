package ua.gov.diia.ui_base.components.molecule.divider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.SpacerAtmType
import ua.gov.diia.core.models.common_compose.mlc.divider.DividerLineMlc
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue

@Composable
fun DividerLineMlc(
    modifier: Modifier = Modifier,
    data: DividerLineMlcData,
    color: Color = ColumbiaBlue
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        SpacerAtm(data = SpacerAtmData(SpacerAtmType.SPACER_24))
        Divider(
            modifier = modifier,
            thickness = 2.dp,
            color = color
        )
    }
}

@Preview
@Composable
fun DividerLineMlcPreview() {
    DividerLineMlc(data = DividerLineMlcData(type = "default"))
}

data class DividerLineMlcData(
    val componentId: UiText? = null,
    val type: String?
) : UIElementData

fun DividerLineMlc.toUiModel(): DividerLineMlcData {
    return DividerLineMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        type = this.type
    )
}
