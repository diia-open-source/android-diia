package ua.gov.diia.documents.ui

import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction

interface DocVM: WithRetryLastAction {
    fun currentDocId(): String?
    fun sendRatingRequest(ratingRequest: RatingRequest)
    fun showRating(doc: DiiaDocument)
    fun forceUpdateDocument(doc: DiiaDocument)
    fun scrollToLastDocPos()
    fun confirmDelDocument()
    fun removeDoc(diiaDocument: DiiaDocument)
    fun loadImageAndShare(docId: String)
    fun showRemoveDocDialog(key: String)
    fun addDocToGallery()
    fun getCertificatePdf(cert: DiiaDocument)
    fun getDocumentPdf(doc: DiiaDocument)
    fun onUIAction(event: UIAction)
    fun invalidateAndScroll(type: String) {}
    fun stopLoading()
}