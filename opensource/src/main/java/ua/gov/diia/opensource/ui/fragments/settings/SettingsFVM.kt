package ua.gov.diia.opensource.ui.fragments.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.biometric.Biometric
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.notifications.store.NotificationsPreferences.AllowNotifications
import ua.gov.diia.notifications.util.push.notification.NotificationEnabledChecker
import javax.inject.Inject

@HiltViewModel
class SettingsFVM @Inject constructor(
    private val diiaStorage: DiiaStorage,
    notificationEnabledChecker: NotificationEnabledChecker,
    biometric: Biometric
) : ViewModel() {

    private var _touchIdEnabled = MutableLiveData<Boolean>()
    val touchIdEnabled: LiveData<Boolean>
        get() = _touchIdEnabled

    private var _touchIdAvailable = MutableLiveData<Boolean>()
    val touchIdAvailable: LiveData<Boolean>
        get() = _touchIdAvailable

    private var _settingsAction = MutableLiveData<SettingsAction>().apply {
        value = SettingsAction.None
    }
    val settingsAction: LiveData<SettingsAction>
        get() = _settingsAction

    init {
        _touchIdEnabled.value = diiaStorage.getBoolean(Preferences.UseTouchId, false)
        _touchIdAvailable.value = biometric.isBiometricAuthAvailable()

        if (!notificationEnabledChecker.notificationEnabled()) {
            diiaStorage.set(AllowNotifications, false)
        }
    }

    fun setTouchIdAccess(isEnabled: Boolean) {
        diiaStorage.set(Preferences.UseTouchId, isEnabled)
    }

    fun openSystemNotifications() {
        _settingsAction.value = SettingsAction.OpenSystemNotificationsAction
    }

    fun changePin() {
        _settingsAction.value = SettingsAction.PinCodeChangeAction
    }

    fun docStack() {
        _settingsAction.value = SettingsAction.DocStack
    }

    fun closeSettings() {
        _settingsAction.value = SettingsAction.CloseSettingsAction
    }

    fun clearAction() {
        _settingsAction.value = SettingsAction.None
    }

    override fun onCleared() {
        super.onCleared()
        _settingsAction.value = SettingsAction.None
    }

    sealed class SettingsAction {
        object None : SettingsAction()
        object PinCodeChangeAction : SettingsAction()
        object DocStack : SettingsAction()
        object CloseSettingsAction : SettingsAction()
        object OpenSystemNotificationsAction : SettingsAction()
    }
}