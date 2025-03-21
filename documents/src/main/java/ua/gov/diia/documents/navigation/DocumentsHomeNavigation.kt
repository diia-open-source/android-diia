package ua.gov.diia.documents.navigation

import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.ManualDocs
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.util.navigation.HomeNavigation

sealed class DocumentsHomeNavigation : HomeNavigation {

    data class ToTemplateDialog(
        val template: TemplateDialogModel,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class ToDocActions(
        val doc: DiiaDocument,
        val position: Int,
        val manualDocs: ManualDocs?,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class OnTickerClick(
        val doc: DiiaDocument,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class ToDocOrder(override var isConsumed: Boolean = false) : DocumentsHomeNavigation()

    data class ToStartNewFlow(
        val deeplink: DeepLinkActionStartFlow,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class ToStackDocs(
        val doc: DiiaDocument,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class ToWebView(
        val link: String,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class ToDocNotExistTemplate(
        val docType: String,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()

    data class ToRatingService(
        val docId: String,
        val form: RatingFormModel,
        val isFromStack: Boolean,
        override var isConsumed: Boolean = false
    ) : DocumentsHomeNavigation()
}