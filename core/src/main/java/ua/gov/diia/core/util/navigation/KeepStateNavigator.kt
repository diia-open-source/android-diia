package ua.gov.diia.core.util.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KeepViewHierarchyInMemory

@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int,
    private val onTransactionComplete: (Class<out Fragment>) -> Unit,
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment

        if (currentFragment != null) {
            if (currentFragment::class.java.isAnnotationPresent(KeepViewHierarchyInMemory::class.java)) {
                transaction.hide(currentFragment)
            } else {
                transaction.detach(currentFragment)
            }
        } else {
            initialNavigate = true
        }


        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)
        } else {
            if (fragment::class.java.isAnnotationPresent(KeepViewHierarchyInMemory::class.java)) {
                transaction.show(fragment)
            } else {
                transaction.attach(fragment)
            }
        }
        fragment.arguments = args

        if (currentFragment?.javaClass == fragment.javaClass) {
            return null
        }

        transaction.apply {
            setPrimaryNavigationFragment(fragment)
            setReorderingAllowed(true)
            runOnCommit {
                onTransactionComplete(fragment::class.java)
            }
            commit()
        }

        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }

}