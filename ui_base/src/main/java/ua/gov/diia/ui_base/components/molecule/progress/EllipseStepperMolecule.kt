package ua.gov.diia.ui_base.components.molecule.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.icon.EllipseStepperAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.state.UIState

@Composable
fun EllipseStepperMolecule(
    modifier: Modifier = Modifier,
    data: EllipseStepperMoleculeData,
) {
    val bulletSize = dimensionResource(id = R.dimen.pin_input_length_indicator_size)
    Row(
        modifier
            .wrapContentWidth()
            .height(bulletSize)
    ) {
        for (position in 0 until data.numberOfBullets) {
            val selection = if (position < data.selectedPosition)
                UIState.Selection.Selected
            else
                UIState.Selection.Unselected
            EllipseStepperAtom(Modifier.size(bulletSize), selection)

            val lastBullet = position == data.numberOfBullets - 1
            if (!lastBullet) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

data class EllipseStepperMoleculeData(
    val numberOfBullets: Int,
    val selectedPosition: Int
) : UIElementData {

    fun onSelectedPositionChanged(position: Int): EllipseStepperMoleculeData {
        return this.copy(selectedPosition = position)
    }
}

@Preview
@Composable
fun EllipseStepperMoleculePreview_Unselected() {
    Box(modifier = Modifier.wrapContentSize()) {
        EllipseStepperMolecule(data = EllipseStepperMoleculeData(4, 0))
    }
}

@Preview
@Composable
fun EllipseStepperMoleculePreview_Selected() {
    Box(modifier = Modifier.wrapContentSize()) {
        EllipseStepperMolecule(data = EllipseStepperMoleculeData(4, 1))
    }
}