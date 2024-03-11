package ua.gov.diia.home.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor

interface HomeHelper {

    /**
     * @return HomeMenuItemConstructor which represent passed fragment menu item
     * */
    fun getNavMenuItem(classObj: Class<out Fragment>): HomeMenuItemConstructor

    /**
     * @return id of navigation graph that represents tabs navigation
     * */
    fun getGraphId(): Int

    /**
     * @return navigation action to specific home screen tab item
     * */
    fun getNavDirection(position: Int): NavDirections
}