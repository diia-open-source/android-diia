package ua.gov.diia.publicservice.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.common.menu.ContextMenuItem

@Parcelize
@JsonClass(generateAdapter = true)
data class PublicService(
    @Json(name = "sortOrder")
    val sortOrder: Int,
    @Json(name = "search")
    val search: String,
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "status")
    val status: CategoryStatus,
    @Json(name = "contextMenu")
    val contextMenu: List<ContextMenuItem>?
) : Parcelable {

    val menu: Array<ContextMenuField>
        get() = contextMenu?.toTypedArray() ?: arrayOf()
}