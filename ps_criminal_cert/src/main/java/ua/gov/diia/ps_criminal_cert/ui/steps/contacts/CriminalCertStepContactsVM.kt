package ua.gov.diia.ps_criminal_cert.ui.steps.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertContacts
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.core.util.phone.PHONE_NUMBER_VALIDATION_PATTERN
import ua.gov.diia.core.util.phone.RAW_PHONE_NUMBER_PREFIX
import ua.gov.diia.core.util.phone.removePhoneCodeIfNeed
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@HiltViewModel
class CriminalCertStepContactsVM @Inject constructor(
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

    private val _onNextEvent = MutableLiveData<UiDataEvent<String>>()
    val onNextEvent = _onNextEvent.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _state = MutableLiveData<CriminalCertContacts>()
    val state = _state.asLiveData()

    val phoneInput = MutableLiveData<String?>()

    val isNextAvailable = phoneInput.map {
        validateRawPhoneNumber(it)
    }

    fun loadContent() {
        executeAction(progressIndicator = _isLoading) {
            _state.value = api.getCriminalCertContacts().also {
                it.template?.apply(::showTemplateDialog)
            }
            if (phoneInput.value == null) {
                phoneInput.value = state.value?.phoneNumber?.removePhoneCodeIfNeed()
            }
        }
    }

    fun onNext() {
        _onNextEvent.value = UiDataEvent("+$RAW_PHONE_NUMBER_PREFIX${phoneInput.value}")
    }

    private fun validateRawPhoneNumber(rawNumber: String?): Boolean {
        val phoneNumber = "${RAW_PHONE_NUMBER_PREFIX}$rawNumber"
        return phoneNumber.matches(PHONE_NUMBER_VALIDATION_PATTERN.toRegex())
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