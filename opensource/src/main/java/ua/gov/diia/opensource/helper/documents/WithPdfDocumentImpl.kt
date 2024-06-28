package ua.gov.diia.opensource.helper.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc
import ua.gov.diia.documents.ui.WithPdfDocument
import javax.inject.Inject

class WithPdfDocumentImpl @Inject constructor() : WithPdfDocument {
    private val _documentPdf =
        MutableLiveData<UiDataEvent<GeneratePdfFromDoc>>()
    override val documentPdf: LiveData<UiDataEvent<GeneratePdfFromDoc>>
        get() = _documentPdf.asLiveData()

    override suspend fun loadDocumentPdf(
        document: DiiaDocument,
        loadDocumentErrorCallback: (template: TemplateDialogModel) -> Unit
    ) {
       //TODO: implement your logic here
    }

}