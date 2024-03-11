package ua.gov.diia.ui_base.components.organism.image

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.LoadActionAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun QRShareOrg(
    modifier: Modifier = Modifier,
    data: QRShareOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.qrTitle,
                style = DiiaTextStyle.t5ExtraSmallDescription,
                color = BlackAlpha50
            )
            Image(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(200.dp),
                bitmap = data.qr.asImageBitmap(),
                contentDescription = stringResource(id = R.string.qr_code),
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        BtnIconPlainGroupMlc(
            data = BtnIconPlainGroupMlcData(
                items = SnapshotStateList<LoadActionAtomData>().apply {
                    add(
                        LoadActionAtomData(
                            actionKey = UIActionKeysCompose.QR_SHARE_ORG,
                            name = data.linkTitle,
                            icon = data.linkIcon,
                            id = data.actionKey,
                            actionData = "",
                            type = ""
                        )
                    )
                }
            ),
            progressIndicator = progressIndicator,
            onUIAction = onUIAction
        )
    }
}

data class QRShareOrgData(
    val actionKey: String = UIActionKeysCompose.QR_SHARE_ORG,
    val qrTitle: String,
    val qr: Bitmap,
    val linkIcon: String,
    val linkTitle: String
) : UIElementData

@Composable
@Preview
fun QRMoleculePreview() {
    val bm1 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
    val data = QRShareOrgData(
        qrTitle = "КОД ДІЯТИМЕ 3 ХВ",
        qr = bm1,
        linkIcon = PreviewBase64Icons.arrowEndTop,
        linkTitle = "Надіслати посилання покупцю"
    )

    val state = remember {
        mutableStateOf(data)
    }

    QRShareOrg(
        data = state.value
    ) {

    }
}
