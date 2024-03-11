package ua.gov.diia.core.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
@Parcelize
data class TextWithParameters(
    @Json(name = "parameters")
    val parameters: List<TextParameter>?,
    @Json(name = "text")
    val text: String?
): Parcelable