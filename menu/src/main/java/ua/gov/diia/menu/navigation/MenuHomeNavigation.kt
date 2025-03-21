package ua.gov.diia.menu.navigation

import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.navigation.HomeNavigation

sealed class MenuHomeNavigation : HomeNavigation {

    data class ToTemplateDialog(
        val template: TemplateDialogModel,
        override var isConsumed: Boolean = false
    ) : MenuHomeNavigation()

    data class ToWebView(
        val link: String,
        override var isConsumed: Boolean = false
    ) : MenuHomeNavigation()

    data class ToNotifications(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToSupport(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToHelp(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToSettings(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToFAQ(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToLogout(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToDiiaID(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToSignHistory(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToAppSession(override var isConsumed: Boolean = false) : MenuHomeNavigation()
    data class ToLinkDialog(override var isConsumed: Boolean = false) : MenuHomeNavigation()
}