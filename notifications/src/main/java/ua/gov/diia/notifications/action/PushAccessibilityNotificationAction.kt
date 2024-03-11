package ua.gov.diia.notifications.action

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.notifications.action.ActionConstants.NOTIFICATION_TYPE_PUSH_ACCESSIBILITY

class PushAccessibilityNotificationAction: BasePushNotificationAction(NOTIFICATION_TYPE_PUSH_ACCESSIBILITY) {

    override fun getNavigationDirection(item: PullNotificationItemSelection): NavDirections? = null

}