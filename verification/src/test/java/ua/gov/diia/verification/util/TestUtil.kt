package ua.gov.diia.verification.util

import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

fun dummyTemplateDialog(key: String? = null) = TemplateDialogModel(
    key = key,
    type = "viris",
    isClosable = false,
    data = TemplateDialogData(
        icon = null,
        title = "errem",
        description = null,
        mainButton = TemplateDialogButton(
            name = null,
            icon = null,
            action = "quot",
            link = null,
        ),
        alternativeButton = null,
    ),
)