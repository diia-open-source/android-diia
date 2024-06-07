package ua.gov.diia.core.models.common.message

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class StubMessageParameterized(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?
) : Parcelable