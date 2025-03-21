package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.feed.helper.FeedHelper
import ua.gov.diia.notifications.models.notification.pull.MessageIdentification
import ua.gov.diia.opensource.NavMainDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.util.extensions.fragment.navigateToPublicService
import ua.gov.diia.publicservice.models.CategoryStatus
import ua.gov.diia.publicservice.models.PublicService

class FeedHelperImpl : FeedHelper {

    override fun navigateToPublicService(fragment: Fragment, serviceCode: String) {
        val service = PublicService(
            sortOrder = 0,
            search = "",
            code = serviceCode,
            name = "",
            status = CategoryStatus.active,
            contextMenu = null
        )
        fragment.navigateToPublicService(service)
    }

    override fun navigateToNotifications(fragment: Fragment) {
        fragment.apply {
            navigate(
                NavMainDirections.actionGlobalNotificationFCompose(),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override fun navigateToNotification(
        fragment: Fragment,
        notification: PullNotificationItemSelection
    ) {
        val messageIdentification = MessageIdentification(
            needAuth = true,
            resourceId = notification.resourceId ?: "",
            notificationId = notification.notificationId ?: ""
        )

        fragment.apply {
            navigate(
                NavMainDirections.actionGlobalToNotificationFull(messageId = messageIdentification),
                findNavControllerById(ua.gov.diia.notifications.R.id.nav_host)
            )
        }
    }

    override fun navigateByNavDirection(fragment: Fragment, navDirection: NavDirections) {
        fragment.apply {
            navigate(
                navDirection,
                findNavControllerById(ua.gov.diia.feed.R.id.nav_host)
            )
        }
    }
}