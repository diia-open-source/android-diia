package ua.gov.diia.documents.helper

import android.os.Parcelable
import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.ui.DocVM

interface DocumentsHelper {

    fun isDocCanBeBroken(docType: String): Boolean

    fun getExpiredDocStatus(docType: String): Int

    /**
     * Leave empty if no migration needed
     */
    suspend fun migrateDocuments(
        data: List<DiiaDocumentWithMetadata>?,
        shouldSaveData: (data: List<DiiaDocumentWithMetadata>) -> Unit): List<DiiaDocumentWithMetadata>?

    fun isDocEligibleForDeletion(docType: String): Boolean

    suspend fun loadDocImageInByteArray(docId: String): ShareByteArr?

    suspend fun addDocToGallery(): TemplateDialogModel?

    fun isDocumentValid(receivedDoc: DiiaDocumentWithMetadata): Boolean

    fun provideListOfDocumentsRequireUpdateOfExpirationDate(focusDocType: String): List<String>?

    fun showVerificationButtons(document: Parcelable): Boolean

    fun isDocRequireGeneralMenuActions(doc: Parcelable): Boolean

    fun getStackHeader(fragment: Fragment, docType: String): String

    /**
     * performs navigation to RatingService
     */
    fun navigateToRatingService(fragment: Fragment,
                                viewModel: DocVM,
                                form: RatingFormModel,
                                isFromStack: Boolean = false
    )

    /**
     * performs navigation to StackDocs
     */
    fun navigateToStackDocs(fragment: Fragment, doc: DiiaDocument)
    /**
     * performs navigation to DocOrder
     */
    fun navigateToDocOrder(fragment: Fragment)

    fun onTickerClick(fragment: Fragment, doc: Parcelable)

    fun getBaseDocumentsList(): List<DiiaDocumentWithMetadata>
}