package ua.gov.diia.core.models.common_compose.org.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryWideAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class CheckboxBtnWhiteOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "items")
    val items: List<BottomGroupCheckboxItem>,
    @Json(name = "btnPrimaryWideAtm")
    val btnPrimaryWideAtm: BtnPrimaryWideAtm,
    @Json(name = "btnPlainAtm")
    val btnPlainAtm: BtnPlainAtm?
) : Parcelable