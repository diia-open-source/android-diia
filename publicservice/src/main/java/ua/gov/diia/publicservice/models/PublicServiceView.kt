package ua.gov.diia.publicservice.models

data class PublicServiceView(
    val sortOrder: Int,
    val search: String,
    val code: String,
    val name: String,
    val status: CategoryStatus,
    val categoryName: String,
    val categoryCode: String
)

fun PublicService.toDisplayService(
    categoryName: String,
    categoryCode: String
) = PublicServiceView(
    sortOrder,
    search,
    code,
    name,
    status,
    categoryName,
    categoryCode
)