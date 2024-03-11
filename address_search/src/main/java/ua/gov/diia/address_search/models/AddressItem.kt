package ua.gov.diia.address_search.models


import android.content.Context
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.search.models.SearchableItem

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressItem(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "errorMessage")
    val errorMessage: String?
) : SearchableBullet, SearchableItem {

    override fun getDisplayName(context: Context): String = name ?: "Unknown"

    override fun getDisplayTitle(): String = name ?: "Unknown"

    override fun getQueryString(): String = name ?: "Unknown"
}