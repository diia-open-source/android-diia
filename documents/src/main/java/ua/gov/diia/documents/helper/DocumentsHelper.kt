package ua.gov.diia.documents.helper

import android.content.res.Resources
import android.os.Parcelable
import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.ui.DocVM
import ua.gov.diia.documents.ui.actions.DocActionsDFCompose
import ua.gov.diia.documents.ui.actions.DocActionsDFComposeArgs
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

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

    fun isDocumentValid(receivedDoc: DiiaDocumentWithMetadata): Boolean

    fun provideListOfDocumentsRequireUpdateOfExpirationDate(focusDocType: String): List<String>?

    fun showVerificationButtons(document: Parcelable): Boolean

    fun isDocRequireGeneralMenuActions(doc: Parcelable): Boolean

    fun isDocRequireHousingMenuActions(doc: Parcelable): Boolean

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
    fun handleAction(fragment: DocActionsDFCompose, action: DocAction, args: DocActionsDFComposeArgs)
    fun provideActions(document: DiiaDocument, enableStackActions: Boolean, resources: Resources): List<ListItemMlcData>?
}