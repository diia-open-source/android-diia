package ua.gov.diia.ui_base.components.atom.divider

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.BlackSqueeze

@Composable
fun DividerSlimAtom(
    modifier: Modifier = Modifier,
    color: Color = BlackSqueeze
) {
    Divider(modifier = modifier, thickness = 1.dp, color = color)
}