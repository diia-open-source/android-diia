package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.SubtitleLabelMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDrawableResourceOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun SubtitleLabelMlc(
    modifier: Modifier = Modifier,
    data: SubtitleLabelMlcData,
    color: Color = Color.Black
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: ""),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (data.icon != null) {
            UiIconWrapperSubatomic(
                modifier = Modifier.size(24.dp),
                icon = data.icon
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            modifier = modifier,
            color = color,
            text = data.label.asString(),
            style = DiiaTextStyle.h4ExtraSmallHeading,
        )
    }
}

data class SubtitleLabelMlcData(
    val label: UiText,
    val icon: UiIcon? = null,
    val componentId: UiText? = null,
) : UIElementData

fun SubtitleLabelMlc?.toUiModel(): SubtitleLabelMlcData? {
    if (this == null) return null
    return SubtitleLabelMlcData(
        label = this.label.toDynamicString(),
        icon = this.icon?.toDrawableResourceOrNull(),
        componentId = this.componentId.toDynamicStringOrNull()
    )
}

enum class SubtitleLabelMlcMockType {
    icon, noicon
}

fun generateSubtitleLabelMlcMockData(mockType: SubtitleLabelMlcMockType): SubtitleLabelMlcData {
    return when (mockType) {
        SubtitleLabelMlcMockType.icon -> SubtitleLabelMlcData(
            UiText.DynamicString(LoremIpsum(5).values.joinToString()),
            UiIcon.DrawableResource(DiiaResourceIcon.PN.code)
        )

        SubtitleLabelMlcMockType.noicon -> SubtitleLabelMlcData(
            UiText.DynamicString(LoremIpsum(5).values.joinToString()), null
        )
    }
}

@Preview
@Composable
fun SubtitleLabelMlcData() {
    SubtitleLabelMlc(data = generateSubtitleLabelMlcMockData(SubtitleLabelMlcMockType.icon))
}


@Preview
@Composable
fun SubtitleLabelMlcData_no_icon() {
    SubtitleLabelMlc(data = generateSubtitleLabelMlcMockData(SubtitleLabelMlcMockType.noicon))
}