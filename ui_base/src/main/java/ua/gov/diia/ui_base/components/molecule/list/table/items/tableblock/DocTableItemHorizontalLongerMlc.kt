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
fun DocTableItemHorizontalLongerMlc(
    modifier: Modifier = Modifier,
    data: DocTableItemHorizontalLongerMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Row(modifier = modifier) {
        Row(modifier = Modifier.weight(0.4f)) {
            data.supportText?.let {
                Box(
                    modifier = Modifier.width(35.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = data.supportText,
                        style = DiiaTextStyle.t5TextSmallDescription,
                        color = Black
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                data.title?.let {
                    Text(
                        text = it,
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
            }
        }
        Spacer(modifier = Modifier.width(20.dp))

        Row(modifier = Modifier.weight(0.6f)) {
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
                            style = DiiaTextStyle.t3TextBody,
                            color = Black,
                            maxLines = 3
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

data class DocTableItemHorizontalLongerMlcData(
    val actionKey: String = UIActionKeysCompose.HORIZONTAL_TABLE_ITEM,
    val id: String? = null,
    val supportText: String? = null,
    val title: String? = null,
    val secondaryTitle: String? = null,
    val value: String? = null,
    val secondaryValue: String? = null,
    val valueAsBase64String: String? = null,
    val iconRight: UiText? = null
) : TableBlockItem

@Composable
@Preview
fun DocTableItemHorizontalLongerMlcPreview_Minimum() {
    val state = DocTableItemHorizontalLongerMlcData(
        id = "123",
        title = "Title",
        value = "Label Value"
    )
    DocTableItemHorizontalLongerMlc(
        data = state
    )
}

@Composable
@Preview
fun DocTableItemHorizontalLongerMlcPreview_SupportText() {
    val state = DocTableItemHorizontalLongerMlcData(
        id = "123",
        title = "Title",
        value = "Label Value",
        supportText = "12"
    )
    DocTableItemHorizontalLongerMlc(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        data = state
    )
}

@Composable
@Preview
fun DocTableItemHorizontalLongerMlcPreview_Full() {
    val state = DocTableItemHorizontalLongerMlcData(
        id = "123",
        title = "Title",
        secondaryTitle = "Secondary title",
        supportText = "С.1.3",
        value = "Label Value",
        secondaryValue = "Secondary label",
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    DocTableItemHorizontalLongerMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}


@Composable
@Preview
fun DocTableItemHorizontalLongerMlcPreview_ValueAsImage() {
    val state = DocTableItemHorizontalLongerMlcData(
        id = "123",
        title = "Title",
        secondaryTitle = "Secondary title",
        supportText = "12",
        value = "Label Value",
        secondaryValue = "Secondary label",
        valueAsBase64String = PreviewBase64Images.sign,
        iconRight = UiText.StringResource(R.drawable.ic_copy)
    )
    DocTableItemHorizontalLongerMlc(
        modifier = Modifier
            .fillMaxWidth(),
        data = state
    )
}