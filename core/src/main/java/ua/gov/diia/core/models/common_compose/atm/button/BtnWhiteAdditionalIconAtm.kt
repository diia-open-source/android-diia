package ua.gov.diia.core.models.common_compose.atm.button

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.BadgeCounterAtm
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.ButtonStates

@JsonClass(generateAdapter = true)
data class BtnWhiteAdditionalIconAtm(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "state")
    val state: ButtonStates?,
    @Json(name = "badgeCounterAtm")
    val badgeCounterAtm: BadgeCounterAtm?,
    @Json(name = "action")
    val action: Action?
)