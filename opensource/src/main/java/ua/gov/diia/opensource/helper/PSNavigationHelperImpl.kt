package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.util.ext.sendPdf
import ua.gov.diia.opensource.util.ext.sendZip
import ua.gov.diia.publicservice.helper.PSNavigationHelper

class PSNavigationHelperImpl : PSNavigationHelper {
    override fun navigateToContextMenu(
        fragment: Fragment,
        menu: Array<ContextMenuField>
    ) {
        fragment.navigate(
            NavMainXmlDirections.actionGlobalDestinationContextMenu(menu)
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
        fragment.sendPdf(file, name)
    }

    override fun sendZip(fragment: Fragment, file: String, name: String) {
        fragment.sendZip(file, name)
    }
}
