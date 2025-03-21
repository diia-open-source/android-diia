package ua.gov.diia.core.models.common_compose.mlc.button

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RadioBtnMlc(
    @Json(name = "id")
    val id: String?,
    @Json(name = "logoLeft")
    val logoLeft: String?,
    @Json(name = "logoRight")
    val logoRight: String?,
    @Json(name = "largeLogoRight")
    val largeLogoRight: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "isSelected")
    val isSelected: Boolean?,
    @Json(name = "isEnabled")
    val isEnabled: Boolean?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "dataJson")
    val dataJson: String? = null,
): Parcelable