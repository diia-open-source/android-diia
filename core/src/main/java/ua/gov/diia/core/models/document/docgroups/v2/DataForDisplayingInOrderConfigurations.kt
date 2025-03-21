package ua.gov.diia.core.models.document.docgroups.v2

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DataForDisplayingInOrderConfigurations(
    @Json(name = "description")
    val description: String?,
    @Json(name = "iconLeft")
    val iconLeft: IconLeft? = null,
    @Json(name = "iconRight")
    val iconRight: IconRight? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "logoLeft")
    val logoLeft: String? = null,
): Parcelable