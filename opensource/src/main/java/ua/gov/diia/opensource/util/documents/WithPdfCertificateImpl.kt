package ua.gov.diia.opensource.util.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.documents.models.GeneratePdfFromDoc
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import javax.inject.Inject

class WithPdfCertificateImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs
) : WithPdfCertificate {
    private val _certificatePdf =
        MutableLiveData<UiDataEvent<GeneratePdfFromDoc>>()
    override val certificatePdf: LiveData<UiDataEvent<GeneratePdfFromDoc>>
        get() = _certificatePdf.asLiveData()

    override suspend fun loadCertificatePdf(
        cert: DiiaDocument
    ) {
    }
}