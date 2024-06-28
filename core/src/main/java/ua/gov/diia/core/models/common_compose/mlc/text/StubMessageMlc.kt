package ua.gov.diia.core.models.common_compose.mlc.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeAdditionalAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class StubMessageMlc(
    @Json(name = "icon")
    val icon: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "btnStrokeAdditionalAtm")
    val btnStrokeAdditionalAtm: BtnStrokeAdditionalAtm? = null,
): Parcelable
