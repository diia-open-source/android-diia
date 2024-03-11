package ua.gov.diia.ui_base.components.atom.divider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Deprecated(
    """Not a Design System element. Should be used only for 'old' PublicServices
            (created and implemented before redesign) """
)
@Composable
fun TableDividerAtm(
    data: TableDividerAtmData
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        SpacerAtm(data = SpacerAtmData(data.spacerAbove))
        DividerSlimAtom(color = PeriwinkleGray)
    }
}

data class TableDividerAtmData(val spacerAbove: SpacerAtmType) : UIElementData

@Preview
@Composable
fun TableDividerAtmPreview() {
    TableDividerAtm(data = TableDividerAtmData(spacerAbove = SpacerAtmType.SPACER_24))
}