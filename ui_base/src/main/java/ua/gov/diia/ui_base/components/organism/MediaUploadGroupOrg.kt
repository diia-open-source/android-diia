package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.media.MediaUploadGroupOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.media.SmallPicAtm
import ua.gov.diia.ui_base.components.atom.media.SmallPicAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun MediaUploadGroupOrg(
    modifier: Modifier = Modifier,
    data: MediaUploadGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val listState = rememberLazyListState()

    Column(modifier = modifier
        .padding(top = 24.dp, start = 24.dp, end = 24.dp,)
        .background(
            color = White, shape = RoundedCornerShape(8.dp)
        )) {

        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody
            )
        }
        data.description?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = BlackAlpha50
            )
        }

        data.photoItems?.let { photoItems ->
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(
                    items = photoItems
                ) { index, item ->
                    SmallPicAtm(
                        data = item,
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
                    if (index < (photoItems.size - 1)) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        DividerSlimAtom(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(1.dp),
            color = BlackSqueeze
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    if (data.photoItems?.size?.let{ it  < 10 } ?: true) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    }
                },
            horizontalArrangement = Arrangement.Center
        ) {
            BtnPlainIconAtm(
                modifier = modifier
                    .padding(vertical = 16.dp),
                data = data.button,

            ){
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

data class MediaUploadGroupOrgData(
    val actionKey: String = UIActionKeysCompose.MEDIA_UPLOAD_GROUP_ORG,
    val title: UiText? = null,
    val description: UiText? = null,
    val maxCount: Int? = null,
    val button: BtnPlainIconAtmData,
    val photoItems: SnapshotStateList<SmallPicAtmData>? = null
) : UIElementData {
    fun setMediaContent(list: List<SmallPicAtmData>): MediaUploadGroupOrgData {
        val newItems = SnapshotStateList<SmallPicAtmData>()
        newItems.addAll(list)
        return this.copy(
            photoItems = if (newItems.isEmpty()) null else newItems,
            button = this.button.changeInteractionState(
                photoItems?.size?.let{ it  < 10 } ?: true)
        )
    }
}

fun MediaUploadGroupOrg.toUiModel(): MediaUploadGroupOrgData {
    return MediaUploadGroupOrgData(
        title = this.title?.let { UiText.DynamicString(it) },
        description = this.description?.let { UiText.DynamicString(it) },
        maxCount = maxCount,
        button = btnPlainIconAtm.toUiModel()
    )
}

@Preview
@Composable
fun MediaTitleOrgPreview() {
    val data = MediaUploadGroupOrgData(
        title = UiText.DynamicString("title"),
        description = UiText.DynamicString("description"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        )
    )

    MediaUploadGroupOrg(data = data) {

    }

}

@Preview
@Composable
fun MediaTitleOrgTestPreview() {
    val data = MediaUploadGroupOrgData(
        title = UiText.DynamicString("Додати фото"),
        description = UiText.DynamicString("Вага фото не має перевищувати 5МБ"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати фото"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
        photoItems = SnapshotStateList<SmallPicAtmData>().apply {
            repeat(5) { i ->
                add(
                    SmallPicAtmData(
                        id = "00$i",
                        url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                        label = UiText.DynamicString("Сталась помилка")
                    )
                )
            }
        }
    )


    MediaUploadGroupOrg(data = data) {}
}

@Preview
@Composable
fun MediaTitleOrgTestPreview_With_One_Photo() {
    val data = MediaUploadGroupOrgData(
        title = UiText.DynamicString("Додати фото"),
        description = UiText.DynamicString("Вага фото не має перевищувати 5МБ"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати фото"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
        photoItems = SnapshotStateList<SmallPicAtmData>().apply {
            add(
                SmallPicAtmData(
                    id = "001",
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    label = UiText.DynamicString("Лейбл")
                )
            )
        }
    )


    MediaUploadGroupOrg(data = data) {}
}