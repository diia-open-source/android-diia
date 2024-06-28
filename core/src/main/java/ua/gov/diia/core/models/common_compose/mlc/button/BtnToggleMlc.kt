package ua.gov.diia.core.models.common_compose.mlc.button

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class BtnToggleMlc(
    @Json(name = "code")
    val code: String,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "notSelected")
    val notSelected: NotSelected,
    @Json(name = "selected")
    val selected: Selected
) {
    @JsonClass(generateAdapter = true)
    data class NotSelected(
        @Json(name = "icon")
        val icon: String
    )

    @JsonClass(generateAdapter = true)
    data class Selected(
        @Json(name = "action")
        val action: Action,
        @Json(name = "icon")
        val icon: String
    )
}