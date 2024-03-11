package ua.gov.diia.core.models.auth

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class FaceRecoConfig(
    @Json(name = "fld")
    val fld: Fld?
): Parcelable