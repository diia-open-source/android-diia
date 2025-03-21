package ua.gov.diia.core.models.rating_service

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RatingItem(
    @Json(name = "chip")
    val chip: Chip?,
    @Json(name = "emoji")
    val emoji: String?,
    @Json(name = "rate")
    val rate: String?,

    val isChecked: Boolean = false
) : Parcelable