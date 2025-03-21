package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.model.TableItemLabelAlignment
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.subatomic.icon.SingIconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TableItemHorizontalMlc(
    modifier: Modifier = Modifier,
    data: TableItemHorizontalMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Row(modifier = modifier
        .semantics {
            testTag = data.componentId
        }) {
        Row(modifier = Modifier.weight(1f)) {
            data.supportText?.let {
                Box(
                    modifier = Modifier.width(36.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        modifier = Modifier.padding(end = 12.dp),
                        text = data.supportText,
                        style = DiiaTextStyle.t3TextBody,
                        color = Black
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                val title = data.title?.asString()
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style = DiiaTextStyle.t3TextBody,
                        color = Black
                    )
                }

                val secondaryTitle = data.secondaryTitle?.asString()
                if (!secondaryTitle.isNullOrEmpty()) {
                    Text(
                        text = secondaryTitle,
                        style = DiiaTextStyle.t3TextBody,
                        color = BlackAlpha30
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        val weightModifier = when (data.valueAlignment) {
            TableItemLabelAlignment.Start -> Modifier.weight(1f)
            TableItemLabelAlignment.End -> Modifier
        }
        Row(modifier = weightModifier) {
            Column(modifier = weightModifier) {
                if (data.valueAsBase64String != null) {
                    Box(
                        modifier = Modifier.size(100.dp, 50.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        SingIconBase64Subatomic(
                            modifier = Modifier.fillMaxSize(),
                            base64Image = data.valueAsBase64String,
                            signImage = null
                        )
                    }
                } else {
                    data.value?.let {
                        Text(
                            text = data.value,
                            style = DiiaTextStyle.t3TextBody,
                            color = Black
                        )
                    }
                    data.secondaryValue?.let {
                        Text(
                            text = data.secondaryValue,
                            style = DiiaTextStyle.t3TextBody,
                            color = BlackAlpha30
                        )
                    }
                }
            }
            data.iconRight?.let {
                Box(
                    modifier = Modifier.width(24.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconWithBadge(
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = data.value
                                    )
                                )
                            },
                        image = data.iconRight
                    )
                }
            }
        }
    }
}

data class TableItemHorizontalMlcData(
    val actionKey: String = UIActionKeysCompose.HORIZONTAL_TABLE_ITEM,
    val id: String? = null,
    val componentId: String = "",
    val supportText: String? = null,
    val title: UiText? = null,
    val secondaryTitle: UiText? = null,
    val value: String? = null,
    val valueAlignment: TableItemLabelAlignment = TableItemLabelAlignment.Start,
    val secondaryValue: String? = null,
    val valueAsBase64String: String? = null,
    val iconRight: UiText? = null
) : TableBlockItem

fun TableItemHorizontalMlc.toUiModel(): TableItemHorizontalMlcData {
    return TableItemHorizontalMlcData(
        componentId = componentId.orEmpty(),
        title = this.label?.let { UiText.DynamicString(it) },
        secondaryTitle = this.secondaryLabel?.let { UiText.DynamicString(it) },
        value = this.value,
        secondaryValue = this.secondaryValue,
        supportText = this.supportingValue,
        valueAsBase64String = this.valueImage
    )
}

@Composable
@Preview
fun TableItemHorizontalMlcPreview_Minimum() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        value = "Label Value"
    )
    TableItemHorizontalMlc(
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalMlcPreview_SupportText() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        value = "Label Value",
        supportText = "12"
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalMlcPreview_Full() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "С.1.3",
        value = "Label Value",
        secondaryValue = "Secondary label",
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalMlcPreview_ValueAsImage() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        value = "Label Value",
        secondaryValue = "Secondary label",
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalMlcPreview_() {
    val state = TableItemHorizontalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        value = "Label Value",
        secondaryValue = "Secondary label",
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemHorizontalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}