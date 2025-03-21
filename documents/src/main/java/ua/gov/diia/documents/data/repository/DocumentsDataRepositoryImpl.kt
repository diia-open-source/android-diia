package ua.gov.diia.documents.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.SourceType
import ua.gov.diia.core.models.document.WithTimestamp
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.data.datasource.local.KeyValueDocumentsDataSource
import ua.gov.diia.documents.data.datasource.remote.NetworkDocumentsDataSource
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.*

class DocumentsDataRepositoryImpl(
    private val scope: CoroutineScope,
    private val keyValueDataSource: KeyValueDocumentsDataSource,
    private val networkDocumentsDataSource: NetworkDocumentsDataSource,
    private val beforePublishActions: List<BeforePublishAction>,
    private val docTypesAvailableToUsers: Set<String>,
    private val withCrashlytics: WithCrashlytics,
    private val documentsHelper: DocumentsHelper
) : DocumentsDataRepository, WithCrashlytics by withCrashlytics {

    private val _isDataLoading = MutableStateFlow(false)
    override val isDataLoading: Flow<Boolean>
        get() = _isDataLoading

    private val _data = MutableSharedFlow<DataSourceDataResult<List<DiiaDocumentWithMetadata>>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val data: Flow<DataSourceDataResult<List<DiiaDocumentWithMetadata>>>
        get() = _data

    private val baseDocumentList = documentsHelper.getBaseDocumentsList()


    private var invalidateDocJob: Job? = null

    override fun invalidate() {
        if (invalidateDocJob?.isActive == true) {
            return
        }

        invalidateDocJob = scope.launch {
            _isDataLoading.value = true
            val cachedDocuments = keyValueDataSource.fetchDocuments()
            if (cachedDocuments != null) {
                emitData(DataSourceDataResult.successful(cachedDocuments))
                _isDataLoading.value = false

                val result = processExpiredData(cachedDocuments)
                if (!result.isNullOrEmpty()) {
                    emitData(DataSourceDataResult.successful(result))
                    _isDataLoading.value = false

                }
            } else {
                _isDataLoading.value = true
                emitData(DataSourceDataResult.successful(baseDocumentList))
                val networkData = networkDocumentsDataSource.fetchData(docTypesAvailableToUsers)
                if (networkData.isSuccessful) {
                    networkData.data?.let {
                        emitData(keyValueDataSource.updateData(it))
                    }
                }
                _isDataLoading.value = false
            }
        }
    }

    override fun updateDocOrder(docTypeList: List<String>) {
        scope.launch { updateOrder(docTypeList) }
    }

    override fun attachExternalDocument(document: DiiaDocumentWithMetadata) {
        scope.launch {
            loadLocalDocData()
                ?.filter { it.type == document.type && it.status == document.status && it.id != document.id }
                ?.toMutableList()
                ?.apply { add(document) }
                ?.also { data -> emitData(keyValueDataSource.updateData(data)) }
        }
    }

    override fun removeDocument(diiaDocument: DiiaDocument) {
        scope.launch {
            emitData(keyValueDataSource.removeDocument(diiaDocument))
        }
    }

    override fun removeDocumentByType(type: String) {
        scope.launch {
            emitData(keyValueDataSource.removeDocumentByType(type))
        }
    }

    override fun updateDocument(diiaDocument: DiiaDocument) {
        scope.launch {
            emitData(keyValueDataSource.updateDocument(diiaDocument))
        }
    }

    override fun replaceExpDateByType(types: List<String>) {
        scope.launch {
            emitData(keyValueDataSource.replaceExpDateByType(types))
        }
    }

    override suspend fun getDocsByType(type: String) = loadLocalDocData()
        ?.filter { it.type == type }
        ?.map { it.diiaDocument }

    override suspend fun getETagForDocType(type: String) =
        loadLocalDocData()?.firstOrNull { it.type == type }?.eTag.orEmpty()

    override fun saveDocTypeOrder(docOrders: List<DocOrder>) {
        scope.launch {
            if (docOrders.isEmpty()) return@launch

            val docsList = loadLocalDocData()
                ?.filter { it.diiaDocument?.getSourceType() != SourceType.STATIC }
                ?: return@launch

            docOrders.forEach { docOrder ->
                docsList.updateDocOrderByType(docOrder)
            }
            keyValueDataSource.updateData(docsList)

            networkDocumentsDataSource.setDocumentsOrder(DocumentsOrder(docOrders))
        }
    }

    override fun saveDocOrderForSpecificType(
        docOrders: List<TypeDefinedDocOrder>,
        docType: String,
    ) {
        scope.launch {
            if (docOrders.isEmpty()) return@launch
            val docsList = loadLocalDocData()
                ?.filter { it.diiaDocument != null && (it.diiaDocument as DiiaDocument).getSourceType() != SourceType.STATIC }
                ?: return@launch

            val documentsFilteredByType = docsList.filter { it.type == docType }
            docOrders.forEach { docOrder ->
                documentsFilteredByType
                    .find { it.diiaDocument?.getDocNum() == docOrder.docNumber }
                    ?.diiaDocument?.setNewOrder(docOrder.order)
            }

            emitData(keyValueDataSource.updateData(docsList))

            networkDocumentsDataSource.saveDocOrderForSpecificType(
                documentType = docType,
                docOrder = TypeDefinedDocumentsOrder(docOrders)
            )
        }
    }

    override suspend fun loadLocalDocData() = keyValueDataSource.loadData()

    override suspend fun saveDocsToStorage(data: List<DiiaDocumentWithMetadata>) {
        keyValueDataSource.updateData(data)
        invalidate()
    }

    override suspend fun transformDocumentToEngagedCert(type: String, dateIssued: String) {

        val rnkoppNum = getDocsByType("taxpayer-card")?.firstOrNull()?.getDocNum()

        val title = when {
            rnkoppNum == null || rnkoppNum.dropLast(1).toLong() % 2 != 0L-> "Сертифікат зарученого"
            else -> "Сертифікат зарученої"
        }

        val document = getDocsByType(type)?.firstOrNull()
        document?.let { oldDoc ->
            val newDoc = oldDoc.makeCopy(title, dateIssued)
            removeDocument(oldDoc)
            val newDocWithMetadata = DiiaDocumentWithMetadata(
                newDoc,
                if (newDoc is WithTimestamp) {
                    newDoc.getTimestamp()
                } else {
                    ""
                },
                "2025-11-28T10:17:58.057Z",
                newDoc.getStatus(),
                newDoc.getItemType(),
                newDoc.getDocOrder()
            )
            attachExternalDocument(newDocWithMetadata)
        }
    }

    override suspend fun clear() = emitData(DataSourceDataResult.successful(emptyList()))

    private fun List<DiiaDocumentWithMetadata>.updateDocOrderByType(docOrder: DocOrder) {
        filter { it.type == docOrder.documentType }.forEach { it.order = docOrder.order }
    }

    private suspend fun updateExpiredData(docTypes: Set<String>): List<DiiaDocumentWithMetadata> {
        val docFetchResult = networkDocumentsDataSource.fetchDocsWithTypes(docTypes)
        if (docFetchResult.isSuccessful) {
            updateOrder(docFetchResult.docOrder)
        }

        return docFetchResult.documents
    }

    private suspend fun emitData(dataResult: DataSourceDataResult<List<DiiaDocumentWithMetadata>>?) {
        val data = dataResult?.data
        if (dataResult?.isSuccessful == true && data != null) {
            _data.tryEmit(DataSourceDataResult.successful(prepareDataToPublish(data)))
        }
    }

    private suspend fun prepareDataToPublish(data: List<DiiaDocumentWithMetadata>): List<DiiaDocumentWithMetadata> {
        beforePublishActions.forEach { action ->
            action.perform(data)
        }
        return baseDocumentList + data
    }

    private suspend fun processExpiredData(data: List<DiiaDocumentWithMetadata>): List<DiiaDocumentWithMetadata>? {
        with(keyValueDataSource) {
            val expireData = processExpiredData(data)
            val expiredList = expireData.map { it.type }.toSet()
            if (expiredList.isNotEmpty()) {
                updateData(updateExpiredData(expiredList))
                return loadLocalDocData()
            }
        }
        return null
    }

    private suspend fun updateOrder(docTypeList: List<String>) {
        try {
            val data = loadLocalDocData() ?: return
            var dataUpdated = false
            data.filter { it.diiaDocument?.getSourceType() != SourceType.STATIC }
                .forEach { doc ->
                    val newOrder = docTypeList.indexOf(doc.type) + 1
                    if (doc.order != newOrder) {
                        dataUpdated = true
                        doc.setNewOrder(newOrder)
                    }
                }

            if (dataUpdated) {
                keyValueDataSource.updateData(data)
            }
        } catch (e: Exception) {
            sendNonFatalError(e)
        }
    }

}