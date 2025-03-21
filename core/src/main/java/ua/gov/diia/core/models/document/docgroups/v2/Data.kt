package ua.gov.diia.core.models.document.docgroups.v2


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "dataForDisplayingInOrderConfigurations")
    val dataForDisplayingInOrderConfigurations: DataForDisplayingInOrderConfigurations?,
    @Json(name = "content")
    val content: List<Content>?,
    @Json(name = "docData")
    val docData: DocData?,
    @Json(name = "docNumber")
    val docNumber: String?,
    @Json(name = "docStatus")
    val docStatus: Int?,
    @Json(name = "frontCard")
    val frontCard: FrontCard?,
    @Json(name = "fullInfo")
    val fullInfo: List<FullInfo>?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "qr")
    val qr: String?
)