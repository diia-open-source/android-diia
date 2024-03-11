package ua.gov.diia.publicservice.models


import android.content.Context
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.ContextMenuField

@Parcelize
@JsonClass(generateAdapter = true)
data class ContextMenu(
    @Json(name = "type")
    val type: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "code")
    val code: String?
): Parcelable, ContextMenuField {

    override fun getActionType() = type

    override fun getSubType(): String? = code

    override fun getDisplayName(c: Context) = name

}