package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.media.SingleMediaUploadGroupOrg
import ua.gov.diia.core.models.media.MediaItem
import ua.gov.diia.core.util.type_enum.TypeEnum
import ua.gov.diia.core.util.type_enum.getEnumTypeValue
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.media.SmallPicAtm
import ua.gov.diia.ui_base.components.atom.media.SmallPicAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.organism.SingleMediaUploadGroupOrgData.AspectRatioType
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun SingleMediaUploadGroupOrg(
    modifier: Modifier = Modifier,
    data: SingleMediaUploadGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .testTag(data.componentId?.asString() ?: "")
    ) {

        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody
            )
        }
        if (data.error != null) {
            Text(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                text = data.error.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = Red
            )
        } else {
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                    text = it.asString(),
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = BlackAlpha50
                )
            }
        }

        DividerSlimAtom(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(1.dp),
            color = BlackSqueeze
        )

        data.photo?.let {
            Box(
                modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (data.error != null) {
                    SmallPicAtm(
                        data = it.copy(progressState = UIState.MediaUploadState.FailedLoading),
                        onUIAction = {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = it.data,
                                    action = it.action
                                )
                            )
                        }
                    )
                } else {
                    SmallPicAtm(
                        data = it,
                        onUIAction = {
                            onUIAction(
                                UIAction(
                                    actionKey = data.actionKey,
                                    data = it.data,
                                    action = it.action
                                )
                            )
                        }
                    )
                }
            }
        }

        if (data.photo == null) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    },
                horizontalArrangement = Arrangement.Center
            ) {

                BtnPlainIconAtm(
                    modifier = modifier
                        .padding(vertical = 16.dp),
                    data = data.button,

                    ) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            action = data.button.action
                        )
                    )
                }
            }
        }
    }
}


data class SingleMediaUploadGroupOrgData(
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.SINGLE_MEDIA_UPLOAD_GROUP_ORG,
    val title: UiText? = null,
    val description: UiText? = null,
    val error: UiText? = null,
    val aspectRatio: AspectRatioType,
    val button: BtnPlainIconAtmData,
    val photo: SmallPicAtmData? = null
) : UIElementData {

    enum class AspectRatioType(override val type: String) : TypeEnum {
        SQUARE("square"), THREE_FOUR("threeFour")
    }


    fun setMediaContent(photo: SmallPicAtmData?): SingleMediaUploadGroupOrgData {
        return this.copy(photo = photo)
    }

    fun setPhotoFromUrl(url: String): SingleMediaUploadGroupOrgData {
        return this.copy(
            photo = SmallPicAtmData(
                id = "document_photo",
                url = url,
                progressState = UIState.MediaUploadState.Loaded,
                isActionRemoveExist = true,
                label = UiText.DynamicString("Сталась\nпомилка"),
                aspectRatioType = SmallPicAtmData.AspectRatioType.THREE_FOUR,
                hasBorder = true
            )
        )
    }

    fun setPhotoFromMediaItem(item: MediaItem): SingleMediaUploadGroupOrgData {
        return this.copy(
            photo = SmallPicAtmData(
                id = item.name,
                url = item.uri.toString(),
                progressState = if (item.isLoading) {
                    UIState.MediaUploadState.InProgress
                } else {
                    UIState.MediaUploadState.Loaded
                },
                isActionRemoveExist = true,
                label = UiText.DynamicString("Сталась помилка"),
                aspectRatioType = SmallPicAtmData.AspectRatioType.THREE_FOUR,
                hasBorder = true
            )
        )
    }
}

fun SingleMediaUploadGroupOrg.toUiModel(): SingleMediaUploadGroupOrgData {
    return SingleMediaUploadGroupOrgData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        title = this.title?.let { UiText.DynamicString(it) },
        description = this.description?.let { UiText.DynamicString(it) },
        aspectRatio = this.aspectRatio?.let { getEnumTypeValue<AspectRatioType>(it) }
            ?: AspectRatioType.THREE_FOUR,
        button = btnPlainIconAtm.toUiModel()
    )
}

enum class SingleMediaUploadGroupOrgMockType {
    no_photo, square_photo, three_four_photo, error
}

fun generateSingleMediaUploadGroupOrgMockData(mockType: SingleMediaUploadGroupOrgMockType): SingleMediaUploadGroupOrgData {
    return when (mockType) {
        SingleMediaUploadGroupOrgMockType.no_photo -> {
            SingleMediaUploadGroupOrgData(
                title = UiText.DynamicString("title"),
                description = UiText.DynamicString("description"),
                aspectRatio = AspectRatioType.THREE_FOUR,
                button = BtnPlainIconAtmData(
                    id = "123",
                    label = UiText.DynamicString("label"),
                    icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
                )
            )
        }

        SingleMediaUploadGroupOrgMockType.square_photo -> {
            SingleMediaUploadGroupOrgData(
                title = UiText.DynamicString("title"),
                description = null,
                aspectRatio = AspectRatioType.SQUARE,
                button = BtnPlainIconAtmData(
                    id = "123",
                    label = UiText.DynamicString("label"),
                    icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code),

                    ),
                photo = SmallPicAtmData(
                    id = "00",
                    aspectRatioType = getEnumTypeValue<SmallPicAtmData.AspectRatioType>(
                        AspectRatioType.SQUARE.type
                    ) ?: SmallPicAtmData.AspectRatioType.SQUARE,
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    label = UiText.DynamicString("Сталась помилка")
                )
            )
        }

        SingleMediaUploadGroupOrgMockType.three_four_photo -> {
            SingleMediaUploadGroupOrgData(
                title = UiText.DynamicString("title"),
                description = UiText.DynamicString("description"),
                aspectRatio = AspectRatioType.THREE_FOUR,
                button = BtnPlainIconAtmData(
                    id = "123",
                    label = UiText.DynamicString("label"),
                    icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code),

                    ),
                photo = SmallPicAtmData(
                    id = "00",
                    aspectRatioType = getEnumTypeValue<SmallPicAtmData.AspectRatioType>(
                        AspectRatioType.THREE_FOUR.type
                    ) ?: SmallPicAtmData.AspectRatioType.THREE_FOUR,
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    label = UiText.DynamicString("Сталась помилка")
                )
            )
        }
        SingleMediaUploadGroupOrgMockType.error -> {
            SingleMediaUploadGroupOrgData(
                title = UiText.DynamicString("title"),
                description = UiText.DynamicString("description"),
                error = UiText.DynamicString("error"),
                aspectRatio = AspectRatioType.THREE_FOUR,
                button = BtnPlainIconAtmData(
                    id = "123",
                    label = UiText.DynamicString("label"),
                    icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code),

                    ),
                photo = SmallPicAtmData(
                    id = "00",
                    aspectRatioType = getEnumTypeValue<SmallPicAtmData.AspectRatioType>(
                        AspectRatioType.THREE_FOUR.type
                    ) ?: SmallPicAtmData.AspectRatioType.THREE_FOUR,
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    label = UiText.DynamicString("Сталась помилка")
                )
            )
        }
    }
}

@Preview
@Composable
fun SingleMediaTitlePreview() {
    SingleMediaUploadGroupOrg(
        data = generateSingleMediaUploadGroupOrgMockData(
            SingleMediaUploadGroupOrgMockType.no_photo
        ),
        onUIAction = {}
    )
}

@Preview
@Composable
fun SingleMediaSquarePreview() {
    SingleMediaUploadGroupOrg(
        data = generateSingleMediaUploadGroupOrgMockData(
            SingleMediaUploadGroupOrgMockType.square_photo
        ),
        onUIAction = {}
    )
}

@Preview
@Composable
fun SingleMediaThreeFourPreview() {
    SingleMediaUploadGroupOrg(
        data = generateSingleMediaUploadGroupOrgMockData(
            SingleMediaUploadGroupOrgMockType.three_four_photo
        ),
        onUIAction = {}
    )
}

@Preview
@Composable
fun SingleMediaThreeFourErrorPreview() {
    SingleMediaUploadGroupOrg(
        data = generateSingleMediaUploadGroupOrgMockData(
            SingleMediaUploadGroupOrgMockType.error
        ),
        onUIAction = {}
    )
}
