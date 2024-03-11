package ua.gov.diia.core.models.common_compose.org.button


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.button.BtnIconRoundedMlc

@JsonClass(generateAdapter = true)
data class BtnIconRoundedGroupOrg(
    @Json(name = "items")
    val items: List<Item>
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "btnIconRoundedMlc")
        val btnIconRoundedMlc: BtnIconRoundedMlc
    )
}