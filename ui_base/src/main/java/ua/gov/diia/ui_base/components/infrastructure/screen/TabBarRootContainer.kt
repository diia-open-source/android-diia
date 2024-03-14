package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganism
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData

@Composable
fun TabBarRootContainer(
    modifier: Modifier = Modifier,
    tabBarViews: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier.navigationBarsPadding()
    ) {
        tabBarViews.forEach {
            when (it) {
                is TabBarOrganismData -> {
                    TabBarOrganism(
                        modifier = modifier,
                        data = it,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}
