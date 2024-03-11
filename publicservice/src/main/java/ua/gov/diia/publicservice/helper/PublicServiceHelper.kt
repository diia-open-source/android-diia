package ua.gov.diia.publicservice.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory

interface PublicServiceHelper {

    /**
     * Navigate to PublicServiceCategory screen
     */
    fun navigateToCategoryServices(fragment: Fragment, category: PublicServiceCategory)
    /**
     * Navigate to PublicServiceSearch screen
     */
    fun navigateToServiceSearch(fragment: Fragment, data: Array<PublicServiceCategory>)
    /**
     * Navigate to PublicService screen
     */
    fun navigateToService(fragment: Fragment, service: PublicService)
}