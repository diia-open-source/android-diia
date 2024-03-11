package ua.gov.diia.core.models.common_compose.atm.button


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class BtnPlainIconAtm(
    @Json(name = "icon")
    val icon: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "state")
    val state: String?,
    @Json(name = "action")
    val action: Action?,
)