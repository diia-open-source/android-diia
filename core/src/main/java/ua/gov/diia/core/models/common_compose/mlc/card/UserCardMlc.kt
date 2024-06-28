package ua.gov.diia.core.models.common_compose.mlc.card

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.UserPictureAtm


@JsonClass(generateAdapter = true)
data class UserCardMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "userPictureAtm")
    val userPictureAtm: UserPictureAtm,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?
)