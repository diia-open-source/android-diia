package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavMainDirections
import ua.gov.diia.opensource.util.extensions.fragment.sendZip
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.publicservice.util.extensions.fragment.sendPdf

class PSNavigationHelperImpl : PSNavigationHelper {
    override fun navigateToContextMenu(
        fragment: Fragment,
        menu: Array<ContextMenuField>
    ) {
        fragment.navigate(
            NavMainDirections.actionGlobalDestinationContextMenu(menu)
        )
    }

    override fun navigateToFaq(fragment: Fragment, categoryId: String) {
        // Implement navigation to FAQ screen
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
        // Implement navigation to Rating service screen
    }

    override fun navigateToSupport(fragment: Fragment) {
        // Implement navigation to support screen
    }

    override fun sendPdf(fragment: Fragment, file: String, name: String) {
        fragment.sendPdf(file, name, "")
    }

    override fun sendZip(fragment: Fragment, file: String, name: String) {
        fragment.sendZip(file, name)
    }

    override fun navigateToGlobalDestinationPS(
        fragment: Fragment,
        currentDestinationId: Int,
        resultKey: String,
        psKey: String
    ) {
        //nav to ps by key
    }
}
