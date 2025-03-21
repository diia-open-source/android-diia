package ua.gov.diia.core.models.common_compose.atm.input

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class InputNumberLargeAtm(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "state")
    val state: String?
) : Parcelable