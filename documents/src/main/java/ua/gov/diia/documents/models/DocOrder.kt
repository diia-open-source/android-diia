package ua.gov.diia.documents.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DocumentsOrder(
    @Json(name = "documentsOrder")
    val documentsOrder: List<DocOrder>
)

@JsonClass(generateAdapter = true)
data class TypeDefinedDocumentsOrder(
    @Json(name = "documentsOrder")
    val documentsOrder: List<TypeDefinedDocOrder>
)


interface DocumentOrderModel

@JsonClass(generateAdapter = true)
data class DocOrder(
    @Json(name = "documentType")
    val documentType: String,
    @Json(name = "order")
    val order: Int
): DocumentOrderModel

@JsonClass(generateAdapter = true)
data class TypeDefinedDocOrder(
    @Json(name = "docNumber")
    val docNumber: String,
    @Json(name = "order")
    val order: Int
): DocumentOrderModel