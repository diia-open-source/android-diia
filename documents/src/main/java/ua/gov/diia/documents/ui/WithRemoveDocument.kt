package ua.gov.diia.documents.ui

import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument

interface WithRemoveDocument {

    /**
     * performs api call to remove specific document
     */
    suspend fun removeDocument(
        diiaDocument: DiiaDocument,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit,
        removeDocumentCallback: (DiiaDocument) -> Unit
    )
    /**
     * handles document remove confirmation
     */
    suspend fun confirmRemoveDocument(
        currentDoc: DiiaDocument?,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit,
        removeDocumentCallback: (DiiaDocument) -> Unit
    )

    /**
     * Load confirmation template to delete document
     */
    suspend fun loadConfirmRemoveDocumentTemplate(
        currentDoc: DiiaDocument?,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit
    )
}