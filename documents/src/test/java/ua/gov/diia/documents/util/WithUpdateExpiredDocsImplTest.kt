package ua.gov.diia.documents.util

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.helper.DocumentsHelper

class WithUpdateExpiredDocsImplTest {

    private val documentsDataSource: DocumentsDataRepository = mock()

    private val documentsHelper: DocumentsHelper = mock()

    private lateinit var withUpdateExpiredDocsImpl: WithUpdateExpiredDocsImpl

    @Before
    fun setUp() {
        withUpdateExpiredDocsImpl = WithUpdateExpiredDocsImpl(documentsDataSource, documentsHelper)
    }

    @After
    fun cleanUp() {
        Mockito.clearAllCaches()
    }

    @Test
    fun `updateExpirationDate with focusDocType calls update with list`() = runBlocking {
        val focusDocType = "someType"
        val typesToUpdate = listOf("type1", "type2")
        whenever(documentsHelper.provideListOfDocumentsRequireUpdateOfExpirationDate(focusDocType)).thenReturn(typesToUpdate)

        withUpdateExpiredDocsImpl.updateExpirationDate(focusDocType)

        verify(documentsDataSource).replaceExpDateByType(typesToUpdate)
        verify(documentsDataSource).invalidate()
    }
    @Test
    fun `updateExpirationDate with list updates expiration dates and invalidates data source`() = runBlocking {
        val typesToUpdate = listOf("type1", "type2")

        withUpdateExpiredDocsImpl.updateExpirationDate(typesToUpdate)

        verify(documentsDataSource).replaceExpDateByType(typesToUpdate)
        verify(documentsDataSource).invalidate()
    }
}