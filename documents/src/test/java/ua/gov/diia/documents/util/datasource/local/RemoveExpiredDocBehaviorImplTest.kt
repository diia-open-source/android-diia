package ua.gov.diia.documents.util.datasource.local

import io.mockk.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.extensions.date_time.getUTCDate
import ua.gov.diia.documents.data.datasource.local.RemoveExpiredDocBehaviorImpl
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.Preferences
import java.util.*

class RemoveExpiredDocBehaviorImplTest {

    private val currentDateProvider: CurrentDateProvider = mockk()

    private val documentsHelper: DocumentsHelper = mockk()

    private lateinit var removeExpiredDocBehaviorImpl: RemoveExpiredDocBehaviorImpl

    @Before
    fun setUp() {
       removeExpiredDocBehaviorImpl = RemoveExpiredDocBehaviorImpl(currentDateProvider, documentsHelper)
    }
    @After
    fun cleanUp() {
        clearAllMocks()
    }

    @Test
    fun `removeExpiredDocs removes expired documents`() = runTest {
        val expiredDoc = mockk<DiiaDocumentWithMetadata>()
        val expiredDocDoc = mockk<DiiaDocument>()
        val validDoc = mockk<DiiaDocumentWithMetadata>()
        val validDocDoc = mockk<DiiaDocument>()
        val ineligibleDoc = mockk<DiiaDocumentWithMetadata>()

        every { expiredDoc.type } returns "expiredDoc"
        every { expiredDoc.diiaDocument } returns expiredDocDoc
        every { expiredDocDoc.getExpirationDateISO() } returns "expiredDoc"

        every { validDoc.type } returns "validDoc"
        every { validDoc.diiaDocument } returns validDocDoc
        every { validDocDoc.getExpirationDateISO() } returns "validDoc"

        every { ineligibleDoc.type } returns "ineligibleDoc"
        every { ineligibleDoc.diiaDocument } returns null

        every { documentsHelper.isDocEligibleForDeletion("expiredDoc") } returns true
        every { documentsHelper.isDocEligibleForDeletion("validDoc") } returns true
        every { documentsHelper.isDocEligibleForDeletion("ineligibleDoc") } returns false

        val utcDateValid = Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))
        val utcDateExpired = Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))
        mockkStatic(::getUTCDate)
        every { getUTCDate(Preferences.DEF) } returns utcDateExpired
        every { getUTCDate("expiredDoc") } returns utcDateExpired
        every { getUTCDate("validDoc") } returns utcDateValid

        every { currentDateProvider.getDate() } returns Date(System.currentTimeMillis())

        val data = listOf(expiredDoc, validDoc, ineligibleDoc)

        val removeDocDelegate = mockk<RemoveDocDelegate>()
        coJustRun { removeDocDelegate.removeDoc(any()) }

        removeExpiredDocBehaviorImpl.removeExpiredDocs(data, removeDocDelegate::removeDoc)
        advanceUntilIdle()

        coVerify(atLeast = 1) { removeDocDelegate.removeDoc(expiredDocDoc) }
        coVerify(atLeast = 0) { removeDocDelegate.removeDoc(validDocDoc) }
    }

    class RemoveDocDelegate{
        suspend fun removeDoc(doc: DiiaDocument){}
    }
}