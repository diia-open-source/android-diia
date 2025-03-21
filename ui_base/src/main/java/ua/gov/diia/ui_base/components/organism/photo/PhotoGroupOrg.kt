package ua.gov.diia.ui_base.components.organism.photo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.photo.PhotoGroupOrg
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmMockType
import ua.gov.diia.ui_base.components.atom.media.generateArticlePicAtmMockData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun PhotoGroupOrg(
    modifier: Modifier = Modifier,
    data: PhotoGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .testTag(data.componentId?.asString() ?: ""),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(130f / 175f)
        ) {
            ArticlePicAtm(
                data = data.photos[0],
                inCarousel = true,
                clickable = false,
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(130f / 175f)
        ) {
            ArticlePicAtm(
                data = data.photos[1],
                inCarousel = true,
                clickable = false,
                onUIAction = onUIAction
            )
        }
    }
}


data class PhotoGroupOrgData(
    val actionKey: String = UIActionKeysCompose.PHOTO_GROUP_ORG,
    val componentId: UiText? = null,
    val id: String? = null,
    val photos: List<ArticlePicAtmData>
) : UIElementData


fun PhotoGroupOrg.toUiModel(): PhotoGroupOrgData {
    return PhotoGroupOrgData(
        componentId = this.componentId.orEmpty().toDynamicString(),
        photos = buildList {
            pictures.forEach { add(it.articlePicAtm.toUiModel()) }
        }
    )
}

enum class PhotoGroupOrgMockType {
    valid_full, invalid_full, mixed_full
}

fun generatePhotoGroupOrgMockData(type: PhotoGroupOrgMockType): PhotoGroupOrgData {
    return when (type) {
        PhotoGroupOrgMockType.valid_full -> {
            PhotoGroupOrgData(
                photos = listOf(
                    generateArticlePicAtmMockData(ArticlePicAtmMockType.valid),
                    generateArticlePicAtmMockData(ArticlePicAtmMockType.valid)
                )
            )
        }

        PhotoGroupOrgMockType.mixed_full -> {
            PhotoGroupOrgData(
                photos = listOf(
                    generateArticlePicAtmMockData(ArticlePicAtmMockType.invalid),
                    generateArticlePicAtmMockData(ArticlePicAtmMockType.valid)
                )
            )
        }

        PhotoGroupOrgMockType.invalid_full -> {
            PhotoGroupOrgData(
                photos = listOf(
                    generateArticlePicAtmMockData(ArticlePicAtmMockType.invalid),
                    generateArticlePicAtmMockData(ArticlePicAtmMockType.invalid)
                )
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PhotoGroupOrgFullValidPreview() {
    PhotoGroupOrg(
        data = generatePhotoGroupOrgMockData(
            PhotoGroupOrgMockType.valid_full
        ),
        onUIAction = {}
    )
}

@Composable
@Preview
fun PhotoGroupOrgFullInvalidPreview() {
    PhotoGroupOrg(
        data = generatePhotoGroupOrgMockData(
            PhotoGroupOrgMockType.invalid_full
        ),
        onUIAction = {}
    )
}

@Composable
@Preview
fun PhotoGroupOrgMixedPreview() {
    PhotoGroupOrg(
        data = generatePhotoGroupOrgMockData(
            PhotoGroupOrgMockType.mixed_full
        ),
        onUIAction = {}
    )
}