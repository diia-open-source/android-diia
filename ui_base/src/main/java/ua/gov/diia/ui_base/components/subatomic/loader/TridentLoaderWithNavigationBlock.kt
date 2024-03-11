package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

@Composable
fun TridentLoaderWithNavigationBlock(
    modifier: Modifier = Modifier,
    contentLoaded: Pair<String, Boolean>,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    if (!contentLoaded.second && (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION)) {
        Box(modifier = modifier.fillMaxWidth()) {
            TopGroupOrg(
                modifier = Modifier.align(Alignment.TopCenter),
                data = TopGroupOrgData(
                    navigationPanelMlcData = NavigationPanelMlcData(
                        title = UiText.DynamicString(""),
                        isContextMenuExist = false
                    )
                ),
                onUIAction = onUIAction,
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
            TridentLoaderMolecule(modifier = Modifier.align(Alignment.Center))
        }
    }
}