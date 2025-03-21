package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.context_menu.helper.ContextMenuNavigationHelper
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavMainDirections

class ContextMenuNavigationHelperImpl : ContextMenuNavigationHelper {
    override fun navigateToContextMenu(
        fragment: Fragment,
        menu: Array<ContextMenuField>
    ) {
        fragment.navigate(NavMainDirections.actionGlobalDestinationContextMenu(menu))
    }

    override fun navigateToFaq(fragment: Fragment, categoryId: String) {
        //fragment.navigate(NavMainDirections.actionGlobalToNavFaq(categoryId))
    }

    override fun navigateToRatingService(
        fragment: Fragment,
        ratingFormModel: RatingFormModel,
        id: String?,
        destinationId: Int,
        resultKey: String,
        screenCode: String?,
        ratingType: String?,
        formCode: String?
    ) {
        /*fragment.navigate(
            NavMainDirections.actionGlobalToRatingDialog(
                dialog = ratingFormModel,
                applicationId = id,
                resultDestinationId = destinationId,
                resultKey = resultKey,
                screenCode = screenCode,
                ratingType = ratingType,
                formCode = formCode
            )
        )*/
    }

    override fun navigateToSupport(fragment: Fragment) {
        //fragment.navigate(NavMainDirections.actionGlobalDestinationSupport())
    }
}