package ua.gov.diia.core.models.common_compose.mlc.list


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class ListItemMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "iconLeft")
    val iconLeft: IconLeft?,
    @Json(name = "iconRight")
    val iconRight: IconRight?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "logoLeft")
    val logoLeft: String?,
    @Json(name = "state")
    val state: String?
) {
    @JsonClass(generateAdapter = true)
    data class IconRight(
        @Json(name = "code")
        val code: String?
    )

    @JsonClass(generateAdapter = true)
    data class IconLeft(
        @Json(name = "code")
        val code: String?
    )

}
