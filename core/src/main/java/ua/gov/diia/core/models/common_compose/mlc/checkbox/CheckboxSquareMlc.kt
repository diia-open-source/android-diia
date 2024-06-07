package ua.gov.diia.core.models.common_compose.mlc.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CheckboxSquareMlc(
    @Json(name = "id")
    val id: String?,
    @Json(name = "blocker")
    val blocker: Boolean?,
    @Json(name = "label")
    val label: String,
    @Json(name = "options")
    val options: List<CBSquareItem>?,
    @Json(name = "isSelected")
    val isSelected: Boolean?,
    @Json(name = "isEnabled")
    val isEnabled: Boolean?,
    @Json(name = "componentId")
    val componentId: String? = null,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class CBSquareItem(
    @Json(name = "id")
    val id: String,
    @Json(name = "isSelected")
    val isSelected: Boolean
) : Parcelable
