package ua.gov.diia.ps_criminal_cert.ui.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertApplicationInfoNextStep
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertInfo
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@HiltViewModel
class CriminalCertWelcomeVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val errorHandlingDelegate: WithErrorHandling,
    private val contextMenuDelegate: WithContextMenu<ContextMenuField>,
    private val retryActionDelegate: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialog,
    private val navigationHelper: PSNavigationHelper,
    private val criminalCertHelper: PSCriminalCertHelper,
) : ViewModel(), WithErrorHandling by errorHandlingDelegate,
    WithContextMenu<ContextMenuField> by contextMenuDelegate,
    WithRetryLastAction by retryActionDelegate,
    WithRatingDialog by withRatingDialog,
    PSNavigationHelper by navigationHelper,
    PSCriminalCertHelper by criminalCertHelper {

    private val _state = MutableLiveData<CriminalCertInfo>()
    val state = _state.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _navigateToReasons = MutableLiveData<UiEvent>()
    val navigateToReasons = _navigateToReasons.asLiveData()

    private val _navigateToRequester = MutableLiveData<UiEvent>()
    val navigateToRequester = _navigateToRequester.asLiveData()

    private val _navigateToDetails = MutableLiveData<UiDataEvent<String>>()
    val navigateToDetails = _navigateToDetails.asLiveData()

    val directionFlag = MutableStateFlow<Boolean?>(null)
    val resId = MutableLiveData<String?>()

    fun loadContent(publicService: String?, directionFlag: Boolean, resourceId: String?) {
        this.directionFlag.value = directionFlag
        this.resId.value = resourceId
        executeAction(progressIndicator = _isLoading) {
            _state.value = api.getCriminalCertInfo(publicService).also { res ->
                if (res.showContextMenu != true) {
                    setContextMenu(null)
                }
                res.template?.apply(::showTemplateDialog)
            }
        }
    }

     fun onNext() {
        when (state.value?.nextScreen) {
            CriminalCertApplicationInfoNextStep.reasons.name -> {
                _navigateToReasons.value = UiEvent()
            }
            CriminalCertApplicationInfoNextStep.requester.name -> {
                _navigateToRequester.value = UiEvent()
            } else  ->{
                //nothing
            }
        }
    }

    fun handleNotification(certId: String?) {
        if (certId != null && navigateToDetails.value == null) {
            _navigateToDetails.value = UiDataEvent(certId)
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