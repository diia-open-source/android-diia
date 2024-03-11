package ua.gov.diia.core.models.common_compose.mlc.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class SmallEmojiPanelMlc(
    @Json(name = "label")
    val label: String?,
    @Json(name = "icon")
    val icon: IconAtm?,
) : Parcelable