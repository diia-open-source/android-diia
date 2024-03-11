package ua.gov.diia.ui_base.components.atom.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.BlackSqueezeTransparent
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue

@Composable
fun BoxScope.GradientDividerAtom(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 40.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BlackSqueezeTransparent,
                            BlackSqueeze
                        )
                    )
                )
        )
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = ColumbiaBlue)
    }
}

@Preview
@Composable
fun GradientDividerAtomPreview() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        GradientDividerAtom()
    }
}
