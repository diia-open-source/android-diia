package ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.subatomic.icon.SingIconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TableItemVerticalMlc(
    modifier: Modifier = Modifier,
    data: TableItemVerticalMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 24.dp)
            .semantics {
                testTag = data.componentId
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                data.supportText?.let {
                    Box(
                        modifier = Modifier.width(35.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 12.dp),
                            text = data.supportText,
                            style = DiiaTextStyle.t5TextSmallDescription,
                            color = Black
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    data.title?.let {
                        Text(
                            text = data.title.asString(),
                            style = DiiaTextStyle.t3TextBody,
                            color = Black
                        )
                    }
                    data.secondaryTitle?.let {
                        Text(
                            text = data.secondaryTitle,
                            style = DiiaTextStyle.t3TextBody,
                            color = BlackAlpha30
                        )
                    }
                    if (data.valueAsBase64String != null) {
                        Box(
                            modifier = Modifier.size(100.dp, 50.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            SingIconBase64Subatomic(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 4.dp),
                                base64Image = data.valueAsBase64String,
                                signImage = data.signBitmap
                            )
                        }
                    } else {
                        data.value?.let {
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
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
                data.icon?.let {
                    Spacer(modifier = Modifier.width(8.dp))
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
                        image = data.icon
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

data class TableItemVerticalMlcData(
    val actionKey: String = UIActionKeysCompose.VERTICAL_TABLE_ITEM,
    val id: String? = null,
    val componentId: String = "",
    val supportText: String? = null,
    val title: UiText? = null,
    val secondaryTitle: String? = null,
    val value: String? = null,
    val secondaryValue: String? = null,
    val valueAsBase64String: String? = null,
    val icon: UiText? = null,
    val signBitmap: ImageBitmap? = null
) : TableBlockItem

@Composable
@Preview
fun TableItemVerticalMlcPreview_ValueAsImage() {
    val data = TableItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = "Secondary title",
        value = "Label Value",
        secondaryValue = "Secondary label",
        valueAsBase64String = PreviewBase64Images.sign,
        icon = UiText.StringResource(R.drawable.ic_copy)
    )
    TableItemVerticalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = data
    )
}

@Composable
@Preview
fun TableItemVerticalMlcPreview_ValueAsText() {
    val data = TableItemVerticalMlcData(
        id = "123",
        title = UiText.DynamicString("Title"),
        secondaryTitle = "Secondary title",
        supportText = "12",
        value = "Label Value",
        secondaryValue = "Secondary label",
    )
    TableItemVerticalMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = data
    )
}