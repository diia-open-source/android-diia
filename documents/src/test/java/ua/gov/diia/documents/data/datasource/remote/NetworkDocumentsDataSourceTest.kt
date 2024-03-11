package ua.gov.diia.documents.data.datasource.remote

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DiiaDocumentsWithOrder
import ua.gov.diia.documents.models.DocOrder
import ua.gov.diia.documents.models.DocumentsOrder
import ua.gov.diia.documents.models.FetchDocumentsResult
import ua.gov.diia.documents.models.TypeDefinedDocOrder
import ua.gov.diia.documents.models.TypeDefinedDocumentsOrder

@RunWith(MockitoJUnitRunner::class)
class NetworkDocumentsDataSourceTest {

    @Mock
    private lateinit var apiDocs: ApiDocuments

    @Mock
    private lateinit var withCrashlytics: WithCrashlytics

    @Mock
    private lateinit var mockDocument: DiiaDocument

    private lateinit var dataSource: NetworkDocumentsDataSource

    @Before
    fun setUp() {
        dataSource = NetworkDocumentsDataSource(
            apiDocs = apiDocs,
            withCrashlytics = withCrashlytics
        )
    }

    @Test
    fun `fetch data successful`() = runTest {
        whenever(apiDocs.fetchDocuments(any())).thenReturn(listOf(doc("t0"), doc("t1")))
        val result = dataSource.fetchData(setOf("t0", "t1"))
        Assert.assertEquals(
            DataSourceDataResult.successful(listOf(doc("t0"), doc("t1"))),
            result
        )
        verify(apiDocs).fetchDocuments(
            mapOf(
                "filter[0]" to "t0",
                "filter[1]" to "t1",
            )
        )
    }

    @Test
    fun `fetch data error`() = runTest {
        val error = RuntimeException()
        whenever(apiDocs.fetchDocuments(any())).thenThrow(error)
        val result = dataSource.fetchData(setOf("t0", "t1"))
        Assert.assertEquals(
            DataSourceDataResult.failed<Nothing>(error),
            result
        )
        verify(withCrashlytics).sendNonFatalError(error)
    }

    @Test
    fun `fetch documents with types successful`() = runTest {
        whenever(apiDocs.fetchDocumentsWithTypes(any())).thenReturn(
            DiiaDocumentsWithOrder(listOf(doc("t0"), doc("t1")), listOf("t0", "t1"))
        )
        val result = dataSource.fetchDocsWithTypes(setOf("t0", "t1"))
        Assert.assertEquals(
            FetchDocumentsResult(
                documents = listOf(doc("t0"), doc("t1")),
                docOrder = listOf("t0", "t1"),
                exception = null,
                isSuccessful = true,
            ),
            result
        )
        verify(apiDocs).fetchDocumentsWithTypes(
            mapOf(
                "filter[0]" to "t0",
                "filter[1]" to "t1",
            )
        )
    }

    @Test
    fun `fetch documents with types error`() = runTest {
        val error = RuntimeException()
        whenever(apiDocs.fetchDocumentsWithTypes(any())).thenThrow(error)
        val result = dataSource.fetchDocsWithTypes(setOf("t0", "t1"))
        Assert.assertEquals(
            FetchDocumentsResult(
                documents = emptyList(),
                docOrder = emptyList(),
                exception = error,
                isSuccessful = false,
            ),
            result
        )
    }

    @Test
    fun `save documents order for specific type successful`() = runTest {
        val docOrder = listOf(
            TypeDefinedDocOrder("14234", 0),
            TypeDefinedDocOrder("14234", 1),
        )
        dataSource.saveDocOrderForSpecificType("t0", TypeDefinedDocumentsOrder(docOrder))
        verify(apiDocs).setTypedDocumentsOrder("t0", TypeDefinedDocumentsOrder(docOrder))
    }

    @Test
    fun `save documents order for specific type error`() = runTest {
        val error = RuntimeException()
        whenever(apiDocs.setTypedDocumentsOrder(any(), any())).thenThrow(error)
        dataSource.saveDocOrderForSpecificType("t0", TypeDefinedDocumentsOrder(listOf()))
        verify(withCrashlytics).sendNonFatalError(error)
    }

    @Test
    fun `set documents order successful`() = runTest {
        val order = DocumentsOrder(
            listOf(
                DocOrder("t0", 1),
                DocOrder("t1", 3),
                DocOrder("t2", 2),
            )
        )
        dataSource.setDocumentsOrder(order)
        verify(apiDocs).setDocumentsOrder(order)
    }

    @Test
    fun `set documents order error`() = runTest {
        val error = RuntimeException()
        whenever(apiDocs.setDocumentsOrder(any())).thenThrow(error)
        dataSource.setDocumentsOrder(DocumentsOrder(listOf()))
        verify(withCrashlytics).sendNonFatalError(error)
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