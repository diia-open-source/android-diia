package ua.gov.diia.documents

import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.network.Http
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.core.models.document.SourceType
import java.util.UUID

@Parcelize
data class DocTest(
    val empty: String = DOCUMENT_TEST,
    val message: String = "",
    override val id: String? = "123"
) : DiiaDocument {

    override fun docId() = id ?: ""
    override fun getItemType() = DOCUMENT_TEST
    override fun getDocExpirationDate(): String = Preferences.DEF
    override fun getExpirationDateISO(): String = Preferences.DEF
    override fun getStatus() = Http.HTTP_200
    override fun getWeight() = Int.MAX_VALUE
    override fun getDocOrder() = DiiaDocumentWithMetadata.LAST_DOC_ORDER
    override fun setNewOrder(newOrder: Int) {
    }
    override fun getSourceType() = SourceType.DYNAMIC
    override fun getDocStackTitle() = ""

    override fun getDocColor() = R.color.colorPrimary
    override fun getDocNum() = id
    override fun makeCopy(): DiiaDocument = this.copy()
    override fun verificationCodesCount() = 1
    override fun getPersonName(): String = ""
    override fun getDisplayDate(): String = ""
    override fun birthCertificateId() = ""
    override fun localization(): LocalizationType = LocalizationType.ua
    override fun setLocalization(code: LocalizationType) {}
    override fun getDocName() = "test"
    override fun getDocOrderDescription() = ""
    override fun getDocOrderLabel() = ""

    companion object {
        private const val DOCUMENT_TEST = "doc_test"
    }
}

@Parcelize
data class DocTest2(
    val empty: String = DOCUMENT_TEST2,
    val message: String = "",
    override val id: String? = UUID.randomUUID().toString()
) : DiiaDocument {

    override fun docId() = id ?: ""
    override fun getItemType() = DOCUMENT_TEST2
    override fun getDocExpirationDate(): String = Preferences.DEF
    override fun getExpirationDateISO(): String = Preferences.DEF
    override fun getStatus() = Http.HTTP_200
    override fun getWeight() = Int.MAX_VALUE
    override fun getDocOrder() = DiiaDocumentWithMetadata.LAST_DOC_ORDER
    override fun setNewOrder(newOrder: Int) {
    }

    override fun getSourceType() = SourceType.DYNAMIC

    override fun getDocStackTitle() = ""


    override fun getDocColor() = R.color.colorPrimary
    override fun getDocNum() = id
    override fun makeCopy(): DiiaDocument = this.copy()
    override fun verificationCodesCount() = 1
    override fun getPersonName(): String = ""
    override fun getDisplayDate(): String = ""
    override fun birthCertificateId() = ""
    override fun localization(): LocalizationType = LocalizationType.ua
    override fun setLocalization(code: LocalizationType) {}
    override fun getDocName() = "test2"
    override fun getDocOrderDescription() = ""
    override fun getDocOrderLabel() = "test"

    companion object {
        private const val DOCUMENT_TEST2 = "doc_test2"
    }
}

@Parcelize
data class DocTest3(
    val empty: String = DOCUMENT_TEST3,
    val message: String = "",
    override val id: String? = UUID.randomUUID().toString()
) : DiiaDocument {

    override fun docId() = id ?: ""
    override fun getItemType() = DOCUMENT_TEST3
    override fun getDocExpirationDate(): String = Preferences.DEF
    override fun getExpirationDateISO(): String = Preferences.DEF
    override fun getStatus() = Http.HTTP_200
    override fun getWeight() = Int.MAX_VALUE
    override fun getDocOrder() = DiiaDocumentWithMetadata.LAST_DOC_ORDER
    override fun setNewOrder(newOrder: Int) {
    }

    override fun getSourceType() = SourceType.DYNAMIC

    override fun getDocStackTitle() = ""


    override fun getDocColor() = R.color.colorPrimary
    override fun getDocNum() = id
    override fun makeCopy(): DiiaDocument = this.copy()
    override fun verificationCodesCount() = 1
    override fun getPersonName(): String = ""
    override fun getDisplayDate(): String = ""
    override fun birthCertificateId() = ""
    override fun localization(): LocalizationType = LocalizationType.ua
    override fun setLocalization(code: LocalizationType) {}
    override fun getDocName() = "test3"
    override fun getDocOrderDescription() = ""
    override fun getDocOrderLabel() = ""

    companion object {
        private const val DOCUMENT_TEST3 = "doc_test3"
    }
}

@Parcelize
data class DocTest4(
    val empty: String = DOCUMENT_TEST4,
    val message: String = "",
    override val id: String? = UUID.randomUUID().toString()
) : DiiaDocument {

    override fun docId() = id ?: ""
    override fun getItemType() = DOCUMENT_TEST4
    override fun getDocExpirationDate(): String = Preferences.DEF
    override fun getExpirationDateISO(): String = Preferences.DEF
    override fun getStatus() = Http.HTTP_200
    override fun getWeight() = Int.MAX_VALUE
    override fun getDocOrder() = DiiaDocumentWithMetadata.LAST_DOC_ORDER
    override fun setNewOrder(newOrder: Int) {
    }
    override fun getSourceType() = SourceType.DYNAMIC
    override fun getDocStackTitle() = ""
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
        private const val DOCUMENT_TEST4 = "doc_test4"
    }
}