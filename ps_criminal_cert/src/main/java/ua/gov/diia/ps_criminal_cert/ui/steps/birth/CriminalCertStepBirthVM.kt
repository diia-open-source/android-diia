package ua.gov.diia.ps_criminal_cert.ui.steps.birth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.address_search.models.AddressNationality
import ua.gov.diia.address_search.models.NationalityItem
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertBirthPlace
import ua.gov.diia.ps_criminal_cert.models.Birth
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.CombinedLiveData
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.core.util.inputs.isCountryOrCityNameValid
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@HiltViewModel
class CriminalCertStepBirthVM @Inject constructor(
    @AuthorizedClient private val apiAddressSearch: ApiAddressSearch,
    @AuthorizedClient private val api: ApiCriminalCert,
    private val errorHandlingDelegate: WithErrorHandling,
    private val contextMenuDelegate: WithContextMenu<ContextMenuField>,
    private val retryActionDelegate: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialog,
    private val navigationHelper: PSNavigationHelper,
) : ViewModel(), WithErrorHandling by errorHandlingDelegate,
    WithContextMenu<ContextMenuField> by contextMenuDelegate,
    WithRetryLastAction by retryActionDelegate,
    WithRatingDialog by withRatingDialog,
    PSNavigationHelper by navigationHelper {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _onNextEvent = MutableLiveData<UiDataEvent<Pair<CriminalCertScreen, Birth>>>()
    val onNextEvent = _onNextEvent.asLiveData()

    private val _nationalities = MutableLiveData<AddressNationality>()
    val nationalities = _nationalities.asLiveData()

    private val _birthPlace = MutableLiveData<CriminalCertBirthPlace>()
    val birthPlace = _birthPlace.asLiveData()

    private val _navigateToCountrySelection =
        MutableLiveData<UiDataEvent<List<NationalityItem>>>()
    val navigateToCountrySelection = _navigateToCountrySelection.asLiveData()

    val countryInput = MutableLiveData<String?>()
    val otherCountryInput = MutableLiveData<String?>()
    val cityInput = MutableLiveData<String?>()
    val isOtherCountryChecked = MutableLiveData(false)

    val isNextAvailable = CombinedLiveData(
        countryInput,
        otherCountryInput,
        cityInput,
        isOtherCountryChecked
    ) { data ->
        val country = data[0]?.toString()
        val otherCountry = data[1]?.toString()
        val city = data[2]?.toString()
        val isOtherCountryChecked = data[3] as? Boolean
        if (isOtherCountryChecked == true) {
            !otherCountry.isNullOrBlank() && !city.isNullOrBlank()
                    && isCountryOrCityNameValid(otherCountry) && isCountryOrCityNameValid(city)
        } else {
            !country.isNullOrBlank() && !city.isNullOrBlank() && isCountryOrCityNameValid(city)
        }
    }

    fun loadContent() {
        executeAction(progressIndicator = _isLoading) {
            _nationalities.value = apiAddressSearch.getNationalities()
            val res = api.getCriminalCertBirthPlace()
            _birthPlace.value = res
            if (res.data?.country?.value != null) {
                isOtherCountryChecked.value = false
                countryInput.value = res.data.country.value
            }
            res.template?.apply(::showTemplateDialog)
        }
    }

    fun selectCountry() {
        nationalities.value?.also {
            _navigateToCountrySelection.value = UiDataEvent(it.nationalities)
        }
    }

    fun setCountry(item: NationalityItem) {
        countryInput.value = item.name
    }

    fun checkOtherCountry() {
        isOtherCountryChecked.value = isOtherCountryChecked.value == false
        if (isOtherCountryChecked.value == true) {
            countryInput.value = null
        }
    }

    fun onNext() {
        birthPlace.value?.also {
            it.data?.nextScreen ?: return@also
            _onNextEvent.value = UiDataEvent(
                content = it.data.nextScreen to Birth(
                    country = if (isOtherCountryChecked.value == true) {
                        otherCountryInput.value.orEmpty()
                    } else {
                        countryInput.value.orEmpty()
                    },
                    city = cityInput.value.orEmpty()
                )
            )
        }
    }

    fun getRatingForm() {
        getRating(
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }

    fun sendRatingRequest(ratingRequest: RatingRequest) {
        sendRating(
            ratingRequest = ratingRequest,
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }
}