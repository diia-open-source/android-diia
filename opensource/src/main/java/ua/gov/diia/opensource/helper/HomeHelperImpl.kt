package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import ua.gov.diia.home.helper.HomeHelper
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.documents.ui.gallery.DocGalleryFCompose
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.home.ui.HomeActions
import ua.gov.diia.menu.ui.MenuFCompose
import ua.gov.diia.opensource.NavHomeChildrenDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.ui.fragments.FeedF
import ua.gov.diia.publicservice.ui.categories.compose.PublicServicesCategoriesComposeF

class HomeHelperImpl: HomeHelper {

    override fun getNavMenuItem(classObj: Class<out Fragment>): HomeMenuItemConstructor = when (classObj) {
        DocGalleryFCompose::class.java -> HomeMenuItem.DOCUMENTS
        MenuFCompose::class.java -> HomeMenuItem.MENU
        PublicServicesCategoriesComposeF::class.java -> HomeMenuItem.SERVICES
        FeedF::class.java -> HomeMenuItem.FEED
        else -> HomeMenuItem.MENU
    }

    override fun getGraphId(): Int {
        return R.navigation.nav_home_children
    }

    override fun getNavDirection(position: Int): NavDirections = when (position) {
        HomeActions.HOME_DOCUMENTS -> NavHomeChildrenDirections.globalToDocGalleryFCompose()
        HomeActions.HOME_MENU -> NavHomeChildrenDirections.globalToMenuFCompose()
        HomeActions.HOME_SERVICES -> NavHomeChildrenDirections.globalToPublicServicesFCompose()
        HomeActions.HOME_FEED -> NavHomeChildrenDirections.globalToFeedF()
        else -> NavHomeChildrenDirections.globalToMenuFCompose()
    }
}