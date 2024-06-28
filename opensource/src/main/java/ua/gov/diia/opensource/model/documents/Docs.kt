package ua.gov.diia.opensource.model.documents

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.documents.models.Expiring
import ua.gov.diia.documents.models.WithTimestamp

@Parcelize
@JsonClass(generateAdapter = true)
data class Docs(
    @Json(name = "currentDate")
    internal val currentDate: String = Preferences.DEF,
    @Json(name = "driverLicense")
    val driverLicense: DriverLicenseV2?,
    @Json(name = "expirationDate")
    internal val expirationDate: String = Preferences.DEF,
    @Json(name = "documentsTypeOrder")
    val documentsTypeOrder: List<String> = listOf(),
) : Parcelable, Expiring, WithTimestamp {

    override fun getDocExpirationDate() = expirationDate

    override fun getTimestamp() = currentDate
}