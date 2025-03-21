package ua.gov.diia.documents.ui

import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction

interface DocVM : WithRetryLastAction {
    fun currentDocId(): String?
    fun sendRatingRequest(ratingRequest: RatingRequest)
    fun showRating(doc: DiiaDocument)
    fun forceUpdateDocument(doc: DiiaDocument)
    fun scrollToLastDocPos()
    fun showConfirmDeleteTemplateLocal()
    fun removeDoc(diiaDocument: DiiaDocument)
    fun showConfirmDeleteTemplateRemote(diiaDocument: DiiaDocument){}
    fun removeDocByType(type: String){}
    fun loadImageAndShare(docType: String, docId: String)
    fun showRemoveDocDialog(doc: DiiaDocument)
    fun addDocToGallery()
    fun getCertificatePdf(cert: DiiaDocument)
    fun getDocumentPdf(doc: DiiaDocument)
    fun onUIAction(event: UIAction)
    fun invalidateAndScroll(type: String) {}
    fun invalidateAndRemove() {}
    fun validateIsDocExist(type: String, callback: (Boolean) -> Unit) {}
    fun confirmDocumentShare(type: String) {}
    fun loadAndShareDocument(type: String) {}
    fun checkStack()
}