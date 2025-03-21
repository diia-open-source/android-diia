package ua.gov.diia.opensource.util.documents

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.documents.ui.WithRemoveDocument
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import javax.inject.Inject

class WithRemoveDocumentImpl @Inject constructor(
    @AuthorizedClient
    private val apiDocs: ApiDocs,
) : WithRemoveDocument {

    override suspend fun removeDocument(
        diiaDocument: DiiaDocument,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit,
        removeDocumentCallback: (DiiaDocument) -> Unit
    ) {}

    override suspend fun confirmRemoveDocument(
        currentDoc: DiiaDocument?,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit,
        removeDocumentCallback: (DiiaDocument) -> Unit
    ) {}

    override suspend fun loadConfirmRemoveDocumentTemplate(
        currentDoc: DiiaDocument?,
        showTemplateDialogCallback: (TemplateDialogModel) -> Unit
    ) {}

}