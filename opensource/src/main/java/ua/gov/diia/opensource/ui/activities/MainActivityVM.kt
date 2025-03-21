package ua.gov.diia.opensource.ui.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionAllowAuthorizedLinks
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.lifecycle.consumeEvent
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.opensource.di.actions.GlobalActionForceAppUpdate
import ua.gov.diia.opensource.util.work.LogoutWork
import ua.gov.diia.pin.repository.LoginPinRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    @GlobalActionLogout private val actionLogout: MutableLiveData<UiEvent>,
    private val notificationManager: DiiaNotificationManager,
    private val workManager: WorkManager,
    private val dispatcherProvider: DispatcherProvider,
    private val authorizationRepository: AuthorizationRepository,
    private val loginPinRepository: LoginPinRepository,
    @GlobalActionAllowAuthorizedLinks val allowAuthorizedLinksFlow: MutableSharedFlow<UiDataEvent<Boolean>>,
    @GlobalActionForceAppUpdate val appForceUpdateAction: MutableStateFlow<UiDataEvent<Boolean>>,
    private val deepLinkDelegate: WithDeeplinkHandling,
): ViewModel(), WithDeeplinkHandling by deepLinkDelegate {

    private val _restartApp = MutableLiveData<UiEvent>()
    val restartApp = _restartApp.asLiveData()

    var allowAuthorizedDeepLinks = false
        private set

    init {
        viewModelScope.launch {
            actionLogout.asFlow().collectLatest { event ->
                event.consumeEvent { doLogout() }
            }
        }

        viewModelScope.launch {
            allowAuthorizedLinksFlow.collectLatest { event ->
                if (event.getContentIfNotHandled() == true) {
                    allowAuthorizedDeepLinks()
                }
            }
        }
    }

    fun processIntentPath(path: String) {
        viewModelScope.launch {
            emitDeeplink(UiDataEvent(buildDeepLinkAction(path)))
        }
    }

    fun doLogout() {
        viewModelScope.launch(dispatcherProvider.work) {
            val logoutToken = authorizationRepository.getToken() ?: return@launch
            val mobileUid = authorizationRepository.getMobileUuid()
            val isServiceUser = authorizationRepository.isServiceUser()
            authorizationRepository.logoutUser()
            authorizationRepository.setIsServiceUser(false)
            authorizationRepository.setMobileUuid(mobileUid)
            notificationManager.setBadeNumber(0)
            LogoutWork.enqueue(workManager, logoutToken, mobileUid, isServiceUser)
            _restartApp.postValue(UiEvent())
        }
    }

    fun checkPinCount() {
        viewModelScope.launch {
            if (loginPinRepository.getPinTryCount() >= PIN_TRY_COUNT) {
                loginPinRepository.setPinTryCount(0)
                doLogout()
            }
        }
    }

    private fun allowAuthorizedDeepLinks() {
        viewModelScope.launch {
            allowAuthorizedDeepLinks = true
            val deepLink = deeplinkFlow.value
            if (deepLink?.notHandedYet == true) {
                emitDeeplink(deepLink)
            }
        }
    }

    private companion object {
        const val PIN_TRY_COUNT = 3
    }
}