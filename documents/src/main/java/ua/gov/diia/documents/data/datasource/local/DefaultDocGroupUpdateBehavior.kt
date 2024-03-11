package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.core.network.Http
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import javax.inject.Inject

class DefaultDocGroupUpdateBehavior @Inject constructor(): DocGroupUpdateBehavior {
    override fun canHandleType(type: String) = true

    override fun handleUpdate(
        docType: String,
        docValue: List<DiiaDocumentWithMetadata>,
        status: Int,
        docsToPersist: MutableList<DiiaDocumentWithMetadata>,
        existsId: List<String?>
    ) {
        when (status) {
            Http.HTTP_200, Http.HTTP_404 -> {
                docsToPersist.removeAll { it.type == docType }
                docsToPersist.addAll(docValue)
            }
            Http.COVID_CERT_IN_PROGRESS_STATUS -> {
                docsToPersist.apply {
                    addAll(docValue.map {
                        it.copy(expirationDate = Preferences.DEF)
                    })
                }
            }
            else -> {
                val docs = docsToPersist.filter { it.type == docType }
                if (docs.isEmpty()) {
                    docsToPersist.addAll(docValue)
                } else {
                    docs.forEach {
                        it.expirationDate = docValue.first().expirationDate
                        if (status != Http.HTTP_403) {
                            it.status = status
                        }
                    }
                }
            }
        }
    }
}