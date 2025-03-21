package ua.gov.diia.ui_base.components.atom.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.SectionTitleAtm
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun SectionTitleAtm(
    modifier: Modifier = Modifier,
    isFirstAtBody: Boolean = false,
    data: SectionTitleAtmData
) {
    Text(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .padding(top = if (isFirstAtBody) 8.dp else 32.dp)
            .testTag(data.componentId?.asString() ?: ""),
        text = data.label.asString(),
        style = DiiaTextStyle.t1BigText
    )
}

data class SectionTitleAtmData(
    val label: UiText,
    val componentId: UiText? = null,
) : UIElementData

@Preview
@Composable
fun SectionSectionTitleAtmPreview() {
    val data = SectionTitleAtmData(label = UiText.DynamicString("Label"))
    SectionTitleAtm(
        data = data
    )
}

fun SectionTitleAtm.toUiModel(): SectionTitleAtmData {
    return SectionTitleAtmData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        label = UiText.DynamicString(this.label)
    )
}