package ua.gov.diia.ps_criminal_cert.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.common.message.StubMessage
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.lifecycle.combineWith
import ua.gov.diia.ui_base.util.paging.offsetPagingData
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.ps_criminal_cert.models.CriminalCertHomeState
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertListData.CertItem
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import javax.inject.Inject

@HiltViewModel
class CriminalCertHomeVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val errorHandlingDelegate: WithErrorHandling,
    private val contextMenuDelegate: WithContextMenu<ContextMenuField>,
    private val retryActionDelegate: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialog,
    private val navigationHelper: PSNavigationHelper,
    private val criminalCertHelper: PSCriminalCertHelper
) : ViewModel(),
    WithErrorHandling by errorHandlingDelegate,
    WithContextMenu<ContextMenuField> by contextMenuDelegate,
    WithRetryLastAction by retryActionDelegate,
    WithRatingDialog by withRatingDialog,
    PSNavigationHelper by navigationHelper,
    PSCriminalCertHelper by criminalCertHelper {

    private var certId: String? = null
    val directionFlag = MutableStateFlow<Boolean?>(null)
    val resId = MutableLiveData<String?>()

    private val _state = MutableLiveData(CriminalCertHomeState())
    val state = _state.asLiveData()

    private val showProcessingListDelimiter =
        _state.map { it.hasProcessingList }

    private val showDoneListDelimiter = _state.map { it.hasDoneList }

    private val _isDoneListLoading = MutableLiveData<Boolean>()
    private val _isProcessingLoading = MutableLiveData<Boolean>()
    val isLoading =
        _isProcessingLoading.combineWith(_isDoneListLoading) { isProcessingLoading, isDoneListLoading ->
            isProcessingLoading != false || isDoneListLoading != false
        }.combineWith(_state) { isLoading, state ->
            isLoading != false && state?.hasContent != true
        }

    private val processingListEmptyState = MutableLiveData<StubMessage?>()
    private val doneListEmptyState = MutableLiveData<StubMessage?>()

    private val _navigateToWelcome = MutableLiveData<UiDataEvent<Boolean>>()
    val navigateToWelcome = _navigateToWelcome.asLiveData()

    private val _navigateToDetails = MutableLiveData<UiDataEvent<String>>()
    val navigateToDetails = _navigateToDetails.asLiveData()

    private val initialDoneList = MutableStateFlow<Boolean?>(null)
    private val initialProcessingList = MutableStateFlow<Boolean?>(null)

    private val doneListContent = offsetPagingData { offset, pageSize ->
        fetchDoneList(offset, pageSize)
    }

    private val processingListContent = offsetPagingData { offset, pageSize ->
        fetchProcessingList(offset, pageSize)
    }

    private val _screenHeader = MutableLiveData<String>()
    val screenHeader = _screenHeader.asLiveData()

    init {
        viewModelScope.launch {
            initialDoneList.combine(initialProcessingList) { hasDoneList, hasProcessingList ->
                if (hasDoneList == false && hasProcessingList == false) {
                    _navigateToWelcome.value = UiDataEvent(true)
                } else if (hasDoneList == true || hasProcessingList == true) {
                    handleNotification()
                }
            }.collect()
        }
    }

    fun setDoneListLoading(isLoading: Boolean) {
        _isDoneListLoading.value = isLoading
    }

    fun setProcessingListLoading(isLoading: Boolean) {
        _isProcessingLoading.value = isLoading
    }

    fun setListRefreshing() {
        initialDoneList.value = null
        initialProcessingList.value = null
    }

    fun setCertId(
        certId: String?,
        directionFlag: Boolean,
        resourceId: String?
    ) {
        this.certId = certId
        this.directionFlag.value = directionFlag
        this.resId.value = resourceId
    }

    private fun handleNotification() {
        val certId = certId
        if (certId != null && navigateToDetails.value == null) {
            _navigateToDetails.value = UiDataEvent(certId)
        }
    }

    fun onNext() {
        _navigateToWelcome.value = UiDataEvent(false)
    }

    fun listContent(status: CriminalCertStatus): LiveData<PagingData<CertItem>> {
        return when (status) {
            CriminalCertStatus.PROCESSING -> processingListContent
            CriminalCertStatus.DONE -> doneListContent
        }
    }

    fun stubMessage(status: CriminalCertStatus): LiveData<StubMessage?> {
        return when (status) {
            CriminalCertStatus.PROCESSING -> processingListEmptyState
            CriminalCertStatus.DONE -> doneListEmptyState
        }
    }

    fun delimiterState(status: CriminalCertStatus): LiveData<Boolean?> {
        return when (status) {
            CriminalCertStatus.PROCESSING -> showProcessingListDelimiter
            CriminalCertStatus.DONE -> showDoneListDelimiter
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

    fun setScreenName(header: String) {
        _screenHeader.postValue(header)
    }

    @VisibleForTesting
    suspend fun fetchDoneList(offset: Int, pageSize: Int) = api.getCriminalCertList(
        certStatus = CriminalCertStatus.DONE.str,
        skip = offset,
        limit = pageSize
    ).let { listData ->
        if (initialDoneList.value == null) { // initial page loading
            val hasDoneList = !listData.certificates.isNullOrEmpty()
            _state.value = state.value?.copy(hasDoneList = hasDoneList)
            initialDoneList.value = hasDoneList

            doneListEmptyState.value = listData.stubMessage
        }
        listData.navigationPanel?.contextMenu?.let {
            setContextMenu(it.toTypedArray())
        }
        listData.navigationPanel?.header?.let {
            setScreenName(it)
        }
        listData.certificates.orEmpty()
    }

    @VisibleForTesting
    suspend fun fetchProcessingList(offset: Int, pageSize: Int) = api.getCriminalCertList(
        certStatus = CriminalCertStatus.PROCESSING.str,
        skip = offset,
        limit = pageSize
    ).let { listData ->
        if (initialProcessingList.value == null) { // initial page loading
            val hasProcessingList = !listData.certificates.isNullOrEmpty()
            _state.value =
                state.value?.copy(hasProcessingList = hasProcessingList)
            initialProcessingList.value = hasProcessingList

            processingListEmptyState.value = listData.stubMessage
        }
        listData.navigationPanel?.contextMenu?.let {
            setContextMenu(it.toTypedArray())
        }
        listData.navigationPanel?.header?.let {
            setScreenName(it)
        }
        listData.certificates.orEmpty()
    }
}
