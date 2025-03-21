package ua.gov.diia.core.models.common_compose.mlc.input

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class InputDateMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "value")
    val value: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "dateFormat")
    val dateFormat: DateFormat?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "blocker")
    val blocker: Boolean?,
) : Parcelable

enum class DateFormat {
    full,
    monthAndYear,
}