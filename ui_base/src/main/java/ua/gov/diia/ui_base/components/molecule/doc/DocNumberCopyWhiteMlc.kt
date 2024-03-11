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
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun DocNumberCopyWhiteMlc(
    modifier: Modifier = Modifier,
    data: DocNumberCopyWhiteMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .noRippleClickable {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = data.value
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
                    text = data.value,
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
                    IconWithBadge(
                        modifier = Modifier
                            .size(24.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        image = icon,
                        imageColor = White,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

data class DocNumberCopyWhiteMlcData(
    val actionKey: String = UIActionKeysCompose.DOC_NUMBER_COPY_WHITE,
    val id: String? = null,
    val value: String? = null,
    val icon: UiText? = null
)

@Composable
@Preview
fun DocNumberCopyWhiteMlcDataPreview() {
    val data = DocNumberCopyWhiteMlcData(
        id = "123",
        value = "1234567890",
        icon = UiText.StringResource(R.drawable.ic_copy_settings_white)
    )
    DocNumberCopyWhiteMlc(
        modifier = Modifier.fillMaxWidth(),
        data = data
    ) {}
}