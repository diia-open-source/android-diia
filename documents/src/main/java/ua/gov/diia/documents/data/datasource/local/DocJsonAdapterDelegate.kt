package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.documents.models.DiiaDocument

open class DocJsonAdapterDelegate<T : DiiaDocument>(
    val subtype: Class<T>,
    val label: String
)