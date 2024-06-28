package ua.gov.diia.ui_base.components.infrastructure

data class DataActionWrapper(
    val type: String,
    val subtype: String? = null,
    val resource: String? = null,
    val subresource: String? = null
)