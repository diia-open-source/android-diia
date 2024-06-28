package ua.gov.diia.core.models.common_compose.atm

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpacerAtm(
    @Json(name = "type")
    val type: SpacerAtmType?
)

enum class SpacerAtmType {
    SPACER_8, SPACER_16, SPACER_24, SPACER_32, SPACER_64
}