package ua.gov.diia.core.models.common.message

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TextParameter(
    @Json(name = "data")
    val data: Data?,
    @Json(name = "type")
    val type: String?
) : Parcelable {
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "alt")
        val alt: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "resource")
        val resource: String?
    ) : Parcelable
}