package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.Honeydew

@Composable
fun EllipseStepperAtom(
    modifier: Modifier = Modifier,
    state: UIState.Selection = UIState.Selection.Unselected
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                if (state == UIState.Selection.Unselected) Honeydew else Black
            )
    )

}

@Preview
@Composable
fun EllipseStepperAtomPreview_Unselected() {
    EllipseStepperAtom()
}

@Preview
@Composable
fun EllipseStepperAtomPreview_Selected() {
    EllipseStepperAtom(state = UIState.Selection.Selected)
}