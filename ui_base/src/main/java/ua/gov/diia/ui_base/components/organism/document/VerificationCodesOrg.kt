package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.core.models.common_compose.org.verification.VerificationCodesOrg
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.DIALOG_ACTION_REFRESH
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.molecule.code.BarCodeMlc
import ua.gov.diia.ui_base.components.molecule.code.BarCodeMlcData
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlc
import ua.gov.diia.ui_base.components.molecule.code.QrCodeMlcData
import ua.gov.diia.ui_base.components.molecule.code.toUIModel
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupOrg
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupOrgData
import ua.gov.diia.ui_base.components.organism.group.toUIModel
import ua.gov.diia.ui_base.components.subatomic.timer.ExpireLabel

@Composable
fun VerificationCodesOrg(
    modifier: Modifier = Modifier,
    data: VerificationCodesOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    var expired by remember { mutableStateOf<Boolean?>(false) }

    LaunchedEffect(key1 = data.qrCode.qrLink.asString()) {
        expired = false
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.67f)
            .background(color = White, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .padding(top = 24.dp)
            .testTag(data.componentId?.asString() ?: ""),
        contentAlignment = Alignment.BottomCenter
    ) {
        if ((progressIndicator.first == data.id && progressIndicator.second) || data.idle || (expired == null && data.errorStubMessageMlc == null)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = White, shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                TridentLoaderAtm()
            }
        } else {
            if (data.errorStubMessageMlc != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.98f),
                    contentAlignment = Alignment.Center
                ) {
                    data.stubMessageMlc?.let {
                        StubMessageMlc(
                            modifier = Modifier.padding(bottom = 24.dp),
                            data = data.errorStubMessageMlc,
                            onUIAction = {
                                expired = null
                                onUIAction(it)
                            }
                        )
                    }
                }
            } else {
                if (expired == true) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .fillMaxSize()
                            .alpha(0.98f),
                        contentAlignment = Alignment.Center
                    ) {
                        data.stubMessageMlc?.let {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                StubMessageMlc(
                                    data = data.stubMessageMlc,
                                    onUIAction = {
                                        expired = null
                                        onUIAction(it)
                                    }
                                )
                                Spacer(modifier = Modifier.height(64.dp).fillMaxWidth())
                            }
                        }
                    }
                }
                if (expired == false) {
                    if (data.timer == null && data.toggleButtonGroup == null) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 40.dp)
                                .fillMaxSize()
                                .background(White),
                            contentAlignment = Alignment.Center
                        ) {
                            QrCodeMlc(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f),
                                data = data.qrCode
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                            if (data.timer != null && data.expireLabelFirst != null) {
                                ExpireLabel(
                                    expireLabelFirst = data.expireLabelFirst,
                                    timer = data.timer,
                                    expireLabelLast = data.expireLabelLast,
                                    expired = expired ?: true
                                ) {
                                    expired = true
                                }
                            } else {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            if (data.toggleButtonGroup != null) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .padding(horizontal = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    QrCodeMlc(
                                        modifier = Modifier
                                            .conditional(data.toggleButtonGroup.items.firstOrNull { it.id == VerificationCodesOrgToggleButtonCodes.qr.name }?.selectionState == UIState.Selection.Unselected) {
                                                alpha(0f)
                                                    .height(0.dp)
                                            }
                                            .conditional(data.toggleButtonGroup.items.firstOrNull { it.id == VerificationCodesOrgToggleButtonCodes.qr.name }?.selectionState == UIState.Selection.Selected) {
                                                fillMaxWidth()
                                                    .aspectRatio(1f)
                                            },
                                        data = data.qrCode
                                    )
                                    data.barCode?.let {
                                        BarCodeMlc(
                                            modifier = Modifier.conditional(data.toggleButtonGroup.items.firstOrNull { it.id == VerificationCodesOrgToggleButtonCodes.ean.name }?.selectionState == UIState.Selection.Unselected) {
                                                alpha(0f)
                                                    .height(0.dp)
                                            }, data = data.barCode
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                                ToggleButtonGroupOrg(
                                    modifier = Modifier.fillMaxWidth(),
                                    data = data.toggleButtonGroup,
                                    onUIAction = onUIAction
                                )
                            } else {
                                QrCodeMlc(
                                    modifier = Modifier
                                        .padding(horizontal = 40.dp)
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    data = data.qrCode
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Use [idle] param for loading state (first launch) displaying
 */
data class VerificationCodesOrgData(
    val actionKey: String = UIActionKeysCompose.VERIFICATION_CODES_ORG,
    val id: String? = UIActionKeysCompose.VERIFICATION_CODES_ORG,
    val componentId: UiText? = null,
    val expireLabelFirst: UiText? = null,
    val expireLabelLast: UiText? = null,
    val timer: Int? = null,
    val qrCode: QrCodeMlcData,
    val barCode: BarCodeMlcData? = null,
    val toggleButtonGroup: ToggleButtonGroupOrgData? = null,
    val stubMessageMlc: StubMessageMlcData? = null,
    val errorStubMessageMlc: StubMessageMlcData? = null,
    val idle: Boolean = false
) {
    fun onToggleClicked(id: String): VerificationCodesOrgData {
        return if (id == VerificationCodesOrgToggleButtonCodes.qr.name || id == VerificationCodesOrgToggleButtonCodes.ean.name) {
            this.copy(
                toggleButtonGroup = this.toggleButtonGroup?.onToggleClicked(toggleCode = id)
            )
        } else {
            this
        }
    }

    fun setIdleMode(value: Boolean): VerificationCodesOrgData {
        return this.copy(
            idle = value
        )
    }
}

fun VerificationCodesOrg.toUIModel(idle: Boolean = false): VerificationCodesOrgData {
    return VerificationCodesOrgData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        qrCode = this.ua.qrCodeMlc.toUIModel(),
        barCode = this.ua.barCodeMlc?.toUIModel(),
        expireLabelFirst = this.ua.expireLabel?.expireLabelFirst.toDynamicStringOrNull(),
        expireLabelLast = this.ua.expireLabel?.expireLabelLast.toDynamicStringOrNull(),
        timer = this.ua.expireLabel?.timer,
        toggleButtonGroup = this.ua.toggleButtonGroupOrg?.toUIModel(),
        stubMessageMlc = this.ua.stubMessageMlc.toUIModel(),
        idle = idle
    )
}


enum class VerificationCodesOrgToggleButtonCodes {
    qr, ean
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun VerificationCodesOrgPreview() {

    val coroutineScope = rememberCoroutineScope()
    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.VERIFICATION_CODES_ORG to false) }

    val data = VerificationCodesOrgData(
        qrCode = getQrCodeMlc(),
        barCode = getBarCodeMlc(),
        expireLabelFirst = "Код діятиме".toDynamicString(),
        expireLabelLast = "хв".toDynamicString(),
        timer = 10,
        toggleButtonGroup = getToggleGroup(),
        stubMessageMlc = getStubMessageMlc()
    )
    val state = remember { mutableStateOf(data) }

    VerificationCodesOrg(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        data = state.value,
        progressIndicator = progressIndicator
    ) {
        it.action?.type?.let { action ->
            when (action) {
                VerificationCodesOrgToggleButtonCodes.qr.name,
                VerificationCodesOrgToggleButtonCodes.ean.name -> {
                    state.value = state.value.onToggleClicked(action)
                }

                DIALOG_ACTION_REFRESH -> {
                    coroutineScope.launch {
                        state.value = state.value.setIdleMode(true)
                        delay(3000)
                        updateVerificationCodesOrgFromRemote(state)
                    }
                }
            }
        }

    }
}

private fun updateVerificationCodesOrgFromRemote(state: MutableState<VerificationCodesOrgData>) {
    state.value = state.value.copy(
        qrCode = state.value.qrCode.copy(
            qrLink = "${(state.value.qrCode.qrLink as UiText.DynamicString).value}1".toDynamicString()
        ),
        idle = false
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun VerificationCodesOrgPreview_OnlyQR() {
    val data = VerificationCodesOrgData(
        qrCode = getQrCodeMlc(),
        stubMessageMlc = getStubMessageMlc()
    )
    val state = remember { mutableStateOf(data) }

    VerificationCodesOrg(
        data = state.value,
        progressIndicator = UIActionKeysCompose.VERIFICATION_CODES_ORG to false
    ) {

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun VerificationCodesOrgPreview_With_Error() {

    val coroutineScope = rememberCoroutineScope()
    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.VERIFICATION_CODES_ORG to false) }

    val data = VerificationCodesOrgData(
        qrCode = getQrCodeMlc(),
        barCode = getBarCodeMlc(),
        expireLabelFirst = "Код діятиме".toDynamicString(),
        expireLabelLast = "хв".toDynamicString(),
        timer = 10,
        toggleButtonGroup = getToggleGroup(),
        stubMessageMlc = getStubMessageMlc()
    )
    val state = remember { mutableStateOf(data) }

    VerificationCodesOrg(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        data = state.value,
        progressIndicator = progressIndicator
    ) {
        it.action?.type?.let { action ->
            when (action) {
                VerificationCodesOrgToggleButtonCodes.qr.name,
                VerificationCodesOrgToggleButtonCodes.ean.name -> {
                    state.value = state.value.onToggleClicked(action)
                }

                DIALOG_ACTION_REFRESH -> {
                    coroutineScope.launch {
                        state.value = state.value.setIdleMode(true)
                        delay(3000)
                        updateVerificationCodesOrgFromRemoteWithError(state)
                    }
                }
            }
        }

    }
}

private fun updateVerificationCodesOrgFromRemoteWithError(state: MutableState<VerificationCodesOrgData>) {
    state.value = state.value.copy(
        idle = false,
        errorStubMessageMlc = StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDD90"),
            title = UiText.DynamicString("Не вдалось перевірити коди. Спробуйте ще."),
            button = ButtonStrokeAdditionalAtomData(
                id = "",
                title = UiText.DynamicString("Оновити"),
                interactionState = UIState.Interaction.Enabled,
                action = DataActionWrapper(
                    type = "refresh"
                )
            )
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0080FF)
@Composable
fun VerificationCodesOrgPreview_From_Initial_To_Error() {

    val coroutineScope = rememberCoroutineScope()
    var progressIndicator by remember { mutableStateOf(UIActionKeysCompose.VERIFICATION_CODES_ORG to false) }

    val data = VerificationCodesOrgData(
        qrCode = getQrCodeMlc(),
        barCode = getBarCodeMlc(),
        expireLabelFirst = "Код діятиме".toDynamicString(),
        expireLabelLast = "хв".toDynamicString(),
        timer = 20,
        toggleButtonGroup = getToggleGroup(),
        stubMessageMlc = getStubMessageMlc(),
        idle = true
    )
    val state = remember { mutableStateOf(data) }

    LaunchedEffect(key1 = Unit) {
        delay(3000)
        updateVerificationCodesOrgFromRemote(state)
    }

    VerificationCodesOrg(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        data = state.value,
        progressIndicator = progressIndicator
    ) {
        it.action?.type?.let { action ->
            when (action) {
                VerificationCodesOrgToggleButtonCodes.qr.name,
                VerificationCodesOrgToggleButtonCodes.ean.name -> {
                    state.value = state.value.onToggleClicked(action)
                }

                DIALOG_ACTION_REFRESH -> {
                    coroutineScope.launch {
                        state.value = state.value.setIdleMode(true)
                        delay(3000)
                        updateVerificationCodesOrgFromRemoteWithError(state)
                    }
                }
            }
        }

    }
}

private fun getQrCodeMlc(): QrCodeMlcData {
    val data = QrCodeMlcData(
        qrLink = "https://www.diia.gov.ua/".toDynamicString(),
    )
    return data
}

private fun getBarCodeMlc(): BarCodeMlcData {
    val data = BarCodeMlcData(
        ean13code = "5725556653368".toDynamicString()
    )
    return data
}

private fun getToggleGroup(): ToggleButtonGroupOrgData {
    val qr = BtnToggleMlcData(
        id = VerificationCodesOrgToggleButtonCodes.qr.name,
        label = "QR код".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
        action = DataActionWrapper(
            type = VerificationCodesOrgToggleButtonCodes.qr.name
        ),
        selectionState = UIState.Selection.Selected
    )

    val ean13 = BtnToggleMlcData(
        id = VerificationCodesOrgToggleButtonCodes.ean.name,
        label = "Штрих-код".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
        action = DataActionWrapper(
            type = VerificationCodesOrgToggleButtonCodes.ean.name
        ),
        selectionState = UIState.Selection.Unselected
    )
    val data = ToggleButtonGroupOrgData(
        items = listOf(qr, ean13),
        preselected = VerificationCodesOrgToggleButtonCodes.qr.name
    )
    return data
}

private fun getStubMessageMlc(): StubMessageMlcData {
    return StubMessageMlcData(
        icon = UiText.DynamicString("\uD83D\uDD90"),
        title = UiText.DynamicString("На жаль, сталася помилка"),
        description = UiText.DynamicString("Термін перевірки документу вичерпано. Будь ласка, оновіть код для перевірки."),
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