package ua.gov.diia.core.models.rating_service


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
 data class RatingFormModel(
 @Json(name = "resourceId")
   val resourceId: String?,
 @Json(name = "key")
    val key: String?,
 @Json(name = "comment")
    val comment: Comment?,
 @Json(name = "formCode")
    val formCode: String?,
 @Json(name = "mainButton")
    val mainButton: String?,
 @Json(name = "rating")
    val rating: Rating?,
 @Json(name = "title")
    val title: String?
) : Parcelable