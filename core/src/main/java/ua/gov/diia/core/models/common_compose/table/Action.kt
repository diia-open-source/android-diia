package ua.gov.diia.core.models.common_compose.table

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Action(
    @Json(name = "resource")
    val resource: String?,
    @Json(name = "subtype")
    val subtype: String?,
    @Json(name = "type")
    val type: String
): Parcelable