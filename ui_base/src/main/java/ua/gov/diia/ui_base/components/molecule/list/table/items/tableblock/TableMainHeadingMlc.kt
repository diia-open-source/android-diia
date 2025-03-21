package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun TableMainHeadingMlc(
    modifier: Modifier = Modifier,
    data: TableMainHeadingMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val componentId = data.componentId?.asString().orEmpty()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(componentId)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            data.title?.let { lTitle ->
                Text(
                    text = lTitle.asString(),
                    color = Black,
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
            }
            data.description?.let { lDescription ->
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = lDescription.asString(),
                    color = BlackAlpha50,
                    style = DiiaTextStyle.t2TextDescription
                )
            }
        }
        data.iconAtmData?.let { lIconAtmData ->
            IconAtm(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
                    .noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = UIActionKeysCompose.TABLE_MAIN_HEADING_MLC,
                                data = componentId
                            )
                        )
                    },
                data = lIconAtmData
            )
        }
    }
}

data class TableMainHeadingMlcData(
    val componentId: UiText? = null,
    val title: UiText? = null,
    val description: UiText? = null,
    val iconAtmData: IconAtmData? = null
) : UIElementData

fun TableMainHeadingMlc.toUIModel() = TableMainHeadingMlcData(
    componentId = componentId?.let { UiText.DynamicString(it) },
    title = UiText.DynamicString(label),
    description = description?.let { UiText.DynamicString(it) },
    iconAtmData = icon?.toUiModel()
)

fun UiText?.toTableMainHeadingMlcData() = TableMainHeadingMlcData(
    componentId = null,
    title = this,
    description = null,
    iconAtmData = null
)

@Composable
@Preview
fun TableMainHeadingMlcPreview() {
    val state = TableMainHeadingMlcData(
        title = "Heading".toDynamicString(),
        iconAtmData = IconAtmData(
            code = "copy"
        )
    )
    TableMainHeadingMlc(
        modifier = Modifier.padding(16.dp),
        data = state
    ) {
        /* no-op */
    }
}

@Composable
@Preview
fun TableMainHeadingMlcWithDescriptionPreview() {
    val state = TableMainHeadingMlcData(
        title = "Heading".toDynamicString(),
        description = "Description".toDynamicString(),
        iconAtmData = IconAtmData(
            code = "copy"
        )
    )
    TableMainHeadingMlc(
        modifier = Modifier.padding(16.dp),
        data = state
    ) {
        /* no-op */
    }
}