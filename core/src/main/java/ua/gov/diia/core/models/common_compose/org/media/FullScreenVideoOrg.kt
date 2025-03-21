package ua.gov.diia.core.models.common_compose.org.media

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.media.PlayerBtnAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class FullScreenVideoOrg(
    @Json(name = "source")
    val source: String,
    @Json(name = "playerBtnAtm")
    val playerBtnAtm: PlayerBtnAtm?,
    @Json(name = "btnPrimaryDefaultAtm")
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtm?,
    @Json(name = "btnPlainAtm")
    val btnPlainAtm: BtnPlainAtm?,
    @Json(name = "componentId")
    val componentId: String? = null,
): Parcelable