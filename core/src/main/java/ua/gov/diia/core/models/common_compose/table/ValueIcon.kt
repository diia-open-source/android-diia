package ua.gov.diia.core.models.common_compose.table

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ValueIcon(
    @Json(name = "code")
    val code: String?,
    @Json(name = "description")
    val description: String?
): Parcelable