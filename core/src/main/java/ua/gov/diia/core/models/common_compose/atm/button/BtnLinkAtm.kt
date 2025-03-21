package ua.gov.diia.core.models.common_compose.atm.button

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.ButtonStates

@JsonClass(generateAdapter = true)
data class BtnLinkAtm(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "label")
    val label: String,
    @Json(name = "state")
    val state: ButtonStates?,
    @Json(name = "action")
    val action: Action?,
)