package ua.gov.diia.notifications.action

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.notifications.action.ActionConstants.NOTIFICATION_TYPE_DOCUMENTS_SHARING

class DocumentSharingPushNotificationAction: BasePushNotificationAction(NOTIFICATION_TYPE_DOCUMENTS_SHARING) {

    override fun getNavigationDirection(item: PullNotificationItemSelection): NavDirections? = null
}