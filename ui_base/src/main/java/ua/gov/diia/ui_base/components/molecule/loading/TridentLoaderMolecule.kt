package ua.gov.diia.ui_base.components.molecule.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm

@Composable
fun TridentLoaderMolecule(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 80.dp),

            contentAlignment = Alignment.Center
        ) {
            TridentLoaderAtm(modifier = Modifier)
        }
    }
}

@Preview
@Composable
fun DocsTabLoadingMoleculePreview() {
    TridentLoaderMolecule()
}