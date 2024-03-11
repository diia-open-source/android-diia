package ua.gov.diia.ui_base.fragments.errordialog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.fragments.errordialog.ErrorDialogConst.ACTION_FINISH
import ua.gov.diia.ui_base.fragments.errordialog.ErrorDialogConst.ACTION_RETRY
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeLargeAtomData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.screen.TemplateDialogScreenData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

class ErrorDVM : ViewModel() {

    private val _errorAction = MutableLiveData<UiDataEvent<ErrorAction>>()
    val errorAction: LiveData<UiDataEvent<ErrorAction>>
        get() = _errorAction

    val uiData = mutableStateOf(TemplateDialogScreenData())

    fun constructDialog(error: DialogError) {
        uiData.value = when (error) {
            DialogError.CHECK_TIMEOUT -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.identity_error_timeout),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = true
                )
            }

            DialogError.REQUEST_NOT_REGISTERED -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.identity_error_not_registered),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = true
                )
            }

            DialogError.UNKNOWN_ERROR_RECOVERABLE -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.identity_error_unknown),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_RETRY,
                        title = UiText.StringResource(R.string.identity_error_try_again)
                    ),
                    isCloseable = true
                )
            }

            DialogError.UNKNOWN_ERROR_UNRECOVERABLE -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.identity_error_unknown),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = true
                )
            }

            DialogError.NO_INTERNET -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.identity_error_no_internet),
                    descriptionText = UiText.StringResource(R.string.identity_error_no_internet_desc),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_RETRY,
                        title = UiText.StringResource(R.string.identity_error_try_again)
                    ),
                    isCloseable = false
                )
            }

            DialogError.PAYMENT_ERROR -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.identity_error_payment),
                    primaryButton = BtnPrimaryDefaultAtmData(
                        id = "",
                        actionKey = ACTION_RETRY,
                        title = UiText.StringResource(R.string.identity_error_try_again)
                    ),
                    secondaryButton = BtnPlainAtmData(
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.error_dialog_back)
                    ),
                    isCloseable = false
                )
            }

            DialogError.PAYMENT_FORBIDDEN -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.payment_value_changed),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_RETRY,
                        title = UiText.StringResource(R.string.update_data)
                    ),
                    isCloseable = true
                )
            }

            DialogError.DRIVER_LICENSE_REPLACE -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.dl_replace_not_sent),
                    primaryButton = BtnPrimaryDefaultAtmData(
                        id = "",
                        actionKey = ACTION_RETRY,
                        title = UiText.StringResource(R.string.identity_error_try_again)
                    ),
                    secondaryButton = BtnPlainAtmData(
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.dl_replace_return_to_docs)
                    ),
                    isCloseable = false
                )
            }

            DialogError.CRITICAL_ERROR_DEEP_LINK_NOT_REGISTERED -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.critical_error_icon),
                    titleText = UiText.StringResource(R.string.critical_error_title),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = false
                )
            }

            DialogError.REQUESTED_DOCUMENTS_NOT_FOUND -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.share_doc_not_present),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = false
                )
            }

            DialogError.DOCUMENT_REQUEST_EXPIRED -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.share_doc_timeout),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = false
                )
            }

            DialogError.SPECIFIED_DOCUMENT_NOT_FOUND -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.error_document_not_found),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = false
                )
            }

            DialogError.SERVICE_UNAVAILABLE -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.error_service_not_available),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = false
                )
            }

            DialogError.ADD_DOC_TOO_MANY_ATTEMPTS -> {
                TemplateDialogScreenData(
                    icon = UiText.StringResource(R.string.emoji_sad),
                    titleText = UiText.StringResource(R.string.error_add_doc_request_limit),
                    descriptionText = UiText.StringResource(R.string.error_add_doc_request_limit_desc),
                    strokeButton = ButtonStrokeLargeAtomData(
                        id = "",
                        actionKey = ACTION_FINISH,
                        title = UiText.StringResource(R.string.idenity_error_understood)
                    ),
                    isCloseable = false
                )
            }
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.CLOSE_BUTTON, ACTION_FINISH -> {
                finish()
            }

            ACTION_RETRY -> {
                finishRetry()
            }
        }
    }

    fun finish() {
        _errorAction.value = UiDataEvent(ErrorAction.FINISH)
    }

    private fun finishRetry() {
        _errorAction.value = UiDataEvent(ErrorAction.RETRY)
    }

    enum class ErrorAction {
        FINISH, RETRY
    }
}