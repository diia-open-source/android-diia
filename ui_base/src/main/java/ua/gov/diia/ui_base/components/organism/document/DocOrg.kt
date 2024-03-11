package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.organism.pager.CardFocus
import ua.gov.diia.ui_base.components.organism.pager.DocsCarouselItem
import ua.gov.diia.ui_base.components.theme.WhiteAlpha25
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DocOrg(
    modifier: Modifier = Modifier,
    data: DocOrgData,
    cardFocus: CardFocus = CardFocus.UNDEFINED,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    val cashedData = remember {
        mutableStateOf(data)
    }

    val glideDisplayed =
        remember { mutableStateOf(cardFocus != CardFocus.OUT_OF_FOCUS) }

    LaunchedEffect(key1 = cardFocus) {
        glideDisplayed.value = cardFocus != CardFocus.OUT_OF_FOCUS
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.7F)
            .background(color = WhiteAlpha40, shape = RoundedCornerShape(24.dp))
    ) {
        GlideImage(
            model = cashedData.value.imageUrl,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .fillMaxWidth()
                .aspectRatio(0.7F)
                .alpha(if (glideDisplayed.value) 1f else 0f)

        ) {
            it.error(cashedData.value.placeHolder)
                .placeholder(cashedData.value.placeHolder)
                .load(cashedData.value.imageUrl)
        }

        Column(
            modifier = Modifier
        ) {
            cashedData.value.chipStatusAtmData?.let {
                ChipStatusAtm(
                    modifier = Modifier.padding(
                        top = 20.dp, start = 16.dp,
                        end = 16.dp
                    ), data = it
                )
            }
            cashedData.value.docHeading?.let {
                DocHeadingOrg(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = if (cashedData.value.chipStatusAtmData != null) 0.dp else 20.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                    data = it,
                    onUIAction = onUIAction
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            cashedData.value.docButtonHeading?.let {
                DocButtonHeadingOrg(
                    modifier = Modifier,
                    data = it,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = it.actionKey,
                                data = cashedData.value.docType,
                                optionalId = cashedData.value.position.toString()
                            )
                        )
                    },
                    diiaResourceIconProvider = diiaResourceIconProvider,
                )
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
    if (cashedData.value.docCover != null && cashedData.value.isCurrentPage) {
        Box(modifier = Modifier.fillMaxWidth()) {
            DocCoverMlc(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                data = cashedData.value.docCover!!,
                onUIAction = onUIAction
            )
        }
    }
    if (cashedData.value.docButtonHeading?.isStack == true) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .background(
                    color = WhiteAlpha25,
                    shape = RoundedCornerShape(
                        bottomEnd = 24.dp,
                        bottomStart = 24.dp
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {}
    }

}

data class DocOrgData(
    val actionKey: String = UIActionKeysCompose.DOC_ORG_DATA,
    val imageUrl: String,
    val docHeading: DocHeadingOrgData? = null,
    val docCover: DocCoverMlcData? = null,
    val docButtonHeading: DocButtonHeadingOrgData? = null,
    val docType: String,
    val position: Int,
    val isCurrentPage: Boolean = false,
    val chipStatusAtmData: ChipStatusAtmData? = null,
    val placeHolder: Int
) : DocsCarouselItem, UIElementData

@Preview
@Composable
fun DocOrgDataPreview() {

    val prevData = DocOrgData(
        actionKey = "sasd",
        imageUrl = "your_url",
        docHeading = DocHeadingOrgData(
            ":bnkjdbq",
            heading = HeadingWithSubtitlesMlcData(
                value = "djsfjk",
                subtitles = listOf()
            )
        ),
        docCover = null,
        docType = "dsadasd",
        position = 2,
        placeHolder = R.drawable.diia_article_placeholder
    )

    DocOrg(
        modifier = Modifier,
        data = prevData,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}