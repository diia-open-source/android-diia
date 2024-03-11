package ua.gov.diia.core.push

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection

abstract class BasePushNotificationAction(val id: String) {
    abstract fun getNavigationDirection(item: PullNotificationItemSelection): NavDirections?
}