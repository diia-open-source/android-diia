package ua.gov.diia.ps_criminal_cert.models.response

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.core.models.common.NavigationPanel
import ua.gov.diia.core.models.common.message.StubMessage
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus

@JsonClass(generateAdapter = true)
data class CriminalCertListData(
    @Json(name = "stubMessage")
    val stubMessage: StubMessage?,
    @Json(name = "total")
    val total: Int?,
    @Json(name = "certificatesStatus")
    val certsStatus: Status?,
    @Json(name = "certificates")
    val certificates: List<CertItem>?,
    @Json(name = "navigationPanel")
    val navigationPanel: NavigationPanel?,
) {

    @JsonClass(generateAdapter = true)
    data class CertItem(
        @Json(name = "applicationId")
        val id: String,
        @Json(name = "status")
        val status: CriminalCertStatus?,
        @Json(name = "reason")
        val reason: String,
        @Json(name = "creationDate")
        val creationDate: String,
        @Json(name = "type")
        val type: String,
    ) {
        val labelBackgroundRes: Int
            @ColorRes
            get() = when (status) {
                CriminalCertStatus.PROCESSING -> R.color.state_collecting
                CriminalCertStatus.DONE -> R.color.state_approved
                null -> R.color.state_collecting
            }

        val labelTextRes: Int
            @StringRes
            get() = when (status) {
                CriminalCertStatus.PROCESSING -> R.string.criminal_cert_state_processing
                CriminalCertStatus.DONE -> R.string.criminal_cert_state_done
                null -> R.string.criminal_cert_state_processing
            }


        val labelTextColor: Int
            @ColorRes
            get() = when (status) {
                CriminalCertStatus.PROCESSING -> R.color.black
                CriminalCertStatus.DONE -> R.color.white
                null -> R.color.black
            }
    }

    @JsonClass(generateAdapter = true)
    data class Status(
        @Json(name = "code")
        val status: CriminalCertStatus,
        @Json(name = "name")
        val name: String,
    )
}