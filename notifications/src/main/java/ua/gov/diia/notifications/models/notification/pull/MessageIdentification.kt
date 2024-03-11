package ua.gov.diia.notifications.models.notification.pull

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MessageIdentification(
    val needAuth: Boolean,
    val resourceId: String?,
    val notificationId: String
) : Parcelable