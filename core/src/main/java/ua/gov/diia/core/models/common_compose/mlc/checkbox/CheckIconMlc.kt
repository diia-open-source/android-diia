package ua.gov.diia.core.models.common_compose.mlc.checkbox

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckIconMlc(
    @Json(name = "icon")
    val icon: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "state")
    val state: CbIconState?
){
    enum class CbIconState(val type: String) {
        REST("rest"),
        SELECTED("selected"),
        DISABLE("disable"),
        DISABLE_SELECTED("disableSelected")
    }
}