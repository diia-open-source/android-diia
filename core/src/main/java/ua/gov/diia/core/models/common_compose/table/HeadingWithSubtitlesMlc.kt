package ua.gov.diia.core.models.common_compose.table


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class HeadingWithSubtitlesMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "subtitles")
    val subtitles: List<String>?,
    @Json(name = "value")
    val value: String?
) : Parcelable