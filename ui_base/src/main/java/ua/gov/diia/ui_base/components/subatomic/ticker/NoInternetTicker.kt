package ua.gov.diia.ui_base.components.subatomic.ticker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.atom.text.TickerUsage

@Composable
fun NoInternetTicker(modifier: Modifier = Modifier) {
    TickerAtm(
        modifier = modifier.fillMaxWidth(),
        data = TickerAtomData(
            title = stringResource(id = R.string.no_internet),
            type = TickerType.WARNING,
            usage = TickerUsage.GRAND
        )
    ) {}
}

@Preview
@Composable
fun NoInternetTickerPreview() {
    NoInternetTicker()
}