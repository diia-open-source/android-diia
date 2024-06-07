package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun SubtitleLabelMlc(
    modifier: Modifier = Modifier, data: SubtitleLabelMlcData, color: Color = Color.Black
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp)
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: ""),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (data.icon != null) {
            Image(
                modifier = modifier.size(24.dp),
                painter = painterResource(id = data.icon.res),
                contentDescription = ""
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
    val icon: UiIcon.DrawableResInt? = null,
    val componentId: UiText? = null,
) : UIElementData

@Preview
@Composable
fun SubtitleLabelMlcData() {
    val data = SubtitleLabelMlcData(
        UiText.DynamicString(LoremIpsum(5).values.joinToString()),
        UiIcon.DrawableResInt(R.drawable.ic_invincibility_point)
    )
    SubtitleLabelMlc(data = data)
}


@Preview
@Composable
fun SubtitleLabelMlcData_no_icon() {
    val data = SubtitleLabelMlcData(
        UiText.DynamicString(LoremIpsum(5).values.joinToString()), null
    )
    SubtitleLabelMlc(data = data)
}