package ua.gov.diia.ui_base.components.atom.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import ua.gov.diia.core.util.type_enum.TypeEnum
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha60
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.RedAttention
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SmallPicAtm(
    modifier: Modifier = Modifier,
    data: SmallPicAtmData,
    onUIAction: (UIAction) -> Unit
) {

    var requestState = remember { mutableStateOf(false) }

    val aspectRatio = when (data.aspectRatioType) {
        SmallPicAtmData.AspectRatioType.SQUARE -> 1f
        SmallPicAtmData.AspectRatioType.THREE_FOUR -> 3f / 4f
    }

    val width = when (data.aspectRatioType) {
        SmallPicAtmData.AspectRatioType.SQUARE -> 112.dp
        SmallPicAtmData.AspectRatioType.THREE_FOUR -> 132.dp
    }

    Box(
        modifier = modifier
            .width(width)
            .aspectRatio(aspectRatio)
    )
    {
        GlideSubcomposition(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(ColumbiaBlue)
                .then(
                    if (data.hasBorder) {
                        Modifier.border(
                            width = 1.dp,
                            color = BlackAlpha10,
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
            model = data.url
        ) {
            when (state) {
                RequestState.Failure -> {
                    SmallPicAtmError()
                }

                RequestState.Loading -> {
                    SmallPicAtmLoading()
                }

                is RequestState.Success -> {
                    requestState.value = true
                    Image(
                        painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.noRippleClickable {
                            if (data.progressState == UIState.MediaUploadState.Loaded) {
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = data.id,
                                        action = data.action
                                    )
                                )
                            }
                        }
                    )
                }

            }
        }

        if (data.progressState == UIState.MediaUploadState.InProgress) {
            SmallPicAtmLoading()
        }
// error
        if (data.progressState == UIState.MediaUploadState.FailedLoading) {
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
                    ) {
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
                    if (data.isActionRemoveExist) {
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
        // icon play
        if (requestState.value && data.isActionPlayExist) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = modifier
                        .size(44.dp),
                    painter = painterResource(id = R.drawable.ic_player_btn_atm_play),
                    contentDescription = null
                )
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
    val isActionRemoveExist: Boolean = true,
    val isActionPlayExist: Boolean = false,
    val aspectRatioType: AspectRatioType = AspectRatioType.SQUARE,
    val hasBorder: Boolean = false
) : UIElementData {
    enum class AspectRatioType(override val type: String) : TypeEnum {
        SQUARE("square"), THREE_FOUR("threeFour")
    }
}

@Composable
fun SmallPicAtmLoading() {
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

@Composable
fun SmallPicAtmError() {
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
                modifier = Modifier.size(24.dp),
                data = IconAtmData(code = DiiaResourceIcon.RETRY_WHITE.code),
            )
        }
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .weight(1.0f, true),
            textAlign = TextAlign.Center,
            text = "Сталась помилка",
            style = DiiaTextStyle.t4TextSmallDescription,
            color = White
        )
    }
}

fun generateSmallPicAtmMockData(
    progressState: UIState.MediaUploadState,
    aspectRatioType: SmallPicAtmData.AspectRatioType,
    isActionRemoveExist: Boolean = true,
    isActionPlayExist: Boolean = false,
): SmallPicAtmData {
    return SmallPicAtmData(
        id = "0",
        url = "https://diia.gov.ua/img/diia-october-prod/uploads/public/65b/cf6/157/thumb_901_730_410_0_0_auto.jpg",
        label = UiText.DynamicString("Сталась помилка"),
        progressState = progressState,
        aspectRatioType = aspectRatioType,
        isActionRemoveExist = isActionRemoveExist,
        isActionPlayExist = isActionPlayExist
    )
}

@Preview
@Composable
fun SmallPicAtmInProgressSquarePreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.InProgress,
            SmallPicAtmData.AspectRatioType.SQUARE
        )
    ) {}
}

@Preview
@Composable
fun SmallPicAtmInProgressThreeFourPreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.InProgress,
            SmallPicAtmData.AspectRatioType.THREE_FOUR
        )
    ) {}
}

@Preview
@Composable
fun SmallPicAtmInFailedLoadingSquarePreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.FailedLoading,
            SmallPicAtmData.AspectRatioType.SQUARE
        )
    ) {}
}

@Preview
@Composable
fun SmallPicAtmInFailedLoadingThreeFourPreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.FailedLoading,
            SmallPicAtmData.AspectRatioType.THREE_FOUR
        )
    ) {}
}


@Preview
@Composable
fun SmallPicAtmInLoadedSquarePreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.Loaded,
            SmallPicAtmData.AspectRatioType.SQUARE
        )
    ) {}
}

@Preview
@Composable
fun SmallPicAtmInLoadedThreeFourPreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.Loaded,
            SmallPicAtmData.AspectRatioType.THREE_FOUR
        )
    ) {}
}

@Preview
@Composable
fun SmallPicAtmInLoadedPlaySquarePreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.Loaded,
            aspectRatioType = SmallPicAtmData.AspectRatioType.SQUARE,
            isActionRemoveExist = false,
            isActionPlayExist = true
        )
    ) {}
}

@Preview
@Composable
fun SmallPicAtmInLoadedPlayThreeForPreview() {
    SmallPicAtm(
        data = generateSmallPicAtmMockData(
            UIState.MediaUploadState.Loaded,
            aspectRatioType = SmallPicAtmData.AspectRatioType.THREE_FOUR,
            isActionRemoveExist = false,
            isActionPlayExist = true
        )
    ) {}
}