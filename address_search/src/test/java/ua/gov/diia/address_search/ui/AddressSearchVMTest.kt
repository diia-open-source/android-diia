package ua.gov.diia.address_search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.address_search.MainDispatcherRule
import ua.gov.diia.address_search.models.AddressFieldResponse
import ua.gov.diia.address_search.models.AddressIdentifier
import ua.gov.diia.address_search.models.AddressItem
import ua.gov.diia.address_search.models.AddressParameter
import ua.gov.diia.address_search.models.AddressValidation
import ua.gov.diia.address_search.models.SearchType
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddressSearchVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var addressSearchVM: AddressSearchVMImpl

    lateinit var apiAddressSearch: ApiAddressSearch
    lateinit var errorHandling: WithErrorHandling
    lateinit var retryLastAction: WithRetryLastAction

    lateinit var addressParam: AddressParameter
    lateinit var addressItem: AddressItem
    lateinit var addressValidation: AddressValidation

    lateinit var addressParameterMapper: AddressParameterMapper

    @Before
    fun setUp() {
        apiAddressSearch = mockk()
        errorHandling = mockk(relaxed = true)
        retryLastAction = mockk(relaxed = true)
        addressParameterMapper = mockk(relaxed = true)
        addressSearchVM = AddressSearchVMImpl(apiAddressSearch, errorHandling, retryLastAction, addressParameterMapper)
    }

    lateinit var paramsGetItems: Array<AddressItem>

    fun prepareMockForGetFieldSetupAction(filedType: String, description: String = "description", name: String? = "address_name"): AddressFieldResponse {
        addressParam = mockk<AddressParameter>(relaxed = true)
        addressItem = mockk<AddressItem>(relaxed = true)
        paramsGetItems = arrayOf(addressItem)
        addressValidation = mockk<AddressValidation>()
        every { addressItem.name } returns name
        every { addressValidation.regexp } returns "regexp"
        every { addressParam.getDefaultAddress() } returns addressItem
        every { addressParam.type } returns filedType
        every { addressParam.getSearchType() } returns SearchType.LIST
        every { addressParam.getItems() } returns paramsGetItems
        every { addressParam.validation } returns addressValidation

        val addressParameterList = mutableListOf<AddressParameter>()
        addressParameterList.add(addressParam)
        return AddressFieldResponse("title", description, addressParameterList, null, null, null)
    }

    @Test
    fun `test set address search`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            assertEquals("title", addressSearchVM.screenHeader.value)
            assertEquals("description", addressSearchVM.addressDescription.value)
            assertEquals(true, addressSearchVM.showFlowTitle.value)
        }
    }

    @Test
    fun `test set address search set false to search title if description is gone`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme", true)
            assertEquals(false, addressSearchVM.showFlowTitle.value)
        }
    }

    @Test
    fun `test set address search set false to search title if description is empty`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE, description = "")

            addressSearchVM.setAddressSearchArsImplWithoutSetGone(data, "code", "scheme")
            assertEquals(false, addressSearchVM.showFlowTitle.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set real estate`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.realEstateTypeFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedRealEstate.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set precision`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.PRECISION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.precisionTypeFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedPrecision.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set description`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DESCRIPTION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.descriptionFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedDescription.value)
        }
    }

    @Test
    fun `test selectDescription`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DESCRIPTION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectDescription()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(
                CompoundAddressResultKey.RESULT_KEY_DESC,
                addressSelection.resultCode
            )
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test getFieldSetupAction set country`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.countryFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedCountry.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set region`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.regionFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedRegion.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set district`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.districtFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedDistrict.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set city type`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.cityTypeFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedCityType.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set city`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.cityFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedCity.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set street type`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.streetTypeFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedStreetType.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set post office`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.postOfficeFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedPostOffice.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set street`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.streetFieldParams.value)
            assertEquals(addressItem, addressSearchVM.selectedStreet.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set house`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.houseFieldParams.value)
            assertEquals("address_name", addressSearchVM.house.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set apartment`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.apartmentFieldParams.value)
            assertEquals("address_name", addressSearchVM.apartment.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set corps`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.corpsFieldParams.value)
            assertEquals("address_name", addressSearchVM.corps.value)
        }
    }

    @Test
    fun `test getFieldSetupAction set zip`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(addressParam, addressSearchVM.zipFieldParams.value)
            assertEquals("address_name", addressSearchVM.zip.value)
        }
    }

    @Test
    fun `test setFieldParams isEndForAddressSelection is true`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DESCRIPTION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", "errormessage")
            addressSearchVM.setSelectedDescription(item)

            coVerify(exactly = 2) { response.isEndForAddressSelection() }
            coVerify(exactly = 1) { response.address }
        }
    }
    @Test
    fun `test setSelectedDescription requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DESCRIPTION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", "errormessage")
            addressSearchVM.setSelectedDescription(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedDescription.value)
        }
    }

    @Test
    fun `test selectRealEstate`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            addressSearchVM.selectRealEstate()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(
                CompoundAddressResultKey.RESULT_KEY_REAL_ESTATE,
                addressSelection.resultCode
            )
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedRealEstate requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", "errormessage")
            addressSearchVM.setSelectedRealEstate(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedRealEstate.value)
        }
    }

    @Test
    fun `test selectPrecision`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.PRECISION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectPrecision()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_PRECISION, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedPrecision requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.PRECISION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", "errormessage")
            addressSearchVM.setSelectedPrecision(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedPrecision.value)
        }
    }

    @Test
    fun `test selectCountry`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectCountry()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_COUNTRY, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedCountry requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", "errormessage")
            addressSearchVM.setSelectedCountry(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedCountry.value)
        }
    }

    @Test
    fun `test selectRegion`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectRegion()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_REGION, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedRegion requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedRegion(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedRegion.value)
        }
    }

    @Test
    fun `test setSelectedRegion error message`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", "Error message")
            addressSearchVM.setSelectedRegion(item)

            coVerify(exactly = 0) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedRegion.value)
        }
    }

    @Test
    fun `test selectDistrict`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectDistrict()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_DISTRICT, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedDistrict requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedDistrict(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedDistrict.value)
        }
    }

    @Test
    fun `test selectCityType`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectCityType()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_CITY_TYPE, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedCityType requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedCityType(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedCityType.value)
        }
    }

    @Test
    fun `test selectCity`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectCity()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_CITY, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedCity requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedCity(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedCity.value)
        }
    }

    @Test
    fun `test selectPostOffice`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectPostOffice()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(
                CompoundAddressResultKey.RESULT_KEY_POST_OFFICE,
                addressSelection.resultCode
            )
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedPostOffice requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedPostOffice(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedPostOffice.value)
        }
    }

    @Test
    fun `test selectStreetType`() {
        runTest {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectStreetType()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(
                CompoundAddressResultKey.RESULT_KEY_STREET_TYPE,
                addressSelection.resultCode
            )
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedStreetType requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedStreetType(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedStreetType.value)
        }
    }

    @Test
    fun `test selectStreet`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectStreet()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_STREET, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedStreet requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedStreet(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedStreet.value)
        }
    }

    @Test
    fun `test selectHouse`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectHouse()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_HOUSE, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedHouse requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedHouse(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedHouse.value)
        }
    }

    @Test
    fun `test selectApartment`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectApartment()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_APARTMENT, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedApartment requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedApartment(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedApartment.value)
        }
    }

    @Test
    fun `test selectCorp`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectCorp()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_CORP, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedCorp requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedCorp(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedCorp.value)
        }
    }

    @Test
    fun `test selectZip`() {
        runTest {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")
            addressSearchVM.selectZip()

            val addressSelection = addressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_ZIP, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedZip requestNextField`() {
        runTest {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            addressSearchVM.setSelectedZip(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            assertEquals(item, addressSearchVM.selectedZip.value)
        }
    }

    @Test
    fun `test requestSelectionResult`() {
        val response = mockk<AddressFieldResponse>(relaxed = true)
        val addressIdentifier = mockk<AddressIdentifier>(relaxed = true)
        every { response.address } returns addressIdentifier
        coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
        val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

        addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

        addressSearchVM.requestSelectionResult()
        coVerify(exactly = 1) { apiAddressSearch.getFieldContext(any(), any(), any()) }
        assertEquals(addressIdentifier, addressSearchVM.addressResult.value!!.peekContent())
    }

    @Test
    fun `test requestSelectionResult post already loaded identifier`() {
        val response = mockk<AddressFieldResponse>(relaxed = true)
        val addressIdentifier = mockk<AddressIdentifier>(relaxed = true)
        every { response.address } returns addressIdentifier
        coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
        val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

        addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

        addressSearchVM.requestSelectionResult()
        clearMocks(apiAddressSearch)
        addressSearchVM.requestSelectionResult()

        coVerify(exactly = 0) { apiAddressSearch.getFieldContext(any(), any(), any()) }
        assertEquals(addressIdentifier, addressSearchVM.addressResult.value!!.peekContent())
    }

    @Test
    fun `test setAddressSelectionResult`() {
        val response = mockk<AddressFieldResponse>(relaxed = true)
        val addressIdentifier = mockk<AddressIdentifier>(relaxed = true)
        every { response.address } returns addressIdentifier
        coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
        val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

        addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

        addressSearchVM.setAddressSelectionResult()
        coVerify(exactly = 1) { apiAddressSearch.getFieldContext(any(), any(), any()) }
        assertEquals(addressIdentifier, addressSearchVM.addressResult.value!!.peekContent())
    }

    @Test
    fun `test setAddressSelectionResult if already loaded`() {
        val response = mockk<AddressFieldResponse>(relaxed = true)
        val addressIdentifier = mockk<AddressIdentifier>(relaxed = true)
        every { response.isEndForAddressSelection() } returns true
        every { response.address } returns addressIdentifier
        coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
        val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

        addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

        val item = AddressItem("id", "name", null)
        addressSearchVM.setSelectedCorp(item)

        clearMocks(apiAddressSearch)
        addressSearchVM.setAddressSelectionResult()
        coVerify(exactly = 0) { apiAddressSearch.getFieldContext(any(), any(), any()) }
        assertEquals(addressIdentifier, addressSearchVM.addressResult.value!!.peekContent())
    }

    @Test
    fun `test update live data after set REAL_ESTATE args`() {
        runTest {
            var showCountryFiles = false
            var showCountryFieldMode = -1
            val showCountryFieldObserver = Observer<Boolean>() {
                showCountryFiles = it
            }
            val countryFieldModeObserver = Observer<Int>() {
                showCountryFieldMode = it
            }
            addressSearchVM.showRealEstateField.observeForever(showCountryFieldObserver)
            addressSearchVM.realEstateFieldMode.observeForever(countryFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.realEstateTypeFieldParams.value)
            Assert.assertTrue(showCountryFiles)
            assertEquals(0, showCountryFieldMode)

            addressSearchVM.showRealEstateField.removeObserver(showCountryFieldObserver)
            addressSearchVM.realEstateFieldMode.removeObserver(countryFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set PRECISION args`() {
        runTest {
            var showCountryFiles = false
            var showCountryFieldMode = -1
            val showCountryFieldObserver = Observer<Boolean>() {
                showCountryFiles = it
            }
            val countryFieldModeObserver = Observer<Int>() {
                showCountryFieldMode = it
            }
            addressSearchVM.showPrecisionField.observeForever(showCountryFieldObserver)
            addressSearchVM.precisionFieldMode.observeForever(countryFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.PRECISION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.precisionTypeFieldParams.value)
            Assert.assertTrue(showCountryFiles)
            assertEquals(0, showCountryFieldMode)

            addressSearchVM.showPrecisionField.removeObserver(showCountryFieldObserver)
            addressSearchVM.precisionFieldMode.removeObserver(countryFieldModeObserver)
        }
    }
    @Test
    fun `test update live data after set COUNTRY args`() {
        runTest {
            var showCountryFiles = false
            var showCountryFieldMode = -1
            val showCountryFieldObserver = Observer<Boolean>() {
                showCountryFiles = it
            }
            val countryFieldModeObserver = Observer<Int>() {
                showCountryFieldMode = it
            }
            addressSearchVM.showCountryField.observeForever(showCountryFieldObserver)
            addressSearchVM.countryFieldMode.observeForever(countryFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.countryFieldParams.value)
            Assert.assertTrue(showCountryFiles)
            assertEquals(0, showCountryFieldMode)

            addressSearchVM.showCountryField.removeObserver(showCountryFieldObserver)
            addressSearchVM.countryFieldMode.removeObserver(countryFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set REGION args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            var showError = false
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            val errorFieldObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showRegionsField.observeForever(showObserver)
            addressSearchVM.regionFieldMode.observeForever(regionFieldModeObserver)
            addressSearchVM.showRegionFieldError.observeForever(errorFieldObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.regionFieldParams.value)
            Assert.assertTrue(showField)
            Assert.assertTrue(showError)
            assertEquals(0, showFieldMode)

            addressSearchVM.showRegionsField.removeObserver(showObserver)
            addressSearchVM.regionFieldMode.removeObserver(regionFieldModeObserver)
            addressSearchVM.showRegionFieldError.removeObserver(errorFieldObserver)
        }
    }

    @Test
    fun `test update live data after set DISTRICT args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showDistrictField.observeForever(showObserver)
            addressSearchVM.districtFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.districtFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showDistrictField.removeObserver(showObserver)
            addressSearchVM.districtFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set CITY_TYPE args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showCityTypeField.observeForever(showObserver)
            addressSearchVM.cityTypeFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.cityTypeFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showCityTypeField.removeObserver(showObserver)
            addressSearchVM.cityTypeFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set CITY args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showCityField.observeForever(showObserver)
            addressSearchVM.cityFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.cityFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showCityField.removeObserver(showObserver)
            addressSearchVM.cityFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set POST_OFFICE args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showPostOfficeField.observeForever(showObserver)
            addressSearchVM.postOfficeFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.postOfficeFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showPostOfficeField.removeObserver(showObserver)
            addressSearchVM.postOfficeFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set STREET_TYPE args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showStreetTypeField.observeForever(showObserver)
            addressSearchVM.streetTypeFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.streetTypeFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showStreetTypeField.removeObserver(showObserver)
            addressSearchVM.streetTypeFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set STREET args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showStreetField.observeForever(showObserver)
            addressSearchVM.streetFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.streetFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showStreetField.removeObserver(showObserver)
            addressSearchVM.streetFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set HOUSE args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            addressSearchVM.showHouseField.observeForever(showObserver)
            addressSearchVM.houseFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.houseFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showHouseField.removeObserver(showObserver)
            addressSearchVM.houseFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set APARTMENT args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }

            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }

            addressSearchVM.showApartmentField.observeForever(showObserver)
            addressSearchVM.apartmentFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.apartmentFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showCorpsField.removeObserver(showObserver)
            addressSearchVM.showApartmentField.removeObserver(showObserver)
            addressSearchVM.apartmentFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set DESCRIPTION args`() {
        runTest {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }

            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }

            addressSearchVM.showDescriptionField.observeForever(showObserver)
            addressSearchVM.descriptionFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DESCRIPTION)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.descriptionFieldParams.value)
            Assert.assertTrue(showField)
            assertEquals(0, showFieldMode)

            addressSearchVM.showDescriptionField.removeObserver(showObserver)
            addressSearchVM.descriptionFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set CORPS args`() {
        runTest {
            var showFieldMode = -1
            var showZipFieldMode = -1
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            val zipFieldModeObserver = Observer<Int>() {
                showZipFieldMode = it
            }

            addressSearchVM.corpFieldMode.observeForever(regionFieldModeObserver)
            addressSearchVM.zipFieldMode.observeForever(zipFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.corpsFieldParams.value)
            assertEquals(0, showFieldMode)
            assertEquals(0, showZipFieldMode)

            addressSearchVM.corpFieldMode.removeObserver(regionFieldModeObserver)
            addressSearchVM.zipFieldMode.removeObserver(zipFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set ZIP args`() {
        runTest {
            var showField = false
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            addressSearchVM.showZipField.observeForever(showObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertEquals(data.parameters!![0], addressSearchVM.zipFieldParams.value)
            Assert.assertTrue(showField)

            addressSearchVM.showZipField.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveZipField returns true if data is valid for error`() {
        runTest {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showZipFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP, name = "regexp")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertFalse(showError)

            addressSearchVM.showZipFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveZipField returns false if data is valid`() {
        runTest {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showZipFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP, name = "zipcode")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertTrue(showError)

            addressSearchVM.showZipFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveCorpsField returns true if data is valid for error`() {
        runTest {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showCorpsFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS, name = "regexp")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertFalse(showError)

            addressSearchVM.showCorpsFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveCorpsField returns false if data is valid`() {
        runTest {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showCorpsFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS, name = "zipcode")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertTrue(showError)

            addressSearchVM.showCorpsFieldError.removeObserver(showObserver)
        }
    }


    @Test
    fun `test approveApartmentField returns true if data is valid for error`() {
        runTest {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showApartmentFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT, name = "regexp")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertFalse(showError)

            addressSearchVM.showApartmentFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveApartmentField returns false if data is valid`() {
        runTest {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showApartmentFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT, name = "zipcode")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertTrue(showError)

            addressSearchVM.showApartmentFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveHouseField returns true if data is valid for error`() {
        runTest {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showHouseFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE, name = "regexp")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertFalse(showError)

            addressSearchVM.showHouseFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveHouseField returns false if data is valid`() {
        runTest {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            addressSearchVM.showHouseFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE, name = "zipcode")

            addressSearchVM.setAddressSearchArsImpl(data, "code", "scheme")

            assertTrue(showError)

            addressSearchVM.showHouseFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `no data loaded`() = runTest {
        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REAL_ESTATE)
        addressSearchVM.selectRealEstate()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedRealEstate(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)
        addressSearchVM.selectRegion()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedRegion(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)
        addressSearchVM.selectStreetType()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedStreetType(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)
        addressSearchVM.selectDistrict()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedDistrict(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)
        addressSearchVM.selectCityType()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedCityType(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)
        addressSearchVM.selectHouse()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedHouse(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)
        addressSearchVM.selectStreet()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedStreet(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DESCRIPTION)
        addressSearchVM.selectDescription()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedDescription(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)
        addressSearchVM.selectCorp()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedCorp(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)
        addressSearchVM.selectPostOffice()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedPostOffice(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.PRECISION)
        addressSearchVM.selectPrecision()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedPrecision(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)
        addressSearchVM.selectZip()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedZip(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)

        prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)
        addressSearchVM.selectCountry()
        Assert.assertNull(addressSearchVM.navigateToAddressSelection.value?.getContentIfNotHandled())
        addressSearchVM.setSelectedCountry(addressItem)
        assertFalse(addressSearchVM.loadingFieldData.value == true)
    }
}

class AddressSearchVMImpl(
    apiAddressSearch: ApiAddressSearch,
    errorHandling: WithErrorHandling,
    retryLastAction: WithRetryLastAction,
    addressParameterMapper: AddressParameterMapper,
) : AddressSearchVM(
    apiAddressSearch,
    addressParameterMapper,
    errorHandling, retryLastAction
) {
    fun setAddressSearchArsImplWithoutSetGone(
        data: AddressFieldResponse,
        code: String,
        schema: String
    ) {
        setAddressSearchArs(data, code, schema)
    }

    fun setAddressSearchArsImpl(
        data: AddressFieldResponse,
        code: String,
        schema: String,
        goneDescription: Boolean = false
    ) {
        setAddressSearchArs(data, code, schema, goneDescription)
    }


}