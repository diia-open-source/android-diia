package ua.gov.diia.ps_criminal_cert.ui.steps.requester

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.ui_base.views.NameModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.models.PreviousNames
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertRequester
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepRequesterVM @Inject constructor(
    private val application: Application,
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

    private val _state = MutableLiveData<CriminalCertRequester>()
    val state = _state.asLiveData()
    val isAllNamesValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(_state) { state ->
            value = state?.let {
                it.prevLastNameData.allValid &&
                        it.prevMiddleNameData.allValid &&
                        it.prevFirstNameData.allValid
            }
        }
    }

    private val _onNextEvent =
        MutableLiveData<UiDataEvent<Pair<CriminalCertScreen, PreviousNames>>>()
    val onNextEvent = _onNextEvent.asLiveData()

    fun loadContent() {
        executeAction(progressIndicator = _isLoading) {
            api.getCriminalCertRequester().also { res ->
                _state.value = res.copy(
                    prevFirstNameData = state.value?.prevFirstNameData ?: res.prevFirstNameData,
                    prevLastNameData = state.value?.prevLastNameData ?: res.prevLastNameData,
                    prevMiddleNameData = state.value?.prevMiddleNameData ?: res.prevMiddleNameData
                )
                res.template?.run(::showTemplateDialog)
            }
        }
    }

    fun addFirstName() {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevFirstNameData = state.prevFirstNameData.copy(
                    list = state.prevFirstNameData.list.plus(
                        NameModel(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            title = application.getString(R.string.criminal_cert_requester_prev_first_name),
                            hint = application.getString(R.string.criminal_cert_requester_first_name)
                        )
                    )
                )
            )
        }
    }

    fun addMiddleName() {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevMiddleNameData = state.prevMiddleNameData.copy(
                    list = state.prevMiddleNameData.list.plus(
                        NameModel(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            title = application.getString(R.string.criminal_cert_requester_prev_middle_name),
                            hint = application.getString(R.string.criminal_cert_requester_middle_name)
                        )
                    )
                )
            )
        }
    }

    fun addLastName() {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevLastNameData = state.prevLastNameData.copy(
                    list = state.prevLastNameData.list.plus(
                        NameModel(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            title = application.getString(R.string.criminal_cert_requester_prev_last_name),
                            hint = application.getString(R.string.criminal_cert_requester_last_name)
                        )
                    )
                )
            )
        }
    }

    fun removeFirstName(name: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevFirstNameData = state.prevFirstNameData.copy(
                    list = state.prevFirstNameData.list.toMutableList().apply {
                        remove(name)
                    }
                )
            )
        }
    }

    fun removeMiddleName(name: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevMiddleNameData = state.prevMiddleNameData.copy(
                    list = state.prevMiddleNameData.list.toMutableList().apply {
                        remove(name)
                    }
                )
            )
        }
    }

    fun removeLastName(name: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevLastNameData = state.prevLastNameData.copy(
                    list = state.prevLastNameData.list.toMutableList().apply {
                        remove(name)
                    }
                )
            )
        }
    }

    fun updateFirstName(name: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevFirstNameData = state.prevFirstNameData.copy(
                    list = state.prevFirstNameData.list.map {
                        if (name.id == it.id) {
                            it.copy(name = name.name, isValid = name.isValid)
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }

    fun updateMiddleName(name: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevMiddleNameData = state.prevMiddleNameData.copy(
                    list = state.prevMiddleNameData.list.map {
                        if (name.id == it.id) {
                            it.copy(name = name.name, isValid = name.isValid)
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }

    fun updateLastName(name: NameModel) {
        _state.value?.also { state ->
            _state.value = state.copy(
                prevLastNameData = state.prevLastNameData.copy(
                    list = state.prevLastNameData.list.map {
                        if (name.id == it.id) {
                            it.copy(name = name.name, isValid = name.isValid)
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }

    fun onNext() {
        state.value?.also { state ->
            state.requesterDataScreen ?: return@also
            _onNextEvent.value = UiDataEvent(
                content = state.requesterDataScreen.nextScreen to PreviousNames(
                    previousFirstNameList = state.prevFirstNameData.list.map { it.name },
                    previousMiddleNameList = state.prevMiddleNameData.list.map { it.name },
                    previousLastNameList = state.prevLastNameData.list.map { it.name }
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