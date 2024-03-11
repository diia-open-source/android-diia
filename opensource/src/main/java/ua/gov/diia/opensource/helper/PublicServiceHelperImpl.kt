package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.util.ext.navigateToPublicService
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory

class PublicServiceHelperImpl : PublicServiceHelper {
    override fun navigateToCategoryServices(fragment: Fragment, category: PublicServiceCategory) {
        fragment.apply {
            navigate(
                NavMainXmlDirections.actionGlobalDestinationCategoryDetailsCompose(
                    category = category,
                    resultDestinationId = currentDestinationId ?: return
                ),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override  fun navigateToServiceSearch(fragment: Fragment, data: Array<PublicServiceCategory>) {
        fragment.apply {
            navigate(
                NavMainXmlDirections.actionGlobalDestinationPsSearchCompose(
                    arbitraryDestinationId = currentDestinationId ?: return,
                    categories = data
                ),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override  fun navigateToService(fragment: Fragment, service: PublicService) {
        fragment.apply {
            navigateToPublicService(service)
        }
    }
}