package ua.gov.diia.address_search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CompoundAddressSearchVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var compoundAddressSearchVM: CompoundAddressSearchVM

    lateinit var apiAddressSearch: ApiAddressSearch
    lateinit var clientAlertDialogsFactory: ClientAlertDialogsFactory
    lateinit var addressParameterMapper: AddressParameterMapper

    @Before
    fun setUp() {
        apiAddressSearch = mockk()
        clientAlertDialogsFactory = mockk(relaxed = true)
        addressParameterMapper = mockk(relaxed = true)
        compoundAddressSearchVM =
            CompoundAddressSearchVM(apiAddressSearch, clientAlertDialogsFactory, addressParameterMapper)
    }

    lateinit var addressParam: AddressParameter
    lateinit var addressItem: AddressItem
    lateinit var addressValidation: AddressValidation
    lateinit var paramsGetItems: Array<AddressItem>

    val fieldTitle = "title"
    val fieldDescription = "description"
    fun prepareMockForGetFieldSetupAction(filedType: String, name: String = "address_name", initAddress: Boolean = false): AddressFieldResponse {
        addressParam = mockk<AddressParameter>(relaxed = true)
        addressItem = mockk<AddressItem>()
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

        var address: AddressIdentifier? = null
        if(initAddress) {
            address = mockk()
        }
        return AddressFieldResponse(fieldTitle, fieldDescription, addressParameterList, address, null, null)
    }

    @Test
    fun `test setArgs set title and description`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            Assert.assertEquals(fieldTitle, compoundAddressSearchVM.screenHeader.value)
            Assert.assertEquals(fieldDescription, compoundAddressSearchVM.addressDescription.value)
        }
    }


    @Test
    fun `test setArgs not set twice set title and description`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val addressParam = mockk<AddressParameter>(relaxed = true)
            val addressItem = mockk<AddressItem>()
            val paramsGetItems = arrayOf(addressItem)
            val addressValidation = mockk<AddressValidation>()
            every { addressItem.name } returns "address_name"
            every { addressValidation.regexp } returns "regexp"
            every { addressParam.getDefaultAddress() } returns addressItem
            every { addressParam.type } returns AddressSearchFieldType.FieldType.COUNTRY
            every { addressParam.getSearchType() } returns SearchType.LIST
            every { addressParam.getItems() } returns paramsGetItems
            every { addressParam.validation } returns addressValidation

            val addressParameterList = mutableListOf<AddressParameter>()
            addressParameterList.add(addressParam)
            val fieldTitle2: String = "fieldTitle2"
            val fieldDescription2: String = "fieldDescription2"
            val secondData =
                AddressFieldResponse(fieldTitle, fieldDescription, addressParameterList, null, null, null)

            compoundAddressSearchVM.setArs(secondData, "code", "scheme")

            Assert.assertNotEquals(fieldTitle2, compoundAddressSearchVM.screenHeader.value)
            Assert.assertNotEquals(
                fieldDescription2,
                compoundAddressSearchVM.addressDescription.value
            )
        }
    }

    @Test
    fun `test selectCountry`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            compoundAddressSearchVM.selectCountry()

            var addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            assertEquals(CompoundAddressResultKey.RESULT_KEY_COUNTRY, addressSelection.resultCode)
            assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCountry no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectCountry()

            var addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setFieldParams isEndForAddressSelection true and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)
            val item = AddressItem("id", "name", "errormessage")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            compoundAddressSearchVM.setSelectedCountry(item)

            coVerify(exactly = 1) { response.isEndForAddressSelection() }
            coVerify(exactly = 1) { response.address }

            compoundAddressSearchVM.launchRetryAction()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_COUNTRY,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test setSelectedCountry requestNextField`() {
        runBlocking {
            val loadingState = mutableListOf<Boolean>()
            val observer = Observer<Boolean> {
                loadingState.add(it)
            }
            compoundAddressSearchVM.loading.observeForever(observer)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", "errormessage")
            compoundAddressSearchVM.setSelectedCountry(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedCountry.value)

            Assert.assertEquals(false, compoundAddressSearchVM.loading.value)
            Assert.assertEquals(true, loadingState[0])
            Assert.assertEquals(false, loadingState[1])
            compoundAddressSearchVM.loading.removeObserver(observer)
        }
    }

    @Test
    fun `test setAddressSelectionResult`() {
        val loadingState = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            loadingState.add(it)
        }

        compoundAddressSearchVM.actionButtonLoading.observeForever(observer)
        val response = mockk<AddressFieldResponse>(relaxed = true)
        val addressIdentifier = mockk<AddressIdentifier>(relaxed = true)
        every { response.address } returns addressIdentifier
        coEvery { apiAddressSearch.getFieldContext(any(), any(), any()) } returns response
        val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

        compoundAddressSearchVM.setArs(data, "code", "scheme")
        clearMocks(apiAddressSearch)
        coEvery { apiAddressSearch.getFieldContext(any(), any(), any()) } returns response

        compoundAddressSearchVM.setAddressSelectionResult()
        coVerify(exactly = 1) { apiAddressSearch.getFieldContext(any(), any(), any()) }
        Assert.assertEquals(
            addressIdentifier,
            compoundAddressSearchVM.setAddressSelection.value!!.peekContent()
        )

        clearMocks(apiAddressSearch)
        compoundAddressSearchVM.launchRetryAction()
        coVerify(exactly = 1) { apiAddressSearch.getFieldContext(any(), any(), any()) }
        Assert.assertEquals(
            addressIdentifier,
            compoundAddressSearchVM.setAddressSelection.value!!.peekContent()
        )
        Assert.assertEquals(true, loadingState[0])
        Assert.assertEquals(false, loadingState[1])
        compoundAddressSearchVM.actionButtonLoading.removeObserver(observer)
    }

    @Test
    fun `test setAddressSelectionResult reset address selection if address identifier was set`() {
        val loadingState = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            loadingState.add(it)
        }

        compoundAddressSearchVM.actionButtonLoading.observeForever(observer)
        val response = mockk<AddressFieldResponse>(relaxed = true)
        val addressIdentifier = mockk<AddressIdentifier>(relaxed = true)
        every { response.address } returns addressIdentifier
        coEvery { apiAddressSearch.getFieldContext(any(), any(), any()) } returns response
        val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS, initAddress = true)

        compoundAddressSearchVM.setArs(data, "code", "scheme")
        clearMocks(apiAddressSearch)
        coEvery { apiAddressSearch.getFieldContext(any(), any(), any()) } returns response

        compoundAddressSearchVM.setAddressSelectionResult()

        assertEquals(
            data.address,
            compoundAddressSearchVM.setAddressSelection.value!!.peekContent()
        )
    }
    @Test
    fun `test selectRegion`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectRegion()

            var addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_REGION,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)

        }
    }

    @Test
    fun `test selectRegion no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectRegion()

            var addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedRegion requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedRegion(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedRegion.value)

            clearMocks(apiAddressSearch)
            compoundAddressSearchVM.launchRetryAction()
            var addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_REGION,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectDistrict`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectDistrict()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_DISTRICT,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectDistrict no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectDistrict()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedDistrict requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedDistrict(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedDistrict.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_DISTRICT,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCityType`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectCityType()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_CITY_TYPE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCityType no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectCityType()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedCityType requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedCityType(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedCityType.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_CITY_TYPE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCity`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectCity()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_CITY,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCity no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectCity()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedCity requestNextField`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedCity(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedCity.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_CITY,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectPostOffice`() {
        runBlocking {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectPostOffice()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_POST_OFFICE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectPostOffice no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectPostOffice()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedPostOffice requestNextField`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedPostOffice(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedPostOffice.value)
        }
    }

    @Test
    fun `test retry of selectPostOffice`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery {
                apiAddressSearch.getFieldContext(
                    "code",
                    "scheme",
                    any()
                )
            } throws RuntimeException("error")
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedPostOffice(item)

            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_POST_OFFICE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test consume exception of no internet`() {
        runBlocking {
            val alertNoInternetTemplate = mockk<TemplateDialogModel>()
            every { clientAlertDialogsFactory.alertNoInternet() } returns alertNoInternetTemplate
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery {
                apiAddressSearch.getFieldContext(
                    any(),
                    any(),
                    any()
                )
            } throws SocketTimeoutException("error")
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedPostOffice(item)
            assertEquals(
                alertNoInternetTemplate,
                compoundAddressSearchVM.showTemplateDialog.value!!.peekContent()
            )
        }
    }

    @Test
    fun `test consume runtime exception`() {
        runBlocking {
            val err = java.lang.RuntimeException("error")
            val alertTemplate = mockk<TemplateDialogModel>()
            every {
                clientAlertDialogsFactory.unknownErrorAlert(
                    any(),
                    e = err
                )
            } returns alertTemplate
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext(any(), any(), any()) } throws err
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedPostOffice(item)
            assertEquals(
                alertTemplate,
                compoundAddressSearchVM.showTemplateDialog.value!!.peekContent()
            )
        }
    }

    @Test
    fun `test selectStreetType`() {
        runBlocking {
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectStreetType()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_STREET_TYPE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }
    @Test
    fun `test selectStreetType no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectStreetType()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedStreetType requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data =
                prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedStreetType(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedStreetType.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_STREET_TYPE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectStreet`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectStreet()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_STREET,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectStreet no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectStreet()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedStreet requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedStreet(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedStreet.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_STREET,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectHouse`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectHouse()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_HOUSE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectHouse no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectHouse()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value
            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedHouse requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedHouse(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedHouse.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_HOUSE,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectApartment`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectApartment()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_APARTMENT,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectApartment no processing if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectApartment()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedApartment requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedApartment(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedApartment.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_APARTMENT,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCorp`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectCorp()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_CORP,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectCorp no prcess if not value`() {
        runBlocking {
            compoundAddressSearchVM.selectCorp()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value

            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedCorp requestNextField and retry`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedCorp(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedCorp.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_CORP,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectZip`() {
        runBlocking {
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            compoundAddressSearchVM.setArs(data, "code", "scheme")
            compoundAddressSearchVM.selectZip()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_ZIP,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test selectZip not act if no zipFieldParam`() {
        runBlocking {
            compoundAddressSearchVM.selectZip()

            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value
            assertNull(addressSelection)
        }
    }

    @Test
    fun `test setSelectedZip requestNextField`() {
        runBlocking {
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            val item = AddressItem("id", "name", null)
            compoundAddressSearchVM.setSelectedZip(item)

            coVerify(exactly = 1) { apiAddressSearch.getFieldContext("code", "scheme", any()) }
            Assert.assertEquals(item, compoundAddressSearchVM.selectedZip.value)

            compoundAddressSearchVM.launchRetryAction()
            val addressSelection =
                compoundAddressSearchVM.navigateToAddressSelection.value!!.peekContent()

            Assert.assertEquals(
                CompoundAddressResultKey.RESULT_KEY_ZIP,
                addressSelection.resultCode
            )
            Assert.assertEquals(SearchType.LIST, addressSelection.searchType)
            Assert.assertArrayEquals(paramsGetItems, addressSelection.items)
        }
    }

    @Test
    fun `test update live data after set COUNTRY args`() {
        runBlocking {
            var showCountryFiles = false
            var showCountryFieldMode = -1
            val showCountryFieldObserver = Observer<Boolean>() {
                showCountryFiles = it
            }
            val countryFieldModeObserver = Observer<Int>() {
                showCountryFieldMode = it
            }
            compoundAddressSearchVM.showCountryField.observeForever(showCountryFieldObserver)
            compoundAddressSearchVM.countryFieldMode.observeForever(countryFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.COUNTRY)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.countryFieldParams.value)
            assertTrue(showCountryFiles)
            assertEquals(0, showCountryFieldMode)

            compoundAddressSearchVM.showCountryField.removeObserver(showCountryFieldObserver)
            compoundAddressSearchVM.countryFieldMode.removeObserver(countryFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set REGION args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showRegionsField.observeForever(showObserver)
            compoundAddressSearchVM.regionFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.REGION)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.regionFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showRegionsField.removeObserver(showObserver)
            compoundAddressSearchVM.regionFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set DISTRICT args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showDistrictField.observeForever(showObserver)
            compoundAddressSearchVM.districtFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.DISTRICT)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.districtFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showDistrictField.removeObserver(showObserver)
            compoundAddressSearchVM.districtFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set CITY_TYPE args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showCityTypeField.observeForever(showObserver)
            compoundAddressSearchVM.cityTypeFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY_TYPE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.cityTypeFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showCityTypeField.removeObserver(showObserver)
            compoundAddressSearchVM.cityTypeFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set CITY args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showCityField.observeForever(showObserver)
            compoundAddressSearchVM.cityFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CITY)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.cityFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showCityField.removeObserver(showObserver)
            compoundAddressSearchVM.cityFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set POST_OFFICE args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showPostOfficeField.observeForever(showObserver)
            compoundAddressSearchVM.postOfficeFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.POST_OFFICE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.postOfficeFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showPostOfficeField.removeObserver(showObserver)
            compoundAddressSearchVM.postOfficeFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set STREET_TYPE args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showStreetTypeField.observeForever(showObserver)
            compoundAddressSearchVM.streetTypeFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET_TYPE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.streetTypeFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showStreetTypeField.removeObserver(showObserver)
            compoundAddressSearchVM.streetTypeFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set STREET args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showStreetField.observeForever(showObserver)
            compoundAddressSearchVM.streetFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.STREET)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.streetFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showStreetField.removeObserver(showObserver)
            compoundAddressSearchVM.streetFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set HOUSE args`() {
        runBlocking {
            var showField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showHouseField.observeForever(showObserver)
            compoundAddressSearchVM.houseFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.houseFieldParams.value)
            assertTrue(showField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showHouseField.removeObserver(showObserver)
            compoundAddressSearchVM.houseFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set APARTMENT args`() {
        runBlocking {
            var showField = false
            var showCorpsField = false
            var showFieldMode = -1
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            val showCorpsObserver = Observer<Boolean>() {
                showCorpsField = it
            }
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            compoundAddressSearchVM.showCorpsField.observeForever(showCorpsObserver)
            compoundAddressSearchVM.showApartmentField.observeForever(showObserver)
            compoundAddressSearchVM.apartmentFieldMode.observeForever(regionFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.apartmentFieldParams.value)
            assertTrue(showField)
            assertTrue(showCorpsField)
            assertEquals(0, showFieldMode)

            compoundAddressSearchVM.showCorpsField.removeObserver(showObserver)
            compoundAddressSearchVM.showApartmentField.removeObserver(showObserver)
            compoundAddressSearchVM.apartmentFieldMode.removeObserver(regionFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set CORPS args`() {
        runBlocking {
            var showFieldMode = -1
            var showZipFieldMode = -1
            val regionFieldModeObserver = Observer<Int>() {
                showFieldMode = it
            }
            val zipFieldModeObserver = Observer<Int>() {
                showZipFieldMode = it
            }

            compoundAddressSearchVM.corpFieldMode.observeForever(regionFieldModeObserver)
            compoundAddressSearchVM.zipFieldMode.observeForever(zipFieldModeObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.corpsFieldParams.value)
            assertEquals(0, showFieldMode)
            assertEquals(0, showZipFieldMode)

            compoundAddressSearchVM.corpFieldMode.removeObserver(regionFieldModeObserver)
            compoundAddressSearchVM.zipFieldMode.removeObserver(zipFieldModeObserver)
        }
    }

    @Test
    fun `test update live data after set ZIP args`() {
        runBlocking {
            var showField = false
            val showObserver = Observer<Boolean>() {
                showField = it
            }
            compoundAddressSearchVM.showZipField.observeForever(showObserver)
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP)

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertEquals(data.parameters!![0], compoundAddressSearchVM.zipFieldParams.value)
            assertTrue(showField)

            compoundAddressSearchVM.showZipField.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveZipField returns true if data is valid for error`() {
        runBlocking {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showZipFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP, name = "regexp")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            Assert.assertFalse(showError)

            compoundAddressSearchVM.showZipFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveZipField returns false if data is valid`() {
        runBlocking {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showZipFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.ZIP, name = "zipcode")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertTrue(showError)

            compoundAddressSearchVM.showZipFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveCorpsField returns true if data is valid for error`() {
        runBlocking {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showCorpsFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS, name = "regexp")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            Assert.assertFalse(showError)

            compoundAddressSearchVM.showCorpsFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveCorpsField returns false if data is valid`() {
        runBlocking {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showCorpsFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.CORPS, name = "zipcode")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertTrue(showError)

            compoundAddressSearchVM.showCorpsFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveApartmentField returns true if data is valid for error`() {
        runBlocking {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showApartmentFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT, name = "regexp")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            Assert.assertFalse(showError)

            compoundAddressSearchVM.showApartmentFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveApartmentField returns false if data is valid`() {
        runBlocking {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showApartmentFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.APARTMENT, name = "zipcode")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertTrue(showError)

            compoundAddressSearchVM.showApartmentFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveHouseField returns true if data is valid for error`() {
        runBlocking {
            var showError = false
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showHouseFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE, name = "regexp")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            Assert.assertFalse(showError)

            compoundAddressSearchVM.showHouseFieldError.removeObserver(showObserver)
        }
    }

    @Test
    fun `test approveHouseField returns false if data is valid`() {
        runBlocking {
            var showError = true
            val showObserver = Observer<Boolean>() {
                showError = it
            }
            compoundAddressSearchVM.showHouseFieldError.observeForever(showObserver)
            val response = mockk<AddressFieldResponse>(relaxed = true)
            every { response.isEndForAddressSelection() } returns true
            coEvery { apiAddressSearch.getFieldContext("code", "scheme", any()) } returns response
            val data = prepareMockForGetFieldSetupAction(AddressSearchFieldType.FieldType.HOUSE, name = "zipcode")

            compoundAddressSearchVM.setArs(data, "code", "scheme")

            assertTrue(showError)

            compoundAddressSearchVM.showHouseFieldError.removeObserver(showObserver)
        }
    }
}