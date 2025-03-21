package ua.gov.diia.opensource.util.alert

import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import javax.inject.Inject

class AndroidClientAlertDialogsFactory @Inject constructor(
) : ClientAlertDialogsFactory {

    override fun showCustomAlert(keyAlert: String, isClosable: Boolean): TemplateDialogModel {
        return when (keyAlert) {

            NO_INTERNET -> TemplateDialogModel(
                type = "smallAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = true,
                data = TemplateDialogData(
                    icon = "\uD83D\uDE1E",
                    title = "Немає інтернету",
                    description = "Перевірте з’єднання та спробуйте ще раз",
                    mainButton = TemplateDialogButton(
                        name = "Спробувати ще",
                        action = ActionsConst.GENERAL_RETRY,
                    ),
                ),
            )

            else -> TemplateDialogModel(
                type = "smallAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = isClosable,
                data = TemplateDialogData(
                    icon = "\uD83D\uDE1E",
                    title = "На жаль, сталася помилка",
                    mainButton = TemplateDialogButton(
                        name = if (isClosable) "Спробувати ще" else "Зрозуміло",
                        action = if (isClosable) ActionsConst.GENERAL_RETRY else ActionsConst.ERROR_DIALOG_DEAL_WITH_IT,
                    ),
                ),
            )
        }
    }

    companion object {
        const val UNKNOWN_ERR = "unknownErrorAlert"
        const val NO_INTERNET = "alertNoInternet"

    }
}