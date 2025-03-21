package ua.gov.diia.core.models.common_compose.org.input

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.input.InputDateMlc
import ua.gov.diia.core.models.common_compose.mlc.input.InputTimeMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class InputDateTimeOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "maxDate")
    val maxDate: String?,
    @Json(name = "minDate")
    val minDate: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "inputDateMlc")
    val inputDateMlc: InputDateMlc?,
    @Json(name = "inputTimeMlc")
    val inputTimeMlc: InputTimeMlc?,
    @Json(name = "mandatory")
    val mandatory: Boolean,
) : Parcelable