package ua.gov.diia.doc_driver_license

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.doc_driver_license.utils.DriverLicenceLocalizationChecker
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.docgroups.v2.FrontCard

class DriverLicenseLocalizationCheckerTest {
    private lateinit var driverLicenceLocalizationChecker: DriverLicenceLocalizationChecker
    private lateinit var mockDoc: DiiaDocumentWithMetadata
    private lateinit var mockDriverLicenseData: DriverLicenseV2.Data
    private lateinit var mockFrontCard: FrontCard
    @Before
    fun setUp() {
        driverLicenceLocalizationChecker = DriverLicenceLocalizationChecker()
        mockDoc = mock(DiiaDocumentWithMetadata::class.java)
        mockDriverLicenseData = mock(DriverLicenseV2.Data::class.java)
        mockFrontCard = mock(FrontCard::class.java)
    }

    @Test
    fun `should return NAME when ua is null`() {
        `when`(mockDoc.diiaDocument).thenReturn(mockDriverLicenseData)
        `when`(mockDriverLicenseData.frontCard).thenReturn(mockFrontCard)
        `when`(mockFrontCard.ua).thenReturn(null)

        val result = driverLicenceLocalizationChecker.checkLocalizationDocs(mockDoc)

        assertEquals(DriverLicenseConst.NAME, result)
    }

    @Test
    fun `should return null when ua is not null`() {
        `when`(mockDoc.diiaDocument).thenReturn(mockDriverLicenseData)
        `when`(mockDriverLicenseData.frontCard).thenReturn(mockFrontCard)
        `when`(mockFrontCard.ua).thenReturn(emptyList())

        val result = driverLicenceLocalizationChecker.checkLocalizationDocs(mockDoc)

        assertNull(result)
    }

    @Test
    fun `should return null when document is not DriverLicenseV2 Data`() {
        `when`(mockDoc.diiaDocument).thenReturn(mock(DiiaDocument::class.java))

        val result = driverLicenceLocalizationChecker.checkLocalizationDocs(mockDoc)

        assertNull(result)
    }
}