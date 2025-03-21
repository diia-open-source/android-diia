package ua.gov.diia.documents.data.datasource.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.network.Http.HTTP_200
import ua.gov.diia.core.network.Http.HTTP_403
import ua.gov.diia.core.network.Http.HTTP_500
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.Preferences
import ua.gov.diia.documents.rules.MainDispatcherRule

@RunWith(MockitoJUnitRunner::class)
class DefaultDocGroupUpdateBehaviorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var defaultDocGroupUpdateBehavior: DefaultDocGroupUpdateBehavior

    @Before
    fun setUp() {
        defaultDocGroupUpdateBehavior = DefaultDocGroupUpdateBehavior()
    }

    @Test
    fun `test handle metadata for document with status 200 `() = runBlocking {
        val currentDocValue = mock(DiiaDocumentWithMetadata::class.java)//= DiiaDocumentWithMetadata(null, "timestamp", "expdata", 200, "document")
        `when`(currentDocValue.type).thenReturn("document")
        val newDocValue = mock(DiiaDocumentWithMetadata::class.java)//DiiaDocumentWithMetadata(null, "timestamp2", "expdata2", 200, "document")//
//        `when`(newDocValue.type).thenReturn("document")
        val docValue: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docValue.add(newDocValue)
        val docsToPersist: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docsToPersist.add(currentDocValue)
        val existsId: List<String?> = mutableListOf()

        defaultDocGroupUpdateBehavior.handleUpdate("document", docValue, Http.HTTP_200, docsToPersist, existsId)

        assertEquals(1, docsToPersist.size)
        assertTrue(docsToPersist.contains(newDocValue))
        assertFalse(docsToPersist.contains(currentDocValue))
    }

    @Test
    fun `test handle metadata for document with status 404 `() = runBlocking {
        val currentDocValue = mock(DiiaDocumentWithMetadata::class.java)
        `when`(currentDocValue.type).thenReturn("document")
        val newDocValue = mock(DiiaDocumentWithMetadata::class.java)
        val docValue: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docValue.add(newDocValue)
        val docsToPersist: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docsToPersist.add(currentDocValue)
        val existsId: List<String?> = mutableListOf()

        defaultDocGroupUpdateBehavior.handleUpdate("document", docValue, Http.HTTP_404, docsToPersist, existsId)

        assertEquals(1, docsToPersist.size)
        assertTrue(docsToPersist.contains(newDocValue))
        assertFalse(docsToPersist.contains(currentDocValue))
    }

    @Test
    fun `test handle metadata for document with covid cert in progress status`() = runBlocking {
        val newDocValue = spy(DiiaDocumentWithMetadata(null, "timestemp", "expDateCurrent", HTTP_200, "document"))

        val docValue: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docValue.add(newDocValue)
        val docsToPersist: MutableList<DiiaDocumentWithMetadata> = mutableListOf()

        val existsId: List<String?> = mutableListOf()

        defaultDocGroupUpdateBehavior.handleUpdate("document", docValue, Http.COVID_CERT_IN_PROGRESS_STATUS, docsToPersist, existsId)

        assertEquals(1, docsToPersist.size)
        assertEquals(Preferences.DEF, docsToPersist[0].expirationDate)
    }

    @Test
    fun `test handle metadata with other status if persist not contain this type of doc`() = runBlocking {
        val newDocValue = mock(DiiaDocumentWithMetadata::class.java)
        val docValue: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docValue.add(newDocValue)
        val docsToPersist: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        val existsId: List<String?> = mutableListOf()

        defaultDocGroupUpdateBehavior.handleUpdate("document", docValue, Http.HTTP_500, docsToPersist, existsId)

        assertEquals(1, docsToPersist.size)
        assertTrue(docsToPersist.contains(newDocValue))
    }

    @Test
    fun `test rewrite expiration date and status if status is not 403`() = runBlocking {
        val currentDocValue = DiiaDocumentWithMetadata(null, "timestemp", "expDateCurrent", HTTP_403, "document")

        val newDocValue = mock(DiiaDocumentWithMetadata::class.java)
        `when`(newDocValue.expirationDate).thenReturn("expDateNew")
        val docValue: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docValue.add(newDocValue)
        val docsToPersist: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docsToPersist.add(currentDocValue)
        val existsId: List<String?> = mutableListOf()

        defaultDocGroupUpdateBehavior.handleUpdate("document", docValue, Http.HTTP_500, docsToPersist, existsId)

        assertEquals(1, docsToPersist.size)
        assertEquals("expDateNew", docsToPersist[0].expirationDate)
        assertEquals(Http.HTTP_500, docsToPersist[0].status)
    }

    @Test
    fun `test rewrite only expiration date if status is 403`() = runBlocking {
        val currentDocValue = DiiaDocumentWithMetadata(null, "timestemp", "expDateCurrent", HTTP_500, "document")

        val newDocValue = mock(DiiaDocumentWithMetadata::class.java)
        `when`(newDocValue.expirationDate).thenReturn("expDateNew")
        val docValue: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docValue.add(newDocValue)
        val docsToPersist: MutableList<DiiaDocumentWithMetadata> = mutableListOf()
        docsToPersist.add(currentDocValue)
        val existsId: List<String?> = mutableListOf()

        defaultDocGroupUpdateBehavior.handleUpdate("document", docValue, Http.HTTP_403, docsToPersist, existsId)

        assertEquals(1, docsToPersist.size)
        assertEquals("expDateNew", docsToPersist[0].expirationDate)
        assertEquals(Http.HTTP_500, docsToPersist[0].status)
    }
}