package ua.gov.diia.core.models.dialogs

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TemplateDialogData(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "mainButton")
    val mainButton: TemplateDialogButton,
    @Json(name = "alternativeButton")
    val alternativeButton: TemplateDialogButton? = null,
) : Parcelable