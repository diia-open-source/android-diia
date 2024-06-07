package ua.gov.diia.core.models.common_compose.org.radioBtn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.mlc.button.RadioBtnMlc

@JsonClass(generateAdapter = true)
data class RadioBtnGroupOrg(
    @Json(name = "id")
    val id: String?,
    @Json(name = "blocker")
    val blocker: Boolean?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "condition")
    val condition: String?,
    @Json(name = "items")
    val items: List<RadioBtnGroupOrgItem>,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm?,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "inputCode")
    val inputCode: String? = null,
)

@JsonClass(generateAdapter = true)
data class RadioBtnGroupOrgItem(
    @Json(name = "radioBtnMlc")
    val radioBtnMlc: RadioBtnMlc?,
    @Json(name = "radioBtnAdditionalInputOrg")
    val radioBtnAdditionalInputOrg: RadioBtnAdditionalInputOrg?
)