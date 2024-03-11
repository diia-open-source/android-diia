package ua.gov.diia.core.models.common_compose

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.menu.ContextMenuItem

@Parcelize
@JsonClass(generateAdapter = true)
data class NavigationBarMlcl(
    @Json(name = "label")
    val label: String,
    @Json(name = "ellipseMenu")
    val ellipseMenu: List<ContextMenuItem>?,
): Parcelable