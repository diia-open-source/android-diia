package ua.gov.diia.opensource.helper

import android.os.Parcelable
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.ManualDocs
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.network.getValidStatusesToDisplay
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.navigateOnce
import ua.gov.diia.core.util.share.ShareHelper
import ua.gov.diia.doc_driver_license.models.DocName.DRIVER_LICENSE
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.doc_manual_options.models.DocManualOptions
import ua.gov.diia.documents.NavDocActionsDirections
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.documents.util.AndroidClientAlertDialogsFactory.Companion.UNKNOWN_ERR
import ua.gov.diia.opensource.NavMainDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.util.extensions.fragment.navigateToRatingService

class DocumentsHelperImpl(

    private val shareHelper: ShareHelper,
    @UnauthorizedClient private val okhttp: OkHttpClient,
    private val clientAlertDialogsFactory: ClientAlertDialogsFactory,
) : DocumentsHelper {
    override fun isDocCanBeBroken(docType: String): Boolean = false

    override fun getExpiredDocStatus(docType: String): Int {
        return Http.NEED_UPDATE_STATUS
    }

    override suspend fun migrateDocuments(
        data: List<DiiaDocumentWithMetadata>?,
        shouldSaveData: (data: List<DiiaDocumentWithMetadata>) -> Unit
    ): List<DiiaDocumentWithMetadata>? {
        return data
    }

    override fun isDocEligibleForDeletion(docType: String): Boolean {
        return false
    }

    override suspend fun loadDocImageInByteArray(docType: String, docId: String): ShareByteArr? {
        return shareHelper.getByteArrFromUrl("")
    }


    override suspend fun loadAndShareDocument(
        permission: Boolean,
        diiaDocument: DiiaDocument?,
        onSuccess: (ByteArray) -> Unit,
        onTemplateRecieved: (TemplateDialogModel) -> Unit
    ) {
    }

    override suspend fun addDocToGallery(): TemplateDialogModel? {
        return clientAlertDialogsFactory.showCustomAlert(UNKNOWN_ERR)
    }

    override fun isDocumentValid(receivedDoc: DiiaDocumentWithMetadata): Boolean {
        val docStatus = receivedDoc.diiaDocument?.getStatus()
        val statuses = getValidStatusesToDisplay()
        return statuses.contains(docStatus)
    }

    override fun provideListOfDocumentsRequireUpdateOfExpirationDate(focusDocType: String): List<String>? {
        return null
    }

    override fun showVerificationButtons(document: Parcelable): Boolean {
        return when (document) {
            is DriverLicenseV2.Data -> true
            else -> {
                false
            }
        }
    }

    override fun isDocRequireGeneralMenuActions(doc: Parcelable): Boolean {
        return when (doc) {
            is DriverLicenseV2.Data -> true
            else -> false
        }
    }

    override fun getStackHeader(fragment: Fragment, docType: String): String {
        with(fragment) {
            return when (docType) {
                DRIVER_LICENSE -> getString(R.string.driver_license)
                else -> ""
            }.replace('\n', ' ')
        }
    }

    override fun navigateToRatingService(
        fragment: Fragment,
        currentDocId: String,
        form: RatingFormModel,
        isFromStack: Boolean
    ) {
        with(fragment) {
            this.navigateToRatingService(
                form,
                currentDocId,
                if (isFromStack) {
                    R.id.stackFCompose
                } else {
                    R.id.homeF
                },
                ActionsConst.RATING,
                ActionsConst.DOCUMENTS_CODE,
                ActionsConst.TYPE_USER_INITIATIVE,
                form.formCode
            )
        }
    }

    override fun navigateToStackDocs(fragment: Fragment, doc: DiiaDocument) {
        with(fragment) {
            navigate(
                NavMainDirections.actionGlobalToStackFCompose(doc.getItemType(), doc.getDocColor()),
                findNavControllerById(R.id.nav_host),
            )
        }
    }

    override fun navigateToDocOrder(fragment: Fragment) {
        with(fragment) {
            navigate(NavMainDirections.actionGlobalToStackOrder())
        }
    }

    override fun onTickerClick(fragment: Fragment, doc: Parcelable) {
    }

    override fun getBaseDocumentsList(): List<DiiaDocumentWithMetadata> {
        val LAST_DOC_ORDER = Int.MAX_VALUE - 1
        val FIRST_DOC_ORDER = Int.MIN_VALUE + 1

        val EMPTY_VALUE = ""
        val ERROR_PLACEHOLDER = "error_placeholder"

        return listOf(
            DiiaDocumentWithMetadata(
                DocManualOptions(),
                EMPTY_VALUE,
                EMPTY_VALUE,
                Http.HTTP_200,
                ERROR_PLACEHOLDER,
                //should be after last doc
                LAST_DOC_ORDER + 1
            )
        )
    }

    override fun navigateToDocActions(
        fragment: Fragment,
        doc: DiiaDocument,
        position: Int,
        manualDocs: ManualDocs?
    ) {
        fragment.apply {
            navigateOnce(
                destination = NavDocActionsDirections.actionGlobalDestinationDocActionsCompose(
                    doc = doc,
                    position = position,
                    enableStackActions = false,
                    currentlyDisplayedOdcTypes = DocsConst.DOCUMENT_TYPE_ALL,
                    manualDocs = manualDocs,
                    resultDestinationId = currentDestinationId ?: return
                ),
                targetDestinationId = R.id.destination_docActionsCompose
            )
        }
    }

    override fun navigateToFlow(fragment: Fragment, flowCode: String) {

    }

    override fun showDocDoestNotExistTemplate(fragment: Fragment, type: String) {
    }

    companion object {
        private const val FULL_DOC_INFO_TRANSACTION_TAG = "FULL_DOC_INF"
    }
}