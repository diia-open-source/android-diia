package ua.gov.diia.publicservice.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.event.UiDataEvent

class StubErrorHandlerOnFlow : WithErrorHandlingOnFlow {

    override val showTemplateDialog = MutableSharedFlow<UiDataEvent<TemplateDialogModel>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val lastError = MutableStateFlow<Throwable?>(null)

    override fun showTemplateDialog(templateDialog: TemplateDialogModel, key: String) {
        showTemplateDialog.tryEmit(UiDataEvent(templateDialog.setKey(key)))
    }

    override fun consumeException(exception: Exception, key: String, needRetry: Boolean) {
        lastError.value = exception
    }

    override fun resetErrorCounter() = Unit

    suspend fun lastError() = lastError.filterNotNull().first()
}
