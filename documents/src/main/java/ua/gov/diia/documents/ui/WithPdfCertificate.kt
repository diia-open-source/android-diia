package ua.gov.diia.documents.ui

import androidx.lifecycle.LiveData
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc

interface WithPdfCertificate {
    val certificatePdf: LiveData<UiDataEvent<GeneratePdfFromDoc>>
    /**
     * loads pdf certificate and provides it into certificatePdf LiveData
     * Leave as empty if you do not need to load pdf
     */
    suspend fun loadCertificatePdf(cert: DiiaDocument)
}