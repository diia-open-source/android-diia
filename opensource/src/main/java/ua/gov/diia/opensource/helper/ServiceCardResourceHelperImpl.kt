package ua.gov.diia.opensource.helper

import ua.gov.diia.opensource.ui.compose.ServiceCardIconResource
import ua.gov.diia.ui_base.helper.ServiceCardResourceHelper
import javax.inject.Inject

class ServiceCardResourceHelperImpl @Inject constructor() : ServiceCardResourceHelper {
    override fun getIconResourceId(code: String): Int {
        return ServiceCardIconResource.getResourceId(code)
    }

    override fun geIconContentDescription(code: String): Int {
        return ServiceCardIconResource.getContentDescription(code)
    }

}