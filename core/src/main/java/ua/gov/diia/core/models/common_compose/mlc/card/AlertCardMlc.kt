package ua.gov.diia.core.models.common_compose.mlc.card

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnAlertAdditionalAtm


@JsonClass(generateAdapter = true)
data class AlertCardMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "btnAlertAdditionalAtm")
    val btnAlertAdditionalAtm: BtnAlertAdditionalAtm?,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "text")
    val text: String
)
