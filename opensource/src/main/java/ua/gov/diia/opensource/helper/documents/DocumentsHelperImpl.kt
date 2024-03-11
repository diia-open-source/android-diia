package ua.gov.diia.opensource.helper.documents

import android.content.res.Resources
import android.os.Parcelable
import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.doc_driver_license.DriverLicenseV2
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.ui.DocVM
import ua.gov.diia.documents.ui.actions.DocActionsDFCompose
import ua.gov.diia.documents.ui.actions.DocActionsDFComposeArgs
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import javax.inject.Inject

class DocumentsHelperImpl @Inject constructor(): DocumentsHelper {

    override fun isDocCanBeBroken(docType: String): Boolean = false

    override fun getExpiredDocStatus(docType: String): Int = Http.NEED_UPDATE_STATUS

    override suspend fun migrateDocuments(
        data: List<DiiaDocumentWithMetadata>?,
        shouldSaveData: (data: List<DiiaDocumentWithMetadata>) -> Unit
    ): List<DiiaDocumentWithMetadata>? = data

    override fun isDocEligibleForDeletion(docType: String): Boolean = false

    override fun isDocumentValid(receivedDoc: DiiaDocumentWithMetadata): Boolean {
        val doc = receivedDoc.diiaDocument
        return !(doc is DriverLicenseV2.Data && doc.getStatus() == DriverLicenseV2.STATUS_IS_INVALID)
    }

    override fun provideListOfDocumentsRequireUpdateOfExpirationDate(focusDocType: String): List<String>? = null

    override fun showVerificationButtons(document: Parcelable): Boolean {
        return when (document) {
            is DriverLicenseV2.Data -> true
            else -> false
        }
    }

    override fun isDocRequireGeneralMenuActions(doc: Parcelable): Boolean {
        return when (doc) {
            is DriverLicenseV2.Data -> true
            else -> false
        }
    }

    override fun isDocRequireHousingMenuActions(doc: Parcelable): Boolean = false

    override fun getStackHeader(fragment: Fragment, docType: String): String {
        with(fragment) {
            return when (docType) {
                DocName.DRIVER_LICENSE -> getString(R.string.driver_license)
                else -> ""
            }.replace('\n', ' ')
        }
    }

    override fun navigateToRatingService(
        fragment: Fragment,
        viewModel: DocVM,
        form: RatingFormModel,
        isFromStack: Boolean
    ) = Unit

    override fun navigateToStackDocs(fragment: Fragment, doc: DiiaDocument) {
        with(fragment){
            navigate(
                NavMainXmlDirections.actionGlobalToStackFCompose(doc.getItemType(), doc.getDocColor()),
                findNavControllerById(R.id.nav_host),
            )
        }
    }

    override fun navigateToDocOrder(fragment: Fragment) {
        with(fragment){
            navigate(NavMainXmlDirections.actionGlobalToStackOrder())
        }
    }

    override fun handleAction(
        fragment: DocActionsDFCompose,
        action: DocAction,
        args: DocActionsDFComposeArgs
    ) {

    }

    override fun provideActions(
        document: DiiaDocument,
        enableStackActions: Boolean,
        resources: Resources
    ): List<ListItemMlcData>?  = null
}