package ua.gov.diia.ui_base.components.atom.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.SlideBarAtm
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Green
import ua.gov.diia.ui_base.components.theme.Yellow

@Composable
fun SlideBarAtm(
    modifier: Modifier = Modifier,
    data: SlideBarAtmData
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = when (data.type) {
                    SlideBarAtmType.Attention -> Yellow
                    SlideBarAtmType.Success -> Green
                }
            ),
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = 56.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 12.dp
                ),
            text = data.value?.asString() ?: "",
            style = DiiaTextStyle.t2TextDescription,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

data class SlideBarAtmData(
    val actionKey: String = UIActionKeysCompose.SLIDE_BAR_ATM,
    val componentId: String = "",
    val value: UiText?,
    val type: SlideBarAtmType,
) : UIElementData

fun SlideBarAtm.toUIModel(): SlideBarAtmData {
    return SlideBarAtmData(
        componentId = this.componentId ?: "",
        value = this.value.toDynamicString(),
        type = when (this.type) {
            SlideBarAtm.SlideBarType.success -> SlideBarAtmType.Success
            SlideBarAtm.SlideBarType.attention -> SlideBarAtmType.Attention
        }
    )
}

sealed class SlideBarAtmType {
    object Success : SlideBarAtmType()
    object Attention : SlideBarAtmType()
}

enum class SlideBarAtmMockType {
    success_short_text, success_long_text, attention
}

fun generateSlideBarAtmMockData(mockType: SlideBarAtmMockType): SlideBarAtmData {

    return when (mockType) {
        SlideBarAtmMockType.success_short_text -> SlideBarAtmData(
            value = "notification text".toDynamicString(),
            type = SlideBarAtmType.Success
        )

        SlideBarAtmMockType.success_long_text -> SlideBarAtmData(
            value = LoremIpsum(1000).values.joinToString().toDynamicString(),
            type = SlideBarAtmType.Success
        )

        SlideBarAtmMockType.attention -> SlideBarAtmData(
            value = "notification text".toDynamicString(),
            type = SlideBarAtmType.Attention
        )
    }
}

@Preview
@Composable
fun SlideBarAtm_Success_OneRow() {
    val data = generateSlideBarAtmMockData(mockType = SlideBarAtmMockType.success_short_text)
    SlideBarAtm(data = data)
}

@Preview
@Composable
fun SlideBarAtm_Success_MultipleRow() {
    val data = generateSlideBarAtmMockData(mockType = SlideBarAtmMockType.success_long_text)
    SlideBarAtm(data = data)
}

@Preview
@Composable
fun SlideBarAtm_Attention() {
    val data = generateSlideBarAtmMockData(mockType = SlideBarAtmMockType.attention)
    SlideBarAtm(data = data)
}

@Preview
@Composable
fun SlideBarAtm_FromJson() {
    val json = SlideBarAtm(
        componentId = "123",
        type = SlideBarAtm.SlideBarType.success,
        value = "text"
    )
    SlideBarAtm(data = json.toUIModel())
}
