package ua.gov.diia.ui_base.components.organism.document

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.image.BlurBitmap
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.button.BtnToggleMlcData
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroup
import ua.gov.diia.ui_base.components.organism.group.ToggleButtonGroupData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha40
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha25

@Composable
fun DocCodeOrg(
    modifier: Modifier = Modifier,
    data: DocCodeOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    var docBarcodeExpired by remember { mutableStateOf(data.expired) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(if (!data.isStack) 0.7F else 0.684F),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7F)
                .background(color = White, shape = RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, start = 32.dp, end = 32.dp, bottom = 32.dp)
            ) {

                if (data.actionKey == progressIndicator.first && progressIndicator.second) {
                    TridentLoaderMolecule()
                } else {
                    if (data.exception == null) {
                        if (data.showToggle) {
                            Spacer(
                                modifier = Modifier.height(
                                    if (data.toggle.ean13.selectionState == UIState.Selection.Selected)
                                        58.dp else
                                        8.dp
                                )
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))

                        }

                        data.timerText?.let {
                            TimerText(
                                text = data.timerText,
                                minutes = 3,
                                units = if (data.localization == Localization.ua) "хв" else "min"
                            ) {
                                docBarcodeExpired = true
                            }
                        }
                        if (docBarcodeExpired) {
                            Box(modifier = Modifier) {
                                if (data.toggle.qr.selectionState == UIState.Selection.Selected) {
                                    data.qrBitmap?.let {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = if (data.showToggle) 16.dp else 0.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val originalQRBitmap = it.copy(it.config, true)

                                            BlurBitmap(
                                                bitmap = originalQRBitmap,
                                                width = 231.dp,
                                                height = 231.dp
                                            )
                                        }
                                    }
                                }
                                if (data.toggle.ean13.selectionState == UIState.Selection.Selected) {
                                    data.ean13Bitmap?.let {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = if (data.showToggle) 16.dp else 0.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val originalEan13Bitmap = it.copy(it.config, true)

                                            BlurBitmap(bitmap = originalEan13Bitmap, width = 247.dp, height = 100.dp)
                                        }
                                    }
                                }
                                Column(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(top = 48.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "☝",
                                        style = TextStyle(
                                            fontSize = 32.sp,
                                            lineHeight = 36.sp
                                        ),
                                        fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),

                                        )
                                    Text(
                                        modifier = Modifier.padding(top = 16.dp),
                                        text = if (data.localization == Localization.eng) {
                                            "The code has expired"

                                        } else {
                                            "Час дії коду закінчився"
                                        },
                                        fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp
                                        )
                                    )

                                    BtnStrokeAdditionalAtm(
                                        modifier = Modifier.padding(top = 24.dp),
                                        data = ButtonStrokeAdditionalAtomData(
                                            title = if (data.localization == Localization.eng) {
                                                UiText.DynamicString("Update the code")

                                            } else {
                                                UiText.DynamicString("Оновити код")
                                            }
                                        ),
                                        onUIAction = {
                                            onUIAction(
                                                UIAction(
                                                    actionKey = UIActionKeysCompose.REFRESH_BUTTON
                                                )
                                            )
                                            docBarcodeExpired = false

                                        }
                                    )
                                }

                            }

                        }
                        if (data.toggle.qr.selectionState == UIState.Selection.Selected && !docBarcodeExpired) {
                            data.qrBitmap?.let {
                                Box(
                                    modifier = Modifier
                                        .padding(top = if (data.showToggle || data.timerText != null) 16.dp else 0.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f),
                                        bitmap = data.qrBitmap.asImageBitmap(),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = stringResource(id = R.string.qr_code)
                                    )
                                }
                            }
                        }
                        if (data.toggle.ean13.selectionState == UIState.Selection.Selected && !docBarcodeExpired) {
                            data.ean13Bitmap?.let {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            modifier = Modifier.size(
                                                width = 247.dp,
                                                height = 100.dp
                                            ),
                                            bitmap = data.ean13Bitmap.asImageBitmap(),
                                            contentDescription = stringResource(id = R.string.qr_code),
                                            contentScale = ContentScale.FillBounds
                                        )
                                        data.eanCode?.let {
                                            Text(
                                                text = formatNumber(data.eanCode),
                                                modifier = Modifier.padding(
                                                    top = 12.dp,
                                                    bottom = 32.dp
                                                ),
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    lineHeight = 16.sp,
                                                    letterSpacing = 4.sp
                                                ),
                                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                                color = Black,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (data.showToggle) {
                            ToggleButtonGroup(
                                modifier = Modifier,
                                data = data.toggle,
                                onUIAction = onUIAction
                            )
                        }
                    }
                    if (data.exception != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (data.noRegistry == null) "☝" else "\uD83D\uDE14",
                                style = TextStyle(
                                    fontSize = 36.sp,
                                    lineHeight = 40.sp
                                ),
                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),

                                )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = if (data.localization == Localization.eng) {
                                    if (data.noRegistry == null) "Verification codes failed to upload" else "No registry access"
                                } else {
                                    if (data.noRegistry == null) "Коди для перевірки не завантажено" else "Немає доступу до реєстру"
                                },
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            BtnStrokeAdditionalAtm(
                                modifier = Modifier,
                                data = ButtonStrokeAdditionalAtomData(
                                    title = if (data.localization == Localization.eng) {
                                        UiText.DynamicString("Try again")
                                    } else {
                                        UiText.DynamicString("Спробувати ще раз")
                                    }
                                ),
                                onUIAction = {
                                    onUIAction(
                                        UIAction(
                                            actionKey = UIActionKeysCompose.REFRESH_BUTTON
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        if (data.isStack) {
            Column(
                modifier = modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .background(
                        color = WhiteAlpha25,
                        shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom

            ) {

            }
        }
    }
}

enum class Localization {
    ua, eng
}

data class DocCodeOrgData(
    val actionKey: String = UIActionKeysCompose.DOC_CODE_ORG_DATA,
    val localization: Localization,
    val toggle: ToggleButtonGroupData,
    val qrBitmap: Bitmap? = null,
    val ean13Bitmap: Bitmap? = null,
    val eanCode: String? = null,
    val timerText: String? = null,
    val exception: Exception? = null,
    val expired: Boolean = false,
    val showToggle: Boolean,
    val isStack: Boolean,
    val noRegistry: Int? = null
) : UIElementData {
    fun onToggleClick(toggleId: String?): DocCodeOrgData {
        if (toggleId == null) return this
        return this.copy(
            toggle = toggle.onToggleClicked(toggleId)
        )
    }
}

fun formatNumber(eanCode: String): String {
    val spacedEanCode = StringBuilder(eanCode)
    spacedEanCode.insert(4, " ")
    spacedEanCode.insert(9, " ")
    return spacedEanCode.toString()
}

@Preview
@Composable
fun DocCodeOrgPreview() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            qrBitmap = qrBm,
            showToggle = true,
            isStack = true
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview
@Composable
fun DocCodeOrgPreviewLoading() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data =
        DocCodeOrgData(
            localization = Localization.eng,
            toggle = state.value,
            qrBitmap = qrBm,
            showToggle = true,
            isStack = false
        )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value, progressIndicator = Pair(data.actionKey, true)) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Preview
@Composable
fun DocCodeOrgPreviewWithHttpExeption() {
    val toggle = ToggleButtonGroupData(
        qr = BtnToggleMlcData(
            id = "qr",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
            selectionState = UIState.Selection.Selected,
            action = DataActionWrapper(
                type = "qr"
            )
        ),
        ean13 = BtnToggleMlcData(
            id = "ean",
            label = "Label".toDynamicString(),
            iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE_WHITE.code),
            iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.BARCODE.code),
            selectionState = UIState.Selection.Unselected,
            action = DataActionWrapper(
                type = "ean"
            )
        )
    )
    val state = remember {
        mutableStateOf(toggle)
    }

    val qrBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

    val data = DocCodeOrgData(
        localization = Localization.eng,
        toggle = state.value,
        qrBitmap = qrBm,
        exception = java.lang.Exception(),
        showToggle = true,
        isStack = false
    )
    val stateT = remember {
        mutableStateOf(data)
    }
    DocCodeOrg(modifier = Modifier, stateT.value) {
        stateT.value = stateT.value.onToggleClick(it.data)
    }
}

@Composable
fun TimerText(text: String?, units: String, minutes: Int, onTimeOver: () -> Unit) {
    val density = LocalDensity.current

    var minutes by remember { mutableStateOf(minutes) }
    var seconds by remember { mutableStateOf(0) }

    val secValueMaxWidth = "00"
    val minutesValueMaxWidth = if (minutes < 10) {
        "0"
    } else {
        "00"
    }

    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(
        "$text $minutesValueMaxWidth:$secValueMaxWidth $units",
        DiiaTextStyle.t4TextSmallDescription
    ).size.width

    val textComponentWidth = remember { with(density) { widthInPixels.toDp() } }


    LaunchedEffect(Unit) {
        launch {
            while (minutes > 0 || seconds > 0) {
                delay(1000)
                if (seconds == 0) {
                    minutes--
                    seconds = 59
                } else {
                    seconds--
                }
            }
            onTimeOver()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .width(textComponentWidth),
            text = "$text ${minutes}:${String.format("%02d", seconds)} $units",
            textAlign = TextAlign.Start,
            style = DiiaTextStyle.t4TextSmallDescription,
            color = BlackAlpha40
        )
    }
}

@Preview
@Composable
fun TextCounterPreview() {
    TimerText(text = "Код діятиме ще ", "хв", 3) {
    }
}
