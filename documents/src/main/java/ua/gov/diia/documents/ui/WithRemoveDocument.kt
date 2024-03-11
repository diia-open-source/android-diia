package ua.gov.diia.documents.ui

import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.documents.models.DiiaDocument

interface WithRemoveDocument {

    /**
     * performs api call to remove specific document
     */
    suspend fun removeDocument(
        diiaDocument: DiiaDocument,
        removeDocumentCallback: (DiiaDocument) -> Unit
    )

    /**
     * performs api call to remove MilitaryBond
     */
    suspend fun removeMilitaryBondFromGallery(
        documentType: String,
        documentId: String,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit
    )
    /**
     * handles document remove confirmation
     */
    suspend fun confirmRemoveDocument(
        docName: String,
        currentDoc: () -> DiiaDocument?,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit,
        removeDocumentCallback: (DiiaDocument) -> Unit
    )
}