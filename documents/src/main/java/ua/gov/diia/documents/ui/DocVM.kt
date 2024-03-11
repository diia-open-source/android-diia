package ua.gov.diia.documents.ui

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.util.datasource.DataSourceOwner
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction

interface DocVM: DataSourceOwner, WithRetryLastAction, WithErrorHandlingOnFlow {
    fun currentDocId(): String?
    fun sendRatingRequest(ratingRequest: RatingRequest)
    fun showRating(doc: DiiaDocument)
    fun forceUpdateDocument(doc: DiiaDocument)
    fun scrollToLastDocPos()
    fun confirmDelDocument(docName: String)
    fun removeMilitaryBondFromGallery(
        documentType: String,
        documentId: String
    )

    fun removeDoc(diiaDocument: DiiaDocument)

    fun run(block: suspend ((TemplateDialogModel) -> Unit) -> Unit, dispatcher: CoroutineDispatcher = Dispatchers.Main)

    fun run(block: suspend (String) -> ShareByteArr?, docId: String)

    fun getCertificatePdf(cert: DiiaDocument)
    fun onUIAction(event: UIAction)

    fun invalidateAndScroll(type: String) {}
}