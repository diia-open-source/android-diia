package ua.gov.diia.opensource.helper.documents

import retrofit2.HttpException
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.doc_driver_license.DriverLicenseV2
import ua.gov.diia.documents.barcode.DocumentBarcodeErrorLoadResult
import ua.gov.diia.documents.barcode.DocumentBarcodeFactory
import ua.gov.diia.documents.barcode.DocumentBarcodeRepository
import ua.gov.diia.documents.barcode.DocumentBarcodeRepositoryResult
import ua.gov.diia.documents.barcode.DocumentBarcodeSuccessfulLoadResult
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.QRUrl
import ua.gov.diia.opensource.data.network.api.ApiDocs
import javax.inject.Inject

class DocumentBarcodeRepositoryImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs,
    private val barcodeFactory: DocumentBarcodeFactory,
) : DocumentBarcodeRepository {

    override suspend fun loadBarcode(
        doc: DiiaDocument,
        position: Int,
        fullInfo: Boolean,
    ): DocumentBarcodeRepositoryResult {
        val docName = mapDocName(doc)
        val result = try {
            when (docName) {
                DocName.DRIVER_LICENSE -> {
                    val qrInfo = apiDocs.getShareUrlWithLocalization(
                        docName,
                        doc.id,
                        doc.localization()?.name ?: "ua"
                    )
                    qrUrlToBarcode(qrInfo, position)
                }

                else -> {
                    val qrInfo = apiDocs.getShareUrl(docName, doc.id)
                    qrUrlToBarcode(qrInfo, position)
                }
            }
        } catch (e: Exception) {
            if ((e as? HttpException)?.code() in SERVER_ERROR_HTTP_RANGE) {
                DocumentBarcodeErrorLoadResult(e, 500)
            } else {
                DocumentBarcodeErrorLoadResult(e)
            }
        }
        return DocumentBarcodeRepositoryResult(result, true)
    }

    private fun mapDocName(doc: DiiaDocument): String {
        return when (doc) {
            is DriverLicenseV2.Data -> DocName.DRIVER_LICENSE
            else -> ""
        }
    }

    private suspend fun qrUrlToBarcode(
        shareData: QRUrl,
        itemPosition: Int,
    ): DocumentBarcodeSuccessfulLoadResult {
        barcodeFactory.buildBitmapQrCode(shareData.link)
        shareData.shareCode?.let { ean ->
            barcodeFactory.buildBitmapEan13Code(ean)
        }
        val result = DocumentBarcodeSuccessfulLoadResult(
            barcodeFactory.getQrCodeResult(),
            barcodeFactory.getEan13CodeResult(),
            shareData.shareCode,
            itemPosition,
            shareData.timerText,
            shareData.timerTime
        )
        barcodeFactory.clearResults()
        return result
    }

    companion object {
        private val SERVER_ERROR_HTTP_RANGE = 500..600
    }
}