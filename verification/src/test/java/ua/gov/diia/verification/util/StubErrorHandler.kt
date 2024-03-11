package ua.gov.diia.verification.util

import androidx.lifecycle.MutableLiveData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.event.UiDataEvent

class StubErrorHandler : WithErrorHandling {

    override val showTemplateDialog = MutableLiveData<UiDataEvent<TemplateDialogModel>>()
    var lastError: Exception? = null
        private set

    override fun showTemplateDialog(templateDialog: TemplateDialogModel, key: String) {
        showTemplateDialog.value = UiDataEvent(templateDialog.setKey(key))
    }

    override fun consumeException(exception: Exception, key: String, needRetry: Boolean) {
        lastError = exception
    }

    override fun resetErrorCounter() = Unit
}
