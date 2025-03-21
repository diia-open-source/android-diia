package ua.gov.diia.opensource.util.extensions.activity

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import ua.gov.diia.opensource.R

fun Activity.navigate(
    destination: NavDirections,
    navController: NavController = findNavController(R.id.nav_host)
) = with(navController) {
    currentDestination
        ?.getAction(destination.actionId)
        ?.let { navigate(destination) }
}