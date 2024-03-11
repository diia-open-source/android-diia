package ua.gov.diia.address_search.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressDefaultListItem(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "errorMessage")
    val errorMessage: String?
) : Parcelable{
    fun toAddressItem()  =  AddressItem(id,name, errorMessage)
}
