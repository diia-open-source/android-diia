package ua.gov.diia.ui_base.components.molecule.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.EggshellAlpha80

@Composable
fun FullScreenLoadingMolecule(
    modifier: Modifier = Modifier,
    backgroundColor: Color = EggshellAlpha80
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .noRippleClickable { // To block bottom layer
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color = BlackAlpha50, shape = RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            LoaderCircularEclipse23Subatomic(modifier = Modifier.size(24.dp))
        }
    }
}

@Preview
@Composable
fun FullScreenLoadingMoleculePreview() {
    FullScreenLoadingMolecule()
}