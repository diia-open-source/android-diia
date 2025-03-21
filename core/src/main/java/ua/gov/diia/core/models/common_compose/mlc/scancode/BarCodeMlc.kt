package ua.gov.diia.core.models.common_compose.mlc.scancode


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class BarCodeMlc(
    @Json(name = "barCode")
    val barCode: String,
    @Json(name = "componentId")
    val componentId: String?
): Parcelable