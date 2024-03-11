package ua.gov.diia.bankid.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.HttpUrl
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.bankid.ui.auth.BankAuthConst.CODE
import ua.gov.diia.bankid.ui.auth.BankAuthConst.PROGRESS_ACTIVE
import ua.gov.diia.bankid.ui.auth.BankAuthConst.PROGRESS_INACTIVE
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.verification.model.VerificationFlowResult
import javax.inject.Inject

@HiltViewModel
internal class BankAuthVM @Inject constructor() : ViewModel() {

    private var bankCode: String? = null

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _uiData = mutableStateOf(BankAuthScreenData(progressLoadState = true))
    val uiData: State<BankAuthScreenData> = _uiData

    fun doInit(bankCode: String) {
        this.bankCode = bankCode
    }

    fun parseAuthCode(callbackUrl: String) {
        HttpUrl.get(callbackUrl).queryParameter(CODE)?.let { requestId ->
            val request = VerificationFlowResult.CompleteVerificationStep(requestId, bankCode)
            _navigation.tryEmit(Navigation.CompleteAuth(request))
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            PROGRESS_ACTIVE -> {
                _uiData.value = _uiData.value.copy(progressLoadState = true)
            }

            PROGRESS_INACTIVE -> {
                _uiData.value = _uiData.value.copy(progressLoadState = false)
            }
        }
    }

    sealed class Navigation : NavigationPath {
        data class CompleteAuth(val data: VerificationFlowResult) : Navigation()
    }
}