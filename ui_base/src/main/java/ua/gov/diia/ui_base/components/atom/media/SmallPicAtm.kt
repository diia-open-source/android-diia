package ua.gov.diia.ui_base.components.atom.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.BlackAlpha60
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue
import ua.gov.diia.ui_base.components.theme.RedAttention
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SmallPicAtm(
    modifier: Modifier = Modifier,
    data: SmallPicAtmData,
    onUIAction: (UIAction) -> Unit
) {
    Box(modifier = modifier.size(height = 112.dp, width = 112.dp)) {
        GlideSubcomposition(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(ColumbiaBlue),
            model = data.url
        )
        {
            Image(
                painter,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

//loading
        if(data.progressState == UIState.MediaUploadState.InProgress) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlackAlpha60, shape = RoundedCornerShape(8.dp)),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoaderCircularEclipse23Subatomic(
                    modifier = Modifier.size(18.dp)
                )
            }
        }
// error
        if(data.progressState == UIState.MediaUploadState.FailedLoading){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RedAttention, shape = RoundedCornerShape(8.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1.0f, true))
                Column(
                    modifier = Modifier
                        .size(height = 32.dp, width = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    IconAtm(
                        modifier = modifier.size(24.dp),
                        data = IconAtmData(code = DiiaResourceIcon.RETRY_WHITE.code),
                    ){
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = data.id,
                                action = DataActionWrapper(
                                    type = "actionRetry"
                                )
                            )
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .weight(1.0f, true),
                    textAlign = TextAlign.Center,
                    text = data.label?.asString() ?: "",
                    style = DiiaTextStyle.t4TextSmallDescription,
                    color = White
                )
            }
        }
// icon close
        if (data.progressState == UIState.MediaUploadState.FailedLoading ||
            data.progressState == UIState.MediaUploadState.Loaded
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Column(
                    modifier = Modifier.size(height = 32.dp, width = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    IconAtm(
                        modifier = modifier.size(24.dp),
                        data = IconAtmData(code = DiiaResourceIcon.ELLIPSE_CROSS.code),
                    ) {
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
                }
            }
        }
    }
}

data class SmallPicAtmData(
    val actionKey: String = UIActionKeysCompose.SMALL_PIC_ATM_DATA,
    var id: String,
    val url: String,
    val contentDescription: UiText? = null,
    val label: UiText? = null,
    val action: DataActionWrapper? = null,
    var progressState: UIState.MediaUploadState = UIState.MediaUploadState.InProgress,
): UIElementData

@Preview
@Composable
fun SmallPicAtmInProgressPreview() {
    val data = SmallPicAtmData(
        id = "0",
        url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
        label = UiText.DynamicString("Сталась помилка"),
        progressState = UIState.MediaUploadState.InProgress
    )

    SmallPicAtm(data = data) {}
}

@Preview
@Composable
fun SmallPicAtmInFailedLoadingPreview() {
    val data = SmallPicAtmData(
        id = "0",
        url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
        label = UiText.DynamicString("Сталась помилка"),
        progressState = UIState.MediaUploadState.FailedLoading
    )

    SmallPicAtm(data = data) {}
}

@Preview
@Composable
fun SmallPicAtmInLoadedPreview() {
    val data = SmallPicAtmData(
        id = "0",
        url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
        label = UiText.DynamicString("Сталась помилка"),
        progressState = UIState.MediaUploadState.Loaded
    )

    SmallPicAtm(data = data) {}
}