package ua.gov.diia.opensource.helper.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc
import ua.gov.diia.documents.ui.WithPdfCertificate
import javax.inject.Inject

class WithPdfCertificateImpl @Inject constructor() : WithPdfCertificate {
    private val _certificatePdf =
        MutableLiveData<UiDataEvent<GeneratePdfFromDoc>>()
    override val certificatePdf: LiveData<UiDataEvent<GeneratePdfFromDoc>>
        get() = _certificatePdf.asLiveData()

    override suspend fun loadCertificatePdf(
        cert: DiiaDocument
    ) = Unit
}