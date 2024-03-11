package ua.gov.diia.notifications.ui.fragments.home.notificationsettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.noInternetException
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.models.notification.Subscriptions
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsVM @Inject constructor(
    @AuthorizedClient private val apiNotifications: ApiNotifications,
    private val clientAlertDialogsFactory: ClientAlertDialogsFactory
) : ViewModel() {

    private val _isDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isDataLoading = _isDataLoading.asLiveData()

    private val _subscriptions: MutableLiveData<Subscriptions> = MutableLiveData()
    val subscriptions = _subscriptions.asLiveData()

    private val _getSubs: MutableLiveData<Boolean?> = MutableLiveData()
    val getSubs = _getSubs.asLiveData()

    private var _error = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    val error = _error.asLiveData()

    init {
        getSubs()
    }

    fun getSubs() {
        viewModelScope.launch {
            _isDataLoading.value = true
            try {
                val subscriptions = apiNotifications.getSubscriptions()
                _subscriptions.value = subscriptions
            } catch (e: Exception) {
                consumeException(e)
            } finally {
                _isDataLoading.value = false
            }
        }
    }

    fun subscribe(code: String) {
        viewModelScope.launch {
            _isDataLoading.value = true
            try {
                val response = apiNotifications.subscribe(code)
                if (response.template != null) {
                    _error.value = UiDataEvent(response.template)
                } else {
                    _getSubs.value = response.success
                }
            } catch (e: Exception) {
                consumeException(e)
            } finally {
                _isDataLoading.value = false
            }
        }
    }

    fun unsubscribe(code: String) {
        viewModelScope.launch {
            _isDataLoading.value = true
            try {
                val response = apiNotifications.unsubscribe(code)
                if (response.template != null) {
                    _error.value = UiDataEvent(response.template)
                } else {
                    _getSubs.value = response.success
                }
            } catch (e: Exception) {
                consumeException(e)
            } finally {
                _isDataLoading.value = false
            }
        }
    }

    private fun consumeException(e: Exception) {
        if (e.noInternetException()) {
            _error.postValue(UiDataEvent(clientAlertDialogsFactory.alertNoInternet()))
        } else {
            _error.postValue(UiDataEvent(clientAlertDialogsFactory.unknownErrorAlert(false, e = e)))
        }
    }

}