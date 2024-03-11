package ua.gov.diia.address_search.ui

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.address_search.models.AddressFieldApproveRequest
import ua.gov.diia.address_search.models.AddressFieldMode
import ua.gov.diia.address_search.models.AddressItem
import ua.gov.diia.address_search.models.AddressParameter
import ua.gov.diia.address_search.models.AddressValidation

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddressParameterMapperTest {

    lateinit var addressParameterMapper: AddressParameterMapper

    @Before
    fun setUp() {
        addressParameterMapper = AddressParameterMapper()
    }

    @Test
    fun `test getViewMode`() {
        val buttonParam = mockk<AddressParameter>()
        every { buttonParam.getFieldMode() } returns AddressFieldMode.BUTTON
        assertEquals(0, addressParameterMapper.getViewMode(buttonParam))

        val editableParam = mockk<AddressParameter>()
        every { editableParam.getFieldMode() } returns AddressFieldMode.EDITABLE
        assertEquals(1, addressParameterMapper.getViewMode(editableParam))

        assertEquals(0, addressParameterMapper.getViewMode(null))
    }

    @Test
    fun `test getEditableModeFieldRequest`() {
        val value = "value"
        val type = "type"

        val param = mockk<AddressParameter>()
        every { param.isEditableMode() } returns true
        every { param.type } returns type
        val addressFieldRequestValue = addressParameterMapper.getEditableModeFieldRequest(value, param)
        assertNotNull(addressFieldRequestValue)
        assertEquals(type, addressFieldRequestValue!!.type)
        assertEquals(value, addressFieldRequestValue.value)


        val param2 = mockk<AddressParameter>()
        every { param2.isEditableMode() } returns false
        assertNull(addressParameterMapper.getEditableModeFieldRequest(value, param2))


        assertNull(addressParameterMapper.getEditableModeFieldRequest(null, param2))
        assertNull(addressParameterMapper.getEditableModeFieldRequest(value, null))
        assertNull(addressParameterMapper.getEditableModeFieldRequest(null, null))
    }

    @Test
    fun `test toFieldApproveRequest process BUTTON field mode`() {
        val regex = "regex"
        val param = mockk<AddressParameter>()
        val addressItemParam = mockk<AddressItem>()
        val addressValidation = mockk<AddressValidation>()
        every { param.getFieldMode() } returns AddressFieldMode.BUTTON
        every { param.mandatory } returns true
        every { param.validation } returns addressValidation
        every { addressValidation.regexp } returns regex

        val addressFieldApproveRequest = addressParameterMapper.toFieldApproveRequest(listOf(param, addressItemParam))

        assertEquals(true, addressFieldApproveRequest.mandatory)
        assertEquals(regex, addressFieldApproveRequest.regex)
        assertEquals(addressItemParam, addressFieldApproveRequest.data)
    }

    @Test
    fun `test toFieldApproveRequest process EDITABLE field mode`() {
        val regex = "regex"
        val param = mockk<AddressParameter>()
        val editableStr = "editablestr"
        val addressValidation = mockk<AddressValidation>()
        every { param.getFieldMode() } returns AddressFieldMode.EDITABLE
        every { param.mandatory } returns true
        every { param.validation } returns addressValidation
        every { addressValidation.regexp } returns regex

        val addressFieldApproveRequest = addressParameterMapper.toFieldApproveRequest(listOf(param, editableStr))

        assertEquals(true, addressFieldApproveRequest.mandatory)
        assertEquals(regex, addressFieldApproveRequest.regex)
        assertEquals(editableStr, addressFieldApproveRequest.data)
    }

    @Test
    fun `test toFieldApproveRequest process without field mode`() {
        val regex = "regex"
        val param = mockk<AddressParameter>(relaxed = true)
        val addressValidation = mockk<AddressValidation>(relaxed = true)
        every { param.mandatory } returns true
        every { param.validation } returns addressValidation
        every { addressValidation.regexp } returns regex

        val addressFieldApproveRequest = addressParameterMapper.toFieldApproveRequest(listOf(param))

        assertEquals(true, addressFieldApproveRequest.mandatory)
        assertEquals(regex, addressFieldApproveRequest.regex)
    }
    @Test
    fun `test toFieldApproveRequest process mandatory not set`() {
        val param = mockk<AddressParameter>(relaxed = true)
        every { param.validation } returns null

        val addressFieldApproveRequest = addressParameterMapper.toFieldApproveRequest(listOf(param))

        assertEquals(false, addressFieldApproveRequest.mandatory)
        assertEquals(null, addressFieldApproveRequest.regex)
        assertEquals(null, addressFieldApproveRequest.data)
    }

    @Test
    fun `test toFieldApproveRequest process without address params`() {

        val addressFieldApproveRequest = addressParameterMapper.toFieldApproveRequest(listOf("param"))

        assertEquals(false, addressFieldApproveRequest.mandatory)
        assertEquals(null, addressFieldApproveRequest.regex)
        assertEquals(null, addressFieldApproveRequest.data)
    }
    @Test
    fun `test toFieldApproveRequest process mandatory set as false`() {
        val regex = "regex"
        val param = mockk<AddressParameter>(relaxed = true)
        val addressValidation = mockk<AddressValidation>(relaxed = true)
        every { param.mandatory } returns false
        every { param.validation } returns addressValidation
        every { addressValidation.regexp } returns regex

        val addressFieldApproveRequest = addressParameterMapper.toFieldApproveRequest(listOf(param))

        assertEquals(false, addressFieldApproveRequest.mandatory)
        assertEquals(regex, addressFieldApproveRequest.regex)
    }

    @Test
    fun `test approveFiledData`() {
        assertTrue(addressParameterMapper.approveFiledData("string"))

        val request = mockk<AddressFieldApproveRequest>()
        every { request.approved() } returns false
        assertFalse(addressParameterMapper.approveFiledData(request))
        verify(exactly = 1) { request.approved() }
    }
}