package ua.gov.diia.documents.ui.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.documents.di.GlobalActionUpdateDocument
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.ui.DocumentsContextMenuActions
import ua.gov.diia.documents.ui.actions.VerificationActions.VERIFICATION_CODE_EAN13
import ua.gov.diia.documents.ui.actions.VerificationActions.VERIFICATION_CODE_QR
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import javax.inject.Inject

@HiltViewModel
class DocActionsVMCompose @Inject constructor(
    @GlobalActionUpdateDocument val globalActionUpdateDocument: MutableStateFlow<UiDataEvent<DiiaDocument>?>,
    private val docActionsProvider: DocActionsProvider,
) : ViewModel(),
    DocActionsProvider by docActionsProvider {

    private val _docAction = MutableSharedFlow<DocAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val docAction = _docAction.asSharedFlow()

    private val _dismiss = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val dismiss = _dismiss.asSharedFlow()

    fun switchLocalization(diiaDocument: DiiaDocument, code: LocalizationType) {
        viewModelScope.launch {
            val doc = diiaDocument.makeCopy()
            doc.setLocalization(code)
            globalActionUpdateDocument.emit(UiDataEvent(doc))
        }
    }

    fun onUIAction(event: UIAction) {
        when (event.action?.type ?: event.actionKey) {

            DocumentsContextMenuActions.REMOVE_DOC.action -> {
                _docAction.tryEmit(DocActions.RemoveDoc)
            }

            DocumentsContextMenuActions.TRANSLATE_TO_UA.action -> {
                _docAction.tryEmit(DocActions.TranslateToUa)
            }

            DocumentsContextMenuActions.TRANSLATE_TO_ENG.action -> {
                _docAction.tryEmit(DocActions.TranslateToEng)
            }

            DocumentsContextMenuActions.RATE_DOCUMENT.action -> {
                _docAction.tryEmit(DocActions.RateDocument)
            }

            DocumentsContextMenuActions.SHARE_WITH_FRIENDS.action -> {
                _docAction.tryEmit(DocActions.ShareWithFriends)
            }

            DocumentsContextMenuActions.VERIFICATION_CODE.action -> {
                _docAction.tryEmit(DocActions.OpenVerificationCode(event.data!!))
            }

            DocumentsContextMenuActions.UPDATE_DOC.action -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.UpdateDoc(event.data!!))
                }
            }

            VERIFICATION_CODE_QR -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.OpenQr(event.data!!))
                }
            }

            VERIFICATION_CODE_EAN13 -> {
                event.data?.let {
                    _docAction.tryEmit(DocActions.OpenEan13(event.data!!))
                }
            }


            UIActionKeysCompose.BUTTON_REGULAR -> {
                _dismiss.tryEmit(UiEvent())
            }

            else -> {
                event.action?.type?.let {
                    _docAction.tryEmit(DocActions.NavigateByDocAction(action = it))
                }
            }
        }
    }

    sealed class DocActions : DocAction {
        object RemoveDoc : DocActions()
        object RemoveAward : DocActions()
        object TranslateToUa : DocActions()
        object TranslateToEng : DocActions()
        object RateDocument : DocActions()
        object AddDoc : DocActions()
        object ShareWithFriends : DocActions()
        data class UpdateDoc(val type: String) : DocActions()
        data class OpenQr(val id: String) : DocActions()
        data class OpenEan13(val id: String) : DocActions()
        data class OpenVerificationCode(val id: String) : DocActions()
        data class NavigateByDocAction(val action: String, val data: String? = null) : DocActions()
    }
}