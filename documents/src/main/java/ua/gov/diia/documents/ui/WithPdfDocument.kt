package ua.gov.diia.documents.ui

import androidx.lifecycle.LiveData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc

interface WithPdfDocument {
    val documentPdf: LiveData<UiDataEvent<GeneratePdfFromDoc>>
    /**
     * loads pdf document and provides it into documentPdf LiveData
     * Leave as empty if you do not need to load pdf
     */
    suspend fun loadDocumentPdf(document: DiiaDocument, loadDocumentErrorCallback: (template: TemplateDialogModel) -> Unit)
}