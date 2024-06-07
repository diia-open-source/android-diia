package ua.gov.diia.core.models.common_compose.mlc.header


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class TitleGroupMlc(
    @Json(name = "heroText")
    val heroText: String,
    @Json(name = "label")
    val label: String?,
    @Json(name = "leftNavIcon")
    val leftNavIcon: LeftNavIcon?,
    @Json(name = "mediumIconRight")
    val mediumIconRight: MediumIconRight?,
    @Json(name = "componentId")
    val componentId: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class MediumIconRight(
        @Json(name = "action")
        val action: Action?,
        @Json(name = "code")
        val code: String
    )

    @JsonClass(generateAdapter = true)
    data class LeftNavIcon(
        @Json(name = "accessibilityDescription")
        val accessibilityDescription: String?,
        @Json(name = "action")
        val action: Action?,
        @Json(name = "code")
        val code: String
    )
}