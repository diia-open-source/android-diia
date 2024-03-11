package ua.gov.diia.search.models

import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.search.models.SearchableItem

interface SearchableItemDoubleLine : SearchableItem {

    fun getDisplayText(): String

    val showTitle: Boolean
        get() = getDisplayTitle().isStringValid()

    val showText: Boolean
        get() = getDisplayText().isStringValid()
}