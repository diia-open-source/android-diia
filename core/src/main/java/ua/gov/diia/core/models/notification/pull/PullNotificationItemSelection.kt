package ua.gov.diia.core.models.notification.pull

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PullNotificationItemSelection(
    val notificationId: String? = null,
    val resourceId: String?,
    val resourceType: String,
    val resourceSubtype: String?
) : Parcelable