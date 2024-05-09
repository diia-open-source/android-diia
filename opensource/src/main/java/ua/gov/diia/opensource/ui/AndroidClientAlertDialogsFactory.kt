package ua.gov.diia.opensource.ui

import org.json.JSONException
import org.json.JSONObject
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.CommonConst
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.opensource.BuildConfig
import javax.inject.Inject

class AndroidClientAlertDialogsFactory @Inject constructor(
    private val crashlytics: WithCrashlytics,
) : ClientAlertDialogsFactory {

    /**
     * For debug purposes to open verify user person at any point of app
     */
    override fun userVerifySuggestion(key: String) = TemplateDialogModel(
        type = "largeAlert",
        key = key,
        isClosable = false,
        data = TemplateDialogData(
            icon = "☝",
            title = "Підтвердьте особу користувача",
            description = "Будь ласка, підтвердьте особу користувача за допомогою фотоперевірки.\n\nМи дбаємо про безпеку ваших персональних даних та хочемо впевнитися, що дані із застосунку доступні тільки вам.",
            mainButton = TemplateDialogButton(
                name = "Підтвердити",
                action = "authMethods",
            ),
            alternativeButton = TemplateDialogButton(
                name = "Вийти",
                action = ActionsConst.DIALOG_ACTION_CODE_LOGOUT,
            )
        ),
    )

    override fun nfcCardNotSupported(key: String) = TemplateDialogModel(
        type = "middleCenterAlignAlert",
        key = key,
        isClosable = false,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "Ця картка не\nпідтримується",
            description = "Використовуйте тільки ID-карту або закордонний біометричний паспорт.",
            mainButton = TemplateDialogButton(
                name = "Спробувати ще",
                action = ActionsConst.GENERAL_RETRY,
            ),
            alternativeButton = TemplateDialogButton(
                name = "Вийти",
                action = ActionsConst.DIALOG_ACTION_EXIT,
            )
        ),
    )

    override fun nfcResidenceCardNotSupported(key: String) = TemplateDialogModel(
        type = "middleCenterAlignAlert",
        key = key,
        isClosable = false,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "Ця картка не\nпідтримується",
            description = "Потрібне посвідчення з біометричним чипом.🪪",
            mainButton = TemplateDialogButton(
                name = "Спробувати ще ",
                action = ActionsConst.GENERAL_RETRY,
            ),
            alternativeButton = TemplateDialogButton(
                name = "Вийти🔙",
                action = ActionsConst.DIALOG_ACTION_EXIT,
            )
        ),
    )

    override fun nfcScanFailed(e: Exception, key: String): TemplateDialogModel {
        e.printLog()
        return TemplateDialogModel(
            type = "middleCenterAlignAlert",
            key = key,
            isClosable = false,
            data = TemplateDialogData(
                icon = "\uD83D\uDE1E",
                title = "На жаль, сталася помилка😒",
                description = "Дані не зчитано. Втрачено зв’язок з картою або сталася помилка при зчитуванні🪲",
                mainButton = TemplateDialogButton(
                    name = "Спробувати ще🔄️",
                    action = ActionsConst.GENERAL_RETRY,
                )
            ),
        )
    }

    override fun codeScanFailed(key: String): TemplateDialogModel {
        return TemplateDialogModel(
            type = "middleCenterAlignAlert",
            key = key,
            isClosable = false,
            data = TemplateDialogData(
                icon = "\uD83D\uDE1E",
                title = "На жаль, сталася помилка",
                description = "Дані не зчитано. Ви відсканували не існуючий код або сталася помилка при зчитуванні. Спробуйте знову.",
                mainButton = TemplateDialogButton(
                    name = "Спробувати ще",
                    action = ActionsConst.GENERAL_RETRY,
                )
            ),
        )
    }

    override fun nfcScanFailedV2(
        e: Exception,
        key: String,
        closable: Boolean
    ): TemplateDialogModel {
        e.printLog()
        return TemplateDialogModel(
            type = "middleCenterAlignAlert",
            key = key,
            isClosable = closable,
            data = TemplateDialogData(
                icon = "\uD83D\uDE1E",
                title = "На жаль, сталася помилка😒",
                description = "Дані не зчитано. Втрачено зв’язок з картою або сталася помилка при зчитуванні",
                mainButton = TemplateDialogButton(
                    name = "Спробувати ще",
                    action = "mrzScan",
                )
            ),
        )
    }

    override fun alertNoInternet(key: String) = TemplateDialogModel(
        type = "smallAlert",
        key = key,
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

    override fun alertVerificationFailed(key: String) = TemplateDialogModel(
        type = "middleCenterAlignAlert",
        key = key,
        isClosable = false,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "Перевірку не пройдено",
            description = "Спробуйте увійти через свій інтернет-банкінг",
            mainButton = TemplateDialogButton(
                icon = "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",
                action = "authBanks",
            ),
            alternativeButton = TemplateDialogButton(
                name = "Повернутись назад",
                action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
            ),
        ),
    )

    override fun unknownErrorAlert(
        closable: Boolean,
        key: String,
        e: Exception
    ): TemplateDialogModel {
        e.printLog()
        val buttonLabel = if (closable) "Спробувати ще" else "Зрозуміло"
        val action =
            if (closable) ActionsConst.GENERAL_RETRY else ActionsConst.ERROR_DIALOG_DEAL_WITH_IT
        return TemplateDialogModel(
            type = "smallAlert",
            key = key,
            isClosable = closable,
            data = TemplateDialogData(
                icon = "\uD83D\uDE1E",
                title = "На жаль, сталася помилка",
                mainButton = TemplateDialogButton(
                    name = buttonLabel,
                    action = action,
                ),
            ),
        )
    }

    override fun userPhotoIdTryCountReached(closable: Boolean, key: String): TemplateDialogModel {
        return TemplateDialogModel(
            type = "middleCenterAlignAlert",
            key = key,
            isClosable = closable,
            data = TemplateDialogData(
                icon = "\uD83D\uDE1E",
                title = "Перевірку не пройдено",
                description = "Спробуйте обрати інший спосіб для підтвердження особи користувача",
                mainButton = TemplateDialogButton(
                    name = "Спробувати ще",
                    action = ActionsConst.GENERAL_RETRY,
                ),
                alternativeButton = TemplateDialogButton(
                    name = "Вийти",
                    action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
                ),
            ),
        )
    }

    override fun getUnsupportedOptionDialog(key: String): TemplateDialogModel {
        return TemplateDialogModel(
            key,
            "middleCenterAlignAlert",
            isClosable = true,
            TemplateDialogData(
                "\uD83D\uDE1E",
                "На жаль,\nсталася помилка",
                "Дана опція тимчасово недоступна.",
                TemplateDialogButton("Зрозуміло", null, "skip")
            )
        )
    }

    override fun getUnsupportedNFCDialog(key: String): TemplateDialogModel {
        return TemplateDialogModel(
            key,
            "middleCenterAlignAlert",
            isClosable = true,
            TemplateDialogData(
                "\uD83D\uDE1E",
                "На жаль, ви не\nможете активувати\nДія.Підпис",
                "На вашому пристрої немає NFC для зчитування даних з ID-картки або біометричного закордонного паспорта.",
                TemplateDialogButton("Зрозуміло", null, "skip")
            )
        )
    }

    override fun getNoVerificationMethodsDialog(key: String): TemplateDialogModel {
        return TemplateDialogModel(
            key,
            "middleCenterAlignAlert",
            isClosable = true,
            TemplateDialogData(
                "\uD83D\uDE1E",
                "Неможливо підтвердити особу користувача",
                "На жаль, на поточний момент немає доступних методів підтвердження особи користувача для створення Дія.Підпису. Спробуйте пізніше.",
                TemplateDialogButton("Зрозуміло", null, "skip")
            )
        )
    }

    override fun getResetSignaturePasswordDialog(key: String): TemplateDialogModel =
        TemplateDialogModel(
            type = "middleCenterAlignAlert",
            key = key,
            isClosable = true,
            data = TemplateDialogData(
                icon = "☝",
                title = "Потрібно створити новий Дія.Підпис",
                description = "Код до Дія.Підпису неможливо відновити. Якщо ви забули його, потрібно створити новий. Попередній Дія.Підпис ми видалимо.",
                mainButton = TemplateDialogButton(
                    name = "Створити новий",
                    action = ActionsConst.DIALOG_ACTION_REMOVE_SIGNATURE,
                ),
                alternativeButton = TemplateDialogButton(
                    name = "Ні, створю пізніше",
                    action = ActionsConst.DIALOG_ACTION_CODE_SKIP,
                ),
            ),
        )

    override fun showMockGeo(key: String): TemplateDialogModel =
        TemplateDialogModel(
            type = "middleCenterAlignAlert",
            key = key,
            isClosable = true,
            data = TemplateDialogData(
                icon = "\uD83D\uDE1E",
                title = "Геолокацію\nне підтверджено",
                description = "Дія не може визначити вашу геолокацію через сторонні сервіси, що блокують доступ до справжньої геолокації. Будь ласка, перевірте, чи не використовуєте ви такі сервіси, та вимкніть, коли Дія підтверджуватиме геолокацію.",
                mainButton = TemplateDialogButton(
                    name = "Зрозуміло",
                    action = ActionsConst.DIALOG_ACTION_CODE_SKIP,
                )
            ),
        )

    override fun expiredOtp(key: String): TemplateDialogModel = TemplateDialogModel(
        type = "smallAlert",
        key = key,
        isClosable = false,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "Закінчився термін\nочікування відповіді",
            mainButton = TemplateDialogButton(
                name = "Зрозуміло",
                action = ActionsConst.DIALOG_ACTION_EXIT,
            ),
        ),
    )

    override fun showAlertAfterInvalidPin(key: String): TemplateDialogModel = TemplateDialogModel(
        key = key,
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

    override fun showAlertAfterConfirmPin(key: String): TemplateDialogModel = TemplateDialogModel(
        key = key,
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

    override fun getUnsupportedGLEDialog(key: String) = TemplateDialogModel(
        key,
        "middleCenterAlignAlert",
        isClosable = true,
        TemplateDialogData(
            "\uD83D\uDE1E",
            "Не вдалося запустити гру",
            "На жаль, гра не підтримується на вашому пристрої.",
            TemplateDialogButton("Зрозуміло", null, "skip")
        )
    )

    override fun getCancelOfficialPollCreationDialog(key: String) = TemplateDialogModel(
        key,
        "middleCenterAlignAlert",
        isClosable = false,
        TemplateDialogData(
            "\uD83D\uDE1E",
            "Скасувати опитування?",
            "Уся введена інформація буде втрачена.",
            TemplateDialogButton("Так, скасувати", null, ActionsConst.DIALOG_ACTION_EXIT_CONFIRM),
            TemplateDialogButton("Ні, залишити", null, ActionsConst.DIALOG_ACTION_CANCEL)
        )
    )

    override fun getCancelledOfficialPollCreationDialog(key: String) = TemplateDialogModel(
        key,
        "middleCenterAlignAlert",
        isClosable = false,
        TemplateDialogData(
            "✅",
            "Створення опитування скасовано",
            "Ви можете створити інше опитування, коли знадобиться.",
            TemplateDialogButton("Зрозуміло", null, ActionsConst.DIALOG_ACTION_EXIT),
        )
    )

    override fun getDeletePollDialog(key: String) = TemplateDialogModel(
        key,
        "middleCenterAlignAlert",
        isClosable = false,
        TemplateDialogData(
            "\uD83D\uDE1E",
            "Видалити опитування?",
            "Опитування стане недоступним для всіх, а набрані голоси будуть втрачені.",
            TemplateDialogButton("Так, видалити", null, ActionsConst.DIALOG_ACTION_CODE_DELETE),
            TemplateDialogButton("Ні, залишити", null, ActionsConst.DIALOG_ACTION_CANCEL)
        )
    )

    override fun nfcEnableDialog(key: String) = TemplateDialogModel(
        key,
        "middleCenterAlignAlert",
        isClosable = false,
        TemplateDialogData(
            "☝️",
            "Потрібен доступ до NFC",
            "Щоб пройти верифікацію, потрібно дозволити використання технології NFC на вашому девайсі.",
            TemplateDialogButton("Активувати NFC", null, ActionsConst.DIALOG_ACTION_CONFIRM),
            TemplateDialogButton("Інший спосіб авторизації", null, ActionsConst.DIALOG_ACTION_CANCEL)
        )
    )

    override fun alertNoOfflineMap(key: String) = TemplateDialogModel(
        type = "smallAlert",
        key = key,
        isClosable = false,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "Немає завантажених мап",
            description = "Зараз на пристрої немає інтернету та збережених мап. Повертайтеся до застосунку, щойно відновиться інтернет-зʼєднання.\n\nНаразі дізнатися адреси Пунктів незламності та укриттів можна в органах місцевого самоврядування.",
            mainButton = TemplateDialogButton(
                name = "Зрозуміло",
                action = ActionsConst.DIALOG_ACTION_CLOSE,
            ),
        ),
    )

    override fun locationNotAvailable(key: String) = TemplateDialogModel(
        type = "smallAlert",
        key = key,
        isClosable = true,
        data = TemplateDialogData(
            icon = "\uD83D\uDE1E",
            title = "Не вдалося визначити геолокацію",
            description = "Йой! Не вдалося визначити ваше місцезнаходження. Будь ласка, перевірте інтернет та спробуйте ще раз.",
            mainButton = TemplateDialogButton(
                name = "Зрозуміло",
                action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
            ),
        ),
    )

    override fun failedToDownloadMap(key: String) = TemplateDialogModel(
        type = "middleCenterAlignAlert",
        key = key,
        isClosable = true,
        data = TemplateDialogData(
            icon = "\uD83D\uDE14",
            title = "Не вдалося завантажити мапу",
            description = "Йой! Через проблеми з інтернетом або відсутність вільної памʼяті на смартфоні не вдається завантажити мапу.",
            mainButton = TemplateDialogButton(
                name = "Спробувати ще раз",
                action = "download",
            ),
            alternativeButton = TemplateDialogButton(
                name = "Скасувати",
                action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
            ),
        ),
    )

    override fun failedToSendRating(key: String) = TemplateDialogModel(
        type = "smallAlert",
        key = key,
        isClosable = true,
        data = TemplateDialogData(
            icon = "\uD83D\uDE14",
            title = "Неможливо надіслати відгук",
            description = "Йой! Залишити відгук офлайн неможливо. Чекаємо на вас, коли знову з’явиться інтернет-з’єднання.",
            mainButton = TemplateDialogButton(
                name = "Зрозуміло",
                action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
            )
        ),
    )

    override fun failedToSendReportPoint(key: String) = TemplateDialogModel(
        type = "smallAlert",
        key = key,
        isClosable = true,
        data = TemplateDialogData(
            icon = "☝",
            title = "Повідомлення вже надіслано",
            description = "Хочете повідомити про закритий пункт незламності ще раз? Треба трохи зачекати. Надіслати нове повідомлення можна за годину.",
            mainButton = TemplateDialogButton(
                name = "Дякую",
                action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
            )
        ),
    )

    override fun failedToSendReportShelter(key: String) = TemplateDialogModel(
        type = "smallAlert",
        key = key,
        isClosable = true,
        data = TemplateDialogData(
            icon = "☝",
            title = "Повідомлення вже надіслано",
            description = "Хочете повідомити про закрите укриття ще раз? Треба трохи зачекати. Надіслати нове повідомлення можна за годину.",
            mainButton = TemplateDialogButton(
                name = "Дякую",
                action = ActionsConst.DIALOG_ACTION_CODE_CLOSE,
            )
        ),
    )

    private fun Exception.printLog() {
        if (BuildConfig.BUILD_TYPE != CommonConst.BUILD_TYPE_RELEASE) {
            try {
                val unknownErrorAlert = JSONObject()
                unknownErrorAlert.put("unknownErrorAlert", localizedMessage)
            } catch (e: JSONException) {
                crashlytics.sendNonFatalError(e)
            }
        }
    }
}
