package ua.gov.diia.opensource.util

import androidx.lifecycle.MutableLiveData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.noInternetException
import ua.gov.diia.ui_base.fragments.errordialog.RequestTryCountTracker
import javax.inject.Inject

class DefaultErrorHandlingBehaviour @Inject constructor(
    private val alertFactory: ClientAlertDialogsFactory,
    private val withCrashlytics: WithCrashlytics,
) : WithErrorHandling {

    private val tryCountTracker = RequestTryCountTracker()

    private val _showTemplateDialog = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    override val showTemplateDialog = _showTemplateDialog.asLiveData()

    override fun consumeException(
        exception: Exception,
        key: String,
        needRetry: Boolean
    ) {
        withCrashlytics.sendNonFatalError(exception)
        val templateData = if (exception.noInternetException()) {
            alertFactory.alertNoInternet()
        } else {
            val closable = (tryCountTracker.tryCount < 1 && needRetry)
            alertFactory.unknownErrorAlert(closable, e = exception)
        }
        tryCountTracker.increment()
        _showTemplateDialog.postValue(UiDataEvent(templateData.setKey(key)))
    }

    override fun resetErrorCounter() {
        tryCountTracker.reset()
    }

    override fun showTemplateDialog(
        templateDialog: TemplateDialogModel,
        key: String
    ) {
        _showTemplateDialog.postValue(UiDataEvent(templateDialog.setKey(key)))
    }

}