package ua.gov.diia.pin.ui.input

import androidx.annotation.VisibleForTesting
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
import kotlinx.coroutines.flow.transform
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.models.UserType
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.DIALOG_ACTION_CANCEL
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.DIALOG_ACTION_CODE_LOGOUT
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.pin.R
import ua.gov.diia.pin.helper.PinHelper
import ua.gov.diia.pin.repository.LoginPinRepository
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData
import javax.inject.Inject

@HiltViewModel
class PinInputVM @Inject constructor(
    @GlobalActionLogout private val actionLogout: MutableLiveData<UiEvent>,
    private val loginPinRepository: LoginPinRepository,
    private val pinHelper: PinHelper,
    private val authorizationRepository: AuthorizationRepository,
    private val clientAlertDialogsFactory: ClientAlertDialogsFactory,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling,
    AlternativeAuthCallback {

    private val _validationFinished = MutableStateFlow(true)
    val validationFinished: Flow<Pair<String, Boolean>> =
        _validationFinished.transform { value ->
            UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING to value
        }

    private var isVerification = false
    private var alternativeAuthCount = 0

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _uiData = mutableStateListOf<UIElementData>()
    val uiData: SnapshotStateList<UIElementData> = _uiData


    init {
        _uiData.add(
            NavigationPanelMlcData(
                title = UiText.StringResource(R.string.pin_input_title_text),
                isContextMenuExist = false
            )
        )
        _uiData.add(NumButtonTileOrganismData())
        _uiData.add(TextLabelMlcData(text = UiText.StringResource(R.string.pin_input_description_text)))
        checkForAlternativeAuth()
        resetAlternativeAuthCount()
    }

    fun doInit(verification: Boolean) {
        isVerification = verification
    }

    @VisibleForTesting
    fun checkForAlternativeAuth() {
        executeActionOnFlow(contentLoadedIndicator = _validationFinished) {
            if (pinHelper.isAlternativeAuthEnabled()) {
                enabledAlternativeAuthBtn(true)
                alternativeAuth()
            }
        }
    }

    private fun approvePin(pin: String) {
        executeActionOnFlow(contentLoadedIndicator = _validationFinished) {
            if (loginPinRepository.isPinValid(pin)) {
                completeVerification(pin)
            } else {
                validateTryCount {
                    clearPinWithShake()
                }
            }
        }
    }

    private fun showResetPinRationale() {
        val dialogData = TemplateDialogModel(
            key = FRAGMENT_USER_ACTION_RESULT_KEY,
            type = ALERT_TYPE_HORIZONTAL_BUTTONS,
            isClosable = false,
            data = TemplateDialogData(
                icon = null,
                title = "Забули код для входу?",
                description = "Пройдіть повторну авторизацію у застосунку",
                mainButton = TemplateDialogButton(
                    name = "Скасувати",
                    action = DIALOG_ACTION_CANCEL
                ),
                alternativeButton = TemplateDialogButton(
                    name = "Авторизуватися",
                    action = DIALOG_ACTION_CODE_LOGOUT
                )
            )
        )
        showTemplateDialog(dialogData)
    }

    fun resetPin() {
        actionLogout.value = UiEvent()
    }

    private fun alternativeAuth() {
        _navigation.tryEmit(Navigation.AlternativeAuth)
    }

    override fun onAlternativeAuthSuccessful() {
        executeActionOnFlow(contentLoadedIndicator = _validationFinished) {
            completeVerification()
        }
    }

    override fun onAlternativeAuthFailed() {
        executeActionOnFlow(contentLoadedIndicator = _validationFinished) {
            validateAlternativeAuthTryCount()
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.TEXT_LABEL_MLC -> {
                showResetPinRationale()
            }

            UIActionKeysCompose.PIN_CLEARED_NUM_BUTTON_ORGANISM -> {
                _uiData.findAndChangeFirstByInstance<NumButtonTileOrganismData> {
                    it.copy(clearPin = false, clearWithShake = false)
                }
            }

            UIActionKeysCompose.TOUCH_ID_BUTTON -> {
                alternativeAuth()
            }

            UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM -> {
                approvePin(uiAction.data ?: return)
            }
        }
    }

    private fun enabledAlternativeAuthBtn(enable: Boolean) {
        _uiData.findAndChangeFirstByInstance<NumButtonTileOrganismData> {
            it.copy(hasBiometric = enable)
        }
    }

    private fun clearPinWithShake() {
        _uiData.findAndChangeFirstByInstance<NumButtonTileOrganismData> {
            it.copy(clearWithShake = true)
        }
    }

    private suspend fun completeVerification(pin: String? = null) {
        loginPinRepository.setPinTryCount(0)
        resetAlternativeAuthCount()
        if (isVerification) {
            _navigation.tryEmit(Navigation.PinApproved)
        } else {
            pin?.let {
                loginPinRepository.setUserAuthorized(it)
            }
            when (authorizationRepository.getUserType()) {
                UserType.PRIMARY_USER -> _navigation.tryEmit(Navigation.ToHome)
                UserType.SERVICE_USER -> _navigation.tryEmit(Navigation.ToQr)
            }
        }
    }

    private suspend inline fun validateTryCount(doIdHasAttempt: () -> Unit) {
        val incrementedPinTryCount = loginPinRepository.getPinTryCount().inc()
        loginPinRepository.setPinTryCount(incrementedPinTryCount)
        if (incrementedPinTryCount >= PIN_TRY_COUNT) {
            showTemplateDialog(clientAlertDialogsFactory.showAlertAfterInvalidPin())
        } else {
            doIdHasAttempt.invoke()
        }
    }

    private fun validateAlternativeAuthTryCount() {
        alternativeAuthCount += 1
        if (alternativeAuthCount > ALT_AUTH_TRY_COUNT) {
            enabledAlternativeAuthBtn(false)
        }
    }

    private fun resetAlternativeAuthCount() {
        alternativeAuthCount = 0
    }

    private companion object {
        const val PIN_TRY_COUNT = 3
        const val ALT_AUTH_TRY_COUNT = 5
        const val ALERT_TYPE_HORIZONTAL_BUTTONS = "horizontalButtons"
    }

    sealed class Navigation : NavigationPath {
        object PinApproved : Navigation()
        object ToQr : Navigation()
        object ToHome : Navigation()
        object AlternativeAuth : Navigation()
    }
}