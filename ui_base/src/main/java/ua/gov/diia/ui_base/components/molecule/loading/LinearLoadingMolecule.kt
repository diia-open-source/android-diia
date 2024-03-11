package ua.gov.diia.ui_base.components.molecule.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.subatomic.loader.LineLoaderSubatomic
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun LinearLoadingMolecule(
    modifier: Modifier = Modifier,
    labelDisplayed: Boolean = true
) {
    Column(modifier = modifier) {
        LineLoaderSubatomic(modifier = modifier)
        if (labelDisplayed) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 24.dp),
                style = DiiaTextStyle.t3TextBody,
                text = stringResource(id = R.string.loading_ellipsized)
            )
        }

    }
}

@Composable
@Preview
fun LinearLoadingMoleculePreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(12.dp))
        LinearLoadingMolecule()
    }
}
