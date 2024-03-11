package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun TableItemPrimaryMlc(
    modifier: Modifier = Modifier,
    data: TableItemPrimaryMlcData,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .background(White)
        .conditional(data.interaction == UIState.Interaction.Enabled) {
            clickable {
                onUIAction(
                    UIAction(actionKey = data.actionKey, data = data.value)
                )
            }
        }
        .semantics {
            testTag = data.componentId
        }
    ) {

        data.title?.let {
            Text(
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody,
                color = Black
            )
        }

        Row(
            modifier = Modifier
                .background(White)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        )
        {
            data.value?.let {
                Text(
                    modifier = Modifier
                        .weight(1f, false),
                    text = it,
                    style = DiiaTextStyle.h2MediumHeading,
                    color = Black
                )
            }
            data.icon?.let {
                Column(
                    modifier = Modifier
                        .defaultMinSize(24.dp)
                        .align(alignment = Alignment.Top)
                ) {
                    UiIconWrapperSubatomic(
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = data.value
                                    )
                                )
                            },
                        icon = data.icon,
                        diiaResourceIconProvider = diiaResourceIconProvider,
                    )
                }
            }
        }
    }
}

data class TableItemPrimaryMlcData(
    val actionKey: String = UIActionKeysCompose.PRIMARY_TABLE_ITEM,
    val id: String? = null,
    val componentId: String = "",
    val title: UiText? = null,
    val value: String? = null,
    val icon: UiIcon? = null,
    val interaction: UIState.Interaction = UIState.Interaction.Enabled,
) : TableBlockItem

@Composable
@Preview
fun PrimaryTableItemMoleculePreview_ValueAsImage() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        title = UiText.DynamicString("Номер сертифіката"),
        value = "1234567890"
    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    )
}

@Composable
@Preview
fun PrimaryTableItemMoleculePreview_ValueAsImage_disabled() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        title = UiText.DynamicString("Номер сертифіката"),
        value = "1234567890",
    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    )
}

@Composable
@Preview
fun PrimaryTableItemMoleculePreviewWithCopy() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        title = UiText.DynamicString("Номер сертифіката"),
        value = "12345601234560123456012345601234560",
        icon = UiIcon.DrawableResource(CommonDiiaResourceIcon.COPY.code)
    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    )
}