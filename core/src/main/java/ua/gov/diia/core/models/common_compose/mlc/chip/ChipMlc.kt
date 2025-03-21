package ua.gov.diia.core.models.common_compose.mlc.chip

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.BadgeCounterAtm
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class ChipMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "code")
    val code: String,
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "badgeCounterAtm")
    val badgeCounterAtm: BadgeCounterAtm?,
    @Json(name = "active")
    val active: Boolean?,
    @Json(name = "selectedIcon")
    val selectedIcon: String?,
    @Json(name = "action")
    val action: Action?
)