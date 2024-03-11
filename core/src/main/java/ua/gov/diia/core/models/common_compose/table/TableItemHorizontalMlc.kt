package ua.gov.diia.core.models.common_compose.table

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class TableItemHorizontalMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "icon")
    val icon: IconAtm? = null,
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "secondaryLabel")
    val secondaryLabel: String? = null,
    @Json(name = "secondaryValue")
    val secondaryValue: String? = null,
    @Json(name = "supportingValue")
    val supportingValue: String? = null,
    @Json(name = "value")
    val value: String? = null,
    @Json(name = "valueImage")
    val valueImage: String? = null,
) : Parcelable