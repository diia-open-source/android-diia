package ua.gov.diia.pin.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.event.UiDataEvent

class StubErrorHandlerOnFlow : WithErrorHandlingOnFlow {

    override val showTemplateDialog = MutableSharedFlow<UiDataEvent<TemplateDialogModel>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    var lastError: Exception? = null
       private set

    override fun showTemplateDialog(templateDialog: TemplateDialogModel, key: String) {
        showTemplateDialog.tryEmit(UiDataEvent(templateDialog.setKey(key)))
    }

    override fun consumeException(exception: Exception, key: String, needRetry: Boolean) {
        lastError = exception
    }

    override fun resetErrorCounter() = Unit
}