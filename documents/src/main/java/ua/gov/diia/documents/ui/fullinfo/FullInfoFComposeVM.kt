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
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.documents.ui.DocsConst.ACTION_REFRESH
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataRepository
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataResult
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrgData
import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrgToggleButtonCodes
import javax.inject.Inject

@HiltViewModel
class FullInfoFComposeVM @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val withRetryLastAction: WithRetryLastAction,
    private val composeMapper: DocumentComposeMapper,
    private val verificationDataRepository: DocumentVerificationDataRepository,
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
            loadVerificationCodesOrg(it)
            _documentCardData.postValue(it)
        }
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
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

            UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                event.action?.let {
                    _docAction.tryEmit(DocActions.FullInfoAction(it))
                }
            }

            UIActionKeysCompose.VERIFICATION_CODES_ORG -> {
                handleVerificationCodesOrgEvents(event)
            }
        }
    }

    private fun configureVerificationCodesOrg(verificationData: DocumentVerificationDataResult?) {
        if (verificationData != null) {
            _bodyData.findAndChangeFirstByInstance<VerificationCodesOrgData> {
                verificationData.verificationCodesOrgData
            }
        }
    }

    private fun loadVerificationCodesOrg(doc: DiiaDocument) {
        executeActionOnFlow(
            dispatcher = dispatcherProvider.ioDispatcher(),
            progressIndicator = _progressIndicator.also {
                _progressIndicatorKey.value =
                    UIActionKeysCompose.VERIFICATION_CODES_ORG
            }
        ) {
            val verificationCodesOrgResult = verificationDataRepository.loadVerificationData(
                doc = doc,
                position = 0,
                fullInfo = true,
                localizationType = doc.localization() ?: LocalizationType.ua
            )
            configureVerificationCodesOrg(verificationCodesOrgResult?.result?: return@executeActionOnFlow)
        }
    }

    private fun handleVerificationCodesOrgEvents(event: UIAction) {
        event.action?.let {
            when (val type = it.type) {
                ACTION_REFRESH -> {
                    _docAction.tryEmit(DocActions.DefaultBrightness)
                    documentCardData.value?.let { loadVerificationCodesOrg(it) }
                }

                VerificationCodesOrgToggleButtonCodes.qr.name -> {
                    _docAction.tryEmit(DocActions.DefaultBrightness)
                    _bodyData.findAndChangeFirstByInstance<VerificationCodesOrgData> {
                        it.onToggleClicked(type)
                    }
                }

                VerificationCodesOrgToggleButtonCodes.barcode.name -> {
                    _docAction.tryEmit(DocActions.HighBrightness)
                    _bodyData.findAndChangeFirstByInstance<VerificationCodesOrgData> {
                        it.onToggleClicked(type)
                    }
                }

                else -> {

                }
            }
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
        data class FullInfoAction(val action: DataActionWrapper) : DocActions()
    }
}