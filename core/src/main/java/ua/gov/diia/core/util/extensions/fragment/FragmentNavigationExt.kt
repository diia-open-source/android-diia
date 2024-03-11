package ua.gov.diia.core.util.extensions.fragment

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

fun Fragment.findNavControllerById(@IdRes id: Int): NavController {
    var parent = parentFragment
    while (parent != null) {
        if (parent is NavHostFragment && parent.id == id) {
            return parent.navController
        }
        parent = parent.parentFragment
    }
    throw RuntimeException("NavController with specified id not found")
}

/**
 * Simplified version to get the current fragment destination id from navigation graph.
 */
val Fragment.currentDestinationId: Int?
    get() = findNavController().currentDestination?.id

val Fragment.previousDestinationId: Int?
    get() = findNavController().previousBackStackEntry?.destination?.id

fun Fragment.doOnSystemBackPressed(todo: () -> Unit) {
    activity?.onBackPressedDispatcher?.addCallback(
        viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                todo.invoke()
            }
        })
}

fun Fragment.navigate(
    destination: NavDirections,
    navController: NavController = findNavController()
) = with(navController) {
    currentDestination
        ?.getAction(destination.actionId)
        ?.let { navigate(destination) }
}

fun Activity.collapseApp(){
    moveTaskToBack(false);
}