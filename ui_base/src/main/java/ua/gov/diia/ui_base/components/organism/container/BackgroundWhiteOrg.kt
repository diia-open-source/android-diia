package ua.gov.diia.ui_base.components.organism.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.TextLabelAtm
import ua.gov.diia.core.models.common_compose.org.container.BackgroundWhiteOrg
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.core.models.common_compose.table.TableSecondaryHeadingMlc
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtmData
import ua.gov.diia.ui_base.components.atom.text.TextLabelAtmMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.TableItemCheckboxMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableSecondaryHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.theme.Grey
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun BackgroundWhiteOrg(
    modifier: Modifier = Modifier,
    data: BackgroundWhiteOrgData,
    onUIAction: (UIAction) -> Unit

) {
    Column(
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .testTag(data.componentId?.asString() ?: "")
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        data.items.forEach { item ->
            when (item) {
                is TableMainHeadingMlcData -> {
                    TableMainHeadingMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is TableItemCheckboxMlcData -> {
                    TableItemCheckboxMlc(
                        data = item,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

data class BackgroundWhiteOrgData(
    val actionKey: String = UIActionKeysCompose.BACKGROUND_WHITE_ORG,
    val componentId: UiText? = null,
    val items: List<UIElementData>,
) : UIElementData {
    fun changeCheckboxState(id: String): BackgroundWhiteOrgData {
        val items = this.items
        return this.copy(
            items = SnapshotStateList<UIElementData>().apply {
                items.forEach {
                    if (it is TableItemCheckboxMlcData && (it.componentId as UiText.DynamicString).value == id) {
                        add(it.onCheckboxClick())
                    } else {
                        add(it)
                    }
                }
            }.toList()
        )
    }
}

fun BackgroundWhiteOrg.toUIModel(): BackgroundWhiteOrgData {
    val items = items
    return BackgroundWhiteOrgData(
        componentId = this.componentId.toDynamicStringOrNull(),
        items = SnapshotStateList<UIElementData>().apply {
            items?.forEach { item ->
                item.tableMainHeadingMlc?.let {
                    add(it.toUIModel())
                }
                item.tableSecondaryHeadingMlc?.let {
                    add(it.toUIModel())
                }
                item.tableItemCheckboxMlc?.let {
                    add(it.toUIModel() as UIElementData)
                }
            }
        }
    )
}

@Preview
@Composable
fun BackgroundWhiteOrg_AllActive() {
    val data = generateBackgroundWhiteOrgMockData(BackgroundWhiteOrgMockType.all_active)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        BackgroundWhiteOrg(
            data = state
        ) {
            it.data?.let {
                state = state.changeCheckboxState(it)
            }
        }
    }
}

@Preview
@Composable
fun BackgroundWhiteOrg_PartActive() {
    val data = generateBackgroundWhiteOrgMockData(BackgroundWhiteOrgMockType.part_active)
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        BackgroundWhiteOrg(
            data = state
        ) {
            it.data?.let {
                state = state.changeCheckboxState(it)
            }
        }
    }
}


@Preview
@Composable
fun BackgroundWhiteOrg_FromJson() {
    val firstTitle = TableMainHeadingMlc(
        componentId = "1",
        label = "First title",
        description = null,
        icon = null
    )
    val secondTitle = TableSecondaryHeadingMlc(
        componentId = "2",
        label = "Second title",
        description = null,
        icon = null
    )
    val checkbox = ua.gov.diia.core.models.common_compose.mlc.checkbox.TableItemCheckboxMlc(
        componentId = "1",
        inputCode = "inputCode",
        mandatory = true,
        items = listOf(
            TextLabelAtm(
                componentId = "someId",
                mode = TextLabelAtm.Mode.PRIMARY,
                label = "label",
                value = "value"
            )
        ),
        isSelected = true,
        isEnabled = true
    )
    val json = BackgroundWhiteOrg(
        componentId = "123",
        items = listOf(
            BackgroundWhiteOrg.Item(tableMainHeadingMlc = firstTitle),
            BackgroundWhiteOrg.Item(tableSecondaryHeadingMlc = secondTitle),
            BackgroundWhiteOrg.Item(tableItemCheckboxMlc = checkbox))
    )
    val data = json.toUIModel()
    var state by remember { mutableStateOf(data) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey)
    ) {
        BackgroundWhiteOrg(
            data = state
        ) {
            it.data?.let {
                state = state.changeCheckboxState(it)
            }
        }
    }
}

enum class BackgroundWhiteOrgMockType {
    all_active, part_active
}

fun generateBackgroundWhiteOrgMockData(mockType: BackgroundWhiteOrgMockType): BackgroundWhiteOrgData {
    val tableMainHeadingMlc = "Main title".toDynamicString().toTableMainHeadingMlcData()
    val tableSecondaryHeadingMlc = "Secondary title".toDynamicString().toTableSecondaryHeadingMlcData()
    val row1 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.PRIMARY,
        label = "Освіта:".toDynamicString(),
        value = "Вища".toDynamicString(),
        isEnabled = true
    )
    val row2 = TextLabelAtmData(
        componentId = "default".toDynamicString(),
        mode = TextLabelAtmMode.SECONDARY,
        label = "Заклад освіти:".toDynamicString(),
        value = "Академія кіно, доміно, ВЛК та нових медіа".toDynamicString(),
        isEnabled = true
    )
    val checkboxMlc1 = TableItemCheckboxMlcData(
        componentId = "1".toDynamicString(),
        rows = listOf(
            row1, row2
        ),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
        mandatory = true
    )
    val checkboxMlc2 = TableItemCheckboxMlcData(
        componentId = "2".toDynamicString(),
        rows = listOf(
            row1, row2
        ),
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected,
        mandatory = true
    )

    val checkboxMlc3 = TableItemCheckboxMlcData(
        componentId = "2".toDynamicString(),
        rows = listOf(
            row1, row2
        ),
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected,
        mandatory = true
    )

    return when (mockType) {
        BackgroundWhiteOrgMockType.all_active -> BackgroundWhiteOrgData(
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc)
                add(tableSecondaryHeadingMlc)
                add(checkboxMlc1)
                add(checkboxMlc2)
            }
        )

        BackgroundWhiteOrgMockType.part_active -> BackgroundWhiteOrgData(
            items = SnapshotStateList<UIElementData>().apply {
                add(tableMainHeadingMlc)
                add(tableSecondaryHeadingMlc)
                add(checkboxMlc1)
                add(checkboxMlc3)
            }
        )
    }
}