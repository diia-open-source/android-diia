package ua.gov.diia.ui_base.components.molecule.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * Applied in order to block all user actions for below elements
 */
@Composable
fun FullScreenTransparentMolecule() {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(enabled = true, indication = null, onClick = {}, interactionSource = interactionSource)
    )
}


@Preview
@Composable
fun FullScreenTransparentMoleculePreview() {
    FullScreenTransparentMolecule()
}