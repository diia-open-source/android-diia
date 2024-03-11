package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrg
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrg
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData

@Composable
fun BottomBarRootContainer(
    modifier: Modifier = Modifier,
    bottomViews: SnapshotStateList<UIElementData>,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    if (bottomViews.size != 0) {
        Column(modifier = modifier) {
            bottomViews.forEachIndexed { index, element ->
                when (element) {
                    is BottomGroupOrgData -> {
                        BottomGroupOrg(
                            data = element,
                            progressIndicator = progressIndicator,
                            onUIAction = onUIAction
                        )
                    }

                    is CheckboxBtnOrgData -> {
                        CheckboxBtnOrg(
                            data = element,
                            progressIndicator = progressIndicator,
                            onUIAction = onUIAction
                        )
                    }
                    is ListItemGroupOrgData -> {
                        ListItemGroupOrg(
                            data = element,
                            onUIAction = onUIAction,
                            progressIndicator = progressIndicator,
                            diiaResourceIconProvider = diiaResourceIconProvider,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomBarRootContainerPreview_Case1() {

}