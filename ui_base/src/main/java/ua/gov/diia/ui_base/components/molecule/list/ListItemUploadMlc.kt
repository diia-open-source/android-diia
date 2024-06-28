package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red

@Composable
fun ListItemUploadMlc(
    modifier: Modifier = Modifier,
    data: ListItemUploadMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier.testTag(data.componentId?.asString() ?: ""),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                modifier = modifier
                    .conditional(
                        data.progressState == UIState.MediaUploadState.InProgress ||
                                    data.errorDescription != null
                    ) {
                        alpha(0.3f)
                    },
                text = data.label.asString(),
                style = DiiaTextStyle.t1BigText,
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = data.description.asString(),
                    style = DiiaTextStyle.t2TextDescription,
                    color = BlackAlpha30
                )
            }
            data.errorDescription?.let { error ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = error.asString(),
                    style = DiiaTextStyle.t2TextDescription,
                    color = Red
                )
            }
        }

        Box {
// Loading
            if (data.progressState == UIState.MediaUploadState.InProgress) {
                Column {
                    LoaderCircularEclipse23Subatomic(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }
            }
// Error
            if (data.progressState == UIState.MediaUploadState.FailedLoading) {
                Column(
                    modifier = Modifier
                        .noRippleClickable {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = data.id,
                                    action = DataActionWrapper(
                                        type = when(data.errorType) {
                                            UploadFileErrorTypes.ERROR_TYPE_VIRUS.type,
                                            UploadFileErrorTypes.ERROR_TYPE_BIG_FILE_SIZE.type,
                                            UploadFileErrorTypes.ERROR_TYPE_WRONG_FORMAT.type,
                                            UploadFileErrorTypes.ERROR_DEFAULT.type -> "actionRemove"
                                            else -> {
                                                "actionRetry"
                                            }
                                        }
                                    )
                                )
                            )
                        }
                ) {
                    Image(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp),

                        painter = when(data.errorType) {
                            UploadFileErrorTypes.ERROR_TYPE_VIRUS.type,
                            UploadFileErrorTypes.ERROR_TYPE_BIG_FILE_SIZE.type,
                            UploadFileErrorTypes.ERROR_TYPE_WRONG_FORMAT.type,
                            UploadFileErrorTypes.ERROR_DEFAULT.type-> painterResource(
                                id = DiiaResourceIcon.getResourceId(DiiaResourceIcon.DELETE.code))
                            else -> {
                                painterResource(id = DiiaResourceIcon.getResourceId(DiiaResourceIcon.UPDATE.code))
                            }
                        }

                                ,

                        contentDescription = data.iconRightContentDescription?.asString()
                    )
                }
            }
// Loaded
            if (data.progressState == UIState.MediaUploadState.Loaded) {
                Column(
                    modifier = Modifier
                        .noRippleClickable {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = data.id,
                                    action = DataActionWrapper(
                                        type = "actionRemove"
                                    )
                                )
                            )
                        }
                ) {
                    Image(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp), painter = painterResource(
                            id = DiiaResourceIcon.getResourceId(DiiaResourceIcon.DELETE.code)
                        ), contentDescription = data.iconRightContentDescription?.asString()
                    )
                }
            }
        }
    }
}

data class ListItemUploadMlcData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_UPLOAD_MLC,
    val componentId: UiText? = null,
    var id: String,
    val label: UiText,
    val description: UiText? = null,
    var errorDescription: UiText? = null,
    var errorType: String? = null,
    val iconRight: UiIcon.DrawableResource? = null,
    val iconRightContentDescription: UiText? = null,
    var progressState: UIState.MediaUploadState = UIState.MediaUploadState.InProgress
) : UIElementData

enum class UploadFileErrorTypes(val type: String) { //todo change types name as on back
    ERROR_TYPE_BIG_FILE_SIZE("bigFileSize"),
    ERROR_TYPE_VIRUS("virus"),
    ERROR_TYPE_WRONG_FORMAT("format"),
    ERROR_DEFAULT("default")
}

@Composable
@Preview
fun ListItemUploadMlcPreview_Full() {
    val state = ListItemUploadMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.DELETE.code),
        progressState = UIState.MediaUploadState.Loaded
    )
    ListItemUploadMlc(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ListItemUploadMlcPreview_Error() {
    val state = ListItemUploadMlcData(
        id = "123",
        label = UiText.DynamicString("img_long_name_to_test_text_overflow_ellipsis.jpg"),
        errorDescription = UiText.DynamicString("Error Description"),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.DELETE.code),
        progressState = UIState.MediaUploadState.FailedLoading
    )
    ListItemUploadMlc(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ListItemUploadMlcPreview_Error_Reload() {
    val state = ListItemUploadMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        errorDescription = UiText.DynamicString("Error Description"),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.UPDATE.code),
        progressState = UIState.MediaUploadState.FailedLoading
    )
    ListItemUploadMlc(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun ListItemUploadMlcPreview_Loading() {
    val state = ListItemUploadMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        progressState = UIState.MediaUploadState.InProgress
    )
    ListItemUploadMlc(modifier = Modifier, data = state,) {}
}