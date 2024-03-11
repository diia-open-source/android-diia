package ua.gov.diia.documents.util.datasource.local

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.documents.data.datasource.local.BrokenDocFilterImpl
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

class BrokenDocFilterImplTest {

    private val documentsHelper: DocumentsHelper = mock()

    private lateinit var brokenDocFilterImpl: BrokenDocFilterImpl

    @Before
    fun setUp() {
        brokenDocFilterImpl = BrokenDocFilterImpl(documentsHelper)
    }
    @After
    fun cleanUp() {
        Mockito.clearAllCaches()
    }

    @Test
    fun `filter separates documents correctly`() {
        val docCanBreakWithId = Mockito.mock(DiiaDocumentWithMetadata::class.java)
        val docCanBreakWithoutId = Mockito.mock(DiiaDocumentWithMetadata::class.java)
        val docCannotBreak = Mockito.mock(DiiaDocumentWithMetadata::class.java)

        val docCanBreakWithIdDoc = Mockito.mock(DiiaDocument::class.java)
        val docCanBreakWithoutIdDoc = Mockito.mock(DiiaDocument::class.java)
        val docCannotBreakDoc = Mockito.mock(DiiaDocument::class.java)

        whenever(docCanBreakWithId.type).thenReturn("docCanBreakWithId")
        whenever(docCanBreakWithId.diiaDocument).thenReturn(docCanBreakWithIdDoc)
        whenever(docCanBreakWithIdDoc.docId()).thenReturn("123")

        whenever(docCanBreakWithoutId.type).thenReturn("docCanBreakWithoutId")
        whenever(docCanBreakWithoutId.diiaDocument).thenReturn(docCanBreakWithoutIdDoc)
        whenever(docCanBreakWithoutIdDoc.docId()).thenReturn(null)

        whenever(docCannotBreak.type).thenReturn("docCannotBreak")
        whenever(docCannotBreak.diiaDocument).thenReturn(docCannotBreakDoc)
        whenever(docCannotBreakDoc.docId()).thenReturn("docCannotBreak")

        whenever(documentsHelper.isDocCanBeBroken(any())).thenAnswer { invocation ->
            val type = invocation.getArgument<String>(0)
            type == docCanBreakWithId.type || type == docCanBreakWithoutId.type
        }

        val existsId = mutableListOf<String?>()
        val removeList = mutableListOf<DiiaDocumentWithMetadata>()
        val docs = listOf(docCanBreakWithId, docCanBreakWithoutId, docCannotBreak)

        brokenDocFilterImpl.filter(docs, existsId, removeList)

        assertTrue(existsId.contains(docCanBreakWithId.diiaDocument?.docId()))
        assertTrue(removeList.contains(docCanBreakWithoutId))
        assertTrue(!existsId.contains(docCannotBreak.diiaDocument?.docId()) && !removeList.contains(docCannotBreak))
    }
}