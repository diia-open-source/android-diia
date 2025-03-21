package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.list.toUiModel
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ListItemGroupOrg(
    modifier: Modifier = Modifier,
    data: ListItemGroupOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit

) {
    var isLoading by remember {
        mutableStateOf(checkIfAnyElementInLoadingState(data.itemsList, progressIndicator))
    }

    LaunchedEffect(key1 = progressIndicator) {
        isLoading = checkIfAnyElementInLoadingState(data.itemsList, progressIndicator)
    }

    Column(
        modifier = modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: ""),
    ) {

        data.title?.let {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .conditional(data.itemsList.none {
                        it.interactionState == UIState.Interaction.Enabled
                    }) {
                        alpha(0.3f)
                    },
                text = data.title.asString(),
                style = DiiaTextStyle.t3TextBody,
                color = Black
            )
            DividerSlimAtom(color = BlackSqueeze)
        }

        if(data.itemsList.isNotEmpty()){
            data.itemsList.forEachIndexed { index, item ->
                ListItemMlc(
                    data = item.copy(
                        interactionState = if (isLoading) {
                            if (progressIndicator.first == item.id) {
                                item.interactionState
                            } else {
                                UIState.Interaction.Disabled
                            }
                        } else {
                            item.interactionState
                        }
                    ),
                    progressIndicator = progressIndicator,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = it.data,
                                optionalId = data.id,
                                action = it.action
                            )
                        )
                    },
                )
                if (index != data.itemsList.size - 1) {
                    DividerSlimAtom(color = BlackSqueeze)
                }
            }
        }

// BTN ADD ITEM
        if(data.button != null && data.itemsList.isNotEmpty()) {
            DividerSlimAtom(
                modifier = Modifier.height(1.dp),
                color = BlackSqueeze
            )
        }
        data.button?.let {
            Row(
                modifier = Modifier.padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                BtnPlainIconAtm(
                    modifier = modifier,
                    data = data.button,

                    ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            action = data.button.action
                        )
                    )
                }
            }
        }
    }
}

private fun checkIfAnyElementInLoadingState(
    items: List<ListItemMlcData>,
    progressIndicator: Pair<String, Boolean>
): Boolean {
    return items.any {
        it.id == progressIndicator.first && progressIndicator.second
    }
}

data class ListItemGroupOrgData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
    val id: String? = null,
    val componentId: UiText? = null,
    val title: UiText? = null,
    val itemsList: List<ListItemMlcData>,
    val button: BtnPlainIconAtmData? = null
    ) : UIElementData

fun ListItemGroupOrg.toUIModel(): ListItemGroupOrgData {
    val entity: ListItemGroupOrg = this
    return ListItemGroupOrgData(
        componentId = this.componentId.toDynamicStringOrNull(),
        title = this.title?.let { it.toDynamicString() },
        itemsList = SnapshotStateList<ListItemMlcData>().apply {
            entity.items.forEachIndexed { index, item ->
                add(item.toUiModel(id = index.toString()))
            }
        },
        button = this.btnPlainIconAtm?.toUiModel()
    )
}

enum class ActionTypes {
    enabled, disabled, invisible
}

enum class ListItemGroupOrgMockType {
    default, disabled, disabledAndEnabled, withBtnAdd, btnAddWithoutElements
}

fun generateListItemGroupOrgData(mockType: ListItemGroupOrgMockType): ListItemGroupOrgData {
    return when(mockType) {
        ListItemGroupOrgMockType.default -> ListItemGroupOrgData(
            title = UiText.DynamicString("title:"),
            itemsList = SnapshotStateList<ListItemMlcData>().apply {
                repeat(3) {
                    add(
                        ListItemMlcData(
                            id = it.toString(),
                            label = UiText.DynamicString("Label"),
                            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code)
                        )
                    )
                }
            }
        )
        ListItemGroupOrgMockType.disabled -> ListItemGroupOrgData(
            title = UiText.DynamicString("title:"),
            itemsList = SnapshotStateList<ListItemMlcData>().apply {
                repeat(3) {
                    add(
                        ListItemMlcData(
                            id = it.toString(),
                            label = UiText.DynamicString("Label"),
                            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                            interactionState = UIState.Interaction.Disabled
                        )
                    )
                }
            }
        )

        ListItemGroupOrgMockType.disabledAndEnabled -> ListItemGroupOrgData(
            title = UiText.DynamicString("title:"),
            itemsList = SnapshotStateList<ListItemMlcData>().apply {
                add(
                    ListItemMlcData(
                        id = "999",
                        label = UiText.DynamicString("Label"),
                        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                        interactionState = UIState.Interaction.Enabled
                    )
                )
                repeat(2) {
                    add(
                        ListItemMlcData(
                            id = it.toString(),
                            label = UiText.DynamicString("Label"),
                            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                            interactionState = UIState.Interaction.Disabled
                        )
                    )
                }
            }
        )
        ListItemGroupOrgMockType.withBtnAdd -> ListItemGroupOrgData(
            title = UiText.DynamicString("title:"),
            itemsList = SnapshotStateList<ListItemMlcData>().apply {
                repeat(3) {
                    add(
                        ListItemMlcData(
                            id = it.toString(),
                            label = UiText.DynamicString("Label"),
                            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code)
                        )
                    )
                }
            },
            button = BtnPlainIconAtmData(
                id = "123",
                label = UiText.DynamicString("Додати"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
            )
        )
        ListItemGroupOrgMockType.btnAddWithoutElements -> ListItemGroupOrgData(
            itemsList = listOf(),
            button = BtnPlainIconAtmData(
                id = "123",
                label = UiText.DynamicString("Додати"),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
            )
        )
    }
}




@Composable
@Preview
fun ListItemGroupOrgPreview() {
    ListItemGroupOrg(data = generateListItemGroupOrgData(ListItemGroupOrgMockType.default)) {}
}

@Composable
@Preview
fun ListItemGroupOrgPreview_Disabled() {
    ListItemGroupOrg(data = generateListItemGroupOrgData(ListItemGroupOrgMockType.disabled)) {}
}

@Composable
@Preview
fun ListItemGroupOrgPreview_Disabled_and_Enabled() {
    ListItemGroupOrg(data = generateListItemGroupOrgData(ListItemGroupOrgMockType.disabledAndEnabled)) {}
}

@Composable
@Preview
fun ListItemGroupOrgPreview_BtnAdd() {
    ListItemGroupOrg(data = generateListItemGroupOrgData(ListItemGroupOrgMockType.withBtnAdd)) {}
}


@Composable
@Preview
fun ListItemGroupOrgPreview_BtnAdd_WithoutList() {
    ListItemGroupOrg(data = generateListItemGroupOrgData(ListItemGroupOrgMockType.btnAddWithoutElements)) {}
}

@Composable
@Preview
fun ListItemGroupOrgPreview_Loading() {
    val isLoading by remember {
        mutableStateOf(false)
    }
    val loadingItemId = "999"

    var progressIndicator by remember {
        mutableStateOf(loadingItemId to isLoading)
    }

    val state = ListItemGroupOrgData(
        title = UiText.DynamicString("title:"),
        itemsList = SnapshotStateList<ListItemMlcData>().apply {
            add(
                ListItemMlcData(
                    id = "2",
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                    interactionState = UIState.Interaction.Disabled
                )
            )
            add(
                ListItemMlcData(
                    id = loadingItemId,
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                    interactionState = UIState.Interaction.Enabled
                )
            )
            add(
                ListItemMlcData(
                    id = "1",
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
                    interactionState = UIState.Interaction.Enabled
                )
            )
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListItemGroupOrg(data = state, progressIndicator = progressIndicator) {
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = {
            progressIndicator = progressIndicator.copy(second = !progressIndicator.second)
        }) {
            Text("Change loading state")
        }
    }
}