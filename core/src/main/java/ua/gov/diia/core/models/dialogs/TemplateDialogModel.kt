package ua.gov.diia.core.models.dialogs

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TemplateDialogModel(
    @Json(name = "key?")
    val key: String?,
    @Json(name = "type")
    val type: String,
    @Json(name = "isClosable")
    val isClosable: Boolean,
    @Json(name = "data")
    val data: TemplateDialogData,
) : Parcelable {

    fun setKey(key: String) = copy(key = key)
}