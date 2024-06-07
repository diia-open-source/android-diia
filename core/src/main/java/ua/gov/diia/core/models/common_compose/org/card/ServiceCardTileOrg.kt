package ua.gov.diia.core.models.common_compose.org.card

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.card.ServiceCardMlc

@JsonClass(generateAdapter = true)
data class ServiceCardTileOrg(
    @Json(name = "items")
    val items: List<ServiceCardMlc>,
    @Json(name = "componentId")
    val componentId: String?
)