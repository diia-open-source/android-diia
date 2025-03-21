package ua.gov.diia.notifications.ui.fragments.home.notificationsettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.models.notification.Subscriptions
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsVM @Inject constructor(
    @AuthorizedClient private val apiNotifications: ApiNotifications,
    private val errorHandling: WithErrorHandling,
    private val retryLastAction: WithRetryLastAction,
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandling by errorHandling{

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
        executeAction(progressIndicator = _isDataLoading) {
            val subscriptions = apiNotifications.getSubscriptions()
            _subscriptions.value = subscriptions
        }
    }

    fun subscribe(code: String) {
        executeAction(progressIndicator = _isDataLoading) {
            val response = apiNotifications.subscribe(code)
            if (response.template != null) {
                _error.value = UiDataEvent(response.template)
            } else {
                _getSubs.value = response.success
            }
        }
    }

    fun unsubscribe(code: String) {

        executeAction(progressIndicator = _isDataLoading) {
            val response = apiNotifications.unsubscribe(code)
            if (response.template != null) {
                _error.value = UiDataEvent(response.template)
            } else {
                _getSubs.value = response.success
            }
        }
    }

}