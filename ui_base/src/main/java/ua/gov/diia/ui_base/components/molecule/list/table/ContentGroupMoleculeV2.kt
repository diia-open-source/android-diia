package ua.gov.diia.ui_base.components.molecule.list.table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.ContentGroupItemV2
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.TableBlockOrg
import ua.gov.diia.ui_base.components.molecule.list.table.items.contentgroup.TableBlockOrgData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemPrimaryMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableHeadingMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData

@Deprecated("Use tableBlockOrg")
@Composable
fun ContentGroupMoleculeV2(
    modifier: Modifier = Modifier,
    data: ContentGroupMoleculeV2Data,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        data.items.forEachIndexed { index, item ->
            when (item) {
                is TableBlockOrgData -> {
                    TableBlockOrg(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                else -> {

                }
            }

            if (index != data.items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class ContentGroupMoleculeV2Data(val items: List<ContentGroupItemV2>) : UIElementData

@Composable
@Preview
fun ContentGroupMoleculeV2Preview() {
    val tableBlockStateH = TableBlockOrgData(
        header = TableHeadingMoleculeData(id = "123", title = "Heading HorizontalTable".let { UiText.DynamicString(it) }),
        items = listOf(
            TableItemHorizontalMlcData(id = "1", title = UiText.DynamicString("Item title"), value = "Value"),
            TableItemHorizontalMlcData(id = "3", title = UiText.DynamicString("РНОКПП:"), value = "1234567890"),
            TableItemHorizontalMlcData(
                id = "4",
                title = UiText.DynamicString("Дата народження:"),
                value = "05.12.1991"
            ),
            TableItemHorizontalMlcData(
                id = "5",
                title = UiText.DynamicString("ПІБ:"),
                value = "Дія Надія \nВолодимирівна"
            ),
        )
    )

    val tableBlockStateV = TableBlockOrgData(
        header = TableHeadingMoleculeData(id = "123", title ="Heading VerticalTable".let { UiText.DynamicString(it) }),
        items = listOf(
            TableItemVerticalMlcData(id = "1", title = UiText.DynamicString("Item title:"), value = "Value"),
            TableItemVerticalMlcData(
                id = "5",
                title = UiText.DynamicString("Адреса:"),
                value = "м. Київ, Голосіївський район, вул. Генерала Тупікова,  буд. 12/а, кв. 16"
            ),
        )
    )

    val tableBlockStateP = TableBlockOrgData(
        items = listOf(
            TableItemPrimaryMlcData(
                id = "1",
                title = UiText.DynamicString("Номер сертифіката"),
                value = "1234567890".toDynamicString()
            ),
        )
    )

    val startState = ContentGroupMoleculeV2Data(
        items = listOf(
            tableBlockStateH,
            tableBlockStateV,
            tableBlockStateP
        )
    )

    val state = remember { mutableStateOf(startState) }


    ContentGroupMoleculeV2(modifier = Modifier, data = state.value,
        onUIAction = {

        })
}