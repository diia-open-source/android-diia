package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.FullScreenVideoMlc
import ua.gov.diia.ui_base.components.molecule.FullScreenVideoMlcData

@Composable
fun FullScreenVideoOrg(
    modifier: Modifier = Modifier,
    data: FullScreenVideoOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {

    val state = remember { mutableStateOf(true) }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            data.source?.let {
                FullScreenVideoMlc(
                    modifier = Modifier,
                    data = FullScreenVideoMlcData(url = it),
                    connectivityState = state
                )
            }
        }

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            data.btnPrimaryDefaultAtm?.let {
                BtnPrimaryDefaultAtm(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    data = it,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
            data.btnPlainAtm?.let {
                BtnPlainAtm(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    data = it,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
            SpacerAtm(
                data = SpacerAtmData( SpacerAtmType.SPACER_16 )
            )
        }
    }
}

data class FullScreenVideoOrgData(
    val source: String? = null,
    val playerBtnAtm: BtnPrimaryDefaultAtmData? = null,
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtmData? = null,
    val btnPlainAtm: BtnPlainAtmData? = null,
) : UIElementData

@Preview
@Composable
fun FullScreenVideoOrg_Preview() {
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
    FullScreenVideoOrg(data = data){}
}