package ua.gov.diia.documents.ui.fullinfo

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.ui.ToggleId
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.documents.barcode.*
import ua.gov.diia.documents.ui.DocumentComposeMapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.organism.document.DocCodeOrgData
import javax.inject.Inject

@HiltViewModel
class FullInfoFComposeVM @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val withRetryLastAction: WithRetryLastAction,
    private val composeMapper: DocumentComposeMapper,
    private val barcodeRepository: DocumentBarcodeRepository,
    private val docFullComposeMapper: DocFullInfoComposeMapper
) : ViewModel(),
    WithErrorHandlingOnFlow by errorHandling,
    WithRetryLastAction by withRetryLastAction,
    DocumentComposeMapper by composeMapper {

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _progressIndicatorKey = MutableStateFlow("")
    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator: Flow<Pair<String, Boolean>> =
        _progressIndicator.combine(_progressIndicatorKey) { value, key ->
            key to value
        }

    private val _docAction = MutableSharedFlow<DocAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val docAction = _docAction.asSharedFlow()

    private val _documentCardData = MutableLiveData<DiiaDocument>()
    val documentCardData = _documentCardData.asLiveData()

    fun configureBody(document: Parcelable) {
        (document as? DiiaDocument)?.let {
            docFullComposeMapper.mapDocToBody(it, _bodyData)
            loadQR(it)
            _documentCardData.postValue(it)
        }
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE -> {
                event.data?.let {
                    onToggleClick(event.data!!)
                }
            }

            UIActionKeysCompose.REFRESH_BUTTON -> {
                documentCardData.value?.let { loadQR(it) }
            }

            UIActionKeysCompose.DOC_NUMBER_COPY -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.DocNumberCopy(event.data!!))
                }
            }

            UIActionKeysCompose.VERTICAL_TABLE_ITEM -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.ItemVerticalValueCopy(event.data!!))
                }
            }

            UIActionKeysCompose.HORIZONTAL_TABLE_ITEM -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.ItemHorizontalValueCopy(event.data!!))
                }
            }

            UIActionKeysCompose.PRIMARY_TABLE_ITEM -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.ItemPrimaryValueCopy(event.data!!))
                }
            }

            UIActionKeysCompose.BOTTOM_SHEET_DISMISS -> {
                _docAction.tryEmit(DocActions.DismissDoc)
            }
        }
    }

    private fun onToggleClick(toggleId: String) {
        val index = _bodyData.indexOfFirst { it is DocCodeOrgData }
        if (index == -1) {
            return
        } else {
            _bodyData[index] =
                (_bodyData[index] as DocCodeOrgData).onToggleClick(toggleId)
        }
        when (toggleId) {
            ToggleId.qr.value -> _docAction.tryEmit(DocActions.DefaultBrightness)
            ToggleId.ean.value -> _docAction.tryEmit(DocActions.HighBrightness)
        }
    }

    private fun configureDocCode(barcode: DocumentBarcodeRepositoryResult) {

        val index = _bodyData.indexOfFirst { it is DocCodeOrgData }

        if (index != -1) {
            val oldValue: DocCodeOrgData = _bodyData[index] as DocCodeOrgData

            val data = toComposeDocCodeOrg(
                barcode.result,
                LocalizationType.ua,
                barcode.showToggle
            )

            val updatedItem = data?.let {
                oldValue.copy(
                    localization = data.localization,
                    toggle = it.toggle,
                    qrBitmap = data.qrBitmap,
                    ean13Bitmap = data.ean13Bitmap,
                    eanCode = data.eanCode,
                    timerText = data.timerText,
                    exception = data.exception,
                    expired = data.expired
                )
            }

            if (updatedItem != null) {
                _bodyData[index] = updatedItem
            }

        }
    }

    private fun loadQR(doc: DiiaDocument) {
        executeActionOnFlow(
            dispatcher = dispatcherProvider.ioDispatcher(),
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value =
                    UIActionKeysCompose.DOC_CODE_ORG_DATA
            }
        ) {

            val barcodeResult = barcodeRepository.loadBarcode(doc, 0, true)
            configureDocCode(barcodeResult)
        }
    }

    sealed class DocActions : DocAction {
        data class DocNumberCopy(val value: String) : DocActions()
        data class ItemHorizontalValueCopy(val value: String) : DocActions()
        data class ItemVerticalValueCopy(val value: String) : DocActions()
        data class ItemPrimaryValueCopy(val value: String) : DocActions()
        object HighBrightness : DocActions()
        object DefaultBrightness : DocActions()
        object DismissDoc : DocActions()
    }
}