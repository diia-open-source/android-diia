package ua.gov.diia.address_search.models

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddressFieldApproveRequestTest {

    @Test
    fun `test that approve returns value regarding regex value if data is string and not mandatory`() {

        val model = AddressFieldApproveRequest(false, "data", "-?\\d+")
        assertFalse(model.approved())

        val model2 = AddressFieldApproveRequest(false, "123", "-?\\d+")
        assertTrue(model2.approved())

        val model3 = AddressFieldApproveRequest(false, "123", null)
        assertTrue(model3.approved())

        val model4 = AddressFieldApproveRequest(false, "", null)
        assertTrue(model4.approved())
    }

    @Test
    fun `test that approve returns value regarding regex value if data is string and model is mandatory`() {

        val model = AddressFieldApproveRequest(true, "data", "-?\\d+")
        assertFalse(model.approved())

        val model2 = AddressFieldApproveRequest(true, "123", "-?\\d+")
        assertTrue(model2.approved())

        val model3 = AddressFieldApproveRequest(true, "123", null)
        assertTrue(model3.approved())

        val model4 = AddressFieldApproveRequest(true, "", null)
        assertFalse(model4.approved())
    }

    @Test
    fun `test that approved returns true if not mandatory`() {
        val model = AddressFieldApproveRequest(false, 1, "-?\\d+")

        assertTrue(model.approved())
    }

    @Test
    fun `test that approved returns true if data exist and mandatory`() {
        val model = AddressFieldApproveRequest(true, 1, "-?\\d+")

        assertTrue(model.approved())
    }

    @Test
    fun `test that approved returns false if data is null and mandatory`() {
        val model = AddressFieldApproveRequest(true, null, "-?\\d+")

        assertFalse(model.approved())
    }
}