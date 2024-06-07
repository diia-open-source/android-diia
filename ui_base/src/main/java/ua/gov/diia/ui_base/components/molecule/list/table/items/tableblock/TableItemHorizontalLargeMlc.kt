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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.table.TableItemHorizontalLargeMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.icon.SingIconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun TableItemHorizontalLargeMlc(
    modifier: Modifier = Modifier,
    data: TableItemHorizontalLargeMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Row(modifier = modifier.testTag(data.componentId?.asString() ?: "")) {
        Row(modifier = Modifier.weight(1f)) {
            data.supportText?.let {
                Box(
                    modifier = Modifier.width(36.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = data.supportText,
                        style = DiiaTextStyle.t4TextSmallDescription,
                        color = Black
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                data.title?.let {
                    Text(
                        text = it.asString(),
                        style = DiiaTextStyle.t1BigText,
                        color = Black
                    )
                }
                data.secondaryTitle?.let {
                    Text(
                        text = data.secondaryTitle.asString(),
                        style = DiiaTextStyle.t1BigText,
                        color = BlackAlpha30
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(20.dp))

        Row(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.weight(1f)) {
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
                            style = DiiaTextStyle.t1BigText,
                            color = Black
                        )
                    }
                    data.secondaryValue?.let {
                        Text(
                            text = data.secondaryValue.asString(),
                            style = DiiaTextStyle.t1BigText,
                            color = BlackAlpha30
                        )
                    }
                }

            }
            data.iconRight?.let {
                IconAtm(
                    modifier = Modifier.size(24.dp),
                    data = data.iconRight,
                    onUIAction = onUIAction
                )
            }
        }
    }
}


fun TableItemHorizontalLargeMlc.toUiModel(): TableItemHorizontalLargeMlcData {
    return TableItemHorizontalLargeMlcData(
        id = "this.id",
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        supportText = this.supportingValue,
        title = UiText.DynamicString(this.label.orEmpty()),
        secondaryTitle = UiText.DynamicString(this.secondaryLabel.orEmpty()),
        value = this.value,
        secondaryValue = UiText.DynamicString(this.secondaryValue.orEmpty()),
        valueAsBase64String = this.valueImage,
        iconRight = this.icon?.toUiModel()
    )
}

data class TableItemHorizontalLargeMlcData(
    val actionKey: String = UIActionKeysCompose.HORIZONTAL_TABLE_ITEM,
    val id: String? = null,
    val componentId: UiText? = null,
    val supportText: String? = null,
    val title: UiText? = null,
    val secondaryTitle: UiText? = null,
    val value: String? = null,
    val secondaryValue: UiText? = null,
    val valueAsBase64String: String? = null,
    val iconRight: IconAtmData? = null
) : TableBlockItem

@Composable
@Preview
fun TableItemHorizontalLargeMlcPreview_Minimum() {
    val state = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        value = "Label Value"
    )
    TableItemHorizontalLargeMlc(
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalLargeMlcPreview_SupportText() {
    val state = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        value = "Label Value",
        supportText = "12"
    )
    TableItemHorizontalLargeMlc(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalLargeMlcPreview_Full() {
    val state = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "С.1.3",
        value = "Label Value",
        secondaryValue = UiText.DynamicString("Secondary label"),
        iconRight = IconAtmData(
            id = "1",
            code = DiiaResourceIcon.COPY.code,
            accessibilityDescription = "Button"
        )
    )
    TableItemHorizontalLargeMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}


@Composable
@Preview
fun TableItemHorizontalLargeMlcPreview_ValueAsImage() {
    val state = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        value = "Label Value",
        secondaryValue = UiText.DynamicString("Secondary label"),
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = IconAtmData(
            id = "1",
            code = DiiaResourceIcon.COPY.code,
            accessibilityDescription = "Button"
        )
    )
    TableItemHorizontalLargeMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}

@Composable
@Preview
fun TableItemHorizontalLargeMlcPreview_() {
    val state = TableItemHorizontalLargeMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = UiText.DynamicString("Secondary title"),
        supportText = "12",
        value = "Label Value",
        secondaryValue = UiText.DynamicString("Secondary label"),
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = IconAtmData(
            id = "1",
            code = DiiaResourceIcon.COPY.code,
            accessibilityDescription = "Button"
        )
    )
    TableItemHorizontalLargeMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}