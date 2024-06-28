package ua.gov.diia.search.models

import kotlinx.parcelize.Parcelize

@Parcelize
data class StringSearchableItem(val data: String) : SearchableItem {

    override fun getDisplayTitle() = data

    override fun getQueryString() = data

    override fun isDisabled(): Boolean = false
    
}