package ua.gov.diia.core.models.rating_service


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Chip(
    @Json(name = "chips")
    val chips: List<Chips>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "label")
    val label: String?
) : Parcelable