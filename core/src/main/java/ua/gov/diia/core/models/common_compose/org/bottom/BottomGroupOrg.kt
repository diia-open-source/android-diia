package ua.gov.diia.core.models.common_compose.org.bottom

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryLargeAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryWideAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeWhiteAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.mlc.button.BtnIconPlainGroupMlc
import ua.gov.diia.core.models.common_compose.mlc.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnWhiteOrg

@Parcelize
@JsonClass(generateAdapter = true)
data class BottomGroupOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "btnPrimaryDefaultAtm")
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtm?,
    @Json(name = "btnStrokeDefaultAtm")
    val btnStrokeDefaultAtm: BtnStrokeDefaultAtm?,
    @Json(name = "btnStrokeWhiteAtm")
    val btnStrokeWhiteAtm: BtnStrokeWhiteAtm?,
    @Json(name = "btnPlainAtm")
    val btnPlainAtm: BtnPlainAtm?,
    @Json(name = "checkboxBtnOrg")
    val checkboxBtnOrg: CheckboxBtnOrg?,
    @Json(name = "checkboxBtnWhiteOrg")
    val checkboxBtnWhiteOrg: CheckboxBtnWhiteOrg?,
    @Json(name = "bottomGroupOrg")
    val bottomGroupOrg: BottomGroup?,
    @Json(name = "btnPrimaryLargeAtm")
    val btnPrimaryLargeAtm: BtnPrimaryLargeAtm?,
    @Json(name = "btnIconPlainGroupMlc")
    val btnIconPlainGroupMlc: BtnIconPlainGroupMlc?,
    @Json(name = "btnPrimaryWideAtm")
    val btnPrimaryWideAtm: BtnPrimaryWideAtm? = null,
    @Json(name = "btnLoadIconPlainGroupMlc")
    val btnLoadIconPlainGroupMlc: BtnLoadIconPlainGroupMlc?,
    @Json(name = "tickerAtm")
    val tickerAtm: TickerAtm?
) : Parcelable