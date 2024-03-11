package ua.gov.diia.ps_criminal_cert.models.enums

import com.squareup.moshi.Json

enum class CriminalCertLoadActionType {
    @Json(name = "downloadArchive")
    DOWNLOAD_ARCHIVE,

    @Json(name = "viewPdf")
    VIEW_PDF
}