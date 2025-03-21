package ua.gov.diia.opensource.util.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.documents.models.GeneratePdfFromDoc
import ua.gov.diia.documents.ui.WithPdfDocument
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import javax.inject.Inject

class WithPdfDocumentImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs
) : WithPdfDocument {
    private val _documentPdf =
        MutableLiveData<UiDataEvent<GeneratePdfFromDoc>>()
    override val documentPdf: LiveData<UiDataEvent<GeneratePdfFromDoc>>
        get() = _documentPdf.asLiveData()

    override suspend fun loadDocumentPdf(
        document: DiiaDocument,
        loadDocumentErrorCallback: (template: TemplateDialogModel) -> Unit
    ) {
        val documentPdf = apiDocs.getDocumentPdf(
            document.getItemType(),
            document.docId()
        )

        if (documentPdf.documentFile != null) {
            _documentPdf.value =
                UiDataEvent(
                    GeneratePdfFromDoc(
                        documentPdf.documentFile?.file ?: "",
                        documentPdf.documentFile?.name ?: ""
                    )
                )
        } else {
            documentPdf.template?.let { loadDocumentErrorCallback(it) }
        }
    }

}