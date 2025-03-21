package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.core.models.SystemDialog
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.menu.NavMenuActionsDirections
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionGlobalToNavFaq
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToDiiaId
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToNavAppSessions
import ua.gov.diia.menu.NavMenuActionsDirections.Companion.actionHomeFToSignHistory
import ua.gov.diia.menu.helper.MenuHelper
import ua.gov.diia.opensource.NavMainDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.NavSystemDialogDirections
import ua.gov.diia.web.util.extensions.fragment.navigateToWebView

class MenuHelperImpl : MenuHelper {

    override fun navigateToNotifications(fragment: Fragment) {
        fragment.apply {
            navigate(
                NavMainDirections.actionGlobalNotificationFCompose(),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override fun navigateToSupport(fragment: Fragment) {
        fragment.apply {
            navigate(
                NavMenuActionsDirections.actionHomeFToSupportF(),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override fun navigateToHelp(fragment: Fragment) {
        fragment.apply {
            navigate(
                NavMenuActionsDirections.actionHomeFToHelpF(true),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override fun navigateToSettings(fragment: Fragment) {
        fragment.apply {
            navigate(
                NavMenuActionsDirections.actionHomeFToSettingsF(),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override fun navigateToFAQ(fragment: Fragment) {
        fragment.apply {
            navigate(
                actionGlobalToNavFaq(),
                findNavControllerById(R.id.nav_host)
            )
        }
    }

    override fun navigateToLogout(fragment: Fragment) {
        fragment.navigate(
            NavSystemDialogDirections.actionGlobalToSystemDialog(
                SystemDialog(
                    fragment.getString(ua.gov.diia.menu.R.string.settings_dialog_title_logout),
                    fragment.getString(ua.gov.diia.menu.R.string.settings_dialog_logout_text),
                    fragment.getString(ua.gov.diia.menu.R.string.settings_dialog_logout_stay),
                    fragment.getString(ua.gov.diia.menu.R.string.settings_dialog_logout_leave),
                )
            ),
            fragment.findNavControllerById(ua.gov.diia.menu.R.id.nav_host)
        )
    }

    override fun navigateToDiiaId(fragment: Fragment) {
        fragment.navigate(
            actionHomeFToDiiaId(),
            fragment.findNavControllerById(ua.gov.diia.menu.R.id.nav_host),
        )
    }

    override fun navigateToSignHistory(fragment: Fragment) {
        fragment.navigate(
            actionHomeFToSignHistory(),
            fragment.findNavControllerById(ua.gov.diia.menu.R.id.nav_host),
        )
    }


    override fun navigateToAppSessions(fragment: Fragment) {
        fragment.navigate(
            actionHomeFToNavAppSessions(),
            fragment.findNavControllerById(ua.gov.diia.menu.R.id.nav_host)
        )
    }

    override fun navigateToSettingsSystemDialog(fragment: Fragment) {
        fragment.navigate(
            NavSystemDialogDirections.actionGlobalToSystemDialog(
                SystemDialog(
                    fragment.getString(
                        ua.gov.diia.menu.R.string.settings_dialog_title_faq,
                        fragment.getString(ua.gov.diia.menu.R.string.browser)
                    ),
                    null,
                    fragment.getString(ua.gov.diia.menu.R.string.settings_dialog_support_open),
                    fragment.getString(ua.gov.diia.menu.R.string.settings_dialog_support_cancel),
                )
            ),
            fragment.findNavControllerById(ua.gov.diia.menu.R.id.nav_host)
        )
    }

    override fun navigateToWebViewUrl(fragment: Fragment, url: String) {
        fragment.navigateToWebView(url)
    }

}