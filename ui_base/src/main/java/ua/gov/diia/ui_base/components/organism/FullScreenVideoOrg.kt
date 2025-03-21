package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.media.FullScreenVideoOrg
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.FullScreenVideoMlc
import ua.gov.diia.ui_base.components.molecule.FullScreenVideoMlcData
import ua.gov.diia.ui_base.components.noRippleClickable

@Composable
fun FullScreenVideoOrg(
    modifier: Modifier = Modifier,
    data: FullScreenVideoOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    connectivityState: Boolean,
    onUIAction: (UIAction) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp),
        color = Color.Black,
    ) {

        Box(
            modifier = Modifier
                .background(Color.Black)
                .testTag(data.componentId?.asString() ?: ""),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                data.source?.let {
                    FullScreenVideoMlc(
                        modifier = Modifier.aspectRatio(9f / 16f),
                        data = FullScreenVideoMlcData(url = it),
                        connectivityState = connectivityState
                    )
                }
            }
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 24.dp)
                    .size(32.dp)
                    .noRippleClickable {
                        onUIAction(
                            UIAction(
                                actionKey = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK
                            )
                        )
                    },
                painter = painterResource(R.drawable.ic_close_white),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
            data.btnPrimaryDefaultAtm?.let {
                BtnPrimaryDefaultAtm(
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .align(Alignment.BottomCenter),
                    data = it,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
            data.btnPlainAtm?.let {
                BtnPlainAtm(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.BottomCenter),
                    data = it,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

data class FullScreenVideoOrgData(
    val source: String? = null,
    val componentId: UiText? = null,
    val playerBtnAtm: BtnPrimaryDefaultAtmData? = null,
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtmData? = null,
    val btnPlainAtm: BtnPlainAtmData? = null,
) : UIElementData

fun FullScreenVideoOrg.toUIModel(): FullScreenVideoOrgData {
    return FullScreenVideoOrgData(
        source = this.source,
        playerBtnAtm = null,
        btnPrimaryDefaultAtm = this.btnPrimaryDefaultAtm?.toUIModel(),
        btnPlainAtm = this.btnPlainAtm?.toUIModel(),
        componentId = this.componentId.toDynamicStringOrNull()

    )
}

@Preview
@Composable
fun FullScreenVideoOrg_Preview() {
    val state = remember { mutableStateOf(true) }
    val data = FullScreenVideoOrgData(
        source = "https://player.vimeo.com/progressive_redirect/playback/753810206/rendition/720p/file.mp4?loc=external&signature=e4eefa0f7f99e2149d604a494cfbec74e76b9e7066719e6237bb190aee28e3d0",
        btnPrimaryDefaultAtm = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        ),
        btnPlainAtm = BtnPlainAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    FullScreenVideoOrg(data = data, connectivityState = state.value) {}
}