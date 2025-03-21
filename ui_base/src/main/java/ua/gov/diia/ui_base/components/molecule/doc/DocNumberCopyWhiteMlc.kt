package ua.gov.diia.ui_base.components.molecule.doc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.doc.DocNumberCopyMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun DocNumberCopyWhiteMlc(
    modifier: Modifier = Modifier,
    data: DocNumberCopyWhiteMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val value = data.value?.asString()
    Column(modifier = modifier
        .fillMaxWidth()
        .noRippleClickable {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = value
                )
            )
        }) {
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )
        {

            data.value?.let {
                AutoSizeLimitedText(
                    text = data.value.asString(),
                    modifier = Modifier.weight(1f, false),
                    maxLines = 1,
                    color = White,
                    style = DiiaTextStyle.heroText
                )
            }
            data.icon?.let { icon ->
                Column(
                    modifier = Modifier
                        .defaultMinSize(24.dp)
                        .align(alignment = Alignment.CenterVertically)
                ) {
                    IconAtm(
                        modifier = Modifier.size(24.dp),
                        data = data.icon,
                        onUIAction = {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = value,
                                    action = data.icon.action
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

data class DocNumberCopyWhiteMlcData(
    val actionKey: String = UIActionKeysCompose.DOC_NUMBER_COPY_WHITE,
    val id: String? = null,
    val componentId: UiText? = null,
    val value: UiText? = null,
    val icon: IconAtmData? = null
)

fun DocNumberCopyMlc.toUIModel(id: String? = null): DocNumberCopyWhiteMlcData {
    return DocNumberCopyWhiteMlcData(
        componentId = this.componentId.toDynamicStringOrNull(),
        id = id,
        value = this.value.toDynamicString(),
        icon = this.icon?.toUiModel()
    )
}

@Composable
@Preview
fun DocNumberCopyWhiteMlcDataPreview() {
    val data = DocNumberCopyWhiteMlcData(
        id = "123",
        value = "1234567890".toDynamicString(),
        icon = IconAtmData(
            code = DiiaResourceIcon.COPY_WHITE.code,
            action = DataActionWrapper(
                type = "copy",
                resource = "123456789"
            )
        )
    )
    DocNumberCopyWhiteMlc(
        modifier = Modifier.fillMaxWidth(),
        data = data
    ) {}
}