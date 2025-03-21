package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.background
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
import ua.gov.diia.core.models.common_compose.table.TableItemPrimaryMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun TableItemPrimaryMlc(
    modifier: Modifier = Modifier,
    data: TableItemPrimaryMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    val value = data.value?.asString()
    Column(modifier = modifier
        .fillMaxWidth()
        .background(White)
        .conditional(data.interaction == UIState.Interaction.Enabled) {
            noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = value,
                        action = data.icon?.action
                    )
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
            Text(
                modifier = Modifier
                    .weight(1f, false),
                text = data.value.asString(),
                style = DiiaTextStyle.h2MediumHeading,
                color = Black
            )
            data.icon?.let {
                Column(
                    modifier = Modifier
                        .defaultMinSize(24.dp)
                        .align(alignment = Alignment.Top)
                ) {
                    IconAtm(
                        modifier = Modifier.size(24.dp),
                        data = data.icon,
                    ) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = value,
                                action = data.icon.action
                            )
                        )
                    }
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
    val value: UiText,
    val icon: IconAtmData? = null,
    val interaction: UIState.Interaction = UIState.Interaction.Enabled,
) : TableBlockItem

fun TableItemPrimaryMlc?.toUIModel(): TableItemPrimaryMlcData? {
    return this?.let {
        TableItemPrimaryMlcData(
            componentId = componentId.orEmpty(),
            title = label.toDynamicStringOrNull(),
            value = value.toDynamicString(),
            icon = icon?.let {
                it.toUiModel()
            }
        )
    }
}


@Composable
@Preview
fun PrimaryTableItemMoleculePreview_ValueAsImage() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        title = UiText.DynamicString("Номер сертифіката"),
        value = "1234567890".toDynamicString()
    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview
fun PrimaryTableItemMoleculePreview_no_title() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        value = "1234567890".toDynamicString()
    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview
fun PrimaryTableItemMoleculePreview_ValueAsImage_disabled() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        title = UiText.DynamicString("Номер сертифіката"),
        value = "1234567890".toDynamicString(),
    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview
fun PrimaryTableItemMoleculePreviewWithCopy() {
    val data = TableItemPrimaryMlcData(
        id = "123",
        title = UiText.DynamicString("Номер сертифіката"),
        value = "12345601234560123456012345601234560".toDynamicString(),
        icon = IconAtmData(
            code = DiiaResourceIcon.COPY.code,
            action = DataActionWrapper(
                type = "copy",
                resource = "12345601234560123456012345601234560"
            )
        )

    )
    TableItemPrimaryMlc(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        data = data
    )
}