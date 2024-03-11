package ua.gov.diia.ps_criminal_cert.models.request


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PublicService(
    @Json(name = "code")
    val code: String?,
    @Json(name = "resourceId")
    val resourceId: String?
): Parcelable