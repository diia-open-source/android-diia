package ua.gov.diia.core.models.common_compose.general


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Action(
    @Json(name = "type")
    val type: String,
    @Json(name = "subtype")
    val subtype: String?,
    @Json(name = "resource")
    val resource: String?
) : Parcelable