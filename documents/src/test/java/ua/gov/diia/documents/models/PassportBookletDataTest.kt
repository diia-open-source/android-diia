package ua.gov.diia.documents.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PassportBookletDataTest {
    @Test
    fun `keeps leading zeros in document number`() {
        assertEquals("АА 000001", sample().displayNumber)
    }

    @Test
    fun `omits blank series without adding a leading space`() {
        val source = sample()
        val data = source.copy(issuance = source.issuance.copy(series = " \n", number = " 000001 "))
        assertEquals("000001", data.displayNumber)
    }

    @Test
    fun `supports people without a patronymic and documents without expiry`() {
        val data = sample()
        assertNull(data.identity.middleName)
        assertNull(data.issuance.validUntil)
        assertNull(data.additionalFields)
    }

    @Test
    fun `replacing content preserves unicode and display dates`() {
        val source = sample()
        val updated = source.copy(
            identity = source.identity.copy(firstName = "Марія-Надія"),
            additionalFields = listOf(PassportBookletData.Field("Місце проживання", "м. Київ"))
        )
        assertEquals("Марія-Надія", updated.identity.firstName)
        assertEquals("Надія", source.identity.firstName)
        assertEquals("24.08.1991", updated.identity.birthDate)
        assertEquals("м. Київ", updated.additionalFields?.single()?.value)
    }

    private fun sample() = PassportBookletData(
        identity = PassportBookletData.Identity(
            lastName = "ПРИКЛАД", firstName = "Надія", birthDate = "24.08.1991",
            birthPlace = "м. Івано-Франківськ"
        ),
        issuance = PassportBookletData.Issuance(
            series = "АА", number = "000001", issuedAt = "24.08.2007",
            issuedBy = "Демонстраційний орган видачі"
        )
    )
}
