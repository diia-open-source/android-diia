package ua.gov.diia.address_search.models

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddressFieldResponseTest {

    @Test
    fun `test isEndForAddressSelection return value regarding address`() {
        val model = AddressFieldResponse("title", "description", null, listOf(), mockk(), null, null)
        assertTrue(model.isEndForAddressSelection())

        val model2 = AddressFieldResponse("title", "description", null, listOf(), null, null, null)
        assertFalse(model2.isEndForAddressSelection())

    }
}