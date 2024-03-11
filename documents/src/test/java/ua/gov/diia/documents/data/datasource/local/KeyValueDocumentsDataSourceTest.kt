package ua.gov.diia.documents.data.datasource.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.rules.MainDispatcherRule
import ua.gov.diia.documents.util.datasource.DateCompareExpirationStrategy
import ua.gov.diia.documents.util.datasource.ExpirationStrategy

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class KeyValueDocumentsDataSourceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var diiaStorage: DiiaStorage
    @Mock
    lateinit var jsonAdapter: JsonAdapter<List<DiiaDocumentWithMetadata>>
    @Mock
    lateinit var documentsTransformation: DocumentsTransformation

    lateinit var docTransformations: MutableList<DocumentsTransformation>

    lateinit var docTypesAvailableToUsers: MutableSet<String>
    @Mock
    lateinit var expirationStrategy: ExpirationStrategy

    lateinit var docGroupUpdateBehaviors: MutableList<DocGroupUpdateBehavior>
    @Mock
    lateinit var defaultDocGroupUpdateBehavior: DefaultDocGroupUpdateBehavior
    @Mock
    lateinit var brokenDocFilter: BrokenDocFilter
    @Mock
    lateinit var removeExpiredDocBehavior: RemoveExpiredDocBehavior
    @Mock
    lateinit var withCrashlytics: WithCrashlytics
    @Mock
    lateinit var documentsHelper: DocumentsHelper

    lateinit var keyValueDocumentsDataSource: KeyValueDocumentsDataSource

    @Before
    fun setUp() {
        docGroupUpdateBehaviors = mutableListOf()
        docTransformations = mutableListOf()
        docTypesAvailableToUsers = mutableSetOf()
        docTransformations.add(documentsTransformation)
        keyValueDocumentsDataSource = KeyValueDocumentsDataSource(jsonAdapter, diiaStorage, docTransformations, docTypesAvailableToUsers, expirationStrategy, documentsHelper, docGroupUpdateBehaviors, defaultDocGroupUpdateBehavior, brokenDocFilter, removeExpiredDocBehavior, withCrashlytics)
    }

    @Test
    fun `test fetchDocuments load data from storage`() = runTest {
        val list = mutableListOf<DiiaDocumentWithMetadata>()
        list.add(mock(DiiaDocumentWithMetadata::class.java))
        val storeData = "storeData"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)
        `when`(documentsHelper.migrateDocuments(any(), any())).thenReturn(list)
        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)

        assertEquals(list, keyValueDocumentsDataSource.fetchDocuments())

        verify(diiaStorage, times(2)).containsKey(Preferences.Documents)
        verify(diiaStorage, times(1)).getString(Preferences.Documents, Preferences.DEF)
        verify(jsonAdapter, times(1)).fromJson(storeData)
        verify(documentsHelper, times(1)).migrateDocuments(any(), any())
    }

    @Test
    fun `test fetchDocuments not load data from storage if storage not contain it`() = runTest {
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(false)

        assertNull(keyValueDocumentsDataSource.fetchDocuments())

        verify(diiaStorage, times(1)).containsKey(Preferences.Documents)
        verify(diiaStorage, times(0)).getString(any(), any())
        verify(jsonAdapter, times(0)).fromJson(anyString())
        verify(documentsHelper, times(0)).migrateDocuments(any(), any())
    }

    @Test
    fun `test updateData`() = runTest {
        val docType = "docType"
        val itemMetadata = mock(DiiaDocumentWithMetadata::class.java)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        list.add(itemMetadata)
        val storeData = "storeData"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)
        `when`(jsonAdapter.toJson(any())).thenReturn("list")
        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)

        val passItem = mock(DiiaDocumentWithMetadata::class.java)
        `when`(passItem.type).thenReturn(docType)
        `when`(passItem.status).thenReturn(200)
        val data = mutableListOf<DiiaDocumentWithMetadata>()
        data.add(passItem)
        val result = keyValueDocumentsDataSource.updateData(data)

        assertEquals(true, result!!.isSuccessful)
        assertEquals(list, result.data)
        verify(brokenDocFilter, times(1)).filter(any(), any(), any())
        verify(documentsTransformation, times(1)).transform(list)

        verify(defaultDocGroupUpdateBehavior, times(1)).handleUpdate(docType, listOf(passItem), 200, list, listOf())
    }

    @Test
    fun `test updateData uses behavior from list if it can handle it`() = runTest {
        val behavior = mock(DocGroupUpdateBehavior::class.java)
        `when`(behavior.canHandleType(any())).thenReturn(true)
        docGroupUpdateBehaviors.add(behavior)

        val docType = "docType"
        val itemMetadata = mock(DiiaDocumentWithMetadata::class.java)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        list.add(itemMetadata)
        val storeData = "storeData"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)
        `when`(jsonAdapter.toJson(any())).thenReturn("list")
        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)

        val passItem = mock(DiiaDocumentWithMetadata::class.java)
        `when`(passItem.type).thenReturn(docType)
        `when`(passItem.status).thenReturn(200)
        val data = mutableListOf<DiiaDocumentWithMetadata>()
        data.add(passItem)

        val result = keyValueDocumentsDataSource.updateData(data)

        assertEquals(true, result!!.isSuccessful)

        verify(defaultDocGroupUpdateBehavior, times(0)).handleUpdate(any(), any(), any(), any(), any())
        verify(behavior, times(1)).handleUpdate(docType, listOf(passItem), 200, list, listOf())

        docGroupUpdateBehaviors.remove(behavior)
    }

    @Test
    fun `test updateData returns null if empty docs list was passed`() = runTest {
        val list = mutableListOf<DiiaDocumentWithMetadata>()

        val result = keyValueDocumentsDataSource.updateData(list)

        assertNull(result)
    }

    @Test
    fun `test processExpiredData`() = runTest {
        val availableDoc = "driver_licence"
        docTypesAvailableToUsers.add(availableDoc)
        val itemMetadata = mock(DiiaDocumentWithMetadata::class.java)
        `when`(itemMetadata.type).thenReturn("document")
        `when`(expirationStrategy.isExpired(itemMetadata)).thenReturn(true)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        list.add(itemMetadata)

        val result = keyValueDocumentsDataSource.processExpiredData(list)

        verify(removeExpiredDocBehavior, times(1)).removeExpiredDocs(any(), any())
        verify(expirationStrategy, times(1)).isExpired(itemMetadata)
        assertEquals(2, result.size)
        assertEquals(itemMetadata, result[0])

        assertEquals(availableDoc, result[1].type)
        assertEquals(Http.HTTP_404, result[1].status)
        assertEquals(Preferences.DEF, result[1].expirationDate)
        assertEquals(null, result[1].diiaDocument)
        assertEquals("", result[1].timestamp)
        assertEquals(DiiaDocumentWithMetadata.LAST_DOC_ORDER, result[1].order)

        docTypesAvailableToUsers.remove(availableDoc)
    }

    @Test
    fun `test processExpiredData not add availeble document if it has the same type`() = runTest {
        val availableDoc = "document"
        docTypesAvailableToUsers.add(availableDoc)
        val itemMetadata = mock(DiiaDocumentWithMetadata::class.java)
        `when`(itemMetadata.type).thenReturn("document")
        `when`(expirationStrategy.isExpired(itemMetadata)).thenReturn(true)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        list.add(itemMetadata)

        val result = keyValueDocumentsDataSource.processExpiredData(list)

        assertEquals(list, result)

        docTypesAvailableToUsers.remove(availableDoc)
    }

    @Test
    fun `test processExpiredData reset strategy if it is DateCompareExpirationStrategy`() = runTest {
        val dateCompareExpirationStrategy: DateCompareExpirationStrategy = mock(DateCompareExpirationStrategy::class.java)
        keyValueDocumentsDataSource = KeyValueDocumentsDataSource(jsonAdapter, diiaStorage, docTransformations, docTypesAvailableToUsers, dateCompareExpirationStrategy, documentsHelper, docGroupUpdateBehaviors, defaultDocGroupUpdateBehavior, brokenDocFilter, removeExpiredDocBehavior, withCrashlytics)

        val itemMetadata = mock(DiiaDocumentWithMetadata::class.java)
        `when`(itemMetadata.type).thenReturn("document")

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        list.add(itemMetadata)

        keyValueDocumentsDataSource.processExpiredData(list)

        verify(dateCompareExpirationStrategy, times(1)).reset()
    }

    @Test
    fun `test removeDocument`() = runTest {
        val diiaDocument: DiiaDocument = mock(DiiaDocument::class.java)
        val diiaDocument2: DiiaDocument = mock(DiiaDocument::class.java)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        val docWithMetadata = mock(DiiaDocumentWithMetadata::class.java)
        `when`(docWithMetadata.diiaDocument).thenReturn(diiaDocument)

        val docWithMetadata2 = mock(DiiaDocumentWithMetadata::class.java)
        `when`(docWithMetadata2.diiaDocument).thenReturn(diiaDocument2)
        list.add(docWithMetadata)
        list.add(docWithMetadata2)
        val storeData = "storeData"
        val jsonList = "jsonList"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)

        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)
        `when`(jsonAdapter.toJson(listOf(docWithMetadata2))).thenReturn(jsonList)

        val result = keyValueDocumentsDataSource.removeDocument(diiaDocument)

        assertEquals(true, result!!.isSuccessful)
        assertEquals(listOf(docWithMetadata2), result.data)
        verify(jsonAdapter, times(1)).toJson(listOf(docWithMetadata2))
        verify(diiaStorage, times(1)).set(Preferences.Documents, jsonList)
    }

    @Test
    fun `test removeDocument return null if storage is empty`() = runTest {
        val diiaDocument: DiiaDocument = mock(DiiaDocument::class.java)

        val result = keyValueDocumentsDataSource.removeDocument(diiaDocument)

        assertNull(result)
    }

    @Test
    fun `test updateDocument`() = runTest {
        //GIVEN
        val copiedDocWithMetadata = mock(DiiaDocumentWithMetadata::class.java)
        val docId = "docId"
        val diiaDocumentToUpdate: DiiaDocument = mock(DiiaDocument::class.java)
        val diiaDocument: DiiaDocument = mock(DiiaDocument::class.java)
        `when`(diiaDocument.docId()).thenReturn(docId)
        `when`(diiaDocumentToUpdate.docId()).thenReturn(docId)
        val diiaDocument2: DiiaDocument = mock(DiiaDocument::class.java)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        val docWithMetadata = mock(DiiaDocumentWithMetadata::class.java)

        `when`(docWithMetadata.diiaDocument).thenReturn(diiaDocument)
        `when`(docWithMetadata.copy(diiaDocument = diiaDocumentToUpdate)).thenReturn(copiedDocWithMetadata)

        val docWithMetadata2 = mock(DiiaDocumentWithMetadata::class.java)
        `when`(docWithMetadata2.diiaDocument).thenReturn(diiaDocument2)
        list.add(docWithMetadata)
        list.add(docWithMetadata2)
        val storeData = "storeData"
        val jsonList = "jsonList"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)

        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)
        `when`(jsonAdapter.toJson(listOf(copiedDocWithMetadata, docWithMetadata2))).thenReturn(jsonList)

        //WHEN
        val result = keyValueDocumentsDataSource.updateDocument(diiaDocumentToUpdate)

        //THEN
        assertEquals(true, result!!.isSuccessful)
        assertEquals(listOf(copiedDocWithMetadata, docWithMetadata2), result.data)
        verify(jsonAdapter, times(1)).toJson(listOf(copiedDocWithMetadata, docWithMetadata2))
        verify(diiaStorage, times(1)).set(Preferences.Documents, jsonList)
    }

    @Test
    fun `test updateDocument returns null if no data to update`() = runTest {
        //GIVEN
        val docId = "docId"
        val diiaDocumentToUpdate: DiiaDocument = mock(DiiaDocument::class.java)
        val diiaDocument: DiiaDocument = mock(DiiaDocument::class.java)
        `when`(diiaDocument.docId()).thenReturn(docId)
        `when`(diiaDocumentToUpdate.docId()).thenReturn("docId2")
        val diiaDocument2: DiiaDocument = mock(DiiaDocument::class.java)

        val list = mutableListOf<DiiaDocumentWithMetadata>()
        val docWithMetadata = mock(DiiaDocumentWithMetadata::class.java)

        `when`(docWithMetadata.diiaDocument).thenReturn(diiaDocument)

        val docWithMetadata2 = mock(DiiaDocumentWithMetadata::class.java)
        `when`(docWithMetadata2.diiaDocument).thenReturn(diiaDocument2)
        list.add(docWithMetadata)
        list.add(docWithMetadata2)
        val storeData = "storeData"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)

        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)

        //WHEN
        val result = keyValueDocumentsDataSource.updateDocument(diiaDocumentToUpdate)

        //THEN
        assertNull(result)
    }

    @Test
    fun `test replaceExpDateByType`() = runTest {
        //GIVEN
        val list = mutableListOf<DiiaDocumentWithMetadata>()
        val docWithMetadata = mock(DiiaDocumentWithMetadata::class.java)
        val docWithMetadata2 = mock(DiiaDocumentWithMetadata::class.java)
        list.add(docWithMetadata)
        list.add(docWithMetadata2)
        val storeData = "storeData"
        val jsonList = "jsonList"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)

        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)
        `when`(jsonAdapter.fromJson(storeData)).thenReturn(list)
        `when`(jsonAdapter.toJson(any())).thenReturn(jsonList)

        val processNum = 10
        val newType = "newType"
        val documentTypes = mutableListOf<String>()
        documentTypes.add(newType)
        `when`(documentsHelper.getExpiredDocStatus(newType)).thenReturn(processNum)

        //WHEN
        val result = keyValueDocumentsDataSource.replaceExpDateByType(documentTypes)

        //THEN
        assertEquals(true, result!!.isSuccessful)
        assertEquals(3, result.data!!.size)
        assertEquals(newType, result.data!![2].type)
        assertEquals(processNum, result.data!![2].status)
        assertEquals(Preferences.DEF, result.data!![2].expirationDate)
        assertEquals(null, result.data!![2].diiaDocument)
        assertEquals("", result.data!![2].timestamp)
    }


    @Test
    fun `test replaceExpDateByType returns null if no data to update`() = runTest {
        //GIVEN
        val storeData = "storeData"
        `when`(diiaStorage.containsKey(Preferences.Documents)).thenReturn(true)
        `when`(diiaStorage.getString(Preferences.Documents, Preferences.DEF)).thenReturn(storeData)

        //WHEN
        val result = keyValueDocumentsDataSource.replaceExpDateByType(listOf())

        //THEN
        assertNull(result)
    }
}