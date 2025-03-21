package ua.gov.diia.documents.models.share

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.document.docgroups.v2.Content

@JsonClass(generateAdapter = true)
data class UnCerShareData(
    @Json(name = "id")
    val id: String,
    @Json(name = "content")
    val content: List<Content>,
    @Json(name = "fullName")
    val fullName: String,
    @Json(name = "docName")
    val docName: String,
    @Json(name = "tickerType")
    val tickerType: String,
    @Json(name = "tickerValue")
    val tickerValue: String,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "dateIssued")
    val dateIssued: String,
    @Json(name = "zodiacSign")
    val zodiacSign: String,
    )