package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common.message.AttentionIconMessageMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.InfoYellow

@Composable
fun AttentionIconMessageMlc(
    modifier: Modifier = Modifier,
    data: AttentionIconMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .fillMaxWidth()
            .background(
                color = when (data.backgroundMode) {
                    BackgroundMode.INFO -> InfoYellow
                }, shape = RoundedCornerShape(16.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.wrapContentWidth()) {
                data.icon?.let {
                    SmallIconAtm(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                data.text?.let {
                    Text(
                        text = data.text.asString(),
                        style = DiiaTextStyle.t2TextDescription,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


data class AttentionIconMessageMlcData(
    val componentId: UiText? = null,
    val icon: SmallIconAtmData?,
    val text: UiText?,
    val backgroundMode: BackgroundMode
) : UIElementData

enum class BackgroundMode {
    INFO;
}

fun AttentionIconMessageMlc.toUiModel(): AttentionIconMessageMlcData {
    return AttentionIconMessageMlcData(
        componentId = this.componentId.toDynamicStringOrNull(),
        icon = this.smallIconAtm?.toUiModel(),
        text = this.text?.let {
            UiText.DynamicString(it)
        },
        backgroundMode = when (this.backgroundMode) {
            AttentionIconMessageMlc.BackgroundMode.info -> BackgroundMode.INFO
        }

    )
}

fun generateAttentionIconMessageMlcMockData(): AttentionIconMessageMlcData {
    return AttentionIconMessageMlcData(
        icon = SmallIconAtmData(code = DiiaResourceIcon.ELLIPSE_INFO.code),
        text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
        backgroundMode = BackgroundMode.INFO

    )
}

@Composable
@Preview
fun AttentionIconMessageMlcPreview() {
    AttentionIconMessageMlc(
        data = generateAttentionIconMessageMlcMockData()
    ) {}
}