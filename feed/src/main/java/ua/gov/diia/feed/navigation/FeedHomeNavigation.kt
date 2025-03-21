package ua.gov.diia.feed.navigation

import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.navigation.HomeNavigation

sealed class FeedHomeNavigation : HomeNavigation {

    data class ToStartNewFlow(
        val deeplink: DeepLinkActionStartFlow,
        override var isConsumed: Boolean = false
    ) : FeedHomeNavigation()

    data class ToPublicService(
        val serviceCode: String,
        override var isConsumed: Boolean = false
    ) : FeedHomeNavigation()

    data class ToTemplateDialog(
        val template: TemplateDialogModel,
        override var isConsumed: Boolean = false
    ) : FeedHomeNavigation()

    data class ToCameraRequest(override var isConsumed: Boolean = false) : FeedHomeNavigation()

    data class ToNotification(override var isConsumed: Boolean = false) : FeedHomeNavigation()

    data class OnNotificationSelected(
        val notification: PullNotificationItemSelection,
        override var isConsumed: Boolean = false
    ) : FeedHomeNavigation()
}