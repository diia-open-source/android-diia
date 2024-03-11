package ua.gov.diia.core.models.notification.pull.message

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
data class NotificationMessagesBody(
    @Json(name = "type")
    val type: MessageTypes,
    @Json(name = "data")
    val data: MessageData?
)

@JsonClass(generateAdapter = true)
data class MessageData(
    @Json(name = "text")
    val text: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "link")
    val link: String?,
    @Json(name = "action")
    val action: MessageActions?,
    @Json(name = "statementLoading")
    var statementLoading: Boolean? = false
) {
    val textVisibility: Boolean
        get() = text != null && parameters == null

    val parametersVisibility: Boolean
        get() = text != null && parameters != null

    val imageVisibility: Boolean
        get() {
            val img = image ?: ""
            return img.isNotEmpty()
        }
}