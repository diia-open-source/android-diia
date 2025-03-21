package ua.gov.diia.opensource.util.delegation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.noInternetException
import ua.gov.diia.opensource.util.alert.AndroidClientAlertDialogsFactory.Companion.NO_INTERNET
import ua.gov.diia.opensource.util.alert.AndroidClientAlertDialogsFactory.Companion.UNKNOWN_ERR
import ua.gov.diia.ui_base.fragments.errordialog.RequestTryCountTracker
import javax.inject.Inject

class DefaultErrorHandlingBehaviourOnFlow @Inject constructor(
    private val alertFactory: ClientAlertDialogsFactory,
    private val withCrashlytics: WithCrashlytics,
) : WithErrorHandlingOnFlow {

    private val tryCountTracker = RequestTryCountTracker()

    private val _showTemplateDialog = MutableSharedFlow<UiDataEvent<TemplateDialogModel>>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val showTemplateDialog = _showTemplateDialog.asSharedFlow()

    override fun consumeException(
        exception: Exception,
        key: String,
        needRetry: Boolean
    ) {
        withCrashlytics.sendNonFatalError(exception)
        val templateData = if (exception.noInternetException()) {
            alertFactory.showCustomAlert(NO_INTERNET)
        } else {
            val closable = (tryCountTracker.tryCount < 1 && needRetry)
            alertFactory.showCustomAlert(UNKNOWN_ERR, closable)
        }
        tryCountTracker.increment()
        _showTemplateDialog.tryEmit(UiDataEvent(templateData.setKey(key)))

    }

    override fun resetErrorCounter() {
        tryCountTracker.reset()
    }

    override fun showTemplateDialog(
        templateDialog: TemplateDialogModel,
        key: String
    ) {
        _showTemplateDialog.tryEmit(UiDataEvent(templateDialog.setKey(key)))
    }

}