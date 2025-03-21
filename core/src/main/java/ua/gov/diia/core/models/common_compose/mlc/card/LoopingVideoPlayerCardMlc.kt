package ua.gov.diia.core.models.common_compose.mlc.card


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class LoopingVideoPlayerCardMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "iconRight")
    val iconRight: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "video")
    val video: String
)