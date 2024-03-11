package ua.gov.diia.documents.data.repository

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.data.datasource.local.KeyValueDocumentsDataSource
import ua.gov.diia.documents.data.datasource.remote.NetworkDocumentsDataSource
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DocOrder
import ua.gov.diia.documents.models.FetchDocumentsResult
import ua.gov.diia.documents.models.TypeDefinedDocOrder
import ua.gov.diia.documents.models.TypeDefinedDocumentsOrder

@RunWith(MockitoJUnitRunner::class)
class DocumentsDataRepositoryImplTest {

    @Mock
    private lateinit var keyValueDataSource: KeyValueDocumentsDataSource

    @Mock
    private lateinit var networkDocumentsDataSource: NetworkDocumentsDataSource

    @Mock
    private lateinit var beforePublishAction: BeforePublishAction

    private lateinit var docTypesAvailableToUsers: Set<String>

    @Mock
    private lateinit var withCrashlytics: WithCrashlytics

    @Mock
    private lateinit var mockDocument: DiiaDocument

    private lateinit var scope: CoroutineScope

    private lateinit var repository: DocumentsDataRepositoryImpl

    private val baseDocumentList = listOf(DiiaDocumentWithMetadata.DOC_ERROR)

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        docTypesAvailableToUsers = setOf("t0", "t1", "t2")
        repository = DocumentsDataRepositoryImpl(
            scope = scope,
            keyValueDataSource = keyValueDataSource,
            networkDocumentsDataSource = networkDocumentsDataSource,
            beforePublishActions = listOf(beforePublishAction),
            docTypesAvailableToUsers = docTypesAvailableToUsers,
            withCrashlytics = withCrashlytics
        )
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun clear() = runTest {
        repository.data.test {
            repository.clear()
            Assert.assertEquals(DataSourceDataResult.successful(baseDocumentList), awaitItem())
        }
    }

    @Test
    fun `update order`() = runTest {
        whenever(keyValueDataSource.loadData()).thenReturn(docTypesAvailableToUsers.mapIndexed { i, x ->
            doc(x, i + 1)
        })
        val newOrder = listOf("t2", "t0", "t1")
        repository.updateDocOrder(newOrder)
        scope.joinChildren()
        val expected = docTypesAvailableToUsers.map { x -> doc(x, newOrder.indexOf(x) + 1) }
        verify(keyValueDataSource).updateData(expected)
    }

    @Test
    fun `update order error`() = runTest {
        whenever(keyValueDataSource.loadData()).thenReturn(docTypesAvailableToUsers.mapIndexed { i, x ->
            doc(x, i + 1)
        })
        val error = RuntimeException()
        whenever(keyValueDataSource.updateData(any())).thenThrow(error)
        val newOrder = listOf("t2", "t0", "t1")
        repository.updateDocOrder(newOrder)
        scope.joinChildren()
        verify(withCrashlytics).sendNonFatalError(error)
    }

    @Test
    fun `update order the same`() = runTest {
        whenever(keyValueDataSource.loadData()).thenReturn(docTypesAvailableToUsers.mapIndexed { i, x ->
            doc(x, i + 1)
        })
        val newOrder = docTypesAvailableToUsers.toList()
        repository.updateDocOrder(newOrder)
        scope.joinChildren()
        verify(keyValueDataSource, never()).updateData(any())
    }

    @Test
    fun `invalidate with expiration`() = runTest {
        val docs = listOf(doc("t0"), doc("t2"))
        var data: List<DiiaDocumentWithMetadata>? = null
        whenever(keyValueDataSource.updateData(any())).thenAnswer {
            data = it.arguments[0] as List<DiiaDocumentWithMetadata>?
            DataSourceDataResult.successful(checkNotNull(data))
        }
        whenever(keyValueDataSource.loadData()).thenAnswer { data }
        whenever(networkDocumentsDataSource.fetchDocsWithTypes(any())).thenAnswer {
            val types = it.arguments[0] as Set<String>
            FetchDocumentsResult(documents = docs.filter { it.type in types })
        }
        whenever(keyValueDataSource.fetchDocuments()).thenReturn(docs)
        whenever(keyValueDataSource.processExpiredData(any())).thenReturn(listOf(doc("t0")))

        repository.data.test {
            repository.invalidate()
            scope.joinChildren()
            Assert.assertEquals(
                DataSourceDataResult.successful(baseDocumentList + listOf(doc("t0"))),
                awaitItem()
            )
        }
    }

    @Test
    fun `invalidate without expiration`() = runTest {
        val docs = listOf(doc("t0"), doc("t2"))
        whenever(keyValueDataSource.fetchDocuments()).thenReturn(docs)
        whenever(keyValueDataSource.processExpiredData(any())).thenReturn(listOf())

        repository.data.test {
            repository.invalidate()
            scope.joinChildren()
            Assert.assertEquals(
                DataSourceDataResult.successful(baseDocumentList + docs),
                awaitItem()
            )
        }
    }

    @Test
    fun `invalidate concurrent`() = runTest {
        val docs = listOf(doc("t0"), doc("t2"))
        whenever(keyValueDataSource.fetchDocuments()).thenReturn(docs)
        whenever(keyValueDataSource.processExpiredData(any())).thenReturn(listOf())

        repository.invalidate()
        repository.invalidate()
        Assert.assertFalse(scope.coroutineContext.job.children.count() > 1)
    }

    @Test
    fun `attach external document`() = runTest {
        whenever(keyValueDataSource.loadData()).thenReturn(docTypesAvailableToUsers.mapIndexed { i, x ->
            doc(x, i + 1)
        })
        whenever(keyValueDataSource.updateData(any())).thenAnswer {
            DataSourceDataResult.successful(it.arguments[0])
        }
        val externalDoc = doc("t2")
        repository.data.test {
            repository.attachExternalDocument(externalDoc)
            Assert.assertEquals(
                DataSourceDataResult.successful(baseDocumentList + listOf(doc("t2"))),
                awaitItem()
            )
        }
    }

    @Test
    fun `remove document`() = runTest {
        whenever(keyValueDataSource.removeDocument(any())).thenReturn(
            DataSourceDataResult.successful(
                listOf(doc("t0"))
            )
        )
        repository.data.test {
            repository.removeDocument(mockDocument)
            awaitItem()
        }
        scope.joinChildren()
        verify(keyValueDataSource).removeDocument(mockDocument)
    }

    @Test
    fun `update document`() = runTest {
        whenever(keyValueDataSource.updateDocument(any())).thenReturn(
            DataSourceDataResult.successful(
                listOf(doc("t0"))
            )
        )
        repository.data.test {
            repository.updateDocument(mockDocument)
            awaitItem()
        }
        scope.joinChildren()
        verify(keyValueDataSource).updateDocument(mockDocument)
    }

    @Test
    fun `replace exp date by type`() = runTest {
        val types = listOf("t1", "t2")
        whenever(keyValueDataSource.replaceExpDateByType(any())).thenReturn(
            DataSourceDataResult.successful(
                listOf(doc("t0"))
            )
        )
        repository.data.test {
            repository.replaceExpDateByType(types)
            awaitItem()
        }
        scope.joinChildren()
        verify(keyValueDataSource).replaceExpDateByType(types)
    }

    @Test
    fun `get documents by type`() = runTest {
        whenever(keyValueDataSource.loadData()).thenReturn(null)
        Assert.assertNull(repository.getDocsByType("t2"))

        whenever(keyValueDataSource.loadData()).thenReturn(
            docTypesAvailableToUsers.map { doc(it) }
        )
        Assert.assertEquals(listOf(doc("t2").diiaDocument), repository.getDocsByType("t2"))
    }

    @Test
    fun `save doc type order`() = runTest {
        whenever(keyValueDataSource.loadData()).thenReturn(
            docTypesAvailableToUsers.map { doc(it) }
        )
        repository.saveDocTypeOrder(listOf(DocOrder("t1", 1), DocOrder("t0", 2)))
        scope.joinChildren()
        verify(keyValueDataSource).updateData(
            listOf(doc("t0", 2), doc("t1", 1), doc("t2"))
        )
    }

    @Test
    fun `save doc order for specific type`() = runTest {
        val docs = listOf(doc("t1", 2), doc("t1", 1))
        whenever(keyValueDataSource.loadData()).thenReturn(docs)

        repository.saveDocOrderForSpecificType(
            docOrders = listOf(
                TypeDefinedDocOrder("123", 1),
                TypeDefinedDocOrder("543", 2)
            ),
            docType = "t1"
        )
        scope.joinChildren()
        verify(networkDocumentsDataSource).saveDocOrderForSpecificType(
            documentType = "t1",
            docOrder = TypeDefinedDocumentsOrder(
                documentsOrder = listOf(
                    TypeDefinedDocOrder("123", 1),
                    TypeDefinedDocOrder("543", 2)
                )
            )
        )
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun CoroutineScope.joinChildren() {
        coroutineContext.job.children.forEach { it.join() }
    }

    private fun doc(
        type: String,
        order: Int = DiiaDocumentWithMetadata.LAST_DOC_ORDER,
    ) = DiiaDocumentWithMetadata(
        diiaDocument = mockDocument,
        timestamp = "2023-12-22T08:09:11.143Z",
        expirationDate = "2023-12-22T14:09:11.143Z",
        status = 200,
        type = type,
        order = order
    )
}