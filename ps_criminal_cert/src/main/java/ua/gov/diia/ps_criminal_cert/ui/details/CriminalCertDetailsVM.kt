package ua.gov.diia.ps_criminal_cert.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithPushNotification
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.delegation.download_files.base64.DownloadableBase64File
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType.DOWNLOAD_ARCHIVE
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType.VIEW_PDF
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails.LoadAction
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CriminalCertDetailsVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val errorHandlingDelegate: WithErrorHandling,
    private val contextMenuDelegate: WithContextMenu<ContextMenuField>,
    private val retryActionDelegate: WithRetryLastAction,
    private val pushDelegate: WithPushNotification,
    private val withRatingDialog: WithRatingDialog,
    private val navigationHelper: PSNavigationHelper,
) : ViewModel(), WithErrorHandling by errorHandlingDelegate,
    WithContextMenu<ContextMenuField> by contextMenuDelegate,
    WithRetryLastAction by retryActionDelegate,
    WithPushNotification by pushDelegate,
    WithRatingDialog by withRatingDialog,
    PSNavigationHelper by navigationHelper{

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _state = MutableLiveData<CriminalCertDetails>()
    val state = _state.asLiveData()

    private val _pdfEvent = MutableLiveData<UiDataEvent<DownloadableBase64File>>()
    val pdfEvent = _pdfEvent.asLiveData()

    private val _zipEvent = MutableLiveData<UiDataEvent<DownloadableBase64File>>()
    val zipEvent = _zipEvent.asLiveData()

    private val _isZipLoading = MutableLiveData<Boolean>()
    private val zipLoadingObserver = Observer<Boolean> {
        setLoading(DOWNLOAD_ARCHIVE, it)
    }
    private val _isPdfLoading = MutableLiveData<Boolean>()
    private val pdfLoadingObserver = Observer<Boolean> {
        setLoading(VIEW_PDF, it)
    }

    private val _screenHeader = MutableLiveData<String>()
    val screenHeader = _screenHeader.asLiveData()

    init {
        _isPdfLoading.observeForever(pdfLoadingObserver)
        _isZipLoading.observeForever(zipLoadingObserver)
    }

    fun load(certId: String) {
        executeAction(progressIndicator = _isLoading) {
            _state.value = api.getCriminalCertsDetails(certId).also {
                setContextMenu(it.contextMenu?.toTypedArray())
                if (it.ratingForm != null) {
                    showRatingDialog(it.ratingForm)
                }
                it.navigationPanel?.header?.let { h ->
                    setScreenName(h)
                }
            }
        }
    }

    fun loadAction(loadAction: LoadAction, certId: String) {
        when (loadAction.type) {
            DOWNLOAD_ARCHIVE -> downloadZip(certId)
            VIEW_PDF -> downloadPdf(certId)
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

    private fun downloadZip(certId: String) {
        state.value?.also {
            executeAction(progressIndicator = _isZipLoading) {
                val res = api.getCriminalCertZip(certId)
                res.file?.also {
                    _zipEvent.value = UiDataEvent(
                        DownloadableBase64File(
                            file = it,
                            name = "vytiah_pro_nesudymist_${
                                DateFormats.criminalCertFileFormat.format(
                                    Date()
                                )
                            }.zip",
                            mimeType = "application/zip"
                        )
                    )
                }
                res.template?.apply(::showTemplateDialog)
            }
        }
    }

    private fun downloadPdf(certId: String) {
        state.value?.also {
            executeAction(progressIndicator = _isPdfLoading) {
                val res = api.getCriminalCertPdf(certId)
                res.file?.also {
                    _pdfEvent.value = UiDataEvent(
                        DownloadableBase64File(
                            file = it,
                            name = "vytiah_pro_nesudymist_${
                                DateFormats.criminalCertFileFormat.format(
                                    Date()
                                )
                            }.pdf",
                            mimeType = "application/pdf"
                        )
                    )
                }
                res.template?.apply(::showTemplateDialog)
            }
        }
    }

    private fun setLoading(type: CriminalCertLoadActionType, isLoading: Boolean) {
        val state = _state.value ?: return
        _state.value = state.copy(
            loadActions = state.loadActions?.map {
                if (type == it.type) {
                    it.copy(isEnabled = !isLoading, isLoading = isLoading)
                } else {
                    it.copy(isEnabled = !isLoading)
                }
            }.orEmpty()
        )
    }

    override fun onCleared() {
        super.onCleared()
        _isPdfLoading.removeObserver(pdfLoadingObserver)
        _isZipLoading.removeObserver(zipLoadingObserver)
    }

    fun setScreenName(header: String) {
        _screenHeader.postValue(header)
    }
}