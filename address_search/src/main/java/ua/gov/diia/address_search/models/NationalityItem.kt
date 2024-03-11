package ua.gov.diia.address_search.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.search.models.SearchableItem

@Parcelize
@JsonClass(generateAdapter = true)
data class NationalityItem(
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String
) : SearchableItem {
    override fun getDisplayTitle(): String = name ?: "Unknown"

    override fun getQueryString(): String = name ?: "Unknown"
}