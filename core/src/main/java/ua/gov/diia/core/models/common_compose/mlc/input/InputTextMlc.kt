package ua.gov.diia.core.models.common_compose.mlc.input

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class InputTextMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "blocker")
    val blocker: Boolean?,
    @Json(name = "label")
    val label: String,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "validation")
    val validation: List<ValidationTextItem>?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ValidationTextItem(
    @Json(name = "regexp")
    val regexp: String,
    @Json(name = "flags")
    val flags: List<String>,
    @Json(name = "errorMessage")
    val errorMessage: String
) : Parcelable