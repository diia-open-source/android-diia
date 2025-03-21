package ua.gov.diia.core.models.document


import android.content.Context
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.ContextMenuField

@JsonClass(generateAdapter = true)
@Parcelize
data class ManualDocs(
    @Json(name = "documents")
    val documents: List<DocAction>
) : Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class DocAction(
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String
) : Parcelable, ContextMenuField {

    override fun getActionType() = code

    override fun getSubType() = code

    override fun getDisplayName(c: Context) = name

}