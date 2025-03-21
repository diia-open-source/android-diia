package ua.gov.diia.verification.util

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

            NO_VERIFICATION_METHODS -> TemplateDialogModel(
                ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                "middleCenterAlignAlert",
                isClosable = true,
                TemplateDialogData(
                    "\uD83D\uDE1E",
                    "Неможливо підтвердити особу користувача",
                    "На жаль, на поточний момент немає доступних методів підтвердження особи користувача для створення Дія.Підпису. Спробуйте пізніше.",
                    TemplateDialogButton("Зрозуміло", null, "skip")
                )
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
        const val NO_VERIFICATION_METHODS = "getNoVerificationMethodsDialog"
    }
}