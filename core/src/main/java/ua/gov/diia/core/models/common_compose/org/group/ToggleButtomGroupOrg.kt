package ua.gov.diia.core.models.common_compose.org.group


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.button.BtnToggleMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class ToggleButtonGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "preselected")
    val preselected: String
) : Parcelable {
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "btnToggleMlc")
        val btnToggleMlc: BtnToggleMlc
    ) : Parcelable
}