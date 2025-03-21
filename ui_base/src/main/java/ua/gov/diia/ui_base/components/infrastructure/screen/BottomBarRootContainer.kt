package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.text.LargeTickerAtmData
import ua.gov.diia.ui_base.components.atom.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrg
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnWhiteOrg
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnWhiteOrgData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrg
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData

@Composable
fun BottomBarRootContainer(
    modifier: Modifier = Modifier,
    bottomViews: SnapshotStateList<UIElementData>,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val lazyListScrollState = rememberLazyListState()
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
                            progressIndicator = progressIndicator
                        )
                    }

                    is BtnLoadIconPlainGroupMlcData -> {
                        BtnLoadIconPlainGroupMlc(
                            data = element,
                            onUIAction = onUIAction,
                            progressIndicator = progressIndicator
                        )
                        AddBottomPadding(true)
                    }
                    is CheckboxBtnWhiteOrgData -> {
                        CheckboxBtnWhiteOrg(
                            data = element,
                            onUIAction = onUIAction,
                            progressIndicator = progressIndicator
                        )
                    }
                    is TickerAtomData -> {
                        TickerAtm(
                            data = element,
                            onUIAction = onUIAction,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddBottomPadding(displayBottomSpacer: Boolean) {
    if (displayBottomSpacer) {
        Box(modifier = Modifier) {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun BottomBarRootContainerPreview_Case1() {

}