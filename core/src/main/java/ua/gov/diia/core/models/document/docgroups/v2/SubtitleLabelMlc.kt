package ua.gov.diia.core.models.document.docgroups.v2


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class SubtitleLabelMlc(
    @Json(name = "label")
    val label: String?,
    @Json(name = "icon")
    val icon: IconAtm?,
    @Json(name = "componentId")
    val componentId: String? = null,
) : Parcelable