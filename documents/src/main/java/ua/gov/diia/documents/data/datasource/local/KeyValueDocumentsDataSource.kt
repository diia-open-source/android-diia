package ua.gov.diia.documents.data.datasource.local

import com.squareup.moshi.JsonAdapter
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.model.PreferenceKey
import ua.gov.diia.diia_storage.store.AbstractKeyValueDataSource
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata.Companion.LAST_DOC_ORDER
import ua.gov.diia.documents.util.datasource.DateCompareExpirationStrategy
import ua.gov.diia.documents.util.datasource.ExpirationStrategy

class KeyValueDocumentsDataSource(
    override val jsonAdapter: JsonAdapter<List<DiiaDocumentWithMetadata>>,
    diiaStorage: DiiaStorage,
    private val docTransformations: List<DocumentsTransformation>,
    private val docTypesAvailableToUsers: Set<String>,
    private val expirationStrategy: ExpirationStrategy,
    private val documentsHelper: DocumentsHelper,
    private val docGroupUpdateBehaviors: List<DocGroupUpdateBehavior>,
    private val defaultDocGroupUpdateBehavior: DefaultDocGroupUpdateBehavior,
    private val brokenDocFilter: BrokenDocFilter,
    private val removeExpiredDocBehavior: RemoveExpiredDocBehavior,
    withCrashlytics: WithCrashlytics
) : AbstractKeyValueDataSource<List<DiiaDocumentWithMetadata>>(
    diiaStorage,
    withCrashlytics
) {

    override val preferenceKey: PreferenceKey = Preferences.Documents

    suspend fun fetchDocuments(): List<DiiaDocumentWithMetadata>? {
        return if (store.containsKey(preferenceKey)) {
            val data = loadData()

            return documentsHelper.migrateDocuments(data, ::saveDataToStore)
        } else null
    }

    suspend fun updateData(data: List<DiiaDocumentWithMetadata>): DataSourceDataResult<List<DiiaDocumentWithMetadata>>? {
        if (data.isEmpty()) return null

        val currentlyStoredDocuments = loadData().orEmpty()
        val docsToPersist = currentlyStoredDocuments.toMutableList()
        val existsId = mutableListOf<String?>()
        val removeList = mutableListOf<DiiaDocumentWithMetadata>()
        brokenDocFilter.filter(docsToPersist, existsId, removeList)
        docsToPersist.removeAll(removeList)
        data.groupBy { it.type }.forEach { updateDocGroup ->
            val docType = updateDocGroup.key
            val docValue = updateDocGroup.value
            val behavior =
                docGroupUpdateBehaviors.find { it.canHandleType(docType) }
                    ?: defaultDocGroupUpdateBehavior
            behavior.handleUpdate(
                docType = docType,
                docValue = docValue,
                status = updateDocGroup.value.first().status,
                docsToPersist = docsToPersist,
                existsId = existsId
            )
        }

        processDataAndSaveToStore(docsToPersist)
        return DataSourceDataResult.successful(docsToPersist)
    }

    suspend fun processExpiredData(data: List<DiiaDocumentWithMetadata>): List<DiiaDocumentWithMetadata> {
        removeExpiredDocBehavior.removeExpiredDocs(data, ::removeDocument)
        if (expirationStrategy is DateCompareExpirationStrategy) {
            expirationStrategy.reset()
        }
        var expiredData = data.filter { expirationStrategy.isExpired(it) }

        //handle case when new doc types added but client had not received it before
        val availableDocTypes = data.map { it.type }
        expiredData =
            expiredData + docTypesAvailableToUsers.filter { it !in availableDocTypes }
                .map {
                    DiiaDocumentWithMetadata(
                        status = Http.HTTP_404,
                        type = it,
                        timestamp = "",
                        expirationDate = Preferences.DEF,
                        diiaDocument = null,
                        order = LAST_DOC_ORDER
                    )
                }

        return expiredData
    }

    suspend fun removeDocument(diiaDocument: DiiaDocument): DataSourceDataResult<List<DiiaDocumentWithMetadata>>? {
        val currentData = loadData() ?: return null
        with(currentData.filter { it.diiaDocument != diiaDocument }) {
            saveDataToStore(this)
            return DataSourceDataResult.successful(this)
        }
    }

    suspend fun removeDocumentByType(type: String): DataSourceDataResult<List<DiiaDocumentWithMetadata>>? {
        val currentData = loadData() ?: return null
        with(currentData.filter { it.diiaDocument?.getItemType() != type }) {
            saveDataToStore(this)
            return DataSourceDataResult.successful(this)
        }
    }

    suspend fun updateDocument(document: DiiaDocument): DataSourceDataResult<List<DiiaDocumentWithMetadata>>? {
        val currentData = loadData()
        val newData = mutableListOf<DiiaDocumentWithMetadata>()
        var updateData = false
        currentData?.forEach { diiaDocument ->
            if (diiaDocument.diiaDocument?.docId() == document.docId()) {
                updateData = true
                newData.add(diiaDocument.copy(diiaDocument = document))
            } else {
                newData.add(diiaDocument)
            }
        }
        return if (updateData) {
            saveDataToStore(newData)
            DataSourceDataResult.successful(newData)
        } else null
    }

    suspend fun replaceExpDateByType(documentTypes: List<String>): DataSourceDataResult<List<DiiaDocumentWithMetadata>>? {
        val docList = loadData() ?: return null
        val dataToAdd = documentTypes.map { newType ->
            DiiaDocumentWithMetadata(
                status = documentsHelper.getExpiredDocStatus(newType),
                type = newType,
                timestamp = "",
                expirationDate = Preferences.DEF,
                diiaDocument = null,
                order = LAST_DOC_ORDER
            )
        }
        with(docList + dataToAdd) {
            saveDataToStore(this)
            return DataSourceDataResult.successful(this)
        }
    }

    private fun processDataAndSaveToStore(data: List<DiiaDocumentWithMetadata>) {
        docTransformations.forEach { transformation ->
            transformation.transform(data)
        }
        saveDataToStore(data)
    }

}
