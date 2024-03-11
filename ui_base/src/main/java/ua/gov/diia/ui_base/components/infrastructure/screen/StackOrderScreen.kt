package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemDragOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemDragOrgData


@Composable
fun StackOrderScreen(
    modifier: Modifier = Modifier,
    toolbar: SnapshotStateList<UIElementData>,
    body: SnapshotStateList<UIElementData>,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit,
    onMove: (Int, Int) -> Unit,
) {
    Column(
        modifier = modifier
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        toolbar.forEach {
            if (it is TopGroupOrgData){
                TopGroupOrg(
                    modifier = modifier,
                    data = it,
                    onUIAction = onUIAction,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                )
            }
            if (it is NavigationPanelMlcData) {
                NavigationPanelMlc(
                    modifier = modifier
                        .fillMaxWidth(),
                    data = it,
                    onUIAction = onUIAction
                )
            }
        }

        body.forEach {
            if (it is ListItemDragOrgData) {
                ListItemDragOrg(
                    modifier = modifier.fillMaxHeight(),
                    data = it,
                    onMove = onMove,
                    onUIAction = onUIAction,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                )
            }
        }
    }
}