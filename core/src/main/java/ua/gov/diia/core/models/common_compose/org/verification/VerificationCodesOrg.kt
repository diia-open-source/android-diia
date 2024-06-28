package ua.gov.diia.core.models.common_compose.org.verification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.scancode.BarCodeMlc
import ua.gov.diia.core.models.common_compose.mlc.scancode.QrCodeMlc
import ua.gov.diia.core.models.common_compose.mlc.text.StubMessageMlc
import ua.gov.diia.core.models.common_compose.org.group.ToggleButtonGroupOrg
import ua.gov.diia.core.models.common_compose.subatomic.ExpireLabel

@JsonClass(generateAdapter = true)
data class VerificationCodesOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "EN")
    val en: Localization?,
    @Json(name = "UA")
    val ua: Localization
) {
    @JsonClass(generateAdapter = true)
    data class Localization(
        @Json(name = "barCodeMlc")
        val barCodeMlc: BarCodeMlc?,
        @Json(name = "expireLabel")
        val expireLabel: ExpireLabel?,
        @Json(name = "qrCodeMlc")
        val qrCodeMlc: QrCodeMlc,
        @Json(name = "stubMessageMlc")
        val stubMessageMlc: StubMessageMlc?,
        @Json(name = "toggleButtonGroupOrg")
        val toggleButtonGroupOrg: ToggleButtonGroupOrg?
    )
}