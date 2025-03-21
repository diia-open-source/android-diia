package ua.gov.diia.pin.util

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

            INVALID_PIN -> TemplateDialogModel(
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                type = "horizontalButton",
                isClosable = false,
                data = TemplateDialogData(
                    icon = null,
                    title = "Ви ввели неправильний код тричі",
                    description = "Пройдіть повторну авторизацію у застосунку",
                    mainButton = TemplateDialogButton(
                        name = "Авторизуватися",
                        action = ActionsConst.DIALOG_ACTION_CODE_LOGOUT
                    )
                )
            )

            CONFIRM_PIN -> TemplateDialogModel(
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                type = "horizontalButton",
                isClosable = false,
                data = TemplateDialogData(
                    icon = "\uD83D\uDC4C",
                    title = "Код змінено",
                    description = "Ви змінили код для входу у застосунок Дія.",
                    mainButton = TemplateDialogButton(
                        name = "Дякую",
                        action = ActionsConst.DIALOG_DEAL_WITH_IT
                    )
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
        const val INVALID_PIN = "showAlertAfterInvalidPin"
        const val CONFIRM_PIN = "showAlertAfterConfirmPin"
    }
}