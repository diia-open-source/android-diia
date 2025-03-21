package ua.gov.diia.publicservice.navigation

import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.navigation.HomeNavigation
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory

sealed class PublicServiceHomeNavigation : HomeNavigation {

    data class ToTemplateDialog(
        val template: TemplateDialogModel,
        override var isConsumed: Boolean = false
    ) : PublicServiceHomeNavigation()

    data class ToServiceSearch(
        val data: Array<PublicServiceCategory>,
        override var isConsumed: Boolean = false
    ) : PublicServiceHomeNavigation()

    data class ToService(
        val service: PublicService,
        override var isConsumed: Boolean = false
    ) : PublicServiceHomeNavigation()

    data class ToCategory(
        val category: PublicServiceCategory,
        override var isConsumed: Boolean = false
    ) : PublicServiceHomeNavigation()
}