package ua.gov.diia.opensource.helper.documents

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.CommonPreferenceKeys
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.models.DiiaDocumentGroup
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DiiaDocumentsWithOrder
import ua.gov.diia.documents.models.DocumentsOrder
import ua.gov.diia.documents.models.ManualDocs
import ua.gov.diia.documents.models.TypeDefinedDocumentsOrder
import ua.gov.diia.opensource.data.network.api.ApiDocs
import ua.gov.diia.opensource.model.documents.Docs
import javax.inject.Inject

class ApiDocumentsWrapper @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs,
    private val diiaStorage: DiiaStorage,
    private val currentDateProvider: CurrentDateProvider,
    private val withCrashlytics: WithCrashlytics
) : ApiDocuments, WithCrashlytics by withCrashlytics {

    override suspend fun fetchDocuments(docTypes: Map<String, String>): List<DiiaDocumentWithMetadata> {
        val updatedDocs = loadDocs(docTypes)
        return docsToDocumentWithMetadataList(updatedDocs)
    }

    override suspend fun fetchDocumentsWithTypes(docTypes: Map<String, String>): DiiaDocumentsWithOrder {
        val updatedDocs = loadDocs(docTypes)
        val data = docsToDocumentWithMetadataList(updatedDocs)
        return DiiaDocumentsWithOrder(
            documents = data,
            docOrder = updatedDocs.documentsTypeOrder
        )
    }

    override suspend fun setDocumentsOrder(docOrder: DocumentsOrder) {
        apiDocs.setDocumentsOrder(docOrder = docOrder)
    }

    override suspend fun setTypedDocumentsOrder(
        documentType: String,
        docOrder: TypeDefinedDocumentsOrder
    ) {
        apiDocs.setTypedDocumentsOrder(
            documentType = documentType,
            docOrder = docOrder
        )
    }

    override suspend fun getDocsManual(): ManualDocs {
       return apiDocs.getDocsManual()
    }

    private suspend fun loadDocs(map: Map<String, String> = emptyMap()): Docs {
        val originalDocs = apiDocs.getDocs(map)
        diiaStorage.set(
            CommonPreferenceKeys.LastDocumentUpdate,
            DateFormats.iso8601.format(currentDateProvider.getDate())
        )
        return originalDocs.copy(
            documentsTypeOrder = originalDocs.documentsTypeOrder
        )
    }

    private fun docsToDocumentWithMetadataList(docs: Docs): List<DiiaDocumentWithMetadata> {
        val docsWithMetadata = mutableListOf<DiiaDocumentWithMetadata>()
        docs.driverLicense?.let {
            docsWithMetadata.addAll(groupToDocumentsWithMetadata(it, docs))
        }
        return docsWithMetadata
    }

    private fun groupToDocumentsWithMetadata(
        diiaDocumentGroup: DiiaDocumentGroup<*>,
        docs: Docs,
    ): List<DiiaDocumentWithMetadata> {
        if (diiaDocumentGroup.getData().isEmpty()) {
            return listOf(
                DiiaDocumentWithMetadata(
                    null,
                    docs.getTimestamp(),
                    diiaDocumentGroup.getDocExpirationDate(),
                    diiaDocumentGroup.getStatus(),
                    diiaDocumentGroup.getItemType(),
                    DiiaDocumentWithMetadata.LAST_DOC_ORDER
                )
            )
        } else {
            return diiaDocumentGroup.getData().mapIndexed { i, data ->
                DiiaDocumentWithMetadata(
                    data?.apply { setNewOrder(i) },
                    diiaDocumentGroup.getTimestamp(),
                    diiaDocumentGroup.getDocExpirationDate(),
                    diiaDocumentGroup.getStatus(),
                    diiaDocumentGroup.getItemType(),
                    docs.documentsTypeOrder.indexOf(diiaDocumentGroup.getItemType()) + 1
                )
            }
        }
    }
}