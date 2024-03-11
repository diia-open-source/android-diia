package ua.gov.diia.home.model

import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.home.R
import ua.gov.diia.home.ui.HomeActions

object HomeMenuItem {
    val DOCUMENTS = HomeMenuItemConstructor(
        HomeActions.HOME_DOCUMENTS,
        R.color.colorPrimary,
        R.color.colorPrimary,
        R.string.app_bar_title_empty
    )
    val SERVICES = HomeMenuItemConstructor(
        HomeActions.HOME_SERVICES,
        R.color.colorPrimary,
        R.color.colorPrimary,
        R.string.app_bar_title_empty
    )

    val MENU = HomeMenuItemConstructor(
        HomeActions.HOME_MENU,
        R.color.colorPrimary,
        R.color.colorPrimary,
        R.string.app_bar_title_empty
    )

    val FEED = HomeMenuItemConstructor(
        HomeActions.HOME_FEED,
        R.color.colorPrimary,
        R.color.colorPrimary,
        R.string.app_bar_title_empty
    )
}