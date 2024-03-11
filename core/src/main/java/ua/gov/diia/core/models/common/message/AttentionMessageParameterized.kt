package ua.gov.diia.core.models.common.message

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.TextParameter

@Parcelize
@JsonClass(generateAdapter = true)
data class AttentionMessageParameterized(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "title")
    val title: String?
) : Parcelable