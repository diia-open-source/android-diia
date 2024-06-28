package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.CurrentTimeMlc
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun CurrentTimeMlc(
    modifier: Modifier = Modifier,
    data: CurrentTimeMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .clickable { onUIAction(UIAction(data.actionKey, action = data.action)) }
            .testTag(data.componentId?.asString() ?: ""),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = data.label.asString(),
            color = Black,
            style = DiiaTextStyle.h4ExtraSmallHeading,
            textAlign = TextAlign.Center,
        )
    }
}

data class CurrentTimeMlcData(
    val actionKey: String = UIActionKeysCompose.CURRENT_TIME_MLC,
    val label: UiText,
    val date: String? = null,
    val componentId: UiText? = null,
    val action: DataActionWrapper? = null,
)

fun CurrentTimeMlc.toUiModel(): CurrentTimeMlcData {
    return CurrentTimeMlcData(
        label = UiText.DynamicString(label.orEmpty()),
        componentId = UiText.DynamicString(componentId.orEmpty()),
        action = action?.toDataActionWrapper(),
        date = action?.resource
    )
}

@Composable
@Preview
fun CurrentTimeMlcPreview() {
    val data = CurrentTimeMlcData(label = UiText.DynamicString("label"))
    CurrentTimeMlc(Modifier, data) {}
}