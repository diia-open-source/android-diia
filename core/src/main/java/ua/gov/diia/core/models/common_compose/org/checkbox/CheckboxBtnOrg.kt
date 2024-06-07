package ua.gov.diia.core.models.common_compose.org.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class CheckboxBtnOrg(
    @Json(name = "items")
    val items: List<BottomGroupCheckboxItem>,
    @Json(name = "btnPrimaryDefaultAtm")
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtm?,
    @Json(name = "componentId")
    val componentId: String? = null,
): Parcelable
