package ua.gov.diia.ui_base.components.organism.sharing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.sharing.SharingCodesOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnLoadPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.button.toUIModel
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlc
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlcData
import ua.gov.diia.ui_base.components.molecule.code.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm
import ua.gov.diia.ui_base.components.subatomic.timer.ExpireLabel

@Composable
fun SharingCodesOrg(
    modifier: Modifier = Modifier,
    data: SharingCodesOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val localDensity = LocalDensity.current

    var viewHeight by remember {
        mutableStateOf(0.dp)
    }

    var expired by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = data.qrCode.qrLink.asString()) {
        expired = false
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned { coordinates ->
                viewHeight =
                    with(localDensity) { coordinates.size.height.toDp() }
            }
            .conditional(
                expired || (progressIndicator.first == data.id && progressIndicator.second)
            ) {
                alpha(0.02f)
            }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                if (data.timer != null && data.expireLabelFirst != null) {
                    ExpireLabel(
                        expireLabelFirst = data.expireLabelFirst,
                        timer = data.timer,
                        expireLabelLast = data.expireLabelLast,
                        expired = expired
                    ) {
                        expired = true
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                QrCodeMlc(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 72.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    data = data.qrCode
                )
                data.btnLoadIconPlainGroupMlc?.let {
                    BtnLoadIconPlainGroupMlc(data = it) { action ->
                        if (!expired) {
                            onUIAction(action.copy(actionKey = data.actionKey))
                        }
                    }
                }
            }
        }
        if ((progressIndicator.first == data.id && progressIndicator.second)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 380.dp)
                    .height(viewHeight),
                contentAlignment = Alignment.Center
            ) {
                TridentLoaderAtm()
            }
        }
        if (expired && !(progressIndicator.first == data.id && progressIndicator.second)) {
            data.stubMessageMlc?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(viewHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StubMessageMlc(
                            data = it,
                            onUIAction = {
                                onUIAction(it)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(64.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

data class SharingCodesOrgData(
    val actionKey: String = UIActionKeysCompose.SHARING_CODES_ORG,
    val id: String? = UIActionKeysCompose.SHARING_CODES_ORG,
    val componentId: UiText? = null,
    val expireLabelFirst: UiText? = null,
    val expireLabelLast: UiText? = null,
    val timer: Int? = null,
    val qrCode: QrCodeMlcData,
    val btnLoadIconPlainGroupMlc: BtnLoadIconPlainGroupMlcData? = null,
    val stubMessageMlc: StubMessageMlcData? = null,
) : UIElementData

fun SharingCodesOrg.toUIModel(): SharingCodesOrgData {
    return SharingCodesOrgData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        expireLabelFirst = expireLabel?.expireLabelFirst?.let { UiText.DynamicString(it) },
        expireLabelLast = expireLabel?.expireLabelLast?.let { UiText.DynamicString(it) },
        timer = expireLabel?.timer,
        qrCode = qrCodeMlc.toUIModel(),
        btnLoadIconPlainGroupMlc = btnLoadIconPlainGroupMlc?.toUIModel(),
        stubMessageMlc = stubMessageMlc?.toUIModel()
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun SharingCodesOrgPreview() {

    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.SHARING_CODES_ORG to false) }

    val data = SharingCodesOrgData(
        qrCode = getQrCodeMlc(),
        expireLabelFirst = "Код діятиме".toDynamicString(),
        expireLabelLast = "хв".toDynamicString(),
        timer = 10,
        stubMessageMlc = getStubMessageMlc(),
        btnLoadIconPlainGroupMlc = getBtnLoadGroup()
    )
    val state = remember { mutableStateOf(data) }

    SharingCodesOrg(
        modifier = Modifier,
        data = state.value,
        progressIndicator = progressIndicator
    ) {

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun SharingCodesOrgPreview_WithoutExpiration() {

    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.SHARING_CODES_ORG to false) }

    val data = SharingCodesOrgData(
        qrCode = getQrCodeMlc(),
        stubMessageMlc = getStubMessageMlc(),
        btnLoadIconPlainGroupMlc = getBtnLoadGroup()
    )
    val state = remember { mutableStateOf(data) }

    SharingCodesOrg(
        modifier = Modifier,
        data = state.value,
        progressIndicator = progressIndicator
    ) {

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun SharingCodesOrgPreview_Loading() {

    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.SHARING_CODES_ORG to true) }

    val data = SharingCodesOrgData(
        qrCode = getQrCodeMlc(),
        expireLabelFirst = "Код діятиме".toDynamicString(),
        expireLabelLast = "хв".toDynamicString(),
        timer = 10,
        stubMessageMlc = getStubMessageMlc(),
        btnLoadIconPlainGroupMlc = getBtnLoadGroup()
    )
    val state = remember { mutableStateOf(data) }

    SharingCodesOrg(
        modifier = Modifier,
        data = state.value,
        progressIndicator = progressIndicator
    ) {

    }
}

@Preview
@Composable
fun SharingCodesOrgPreview_Loading_WhenOnlyRequired() {

    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.SHARING_CODES_ORG to true) }

    val data = SharingCodesOrgData(
        qrCode = getQrCodeMlc(),
        stubMessageMlc = getStubMessageMlc(),
    )
    val state = remember { mutableStateOf(data) }

    SharingCodesOrg(
        modifier = Modifier,
        data = state.value,
        progressIndicator = progressIndicator
    ) {

    }
}


private fun getQrCodeMlc(): QrCodeMlcData {
    val data = QrCodeMlcData(
        qrLink = "https://www.diia.gov.ua/".toDynamicString(),
    )
    return data
}

private fun getStubMessageMlc(): StubMessageMlcData {
    return StubMessageMlcData(
        icon = UiText.DynamicString("\uD83D\uDD90"),
        title = UiText.DynamicString("На жаль, сталася помилка"),
        description = TextWithParametersData(
            text = UiText.DynamicString("Будь ласка, оновіть код для перевірки.")
        ),
        button = ButtonStrokeAdditionalAtomData(
            id = "",
            title = UiText.DynamicString("Оновити"),
            interactionState = UIState.Interaction.Enabled,
            action = DataActionWrapper(
                type = "refresh"
            )
        )
    )
}

private fun getBtnLoadGroup(): BtnLoadIconPlainGroupMlcData {
    val btn = BtnLoadPlainIconAtmData(
        componentId = "component_id".toDynamicString(),
        id = "id3",
        label = "Подилитись посиланням".toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.SHARE.code),
        action = DataActionWrapper(
            type = "register",
            resource = "1234567890"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    return BtnLoadIconPlainGroupMlcData(items = listOf(btn))
}