package ua.gov.diia.documents.util

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

            DOC_REMOVE -> TemplateDialogModel(
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                type = "middleCenterAlignAlert",
                isClosable = false,
                data = TemplateDialogData(
                    icon = "☝",
                    title = "Видалити єПодяку?",
                    description = "Ви зможете повернути її, натиснувши на Додати документ в галереї документів.",
                    mainButton = TemplateDialogButton(
                        name = "Так, видалити",
                        action = "remove"
                    ),
                    alternativeButton = TemplateDialogButton(
                        name = "Не видаляти",
                        action = "cancel_remove"
                    ),
                )
            )

            REGISTER_NOT_AVAILABLE -> TemplateDialogModel(
                type = "smallAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = false,
                data = TemplateDialogData(
                    icon = "\uD83D\uDE14",
                    title = "Спробуйте пізніше",
                    description = "Йой! Не виходить перевірити дані актового запису. Реєстр захворів, його вже лікують \uD83C\uDF43 Спробуйте, будь ласка, пізніше.",
                    mainButton = TemplateDialogButton(
                        name = "Зрозуміло",
                        action = ActionsConst.DIALOG_ACTION_CODE_CLOSE
                    )
                ),
            )

            DOCUMENT_NOT_FOUND -> TemplateDialogModel(
                type = "smallAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = false,
                data = TemplateDialogData(
                    icon = "\uD83D\uDE14",
                    title = "Документ не знайдено",
                    description = "Він відсутній у реєстрі або вже недійсний. Ви не можете користуватися документом у Дії.",
                    mainButton = TemplateDialogButton(
                        name = "Зрозуміло",
                        action = ActionsConst.DIALOG_ACTION_CODE_CLOSE
                    )
                ),
            )

            DOCUMENT_NOT_FULL -> TemplateDialogModel(
                type = "smallAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = false,
                data = TemplateDialogData(
                    icon = "\uD83D\uDE14",
                    title = "Неможливо сформувати документ",
                    description = "Дані документа у Державному реєстрі актів цивільного стану громадян неповні.",
                    mainButton = TemplateDialogButton(
                        name = "Зрозуміло",
                        action = ActionsConst.DIALOG_ACTION_CODE_CLOSE
                    )
                ),
            )

            CERT_EXIST -> TemplateDialogModel(
                type = "middleCenterAlignAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = false,
                data = TemplateDialogData(
                    icon = "\uD83D\uDC4D",
                    title = "Вже є сертифікат",
                    description = "Ви вже маєте сертифікат.\nПерегляньте його або створіть новий.",
                    mainButton = TemplateDialogButton(
                        name = "Переглянути",
                        action = "open-existing-unmarriedCertificate",
                    ),
                    alternativeButton = TemplateDialogButton(
                        name = "Створити новий",
                        action = "create-new-unmarriedCertificate"
                    )
                )
            )

            CERT_NOT_EXIST -> TemplateDialogModel(
                type = "middleCenterAlignAlert",
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                isClosable = false,
                data = TemplateDialogData(
                    icon = "☝\uFE0F",
                    title = "Потрібно отримати сертифікат",
                    description = "Ви можете стати парою, коли у вас буде сертифікат неодруженого.",
                    mainButton = TemplateDialogButton(
                        name = "Отримати сертифікат",
                        action = "create-unmarriedCertificate",
                    ),
                    alternativeButton = TemplateDialogButton(
                        name = "Іншим разом",
                        action = "cancel"
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
        const val DOC_REMOVE = "showDocRemoveDialog"
        const val REGISTER_NOT_AVAILABLE = "registerNotAvailable"
        const val DOCUMENT_NOT_FOUND = "documentNotFound"
        const val DOCUMENT_NOT_FULL = "documentNotFull"
        const val CERT_EXIST = "certAlreadyExist"
        const val CERT_NOT_EXIST = "unmarriedCertNotExist"
    }
}