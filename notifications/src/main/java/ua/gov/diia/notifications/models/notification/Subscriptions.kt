package ua.gov.diia.notifications.models.notification


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Subscriptions(
    @Json(name = "description")
    val description: String,
    @Json(name = "subscriptions")
    val subscriptions: List<Subscription>
) : Parcelable