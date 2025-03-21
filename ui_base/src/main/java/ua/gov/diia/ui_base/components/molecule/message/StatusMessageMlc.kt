package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common.message.StatusMessage
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50

@Composable
fun StatusMessageMlc(
    modifier: Modifier = Modifier,
    data: StatusMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(color = WhiteAlpha50, shape = RoundedCornerShape(8.dp))
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.wrapContentWidth()) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = data.icon ?: "☝",
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.title,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                data.description?.let{
                    TextWithParametersAtom(
                        data = data.description,
                        style = DiiaTextStyle.t3TextBody,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}


data class StatusMessageMlcData(
    val icon: String? = null,
    val title: String,
    val description: TextWithParametersData?,
    val componentId: UiText? = null,
): UIElementData

fun StatusMessage?.toUIModel(actionKey: String = UIActionKeysCompose.TEXT_WITH_PARAMETERS): StatusMessageMlcData? {
    val entity: StatusMessage = this ?: return null
    return StatusMessageMlcData(
        icon = entity.icon,
        title = entity.title ?: "",
        description = TextWithParametersData(
            text = UiText.DynamicString(entity.text ?: ""),
            parameters = if (this.parameters != null) {
                mutableListOf<TextParameter>().apply {
                    entity.parameters?.forEach {
                        add(
                            TextParameter(
                                data = TextParameter.Data(
                                    name = it.data?.name.toDynamicStringOrNull(),
                                    resource = it.data?.resource.toDynamicStringOrNull(),
                                    alt = it.data?.alt.toDynamicStringOrNull()
                                ),
                                type = it.type
                            )
                        )
                    }
                }

            } else emptyList()
        ),
        componentId = this.componentId?.let { UiText.DynamicString(it) }
    )
}

fun String?.toUIModelTextWithParameters(): TextWithParametersData? {
    if (this.isNullOrEmpty()) return null
    return TextWithParametersData(
        text = this.toDynamicString(),
        parameters = emptyList()
    )
}

fun generateStatusMessageMlcMockData(): StatusMessageMlcData{
    return StatusMessageMlcData(
        icon = "☝",
        title = "Завершіть заповнення заяви",
        description = TextWithParametersData(
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            parameters = emptyList()
        )
    )
}

@Composable
@Preview
fun StatusMessageMlcPreview() {
    StatusMessageMlc(
        data = generateStatusMessageMlcMockData()
    ) { annotationContent ->
    }

}