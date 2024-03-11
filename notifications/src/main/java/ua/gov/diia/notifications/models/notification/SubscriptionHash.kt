package ua.gov.diia.notifications.models.notification


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SubscriptionHash(
    @Json(name = "name")
    val name: String,
    @Json(name = "hash")
    val hash: String
) : Parcelable