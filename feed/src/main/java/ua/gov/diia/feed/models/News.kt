package ua.gov.diia.feed.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.card.HalvedCardMlc
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class News(
    @Json(name = "items")
    val items: List<Item>?,
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
    @Json(name = "total")
    val total: Int?
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "halvedCardMlc")
        val halvedCardMlc: HalvedCardMlc?
    )
}