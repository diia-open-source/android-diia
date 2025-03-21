package ua.gov.diia.ui_base.components.organism.media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.media.SmallPicAtm
import ua.gov.diia.ui_base.components.atom.media.SmallPicAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun MediaGroupOrg(
    modifier: Modifier = Modifier,
    data: MediaGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .padding(start = 24.dp, top = 16.dp, end = 24.dp)
            .fillMaxWidth()
    ) {
        data.title?.let {
            Text(
                modifier = Modifier,
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody
            )
        }
        data.subtitle?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = it.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = BlackAlpha50
            )
        }

        data.photoItems?.let { photoItems ->
            LazyRow(
                modifier = Modifier.padding(top = 16.dp),
                state = listState,
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
                                    data = index.toString(),
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
    }
}

data class MediaGroupOrgData(
    val actionKey: String = UIActionKeysCompose.MEDIA_GROUP_ORG,
    val componentId: UiText? = null,
    val title: UiText? = null,
    val subtitle: UiText? = null,
    val photoItems: SnapshotStateList<SmallPicAtmData>? = null
) : UIElementData {
}

fun generateMediaGroupOrgMockData(): MediaGroupOrgData {
    return MediaGroupOrgData(
        title = UiText.DynamicString("title"),
        subtitle = UiText.DynamicString("subtitle"),
        photoItems = SnapshotStateList<SmallPicAtmData>().apply {
            add(
                SmallPicAtmData(
                    id = "1",
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    isActionRemoveExist = false,
                    progressState = UIState.MediaUploadState.Loaded
                )
            )
            add(
                SmallPicAtmData(
                    id = "2",
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    isActionRemoveExist = false,
                    progressState = UIState.MediaUploadState.Loaded
                )
            )
            add(
                SmallPicAtmData(
                    id = "3",
                    url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
                    isActionRemoveExist = false,
                    progressState = UIState.MediaUploadState.Loaded,
                    isActionPlayExist = true
                )
            )
        }
    )
}

@Preview
@Composable
fun MediaTitleOrgTestPreview() {
    MediaGroupOrg(data = generateMediaGroupOrgMockData()) {}
}