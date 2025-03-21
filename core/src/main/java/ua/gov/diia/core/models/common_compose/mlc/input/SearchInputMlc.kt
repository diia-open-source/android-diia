package ua.gov.diia.core.models.common_compose.mlc.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchInputMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "iconLeft")
    val iconLeft: SearchIcon?,
    @Json(name = "iconRight")
    val iconRight: SearchIcon?
) {
    @JsonClass(generateAdapter = true)
    data class SearchIcon(
        @Json(name = "code")
        val code: String
    )
}