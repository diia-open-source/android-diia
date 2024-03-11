package ua.gov.diia.documents.models

import kotlinx.parcelize.Parcelize
import ua.gov.diia.documents.R
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata.Companion.LAST_DOC_ORDER
import ua.gov.diia.core.network.Http.HTTP_200
import ua.gov.diia.diia_storage.store.Preferences
import java.util.UUID

@Parcelize
data class DocError(
    val empty: String = DOCUMENT_ERROR,
    val message: String = "",
    override val id: String? = UUID.randomUUID().toString()
) : DiiaDocument {

    override fun docId() = id ?: ""
    override fun getItemType() = DOCUMENT_ERROR
    override fun getDocExpirationDate(): String = Preferences.DEF
    override fun getExpirationDateISO(): String = Preferences.DEF
    override fun getStatus() = HTTP_200
    override fun getWeight() = Int.MAX_VALUE
    override fun getDocOrder() = LAST_DOC_ORDER
    override fun setNewOrder(newOrder: Int) {
    }
    override fun getDocColor() = R.color.colorPrimary
    override fun getDocNum() = id
    override fun makeCopy(): DiiaDocument = this.copy()
    override fun verificationCodesCount() = 1
    override fun getPersonName(): String = ""
    override fun getDisplayDate(): String = ""
    override fun birthCertificateId() = ""
    override fun localization(): LocalizationType = LocalizationType.ua
    override fun setLocalization(code: LocalizationType) {}
    override fun getDocName() = ""
    override fun getDocOrderDescription() = ""
    override fun getDocOrderLabel() = ""

    companion object {
        private const val DOCUMENT_ERROR = "doc_error"
    }
}