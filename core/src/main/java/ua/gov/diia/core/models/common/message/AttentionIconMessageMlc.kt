package ua.gov.diia.core.models.common.message


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class AttentionIconMessageMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "backgroundMode")
    val backgroundMode: BackgroundMode,
    @Json(name = "smallIconAtm")
    val smallIconAtm: SmallIconAtm?,
    @Json(name = "text")
    val text: String?
): Parcelable {
    enum class BackgroundMode {
        info;
    }
}