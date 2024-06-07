package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.payment.PaymentInfoOrg
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableBlockItem
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalLargeMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalLargeMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlc
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemHorizontalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toUIModel
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun PaymentInfoOrg(
    modifier: Modifier = Modifier,
    data: PaymentInfoOrgData
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 8.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .testTag(data.componentId?.asString() ?: "")
    ) {
        if (!data.itemsTableBlock.isNullOrEmpty()) {
            data.itemsTableBlock.forEach {
                if (it is TableItemHorizontalMlcData) {
                    TableItemHorizontalMlc(
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                        data = it
                    )
                }
            }

            val hasHorizontalMlc =
                data.itemsTableBlock.find { it is TableItemHorizontalMlcData } != null
            val hasLargeMlc =
                data.itemsTableBlock.find { it is TableItemHorizontalLargeMlcData } != null
            if (hasHorizontalMlc && hasLargeMlc) {
                DividerSlimAtom(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 11.dp),
                    color = BlackSqueeze
                )
            }

            data.itemsTableBlock.forEach {
                if (it is TableItemHorizontalLargeMlcData) {
                    TableItemHorizontalLargeMlc(
                        modifier = Modifier.padding(16.dp),
                        data = it
                    )
                }
            }

        }
    }
}


fun PaymentInfoOrg.toUiModel(): PaymentInfoOrgData {
    val itemsTableBlock = mutableListOf<TableBlockItem>()
    this.items.forEach {
        it.tableItemPrimaryMlc.toUIModel()?.let { itemsTableBlock.add(it) }
    }
    return PaymentInfoOrgData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        itemsTableBlock = itemsTableBlock
    )
}

data class PaymentInfoOrgData(
    val itemsTableBlock: List<TableBlockItem>? = null,
    val componentId: UiText? = null,
) : UIElementData


@Composable
@Preview
fun PaymentInfoOrgPreview_WithoutCommission() {
    val horizontalMlc = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Cума до сплати:"),
        value = "1 000.00 грн"
    )
    val horizontalLargeMlc = TableItemHorizontalLargeMlcData(
        id = "12323",
        title = UiText.DynamicString("Загалом"),
        value = "1 000.00 грн"
    )
    val data = PaymentInfoOrgData(
        itemsTableBlock = listOf(
            horizontalMlc,
            horizontalLargeMlc
        )
    )
    PaymentInfoOrg(
        data = data
    )
}

@Composable
@Preview
fun PaymentInfoOrgPreview_OnlyTotal() {
    val horizontalLargeMlc = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Загалом"),
        value = "1 000.00 грн"
    )

    val data = PaymentInfoOrgData(
        itemsTableBlock = listOf(
            horizontalLargeMlc
        )
    )
    PaymentInfoOrg(
        data = data
    )
}

@Composable
@Preview
fun PaymentInfoOrgPreview_TableBlock() {
    val horizontalMlc = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        value = "Label Value"
    )
    val horizontalMlc2 = TableItemHorizontalMlcData(
        id = "1223",
        title = UiText.DynamicString("Title 2"),
        value = "Label Value 2"
    )
    val horizontalLargeMlc = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        value = "Label Value"
    )
    val data = PaymentInfoOrgData(
        itemsTableBlock = listOf(
            horizontalMlc,
            horizontalMlc2,
            horizontalLargeMlc
        )
    )
    PaymentInfoOrg(data = data)
}
