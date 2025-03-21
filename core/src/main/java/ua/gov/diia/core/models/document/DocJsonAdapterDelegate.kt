package ua.gov.diia.core.models.document

open class DocJsonAdapterDelegate<T : DiiaDocument>(
    val subtype: Class<T>,
    val label: String
)