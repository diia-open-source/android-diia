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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common.message.AttentionMessageMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.subatomic.border.diiaRadialGradientBorder
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun AttentionMessageMlc(
    modifier: Modifier = Modifier,
    data: AttentionMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .diiaRadialGradientBorder()
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.wrapContentWidth()) {
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = data.icon,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp,
                        lineHeight = 36.sp
                    )
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                data.title?.let {
                    Text(
                        text = data.title,
                        style = DiiaTextStyle.t1BigText,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextWithParametersAtom(
                    data = data.description,
                    style = DiiaTextStyle.t3TextBody,
                    onUIAction = onUIAction
                )
            }
        }
    }
}


data class AttentionMessageMlcData(
    val componentId: UiText? = null,
    val icon: String,
    val title: String? = null,
    val description: TextWithParametersData
) : UIElementData

fun AttentionMessageMlc?.toUIModel(): AttentionMessageMlcData? {
    val entity: AttentionMessageMlc = this ?: return null
    return AttentionMessageMlcData(
        icon = this.icon ?: "",
        title = entity.title,
        componentId = this.componentId.toDynamicStringOrNull(),
        description = TextWithParametersData(
            text = UiText.DynamicString(entity.text ?: ""),
            parameters = if (this.parameters != null) {
                mutableListOf<TextParameter>().apply {
                    entity.parameters?.forEach {
                        add(
                            TextParameter(
                                data = TextParameter.Data(
                                    name = it?.data?.name.toDynamicStringOrNull(),
                                    resource = it?.data?.resource.toDynamicStringOrNull(),
                                    alt = it?.data?.alt.toDynamicStringOrNull()
                                ),
                                type = it?.type
                            )
                        )
                    }
                }

            } else emptyList()
        )
    )
}

@Composable
@Preview
fun AttentionMessageMlcPreview_Negative_Titled() {
    val state = AttentionMessageMlcData(
        icon = "☝",
        title = "Завершіть заповнення заяви",
        description = TextWithParametersData(
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            parameters = emptyList()
        )
    )
    Box {
        AttentionMessageMlc(
            data = state
        ) {
        }
    }
}

@Composable
@Preview
fun AttentionMessageMlcPreview_Attention_Without_title() {
    val state = AttentionMessageMlcData(
        icon = "\uD83D\uDC4D",
        description = TextWithParametersData(
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані."),
            parameters = emptyList()
        )
    )

    Box {
        AttentionMessageMlc(
            data = state
        ) {
        }
    }
}

@Composable
@Preview
fun AttentionMessageMlcPreview_Attention_More_Text() {
    val state = AttentionMessageMlcData(
        icon = "\uD83D\uDC4D",
        description = TextWithParametersData(
            text = UiText.DynamicString("Щоб надіслати заяву, потрібно вказати всі дані. Щоб надіслати заяву, потрібно вказати всі дані. Щоб надіслати заяву, потрібно вказати всі дані. Щоб надіслати заяву, потрібно вказати всі дані. Щоб надіслати заяву, потрібно вказати всі дані. Щоб надіслати заяву, потрібно вказати всі дані."),
            parameters = emptyList()
        )
    )

    Box {
        AttentionMessageMlc(
            data = state
        ) {
        }
    }
}
