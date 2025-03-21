package ua.gov.diia.ui_base.components.organism.table

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.core.models.common_compose.table.TableItemVerticalMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockAccordionOrg.TableBlockAccordionOrg
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlc
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.PlusMinusClickableSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TableBlockAccordionOrg(
    modifier: Modifier = Modifier,
    data: TableBlockAccordionOrgData,
    onUIAction: (UIAction) -> Unit = {}
) {
    val expandState = remember { mutableStateOf(data.expandState) }

    LaunchedEffect(key1 = expandState.value) {
        onUIAction(
            UIAction(
                actionKey = data.actionKey,
                states = listOf(expandState.value)
            )
        )
    }
    Column(
        modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .noRippleClickable {
                    expandState.value = when (expandState.value) {
                        UIState.Expand.Collapsed -> UIState.Expand.Expanded
                        UIState.Expand.Expanded -> UIState.Expand.Collapsed
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.heading?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    color = Black,
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
            }
            PlusMinusClickableSubatomic(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(14.dp),
                expandState = expandState.value
            )
        }
        AnimatedVisibility(visible = expandState.value == UIState.Expand.Expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                data.items?.forEachIndexed { index, item ->
                    when (item) {
                        is TableItemHorizontalMlcData -> {
                            TableItemHorizontalMlc(
                                modifier = Modifier,
                                data = item,
                                onUIAction = onUIAction
                            )
                        }

                        is TableItemVerticalMlcData -> {
                            TableItemVerticalMlc(
                                modifier = Modifier,
                                data = item,
                                onUIAction = onUIAction
                            )
                        }

                        is TableItemPrimaryMlcData -> {
                            TableItemPrimaryMlc(
                                modifier = Modifier,
                                data = item,
                                onUIAction = onUIAction
                            )
                        }

                        is SmallEmojiPanelMlcData -> {
                            SmallEmojiPanelMlc(
                                modifier = modifier,
                                data = item,
                            )
                        }

                        else -> {
                            //nothing
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

data class TableBlockAccordionOrgData(
    val actionKey: String = UIActionKeysCompose.TABLE_BLOCK_ACCORDION_ORG,
    val componentId: UiText? = null,
    val heading: String? = null,
    val expandState: UIState.Expand = UIState.Expand.Collapsed,
    val items: List<TableBlockItem>? = null
) : UIElementData

fun TableBlockAccordionOrg?.toUIModel(): TableBlockAccordionOrgData? {
    val entity = this
    if (entity?.items == null) return null
    val tbItems = mutableListOf<TableBlockItem>().apply {
        (entity.items as List<Item>).forEach { listMlcl ->
            if (listMlcl.tableItemHorizontalMlc is TableItemHorizontalMlc) {
                add(
                    TableItemHorizontalMlcData(
                        componentId = listMlcl.tableItemHorizontalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemHorizontalMlc?.label?.let {
                            UiText.DynamicString(it)
                        },
                        secondaryTitle = listMlcl.tableItemHorizontalMlc?.secondaryLabel?.let {
                            UiText.DynamicString(it)
                        },
                        value = listMlcl.tableItemHorizontalMlc?.value,
                        secondaryValue = listMlcl.tableItemHorizontalMlc?.secondaryValue,
                        supportText = listMlcl.tableItemHorizontalMlc?.supportingValue,
                        valueAsBase64String = listMlcl.tableItemHorizontalMlc?.valueImage
                    )
                )
            }

            if (listMlcl.tableItemVerticalMlc is TableItemVerticalMlc) {
                add(
                    TableItemVerticalMlcData(
                        componentId = listMlcl.tableItemVerticalMlc?.componentId.orEmpty(),
                        title = listMlcl.tableItemVerticalMlc?.label?.let { UiText.DynamicString(it) },
                        secondaryTitle = listMlcl.tableItemVerticalMlc?.secondaryLabel?.let { UiText.DynamicString(it) },
                        value = listMlcl.tableItemVerticalMlc?.value?.let { UiText.DynamicString(it) },
                        secondaryValue = listMlcl.tableItemVerticalMlc?.secondaryValue?.let { UiText.DynamicString(it) },
                        supportText = listMlcl.tableItemVerticalMlc?.supportingValue,
                        valueAsBase64String = listMlcl.tableItemVerticalMlc?.valueImage
                    )
                )
            }

            if (listMlcl.tableItemPrimaryMlc is TableItemPrimaryMlc) {
                val item = (listMlcl.tableItemPrimaryMlc as TableItemPrimaryMlc).toUIModel()
                item?.let {
                    add(it)
                }
            }
        }
    }
    return TableBlockAccordionOrgData(
        heading = this?.heading,
        items = tbItems,
        componentId = UiText.DynamicString(this?.componentId.orEmpty()),
        expandState = if (this?.isOpen == false) UIState.Expand.Collapsed else UIState.Expand.Expanded
    )
}


@Composable
@Preview
fun TableBlockAccordionOrgPreview() {
    val items = listOf(
        TableItemHorizontalMlcData(
            id = "1",
            title = UiText.DynamicString("Item title"),
            value = "Value"
        ),
        TableItemHorizontalMlcData(
            id = "2",
            title = UiText.DynamicString("Item title 2"),
            value = "Value"
        ),
        TableItemHorizontalMlcData(
            id = "3",
            title = UiText.DynamicString("Item title 3"),
            value = "Value"
        ),
        TableItemHorizontalMlcData(
            id = "4",
            title = UiText.DynamicString("Item title 4"),
            value = "Value"
        ),
        TableItemHorizontalMlcData(
            id = "5",
            title = UiText.DynamicString("Item title 5"),
            value = "Value"
        ),
    )

    val startState = TableBlockAccordionOrgData(
        heading = "Heading",
        expandState = UIState.Expand.Collapsed,
        items = items
    )

    TableBlockAccordionOrg(
        modifier = Modifier.padding(16.dp),
        data = startState,
        onUIAction = {
        }
    )
}