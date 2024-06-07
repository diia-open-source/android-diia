package ua.gov.diia.core.models.common.message

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.TextWithParameters

@Parcelize
data class StatusMessage(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "textWithParameters")
    val textWithParameters: TextWithParameters?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?,
    @Json(name = "componentId")
    val componentId: String? = null
): Parcelable
