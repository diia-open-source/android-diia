package ua.gov.diia.ui_base.components.atom.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.GreyTitleAtm
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun GreyTitleAtm(
    modifier: Modifier = Modifier,
    data: GreyTitleAtmData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: "")
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        Text(
            modifier = modifier,
            text = data.label.asString(),
            style = DiiaTextStyle.t2TextDescription,
            color = BlackAlpha30
        )
    }
}

data class GreyTitleAtmData(
    val label: UiText,
    val componentId: UiText? = null,
    override val id: String = "",
) : UIElementData, SimplePagination

fun GreyTitleAtm.toUIModel(): GreyTitleAtmData {
    return GreyTitleAtmData(
        componentId = this.componentId.orEmpty().toDynamicString(),
        label = this.label.toDynamicString(),
        id = this.componentId?: ""
    )
}

@Preview
@Composable
fun GreyTitleAtmPreview() {
    val data = GreyTitleAtmData(
        label = UiText.DynamicString("label"),
    )
    GreyTitleAtm(data = data)
}