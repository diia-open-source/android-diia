package ua.gov.diia.core.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
@Parcelize
data class Token(
    @Json(name = "token")
    val token: String,
    @Json(name = "template")
    val template: TemplateDialogModel?,
) : Parcelable