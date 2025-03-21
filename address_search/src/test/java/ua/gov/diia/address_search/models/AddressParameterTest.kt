package ua.gov.diia.address_search.models

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddressParameterTest {

    @Test
    fun `test AddressParameter returns BULLET type if input is singleCheck`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.singleCheck, true, mockk(), mockk(),  mockk(), "defaultText")

        assertEquals(SearchType.BULLET, model.getSearchType())
    }

    @Test
    fun `test AddressParameter returns LIST type if input is not singleCheck`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.list, true, mockk(), mockk(),  mockk(), "defaultText")

        assertEquals(SearchType.LIST, model.getSearchType())
    }

    @Test
    fun `test AddressParameter returns EDITABLE from getFieldMode if input is textField`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.textField, true, mockk(), mockk(),  mockk(), "defaultText")

        assertEquals(AddressFieldMode.EDITABLE, model.getFieldMode())
    }

    @Test
    fun `test AddressParameter returns BUTTON from getFieldMode if input is not textField`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.list, true, mockk(), mockk(),  mockk(), "defaultText")

        assertEquals(AddressFieldMode.BUTTON, model.getFieldMode())
    }

    @Test
    fun `test isEditableMode returns true if input if textField`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.textField, true, mockk(), mockk(),  mockk(), "defaultText")

        assertTrue(model.isEditableMode())
    }

    @Test
    fun `test isEditableMode returns false if input if textField`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.list, true, mockk(), mockk(),  mockk(), "defaultText")

        assertFalse(model.isEditableMode())
    }

    @Test
    fun `test hasDefault returns true if defaultListItem or defaultTextItem are not null`() {
        val model = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.list, true, mockk(), mockk(),  mockk(), null)
        assertTrue(model.hasDefault())

        val model2 = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.list, true, mockk(), mockk(),  null, "defaultText")
        assertTrue(model2.hasDefault())

        val model3 = AddressParameter("type", "label", "hint", "comment", "mask", AddressFieldInputType.list, true, mockk(), mockk(),  null, null)
        assertFalse(model3.hasDefault())
    }
}