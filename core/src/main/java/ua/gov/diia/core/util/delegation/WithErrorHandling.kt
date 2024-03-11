package ua.gov.diia.core.util.delegation

import androidx.lifecycle.LiveData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.event.UiDataEvent

interface WithTemplateDialog {

    val showTemplateDialog: LiveData<UiDataEvent<TemplateDialogModel>>

    fun showTemplateDialog(
        templateDialog: TemplateDialogModel,
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
    )
}

interface WithErrorHandling : WithTemplateDialog {

    fun consumeException(
        exception: Exception,
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
        needRetry: Boolean = true
    )

    fun resetErrorCounter()
}
