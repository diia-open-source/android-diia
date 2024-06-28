package ua.gov.diia.core.models.common_compose.org.checkbox

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.checkbox.CheckIconMlc
import ua.gov.diia.core.models.common_compose.mlc.checkbox.CheckboxRoundMlc

@JsonClass(generateAdapter = true)
data class CheckboxRoundGroupOrg(
    @Json(name = "id")
    val id: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "blocker")
    val blocker: Boolean?,
    @Json(name = "condition")
    val condition: String?,
    @Json(name = "items")
    val items: List<CbItem>
)

@JsonClass(generateAdapter = true)
data class CbItem(
    @Json(name = "checkboxRoundMlc")
    val checkboxRoundMlc: CheckboxRoundMlc?,
    @Json(name = "checkIconMlc")
    val checkIconMlc: CheckIconMlc?
)