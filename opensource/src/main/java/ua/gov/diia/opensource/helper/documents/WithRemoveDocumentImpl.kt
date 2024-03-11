package ua.gov.diia.opensource.helper.documents

import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.ui.WithRemoveDocument
import javax.inject.Inject

class WithRemoveDocumentImpl @Inject constructor() : WithRemoveDocument {

    override suspend fun removeDocument(
        diiaDocument: DiiaDocument,
        removeDocumentCallback: (DiiaDocument) -> Unit
    ) = Unit

    override suspend fun removeMilitaryBondFromGallery(
        documentType: String,
        documentId: String,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit
    ) = Unit

    override suspend fun confirmRemoveDocument(
        docName: String,
        currentDoc: () -> DiiaDocument?,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit,
        removeDocumentCallback: (DiiaDocument) -> Unit
    ) = Unit
}