package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.runtime.Composable
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule

@Composable
fun TridentLoaderBlock(
    contentLoaded: Pair<String, Boolean>,
) {
    if (!contentLoaded.second && contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_TRIDENT) {
        TridentLoaderMolecule()
    }
}