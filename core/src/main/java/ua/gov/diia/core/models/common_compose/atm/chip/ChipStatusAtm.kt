package ua.gov.diia.core.models.common_compose.atm.chip


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ChipStatusAtm(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "code")
    val code: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "type")
    val type: String?
) : Parcelable

enum class Type(val id: String) {
    SUCCESS("success"),
    PENDING("pending"),
    FAIL("fail"),
    WHITE("white"),
    NEUTRAL("neutral");
}