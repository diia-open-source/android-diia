package ua.gov.diia.documents.models

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class PassportBookletJsonTest {
    private val adapter = Moshi.Builder().build().adapter(PassportBookletData::class.java)
    private val json = """
        {
          "identity": {
            "lastName": "ПРИКЛАД", "firstName": "Надія",
            "birthDate": "24.08.1991", "birthPlace": "м. Івано-Франківськ"
          },
          "issuance": {
            "series": "АА", "number": "000001", "issuedAt": "24.08.2007",
            "issuedBy": "Демонстраційний орган видачі"
          }
        }
    """.trimIndent()

    @Test
    fun `decodes cyrillic and omitted optional fields using generated adapters`() {
        val data = requireNotNull(adapter.fromJson(json))
        assertEquals("Надія", data.identity.firstName)
        assertEquals("м. Івано-Франківськ", data.identity.birthPlace)
        assertEquals("АА 000001", data.displayNumber)
        assertNull(data.identity.middleName)
        assertNull(data.issuance.validUntil)
        assertNull(data.additionalFields)
        assertEquals(data, adapter.fromJson(adapter.toJson(data)))
    }

    @Test
    fun `rejects missing required identity fields`() {
        val missingFirstName = json.replace("\"firstName\": \"Надія\",", "")
        assertThrows(JsonDataException::class.java) { adapter.fromJson(missingFirstName) }
    }
}
