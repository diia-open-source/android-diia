package ua.gov.diia.core.models.rating_service


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RatingRequest(
    @Json(name = "resourceId")
    val resourceId: String?,
    @Json(name = "comment")
    val comment: String?,
    @Json(name = "completingTimeMs")
    val completingTimeMs: Long?,
    @Json(name = "isClosed")
    val isClosed: Boolean? = false,
    @Json(name = "rating")
    val rating: String?,
    @Json(name = "selectedChips")
    val selectedChips: List<String>?,
    @Json(name = "ratingType")
    val ratingType: String?,
    @Json(name = "screenCode")
    val screenCode: String?,
    @Json(name = "formCode")
    val formCode: String?
) : Parcelable