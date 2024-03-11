package ua.gov.diia.address_search.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressNationality(
    @Json(name = "nationalities")
    val nationalities: List<NationalityItem>
) : Parcelable