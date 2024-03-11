package ua.gov.diia.ps_criminal_cert.ui.steps.confirm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.ps_criminal_cert.models.CriminalCertUserData
import ua.gov.diia.ps_criminal_cert.models.request.CriminalCertConfirmationRequest
import ua.gov.diia.ps_criminal_cert.models.request.CriminalCertConfirmationRequest.BirthPlace
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertConfirmation
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepConfirmVM @Inject constructor(
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _isOrdering = MutableLiveData<Boolean>()
    val isOrdering = _isOrdering.asLiveData()

    private val _navigateToDetails = MutableLiveData<UiDataEvent<String>>()
    val navigateToDetails = _navigateToDetails.asLiveData()

    val isConfirmed = MutableLiveData<Boolean>()

    private val _state = MutableLiveData<CriminalCertConfirmation>()
    val state = _state.asLiveData()

    val openLinkListenerAM: (String) -> Unit = { link -> _openLinkAM.value = UiDataEvent(link) }

    private val _openLinkAM = MutableLiveData<UiDataEvent<String>>()
    val openLinkAM = _openLinkAM.asLiveData()

    private var applicationId: String? = null

    fun load(data: CriminalCertUserData) {
        executeAction(progressIndicator = _isLoading) {
            _state.value = api.getCriminalCertConfirmationData(getRequestData(data))
            state.value?.template?.run(::showTemplateDialog)
        }
    }

    fun confirm(data: CriminalCertUserData) {
        executeAction(progressIndicator = _isOrdering) {
            val res = api.orderCriminalCert(getRequestData(data))
            res.template?.run(::showTemplateDialog)
            applicationId = res.applicationId
            res.navigationPanel?.contextMenu?.let {
                setContextMenu(it.toTypedArray())
            }
        }
    }

    fun navigateToDetails() {
        val applicationId = applicationId ?: return
        _navigateToDetails.value = UiDataEvent(applicationId)
    }

    private fun getRequestData(data: CriminalCertUserData): CriminalCertConfirmationRequest {
        return CriminalCertConfirmationRequest(
            reasonId = data.reasonId,
            certificateType = data.certificateType,
            previousFirstName = if (data.prevNames?.previousFirstNameList.isNullOrEmpty()) {
                null
            } else {
                data.prevNames?.previousFirstNameList?.joinToString(", ")
            },
            previousMiddleName = if (data.prevNames?.previousMiddleNameList.isNullOrEmpty()) {
                null
            } else {
                data.prevNames?.previousMiddleNameList?.joinToString(", ")
            },
            previousLastName = if (data.prevNames?.previousLastNameList.isNullOrEmpty()) {
                null
            } else {
                data.prevNames?.previousLastNameList?.joinToString(", ")
            },
            birthPlace = if (data.birth == null) {
                null
            } else {
                BirthPlace(
                    country = data.birth.country,
                    city = data.birth.city
                )
            },
            nationalities = data.nationalities,
            registrationAddressId = data.registrationAddressId,
            phoneNumber = data.phoneNumber,
            publicService = data.publicService
        )
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