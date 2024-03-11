package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerBtnAtm(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "type")
    val type: String?
)