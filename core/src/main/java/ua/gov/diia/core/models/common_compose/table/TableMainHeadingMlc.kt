package ua.gov.diia.core.models.common_compose.table


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class TableMainHeadingMlc(
    @Json(name = "icon")
    val icon: IconAtm?,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?
): Parcelable