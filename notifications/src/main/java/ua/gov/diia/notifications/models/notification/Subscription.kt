package ua.gov.diia.notifications.models.notification


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Subscription(
    @Json(name = "code")
    val code: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "status")
    val status: String
) : Parcelable {

    val switchState
        get() = status == "active"

    val hideItem
    get() = status == "blocked"
}