package ua.gov.diia.address_search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.gov.diia.address_search.models.AddressFieldApproveRequest
import ua.gov.diia.address_search.models.AddressFieldRequest
import ua.gov.diia.address_search.models.AddressFieldRequestValue
import ua.gov.diia.address_search.models.AddressFieldResponse
import ua.gov.diia.address_search.models.AddressIdentifier
import ua.gov.diia.address_search.models.AddressItem
import ua.gov.diia.address_search.models.AddressParameter
import ua.gov.diia.address_search.models.AddressSearchRequest
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.ui_base.fragments.errordialog.RequestTryCountTracker
import ua.gov.diia.core.util.CombinedLiveData
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.noInternetException
import ua.gov.diia.core.util.extensions.vm.executeAction
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class CompoundAddressSearchVM @Inject constructor(
    @AuthorizedClient private val apiAddressSearch: ApiAddressSearch,
    private val addressParameterMapper: AddressParameterMapper,
    private val errorHandling: WithErrorHandling,
    private val retryLastAction: WithRetryLastAction,
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandling by errorHandling{

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading.asLiveData()

    private val _actionButtonLoading = MutableLiveData<Boolean>()
    val actionButtonLoading = _actionButtonLoading.asLiveData()

    private val _screenHeader = MutableLiveData<String?>()
    val screenHeader = _screenHeader.asLiveData()

    private val _addressDescription = MutableLiveData<String?>()
    val addressDescription = _addressDescription.asLiveData()

    //---------- Retry request logic ---------------
    companion object{
        private const val TRY_REQUEST_COUNT = 1
    }

    private val _tryLoadRequestCount = RequestTryCountTracker()

    private var _currentRequest: Request? = null

    private enum class Request {
        REQUEST_RESULT,
        COUNTRY,
        REGION,
        DISTRICT,
        CITY_TYPE,
        CITY,
        POST_OFFICE,
        STREET_TYPE,
        STREET,
        HOUSE,
        APARTMENT,
        CORP,
        ZIP
    }

    fun launchRetryAction() {
        when (_currentRequest) {
            Request.REQUEST_RESULT -> setAddressSelectionResult()
            Request.COUNTRY -> selectCountry()
            Request.REGION -> selectRegion()
            Request.DISTRICT -> selectDistrict()
            Request.CITY_TYPE -> selectCityType()
            Request.CITY -> selectCity()
            Request.POST_OFFICE -> selectPostOffice()
            Request.STREET_TYPE -> selectStreetType()
            Request.STREET -> selectStreet()
            Request.HOUSE -> selectHouse()
            Request.APARTMENT -> selectApartment()
            Request.CORP -> selectCorp()
            Request.ZIP -> selectZip()
            else -> {}
        }
    }

    // ---------- Config --------------------

    private var _featureCode: String? = null
    private var _addressSchema: String? = null

    private val _navigateToAddressSelection = MutableLiveData<UiDataEvent<AddressSearchRequest>>()
    val navigateToAddressSelection = _navigateToAddressSelection.asLiveData()

    private var _isArgsHasBeenHandled: Boolean = false

    fun setArs(data: AddressFieldResponse, code: String, schema: String) {
        if (!_isArgsHasBeenHandled) {
            _isArgsHasBeenHandled = true
            _screenHeader.value = data.title
            _addressDescription.value = data.description
            _featureCode = code
            _addressSchema = schema
            setFieldParams(data)
        }
    }

    //------------ Country -------------------

    private val _countryFieldParams = MutableLiveData<AddressParameter?>()
    val countryFieldParams = _countryFieldParams.asLiveData()

    val showCountryField: LiveData<Boolean> = _countryFieldParams.map { params ->
        params != null
    }

    val countryFieldMode: LiveData<Int> = _countryFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedCountry = MutableLiveData<AddressItem>()
    val selectedCountry = _selectedCountry.asLiveData()

    val countryInput = MutableLiveData<String?>()

    fun selectCountry() {
        _countryFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_COUNTRY, params)
        }
    }

    fun setSelectedCountry(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _countryFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.COUNTRY,
            requestData = request,
            cleanUpEvent = this::cleanUpCountryGroup,
            nextFieldLoaded = { _selectedCountry.value = item }
        )
    }

    //------------ Region ---------------------

    private val _regionFieldParams = MutableLiveData<AddressParameter?>()
    val regionFieldParams = _regionFieldParams.asLiveData()

    val showRegionsField: LiveData<Boolean> = _regionFieldParams.map { params ->
        params != null
    }

    val regionFieldMode: LiveData<Int> = _regionFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedRegion = MutableLiveData<AddressItem?>()
    val selectedRegion = _selectedRegion.asLiveData()

    val regionInput = MutableLiveData<String?>()

    fun selectRegion() {
        _regionFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_REGION, params)
        }
    }

    fun setSelectedRegion(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _regionFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.REGION,
            requestData = request,
            cleanUpEvent = this::cleanUpRegionGroup,
            nextFieldLoaded = { _selectedRegion.value = item }
        )
    }

    //------------ District -------------------

    private val _districtFieldParams = MutableLiveData<AddressParameter?>()
    val districtFieldParams = _districtFieldParams.asLiveData()

    val showDistrictField: LiveData<Boolean> = _districtFieldParams.map { params ->
        params != null
    }

    val districtFieldMode: LiveData<Int> = _districtFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedDistrict = MutableLiveData<AddressItem?>()
    val selectedDistrict = _selectedDistrict.asLiveData()

    val districtInput = MutableLiveData<String?>()

    fun selectDistrict() {
        _districtFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_DISTRICT, params)
        }
    }

    fun setSelectedDistrict(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _districtFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.DISTRICT,
            requestData = request,
            cleanUpEvent = this::cleanUpDistrictGroup,
            nextFieldLoaded = { _selectedDistrict.value = item }
        )
    }

    //------------ City type -------------------

    private val _cityTypeFieldParams = MutableLiveData<AddressParameter?>()
    val cityTypeFieldParams = _cityTypeFieldParams.asLiveData()

    val showCityTypeField: LiveData<Boolean> = _cityTypeFieldParams.map { params ->
        params != null
    }

    val cityTypeFieldMode: LiveData<Int> = _cityTypeFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedCityType = MutableLiveData<AddressItem?>()
    val selectedCityType = _selectedCityType.asLiveData()

    val cityTypeInput = MutableLiveData<String?>()

    fun selectCityType() {
        _cityTypeFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_CITY_TYPE, params)
        }
    }

    fun setSelectedCityType(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _cityTypeFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.CITY_TYPE,
            requestData = request,
            cleanUpEvent = this::cleanUpCityTypeGroup,
            nextFieldLoaded = { _selectedCityType.value = item }
        )
    }

    //------------ City -------------------

    private val _cityFieldParams = MutableLiveData<AddressParameter?>()
    val cityFieldParams = _cityFieldParams.asLiveData()

    val showCityField: LiveData<Boolean> = _cityFieldParams.map { params ->
        params != null
    }

    val cityFieldMode: LiveData<Int> = _cityFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedCity = MutableLiveData<AddressItem?>()
    val selectedCity = _selectedCity.asLiveData()

    val cityInput = MutableLiveData<String?>()

    fun selectCity() {
        _cityFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_CITY, params)
        }
    }

    fun setSelectedCity(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _cityFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.CITY,
            requestData = request,
            cleanUpEvent = this::cleanUpCityGroup,
            nextFieldLoaded = { _selectedCity.value = item }
        )
    }

    //------------ PostOffice -------------------

    private val _postOfficeFieldParams = MutableLiveData<AddressParameter?>()
    val postOfficeFieldParams = _postOfficeFieldParams.asLiveData()

    val showPostOfficeField: LiveData<Boolean> = _postOfficeFieldParams.map { params ->
        params != null
    }

    val postOfficeFieldMode: LiveData<Int> = _postOfficeFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedPostOffice = MutableLiveData<AddressItem?>()
    val selectedPostOffice = _selectedPostOffice.asLiveData()

    val postOfficeInput = MutableLiveData<String?>()

    fun selectPostOffice() {
        _postOfficeFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_POST_OFFICE, params)
        }
    }

    fun setSelectedPostOffice(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _postOfficeFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.POST_OFFICE,
            requestData = request,
            cleanUpEvent = this::cleanUpPostOfficeGroup,
            nextFieldLoaded = { _selectedPostOffice.value = item }
        )
    }

    //------------ Street type -------------------

    private val _streetTypeFieldParams = MutableLiveData<AddressParameter?>()
    val streetTypeFieldParams = _streetTypeFieldParams.asLiveData()

    val showStreetTypeField: LiveData<Boolean> = _streetTypeFieldParams.map { params ->
        params != null
    }

    val streetTypeFieldMode: LiveData<Int> = _streetTypeFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedStreetType = MutableLiveData<AddressItem?>()
    val selectedStreetType = _selectedStreetType.asLiveData()

    val streetTypeInput = MutableLiveData<String?>()

    fun selectStreetType() {
        _streetTypeFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_STREET_TYPE, params)
        }
    }

    fun setSelectedStreetType(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _streetTypeFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.STREET_TYPE,
            requestData = request,
            cleanUpEvent = this::cleanUpStreetTypeGroup,
            nextFieldLoaded = { _selectedStreetType.value = item }
        )
    }

    //------------ Street -------------------

    private val _streetFieldParams = MutableLiveData<AddressParameter?>()
    val streetFieldParams = _streetFieldParams.asLiveData()

    val showStreetField: LiveData<Boolean> = _streetFieldParams.map { params ->
        params != null
    }

    val streetFieldMode: LiveData<Int> = _streetFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedStreet = MutableLiveData<AddressItem?>()
    val selectedStreet = _selectedStreet.asLiveData()

    val streetInput = MutableLiveData<String?>()

    fun selectStreet() {
        _streetFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_STREET, params)
        }
    }

    fun setSelectedStreet(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _streetFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.STREET,
            requestData = request,
            cleanUpEvent = this::cleanUpStreetGroup,
            nextFieldLoaded = { _selectedStreet.value = item }
        )
    }

    //------------ House -------------------

    private val _houseFieldParams = MutableLiveData<AddressParameter?>()
    val houseFieldParams = _houseFieldParams.asLiveData()

    val showHouseField: LiveData<Boolean> = _houseFieldParams.map { params ->
        params != null
    }

    val houseFieldMode: LiveData<Int> = _houseFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedHouse = MutableLiveData<AddressItem?>()
    val selectedHouse = _selectedHouse.asLiveData()

    val house = MutableLiveData<String?>()

    private val houseValidationRegex = MutableLiveData<String?>()

    private val houseValidationPattern: Pattern? by lazy {
        houseValidationRegex.value.let {
            Pattern.compile(
                it
            )
        }
    }

    private fun approveHouseField(value: String): Boolean =
        houseValidationPattern?.matcher(value)?.matches() ?: true

    val showHouseFieldError: LiveData<Boolean> = house.map { house ->
        if (house != null) {
            !approveHouseField(house)
        } else {
            false
        }
    }

    fun selectHouse() {
        _houseFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_HOUSE, params)
        }
    }

    fun setSelectedHouse(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _houseFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.HOUSE,
            requestData = request,
            cleanUpEvent = {},
            nextFieldLoaded = { _selectedHouse.value = item }
        )
    }

    //------------ Apartment -------------------

    private val _apartmentFieldParams = MutableLiveData<AddressParameter?>()
    val apartmentFieldParams = _apartmentFieldParams.asLiveData()

    val showApartmentField: LiveData<Boolean> = _apartmentFieldParams.map { params ->
        params != null
    }

    val apartmentFieldMode: LiveData<Int> = _apartmentFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedApartment = MutableLiveData<AddressItem?>()
    val selectedApartment = _selectedApartment.asLiveData()

    val apartment = MutableLiveData<String?>()

    private val apartmentValidationRegex = MutableLiveData<String?>()

    private val apartmentValidationPattern: Pattern? by lazy {
        apartmentValidationRegex.value.let {
            Pattern.compile(
                it
            )
        }
    }

    private fun approveApartmentField(value: String): Boolean =
        apartmentValidationPattern?.matcher(value)?.matches() ?: true

    val showApartmentFieldError: LiveData<Boolean> = apartment.map { apartment ->
        if (apartment != null) {
            !approveApartmentField(apartment)
        } else {
            false
        }
    }

    fun selectApartment() {
        _apartmentFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_APARTMENT, params)
        }
    }

    fun setSelectedApartment(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _apartmentFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.APARTMENT,
            requestData = request,
            cleanUpEvent = {},
            nextFieldLoaded = { _selectedApartment.value = item }
        )
    }

    //------------ Corps -------------------

    private val _corpsFieldParams = MutableLiveData<AddressParameter?>()
    val corpsFieldParams = _corpsFieldParams.asLiveData()

    val showCorpsField: LiveData<Boolean> = _apartmentFieldParams.map { params ->
        params != null
    }

    val corpFieldMode: LiveData<Int> = _corpsFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedCorp = MutableLiveData<AddressItem?>()
    val selectedCorp = _selectedCorp.asLiveData()

    val corps = MutableLiveData<String?>()

    private val corpsValidationRegex = MutableLiveData<String?>()

    private val corpsValidationPattern: Pattern? by lazy {
        corpsValidationRegex.value.let {
            Pattern.compile(
                it
            )
        }
    }

    private fun approveCorpsField(value: String): Boolean =
        corpsValidationPattern?.matcher(value)?.matches() ?: true

    val showCorpsFieldError: LiveData<Boolean> = corps.map { corps ->
        if (corps != null) {
            !approveCorpsField(corps)
        } else {
            false
        }
    }

    fun selectCorp() {
        _corpsFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_CORP, params)
        }
    }

    fun setSelectedCorp(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _corpsFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.CORP,
            requestData = request,
            cleanUpEvent = {},
            nextFieldLoaded = { _selectedCorp.value = item }
        )
    }

    //------------ Zip -------------------

    private val _zipFieldParams = MutableLiveData<AddressParameter?>()
    val zipFieldParams = _zipFieldParams.asLiveData()

    val showZipField: LiveData<Boolean> = _zipFieldParams.map { params ->
        params != null
    }

    val zipFieldMode: LiveData<Int> = _corpsFieldParams.map { params ->
        addressParameterMapper.getViewMode(params)
    }

    private val _selectedZip = MutableLiveData<AddressItem?>()
    val selectedZip = _selectedZip.asLiveData()

    val zip = MutableLiveData<String?>()

    private val zipValidationRegex = MutableLiveData<String?>()

    private val zipValidationPattern: Pattern? by lazy {
        zipValidationRegex.value.let {
            Pattern.compile(
                it
            )
        }
    }

    private fun approveZipField(value: String): Boolean =
        zipValidationPattern?.matcher(value)?.matches() ?: true

    val showZipFieldError: LiveData<Boolean> = zip.map { zip ->
        if (zip != null) {
            !approveZipField(zip)
        } else {
            false
        }
    }

    fun selectZip() {
        _zipFieldParams.value?.let { params ->
            startSelectionProcess(CompoundAddressResultKey.RESULT_KEY_ZIP, params)
        }
    }

    fun setSelectedZip(item: AddressItem) {
        val request = AddressFieldRequestValue(
            id = item.id,
            type = _zipFieldParams.value?.type,
            value = item.name
        )

        requestNextField(
            requestKey = Request.ZIP,
            requestData = request,
            cleanUpEvent = {},
            nextFieldLoaded = { _selectedZip.value = item }
        )
    }

    //----------- Field params control --------

    private fun setFieldParams(data: AddressFieldResponse) {
        //If this is the end of selection we'll get address identifier object and
        //we should setup it to notify VM that this this the end of the selection process
        if (data.isEndForAddressSelection()) {
            _addressIdentifier = data.address
            return
        }

        data.parameters?.forEach { param ->
            //setup corresponding field with the parameters and default values
            getFieldSetupAction(param.type).invoke(param, param.getDefaultAddress())
        }
    }

    private fun getFieldSetupAction(type: String?): (param: AddressParameter, default: AddressItem?) -> Unit =
        when (type) {
            AddressSearchFieldType.FieldType.COUNTRY -> { p, d ->
                _countryFieldParams.value = p
                d?.let(_selectedCountry::setValue)
            }
            AddressSearchFieldType.FieldType.REGION, AddressSearchFieldType.FieldType.TEXT_REGION -> { p, d ->
                _regionFieldParams.value = p
                d?.let(_selectedRegion::setValue)
            }
            AddressSearchFieldType.FieldType.DISTRICT, AddressSearchFieldType.FieldType.TEXT_DISTRICT -> { p, d ->
                _districtFieldParams.value = p
                d?.let(_selectedDistrict::setValue)
            }
            AddressSearchFieldType.FieldType.CITY_TYPE -> { p, d ->
                _cityTypeFieldParams.value = p
                d?.let(_selectedCityType::setValue)
            }
            AddressSearchFieldType.FieldType.CITY, AddressSearchFieldType.FieldType.TEXT_CITY -> { p, d ->
                _cityFieldParams.value = p
                d?.let(_selectedCity::setValue)
            }
            AddressSearchFieldType.FieldType.POST_OFFICE, AddressSearchFieldType.FieldType.TEXT_POST_OFFICE -> { p, d ->
                _postOfficeFieldParams.value = p
                d?.let(_selectedPostOffice::setValue)
            }
            AddressSearchFieldType.FieldType.STREET_TYPE -> { p, d ->
                _streetTypeFieldParams.value = p
                d?.let(_selectedStreetType::setValue)
            }
            AddressSearchFieldType.FieldType.STREET -> { p, d ->
                _streetFieldParams.value = p
                d?.let(_selectedStreet::setValue)
            }
            AddressSearchFieldType.FieldType.HOUSE -> { p, d ->
                _houseFieldParams.value = p
                houseValidationRegex.value = p.validation?.regexp
                d?.name?.let(house::setValue)
            }
            AddressSearchFieldType.FieldType.APARTMENT -> { p, d ->
                _apartmentFieldParams.value = p
                apartmentValidationRegex.value = p.validation?.regexp
                d?.name?.let(apartment::setValue)
            }
            AddressSearchFieldType.FieldType.CORPS -> { p, d ->
                _corpsFieldParams.value = p
                corpsValidationRegex.value = p.validation?.regexp
                d?.name?.let(corps::setValue)
            }
            AddressSearchFieldType.FieldType.ZIP -> { p, d ->
                _zipFieldParams.value = p
                zipValidationRegex.value = p.validation?.regexp
                d?.name?.let(zip::setValue)
            }
            else -> { _, _ -> }
        }

    private fun cleanUpCountryGroup() {
        _regionFieldParams.value = null
        _selectedRegion.value = null
        regionInput.value = null
        cleanUpRegionGroup()
    }

    private fun cleanUpRegionGroup() {
        _districtFieldParams.value = null
        _selectedDistrict.value = null
        districtInput.value = null
        cleanUpDistrictGroup()
    }

    private fun cleanUpDistrictGroup() {
        _cityTypeFieldParams.value = null
        _selectedCityType.value = null
        cityTypeInput.value = null
        cleanUpCityTypeGroup()
    }

    private fun cleanUpCityTypeGroup() {
        _cityFieldParams.value = null
        _selectedCity.value = null
        cityInput.value = null
        cleanUpCityGroup()
    }

    private fun cleanUpCityGroup() {
        _streetTypeFieldParams.value = null
        _selectedStreetType.value = null
        streetTypeInput.value = null
        cleanUpStreetTypeGroup()
        cleanUpPostOfficeGroup()
    }

    private fun cleanUpPostOfficeGroup() {
        _postOfficeFieldParams.value = null
        _selectedPostOffice.value = null
        postOfficeInput.value = null
//        cleanUpStreetTypeGroup()
    }

    private fun cleanUpStreetTypeGroup() {
        _streetFieldParams.value = null
        _selectedStreet.value = null
        streetInput.value = null
        cleanUpStreetGroup()
    }

    private fun cleanUpStreetGroup() {
        _houseFieldParams.value = null
        _selectedHouse.value = null
        house.value = null

        _apartmentFieldParams.value = null
        _selectedApartment.value = null
        apartment.value = null

        _corpsFieldParams.value = null
        _selectedCorp.value = null
        corps.value = null

        _zipFieldParams.value = null
        _selectedZip.value = null
        zip.value = null
    }

    //----------- Action button control ------

    private val _countryApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _countryFieldParams, _selectedCountry, countryInput
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _regionApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _regionFieldParams, _selectedRegion, regionInput
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _districtApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _districtFieldParams, _selectedDistrict, districtInput
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _cityTypeApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _cityTypeFieldParams, _selectedCityType, cityTypeInput
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _cityApproveData: LiveData<AddressFieldApproveRequest> = CombinedLiveData(
        _cityFieldParams, _selectedCity, cityInput
    ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _postOfficeApproveData: LiveData<AddressFieldApproveRequest> = CombinedLiveData(
        _postOfficeFieldParams, _selectedPostOffice, postOfficeInput
    ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _streetTypeApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _streetTypeFieldParams, _selectedStreetType, streetTypeInput
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _streetApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _streetFieldParams, _selectedStreet, streetInput
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _houseApproveData: LiveData<AddressFieldApproveRequest> = CombinedLiveData(
        _houseFieldParams, _selectedHouse, house
    ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _apartmentApproveData: LiveData<AddressFieldApproveRequest> =
        CombinedLiveData(
            _apartmentFieldParams, _selectedApartment, apartment
        ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _corpApproveData: LiveData<AddressFieldApproveRequest> = CombinedLiveData(
        _corpsFieldParams, _selectedCorp, corps
    ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _zipApproveData: LiveData<AddressFieldApproveRequest> = CombinedLiveData(
        _zipFieldParams, _selectedZip, zip
    ) { values -> addressParameterMapper.toFieldApproveRequest(values) }

    private val _resultsSourceApproved: LiveData<Boolean> = CombinedLiveData(
        _countryApproveData,
        _regionApproveData,
        _districtApproveData,
        _cityTypeApproveData,
        _cityApproveData,
        _postOfficeApproveData,
        _streetTypeApproveData,
        _streetApproveData,
        _houseApproveData,
        _apartmentApproveData,
        _corpApproveData,
        _zipApproveData
    ) { values ->
        values.all { value -> addressParameterMapper.approveFiledData(value) }
    }

    //Enable button when all mandatory fields was field by the user.
    //NOTE!! if some editable fields present we should check it in [setAddressSelectionResult]
    //and send it to the back-end to get [addressIdentifier] to complete the flow
    val enableActionButton: LiveData<Boolean> = _resultsSourceApproved.map { isFieldApproved ->
        isFieldApproved
    }

    private var _addressIdentifier: AddressIdentifier? = null

    //----------- Common logic -----------------

    private fun startSelectionProcess(
        resultKey: String,
        fieldParams: AddressParameter
    ) {
        val request = AddressSearchRequest(
            resultCode = resultKey,
            searchType = fieldParams.getSearchType(),
            items = fieldParams.getItems()
        )

        _navigateToAddressSelection.value = UiDataEvent(request)
    }

    private fun requestNextField(
        requestKey: Request,
        requestData: AddressFieldRequestValue,
        cleanUpEvent: () -> Unit,
        nextFieldLoaded: () -> Unit
    ) {
        _currentRequest = requestKey

        executeAction {
            //validates request input data
            val code = _featureCode ?: return@executeAction
            val schema = _addressSchema ?: return@executeAction
            //creates request object
            val request = AddressFieldRequest(listOf(requestData))
            //starting loading process
            _loading.value = true
            val result = apiAddressSearch.getFieldContext(code, schema, request)
            //clean up dependent fields before sent a new one
            cleanUpEvent.invoke()
            //sets field params for the corresponding fields
            setFieldParams(result)
            //after data has been loaded and set into appropriate field notifies caller
            //to set his selection
            nextFieldLoaded.invoke()
            //resets the loading counter
            _tryLoadRequestCount.reset()
        }

    }

    //----------- Complete selection form -------

    private val _setAddressSelection = MutableLiveData<UiDataEvent<AddressIdentifier>>()
    val setAddressSelection = _setAddressSelection.asLiveData()

    fun setAddressSelectionResult() {
        _currentRequest = Request.REQUEST_RESULT

        val addressIdentifier = _addressIdentifier

        //If the [AddressIdentifier] is present -> it means that all mandatory fields data was
        //collected and sent to the server
        if (addressIdentifier != null) {
            _setAddressSelection.value = UiDataEvent(addressIdentifier)
        }
        //If this value is absent -> we need to collect data from editable fields and send it
        //to the backend to get the [AddressIdentifier] to complete flow.
        else {

            executeAction {
                _actionButtonLoading.value = true
                val code = _featureCode ?: return@executeAction
                val schema = _addressSchema ?: return@executeAction
                val request = AddressFieldRequest(getRequestList())

                val result = apiAddressSearch.getFieldContext(code, schema, request)
                //return result form the address selection if the [AddressIdentifier] isn't a null
                result.address?.let { addressIdentifier ->
                    _setAddressSelection.value = UiDataEvent(addressIdentifier)
                }
                _tryLoadRequestCount.reset()
            }
        }
    }

    fun getRequestList(): List<AddressFieldRequestValue> {

        val country = addressParameterMapper.getEditableModeFieldRequest(
            value = countryInput.value,
            params = _countryFieldParams.value
        )

        val region = addressParameterMapper.getEditableModeFieldRequest(
            value = regionInput.value,
            params = _regionFieldParams.value
        )

        val district = addressParameterMapper.getEditableModeFieldRequest(
            value = districtInput.value,
            params = _districtFieldParams.value
        )

        val cityType = addressParameterMapper.getEditableModeFieldRequest(
            value = cityTypeInput.value,
            params = _cityTypeFieldParams.value
        )

        val city = addressParameterMapper.getEditableModeFieldRequest(
            value = cityInput.value,
            params = _cityFieldParams.value
        )

        val postOffice = addressParameterMapper.getEditableModeFieldRequest(
            value = postOfficeInput.value,
            params = _postOfficeFieldParams.value
        )

        val streetType = addressParameterMapper.getEditableModeFieldRequest(
            value = streetTypeInput.value,
            params = _streetTypeFieldParams.value
        )

        val street = addressParameterMapper.getEditableModeFieldRequest(
            value = streetInput.value,
            params = _streetFieldParams.value
        )

        val house = addressParameterMapper.getEditableModeFieldRequest(
            value = house.value,
            params = _houseFieldParams.value
        )

        val apartment = addressParameterMapper.getEditableModeFieldRequest(
            value = apartment.value,
            params = _apartmentFieldParams.value
        )

        val corp = addressParameterMapper.getEditableModeFieldRequest(
            value = corps.value,
            params = _corpsFieldParams.value
        )

        val zip = addressParameterMapper.getEditableModeFieldRequest(
            value = zip.value,
            params = _zipFieldParams.value
        )

        return listOfNotNull(
            country,
            region,
            district,
            cityType,
            city,
            postOffice,
            streetType,
            street,
            house,
            apartment,
            corp,
            zip
        )
    }

}