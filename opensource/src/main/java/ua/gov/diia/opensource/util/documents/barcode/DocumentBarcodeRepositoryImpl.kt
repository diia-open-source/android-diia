package ua.gov.diia.opensource.util.documents.barcode

import retrofit2.HttpException
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeErrorLoadResult
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeFactory
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeRepository
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeRepositoryResult
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeSuccessfulLoadResult
import ua.gov.diia.doc_driver_license.models.DocName.DRIVER_LICENSE
import ua.gov.diia.documents.models.QRUrl
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import ua.gov.diia.opensource.util.documents.DocNameVerificationMapper
import javax.inject.Inject

class DocumentBarcodeRepositoryImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs,
    private val barcodeFactory: DocumentBarcodeFactory,
    private val docNameVerificationMapper: DocNameVerificationMapper
) : DocumentBarcodeRepository {
    override suspend fun loadBarcode(
        doc: DiiaDocument,
        position: Int,
        fullInfo: Boolean
    ): DocumentBarcodeRepositoryResult {
        val docName =
            if (fullInfo) docNameVerificationMapper.mapDocFullInfo(doc) else docNameVerificationMapper.mapDocGeneral(
                doc
            )
        val result = try {
            when (docName) {

                DRIVER_LICENSE -> {
                    val qrInfo = apiDocs.getShareUrlWithLocalization(
                        docName,
                        doc.id,
                        doc.localization()?.name ?: "ua"
                    )
                    qrUrlToBarcodeAndQR(qrInfo, position)
                }

                else -> {
                    val qrInfo = apiDocs.getShareUrl(docName, doc.id)
                    qrUrlToBarcodeAndQR(qrInfo, position)
                }
            }
        } catch (e: Exception) {
            if ((e as? HttpException)?.code() in SERVER_ERROR_HTTP_RANGE) {
                DocumentBarcodeErrorLoadResult(e, 500)
            } else {
                DocumentBarcodeErrorLoadResult(e)
            }
        }
        return DocumentBarcodeRepositoryResult(result, showToggle(docName))
    }

    private suspend fun qrUrlToBarcodeAndQR(
        shareData: QRUrl,
        itemPosition: Int
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

    private suspend fun qrUrlToQR(
        shareData: QRUrl,
        itemPosition: Int
    ): DocumentBarcodeSuccessfulLoadResult {
        barcodeFactory.buildBitmapQrCode(shareData.link)
        shareData.shareCode?.let { ean ->
            barcodeFactory.buildBitmapEan13Code(ean)
        }
        val result = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcodeFactory.getQrCodeResult(),
            shareEan13 = null,
            shareEanCode = shareData.shareCode,
            position = itemPosition,
            timerText = shareData.timerText,
            timerTime = shareData.timerTime
        )
        barcodeFactory.clearResults()
        return result
    }


    private suspend fun stringToBarcode(
        data: String,
        itemPosition: Int
    ): DocumentBarcodeSuccessfulLoadResult {
        barcodeFactory.buildBitmapQrCode(data)
        val result = DocumentBarcodeSuccessfulLoadResult(
            shareQr = barcodeFactory.getQrCodeResult(),
            null,
            null,
            itemPosition,
            null,
            null
        )
        barcodeFactory.clearResults()
        return result
    }

    private fun base64ToBarcode(
        base64: String,
        itemPosition: Int
    ): DocumentBarcodeSuccessfulLoadResult {
        return DocumentBarcodeSuccessfulLoadResult(
            barcodeFactory.parseBase64Barcode(base64),
            null,
            null,
            itemPosition,
            null,
            null
        )
    }

    private fun showToggle(docName: String): Boolean {
        return when (docName) {

            else -> {
                true
            }
        }
    }

    companion object {
        private val SERVER_ERROR_HTTP_RANGE = 500..600
    }
}