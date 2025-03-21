package ua.gov.diia.opensource.util.extensions.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavHomeChildrenDirections
import ua.gov.diia.opensource.NavMainDirections
import ua.gov.diia.opensource.ui.fragments.publicservice.home.PublicServicesHomeConst
import ua.gov.diia.publicservice.models.PublicService

fun Fragment.navigateToRatingService(
    ratingFormModel: RatingFormModel,
    id: String?,
    @IdRes destinationId: Int,
    resultKey: String,
    screenCode: String? = null,
    ratingType: String? = null,
    formCode: String? = null
) {

}

fun Fragment.navigateToContextMenu(menu: Array<ContextMenuField>) {
    navigate(NavMainDirections.actionGlobalDestinationContextMenu(menu))
}

fun Fragment.navigateToFaq(categoryId: String) {
}

fun Fragment.navigateToTips() {
}

fun Fragment.navigateToSupport() {
}



fun getPullNotificationDirection(item: PullNotificationItemSelection, actions: List<BasePushNotificationAction>): NavDirections? {
    val action = actions.find { it.id == item.resourceType }
    val navDir = action?.getNavigationDirection(item)
    return navDir
}

fun Fragment.navigateToPullNotificationDirection(item: PullNotificationItemSelection, actions: List<BasePushNotificationAction>) {
    val action = actions.find { it.id == item.resourceType }
    val navDir = action?.let { it.getNavigationDirection(item) }

    navigate(navDir ?: NavHomeChildrenDirections.globalToDocGalleryFCompose())
}

fun Fragment.navigateToPublicService(service: PublicService) {
    when (service.code) {
        PublicServicesHomeConst.PS_SERVICE_CRIME_CERTIFICATE -> navigate(
            NavMainDirections.actionHomeFToCriminalCert(
                contextMenu = service.menu
            )
        )
    }
}
