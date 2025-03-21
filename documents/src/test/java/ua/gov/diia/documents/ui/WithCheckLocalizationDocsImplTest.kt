package ua.gov.diia.documents.ui

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.core.models.document.BaseLocalizationChecker
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata

class WithCheckLocalizationDocsImplTest {

    private val checker1: BaseLocalizationChecker = mock()

    private val checker2: BaseLocalizationChecker = mock()

    private lateinit var withCheckLocalizationDocsImpl: WithCheckLocalizationDocsImpl

    private val localizationCheckers = listOf(checker1, checker2)

    @Before
    fun setUp() {
        withCheckLocalizationDocsImpl = WithCheckLocalizationDocsImpl(localizationCheckers)
    }

    @After
    fun cleanUp() {
        Mockito.clearAllCaches()
    }

    @Test
    fun `checkLocalizationDocs should update documents correctly`() {
        val doc1 = Mockito.mock(DiiaDocumentWithMetadata::class.java)
        val doc2 = Mockito.mock(DiiaDocumentWithMetadata::class.java)
        val docs = listOf(doc1, doc2)

        whenever(checker1.checkLocalizationDocs(any())).thenReturn(null)
        whenever(checker2.checkLocalizationDocs(doc1)).thenReturn("doc1Update")
        whenever(checker2.checkLocalizationDocs(doc2)).thenReturn(null)

        val updatedDocs = mutableListOf<String>()
        withCheckLocalizationDocsImpl.checkLocalizationDocs(docs) { updatedDocs.addAll(it) }

        assertTrue("doc1Update" in updatedDocs)
        assertEquals(1, updatedDocs.size)
    }

    @Test
    fun `checkLocalizationDocs should not update if no docs are eligible`() {
        val doc1 = Mockito.mock(DiiaDocumentWithMetadata::class.java)
        val docs = listOf(doc1)

        whenever(checker1.checkLocalizationDocs(any())).thenReturn(null)
        whenever(checker2.checkLocalizationDocs(any())).thenReturn(null)

        var updateCalled = false
        withCheckLocalizationDocsImpl.checkLocalizationDocs(docs) { updateCalled = true }

        assertFalse(updateCalled)
    }

    @Test
    fun `checkLocalizationDocs should handle null docs gracefully`() {
        var updateCalled = false
        withCheckLocalizationDocsImpl.checkLocalizationDocs(null) { updateCalled = true }

        assertFalse(updateCalled)
    }
}