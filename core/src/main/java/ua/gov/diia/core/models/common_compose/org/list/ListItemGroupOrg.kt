package ua.gov.diia.core.models.common_compose.org.list

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class ListItemGroupOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "title")
    val title: String?,
    @Json(name = "items")
    val items: List<ListItemMlc>,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm? = null
) : Parcelable