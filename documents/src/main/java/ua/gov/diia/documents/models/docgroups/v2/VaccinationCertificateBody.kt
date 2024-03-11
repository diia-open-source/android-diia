package ua.gov.diia.documents.models.docgroups.v2

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class VaccinationCertificateBody(
    @Json(name = "qr")
    val qr: String?,
    @Json(name = "isBooster")
    val isBooster: Boolean?,
) : Parcelable