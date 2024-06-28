package ua.gov.diia.core.models.common_compose.mlc.button

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class BtnLoadIconPlainGroupMlc(
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "componentId")
    val componentId: String? = null,
) : Parcelable