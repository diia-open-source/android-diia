package ua.gov.diia.search.util

import kotlinx.parcelize.Parcelize
import ua.gov.diia.search.models.SearchableItem

@Parcelize
data class TestSearchableItem(
    private val displayTitle: String,
    private val queryString: String
) : SearchableItem {

    override fun getDisplayTitle(): String = displayTitle

    override fun getQueryString(): String = queryString
}
