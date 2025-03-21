package ua.gov.diia.ps_criminal_cert.ui.details

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.delegation.download_files.base64.DownloadableBase64File
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.RATING_SERVICE_CATEGORY
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.RATING_SERVICE_CODE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_STATUS_LOAD_PDF_ACTION
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.SCREEN_STATUS_LOAD_ZIP_ACTION
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.navigation.BaseNavigation
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CriminalCertStatusComposeVM @Inject constructor(
    @AuthorizedClient private val api: ApiCriminalCert,
    private val withContextMenu: WithContextMenu<ContextMenuField>,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val navigationHelper: PSNavigationHelper,
    private val withRatingDialog: WithRatingDialogOnFlow,
    private val criminalCertStatusMapper: CriminalCertStatusComposeMapper
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    PSNavigationHelper by navigationHelper,
    WithContextMenu<ContextMenuField> by withContextMenu,
    WithRatingDialogOnFlow by withRatingDialog,
    CriminalCertStatusComposeMapper by criminalCertStatusMapper {

    private val _contentLoadedKey =
        MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL)
    private val _contentLoaded = MutableStateFlow(false)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to value
        }

    private val _progressIndicatorKey = MutableStateFlow("")
    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator: Flow<Pair<String, Boolean>> =
        _progressIndicator.combine(_progressIndicatorKey) { value, key ->
            key to value
        }

    private val _topGroupData = mutableStateListOf<UIElementData>()
    val topGroupData: SnapshotStateList<UIElementData> = _topGroupData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _bottomData = mutableStateListOf<UIElementData>()
    val bottomData: SnapshotStateList<UIElementData> = _bottomData

    private val _navigation =
        MutableSharedFlow<NavigationPath>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val navigation = _navigation.asSharedFlow()

    private var applicationId: String? = null

    private val _shareCert = mutableSharedFlowOf<DownloadableBase64File>()
    val shareCert = _shareCert.asSharedFlow()

    private val _downloadCert = mutableSharedFlowOf<DownloadableBase64File>()
    val downloadCert = _downloadCert.asSharedFlow()

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TOOLBAR_CONTEXT_MENU -> {
                _navigation.tryEmit(BaseNavigation.ContextMenu(withContextMenu.getMenu()))
            }
        }
        val action = event.action
        when (action?.type) {
            SCREEN_STATUS_LOAD_PDF_ACTION -> {
                applicationId?.let { downloadPdf(it) }
            }

            SCREEN_STATUS_LOAD_ZIP_ACTION -> {
                applicationId?.let { downloadZip(it) }
            }
        }
    }

    fun getScreenContent(id: String) {
        executeActionOnFlow(
            contentLoadedIndicator = _contentLoaded.also {
                _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL
            })
        {
            applicationId = id
            api.getCriminalCertsDetails(id).let { response ->
                _topGroupData.addIfNotNull(
                    response.navigationPanel.toTopGroupOrg().also {
                        response.navigationPanel?.contextMenu?.let {
                            setContextMenu(it.toTypedArray())
                        }
                    }
                )
                _bodyData.addAllIfNotNull(
                    response.title.toComposeTitleLabelMlc(),
                    response.statusMessage.toStatusMessageMlcOldApi()
                )
                _bottomData.addAllIfNotNull(
                    response.loadActions.toBtnIconGroupMlc()
                )
            }
        }
    }

    fun downloadPdf(applicationId: String) {
        executeActionOnFlow(progressIndicator = _progressIndicator.also {
            _progressIndicatorKey.value = SCREEN_STATUS_LOAD_PDF_ACTION
        }) {
            val response = api.getCriminalCertPdf(applicationId)
            response.template?.let { showTemplateDialog(it) }
            response.file?.let {
                val downloadRequest = DownloadableBase64File(
                    file = it,
                    name = "vytiah_pro_nesudymist_${
                        DateFormats.criminalCertFileFormat.format(
                            Date()
                        )
                    }.pdf",
                    mimeType = "application/pdf"
                )
                _shareCert.tryEmit(downloadRequest)
            }
        }
    }

    private fun downloadZip(applicationId: String) {
        executeActionOnFlow(progressIndicator = _progressIndicator.also {
            _progressIndicatorKey.value = SCREEN_STATUS_LOAD_ZIP_ACTION
        }) {
            val response = api.getCriminalCertZip(applicationId)
            response.template?.let { showTemplateDialog(it) }
            response.file?.let {
                val downloadRequest = DownloadableBase64File(
                    file = it,
                    name = "vytiah_pro_nesudymist_${
                        DateFormats.criminalCertFileFormat.format(
                            Date()
                        )
                    }.zip",
                    mimeType = "application/zip"
                )
                _downloadCert.tryEmit(downloadRequest)
            }
        }
    }

    fun sendRatingRequest(ratingRequest: RatingRequest) {
        sendRating(ratingRequest, RATING_SERVICE_CATEGORY, RATING_SERVICE_CODE)
    }

    fun getRatingForm() {
        getRating(RATING_SERVICE_CATEGORY, RATING_SERVICE_CODE)
    }
}