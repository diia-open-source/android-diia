package ua.gov.diia.core.models.common_compose.org.radioBtn

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.button.RadioBtnMlc
import ua.gov.diia.core.models.common_compose.mlc.input.InputTextMlc

@JsonClass(generateAdapter = true)
data class RadioBtnAdditionalInputOrg(
    @Json(name = "radioBtnMlc")
    val radioBtnMlc: RadioBtnMlc,
    @Json(name = "inputTextMlc")
    val inputTextMlc: InputTextMlc
)