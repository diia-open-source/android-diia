package ua.gov.diia.core.models.common_compose.mlc.doc


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class DocNumberCopyMlc(
    @Json(name = "icon")
    val icon: IconAtm?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "value")
    val value: String,
    @Json(name = "componentId")
    val componentId: String? = null,
) : Parcelable