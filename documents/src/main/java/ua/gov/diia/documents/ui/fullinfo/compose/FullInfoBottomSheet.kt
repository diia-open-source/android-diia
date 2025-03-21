package ua.gov.diia.documents.ui.fullinfo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.organism.document.ContentTableOrg
import ua.gov.diia.ui_base.components.organism.document.ContentTableOrgData
import ua.gov.diia.ui_base.components.organism.document.DocHeadingOrg
import ua.gov.diia.ui_base.components.organism.document.DocHeadingOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockOrg
import ua.gov.diia.ui_base.components.organism.document.TableBlockOrgData
import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrg
import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrgData
import ua.gov.diia.ui_base.components.organism.list.ChipBlackGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ChipBlackGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.theme.BlueHighlight
import ua.gov.diia.ui_base.components.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullInfoBottomSheet(
    modifier: Modifier = Modifier,
    progressIndicator: Pair<String, Boolean> = Pair("", true),
    data: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit,
) {
    var openBottomSheet by remember { mutableStateOf(true) }
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded
    )

    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue != SheetValue.Expanded) {
            onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.BOTTOM_SHEET_DISMISS
                )
            )
            openBottomSheet = false
        }
    }

    if (openBottomSheet) {
        BottomSheetScaffold(
            scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState),
            sheetDragHandle = {
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 10.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            Gray, RoundedCornerShape(4.dp)
                        )
                )
            },
            sheetContent = {
                Column(
                    modifier = Modifier
                        .provideTestTagsAsResourceId()
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .background(color = BlueHighlight)
                ) {
                    data.forEachIndexed { index, item ->
                        when (item) {
                            is DocHeadingOrgData -> {
                                DocHeadingOrg(
                                    modifier = Modifier.padding(
                                        bottom = 24.dp,
                                        start = 8.dp,
                                        end = 8.dp
                                    ),
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            is TickerAtomData -> {
                                TickerAtm(
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            is ContentTableOrgData -> {
                                ContentTableOrg(
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            is VerificationCodesOrgData -> {
                                VerificationCodesOrg(
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        start = 24.dp,
                                        end = 24.dp
                                    ),
                                    data = item,
                                    progressIndicator = progressIndicator,
                                    onUIAction = onUIAction
                                )
                            }

                            is TableBlockOrgData -> {
                                TableBlockOrg(
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            is ListItemGroupOrgData -> {
                                ListItemGroupOrg(
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }

                            is ChipBlackGroupOrgData -> {
                                ChipBlackGroupOrg(
                                    data = item,
                                    onUIAction = onUIAction
                                )
                            }
                        }
                        if (index == data.size - 1) {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            },
            content = {
                // Nothing
            },
            modifier = modifier.provideTestTagsAsResourceId(),
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = BlueHighlight,
            sheetContainerColor = BlueHighlight,
            sheetPeekHeight = 0.dp,
        )
    }
}