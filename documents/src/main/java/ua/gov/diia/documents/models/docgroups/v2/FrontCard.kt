package ua.gov.diia.documents.models.docgroups.v2

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class FrontCard(
    @Json(name = "EN")
    val en: List<EN>?,
    @Json(name = "UA")
    val ua: List<UA>?
) : Parcelable