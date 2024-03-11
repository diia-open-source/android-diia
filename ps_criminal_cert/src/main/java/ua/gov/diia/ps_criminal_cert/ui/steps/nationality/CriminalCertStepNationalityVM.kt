package ua.gov.diia.ps_criminal_cert.ui.steps.nationality

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.ui_base.views.NameModel
import ua.gov.diia.address_search.models.AddressNationality
import ua.gov.diia.address_search.models.NationalityItem
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertNationalities
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ui_base.views.common.card_item.DiiaCardInputField.FieldMode.BUTTON
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepNationalityVM @Inject constructor(
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

    private val _state = MutableLiveData<CriminalCertNationalities>()
    val state = _state.asLiveData()

    private val _nationalities = MutableLiveData<AddressNationality>()
    val nationalities = _nationalities.asLiveData()

    private val _onNextEvent = MutableLiveData<UiDataEvent<Pair<CriminalCertScreen, List<String>>>>()
    val onNextEvent = _onNextEvent.asLiveData()

    private val _navigateToCountrySelection = MutableLiveData<UiDataEvent<List<NationalityItem>>>()
    val navigateToCountrySelection = _navigateToCountrySelection.asLiveData()

    private var selectionItem: NameModel? = null

    fun loadContent() {
        executeAction(progressIndicator = _isLoading) {
            _nationalities.value = apiAddressSearch.getNationalities()
            api.getCriminalCertNationalities().also { res ->
                _state.value = CriminalCertNationalities(
                    data = res.data,
                    countryList = state.value?.countryList?.map { item ->
                        item.copy(
                            hint = res.data?.country?.hint.orEmpty(),
                            title = res.data?.country?.label.orEmpty()
                        )
                    } ?: listOf(
                        NameModel(
                            id = "0",
                            name = "",
                            withRemove = false,
                            hint = res.data?.country?.hint.orEmpty(),
                            title = res.data?.country?.label.orEmpty(),
                            fieldMode = BUTTON
                        )
                    )
                )
                res.template?.apply(::showTemplateDialog)
            }
        }
    }

    fun addCountry() {
        _state.value?.also { state ->
            if (state.canAdd) {
                _state.value = state.copy(
                    countryList = state.countryList.plus(
                        NameModel(
                            id = "${state.countryList.size}",
                            name = "",
                            hint = state.data?.country?.hint.orEmpty(),
                            title = state.data?.country?.label.orEmpty(),
                            fieldMode = BUTTON
                        )
                    )
                )
            }
        }
    }

    fun removeCountry(model: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                countryList = state.countryList.toMutableList().apply {
                    remove(model)
                }
            )
        }
    }

    fun setCountry(item: NationalityItem) {
        selectionItem?.also {
            updateCountry(it.copy(name = item.name))
        }
    }

    private fun updateCountry(model: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                countryList = state.countryList.map {
                    if (model.id == it.id) {
                        it.copy(name = model.name)
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun selectCountry(model: NameModel) {
        nationalities.value?.also { addressNationality ->
            selectionItem = model
            val selectedList = state.value?.countryList.orEmpty()
            val nationalityList = addressNationality.nationalities.toMutableList()
            nationalityList.removeIf { item ->
                if(selectedList.size > 1 && item.name.uppercase() == "УКРАЇНА") {
                    true
                } else {
                    selectedList.find { it.name == item.name } != null
                }
            }
            _navigateToCountrySelection.value = UiDataEvent(nationalityList)
        }
    }

    fun onNext() {
        state.value?.also { state ->
            state.data?.nextScreen ?: return@also
            _onNextEvent.value = UiDataEvent(
                content = state.data.nextScreen to state.countryList.map { it.name }
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