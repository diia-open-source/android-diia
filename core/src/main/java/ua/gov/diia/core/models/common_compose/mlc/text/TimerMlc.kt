
package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.models.common_compose.atm.button.BtnLinkAtm

@JsonClass(generateAdapter = true)
data class TimerMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "expireLabel")
    val expireLabel: ExpireLabel?,
    @Json(name = "stateAfterExpiration")
    val stateAfterExpiration: StateAfterExpiration
)

@JsonClass(generateAdapter = true)
data class ExpireLabel(
    @Json(name = "expireLabelFirst")
    val expireLabelFirst: String,
    @Json(name = "expireLabelLast")
    val expireLabelLast: String?,
    @Json(name = "timer")
    val timer: Int,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?
)

@JsonClass(generateAdapter = true)
data class StateAfterExpiration(
    @Json(name = "btnLinkAtm")
    val btnLinkAtm: BtnLinkAtm
)