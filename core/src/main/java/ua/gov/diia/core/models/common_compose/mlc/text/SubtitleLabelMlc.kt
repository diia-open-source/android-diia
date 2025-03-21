package ua.gov.diia.core.models.common_compose.mlc.text


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SubtitleLabelMlc(
    @Json(name = "label")
    val label: String?,
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "componentId")
    val componentId: String? = null
) : Parcelable