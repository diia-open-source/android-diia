package ua.gov.diia.core.models.common_compose.org.sharing


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.core.models.common_compose.mlc.scancode.QrCodeMlc
import ua.gov.diia.core.models.common_compose.mlc.text.StubMessageMlc
import ua.gov.diia.core.models.common_compose.subatomic.ExpireLabel

@JsonClass(generateAdapter = true)
data class SharingCodesOrg(
    @Json(name = "btnLoadIconPlainGroupMlc")
    val btnLoadIconPlainGroupMlc: BtnLoadIconPlainGroupMlc?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "expireLabel")
    val expireLabel: ExpireLabel?,
    @Json(name = "qrCodeMlc")
    val qrCodeMlc: QrCodeMlc,
    @Json(name = "stubMessageMlc")
    val stubMessageMlc: StubMessageMlc?
)