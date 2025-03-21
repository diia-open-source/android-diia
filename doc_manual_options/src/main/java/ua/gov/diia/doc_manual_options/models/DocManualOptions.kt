package ua.gov.diia.doc_manual_options.models

import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata.Companion.LAST_DOC_ORDER
import ua.gov.diia.core.network.Http.HTTP_200
import ua.gov.diia.doc_manual_options.DocName.DOCUMENT_MANUAL_OPTIONS
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.core.models.document.SourceType
import ua.gov.diia.doc_manual_options.R
import java.util.UUID

@Parcelize
data class DocManualOptions(
    val empty: String = DOCUMENT_MANUAL_OPTIONS,
    val message: String = "",
    override val id: String? = UUID.randomUUID().toString()
) : DiiaDocument {

    override fun docId() = id ?: ""
    override fun getItemType() = DOCUMENT_MANUAL_OPTIONS
    override fun getDocExpirationDate(): String = DEF
    override fun getExpirationDateISO(): String = DEF
    override fun getStatus() = HTTP_200
    override fun getWeight() = Int.MAX_VALUE
    override fun getDocOrder() = LAST_DOC_ORDER
    override fun setNewOrder(newOrder: Int) {
    }

    override fun getSourceType() = SourceType.STATIC

    override fun getDocColor() = R.color.colorPrimary

    override fun getDocStackTitle() = ""
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
        const val DEF = "PREF_DEF"
    }
}