package ua.gov.diia.address_search.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressValidation(
    @Json(name = "errorMessage")
    val errorMessage: String?,
    @Json(name = "flags")
    val flags: List<String>?,
    @Json(name = "regexp")
    val regexp: String?
): Parcelable