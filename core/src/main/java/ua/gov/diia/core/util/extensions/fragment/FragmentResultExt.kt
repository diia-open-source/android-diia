package ua.gov.diia.core.util.extensions.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.throwExceptionInDebug
import java.io.Serializable

//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
////////////////////// Register for fragments navigation result //////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

inline fun <T : Any> Fragment.registerForNavigationResult(
    key: String,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline resultEvent: (T) -> Unit
) {
    findNavController()
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.observe(lifecycleOwner) {
            validateClassType(it)
            resultEvent.invoke(it)
        }
}

inline fun <T : Any> Fragment.registerForNavigationResultOnce(
    key: String,
    crossinline resultEvent: (T) -> Unit
) {
    findNavController()
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) {
            validateClassType(it)
            clearResultCallback<T>(key)
            resultEvent.invoke(it)
        }
}

fun <T : Any> Fragment.clearResultCallback(key: String) {
    findNavController()
        .currentBackStackEntry
        ?.savedStateHandle
        ?.remove<T>(key)
}

//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
/////////////////////// Register for the template dialog events //////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

inline fun Fragment.registerForTemplateDialogNavResult(
    key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    crossinline resultEvent: (String) -> Unit
) {
    registerForNavigationResult<ConsumableString>(key, lifecycleOwner) { event ->
        event.consumeEvent { action -> resultEvent.invoke(action) }
    }
}

inline fun Fragment.registerForTemplateDialogNavResultOnce(
    key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
    crossinline resultEvent: (String) -> Unit
) {
    registerForNavigationResultOnce<ConsumableString>(key) { event ->
        event.consumeEvent { action -> resultEvent.invoke(action) }
    }
}

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
///////////////////// Register for navigation result from  ///////////////////
///////////////////// pushTop fragments and alertDialogs  ////////////////////
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

inline fun <T : Any> Fragment.registerForDialogNavigationResultOnce(
    key: String,
    crossinline resultEvent: (T) -> Unit
) {
    @IdRes val currentDestinationId = currentDestinationId ?: return
    val navBackStackEntry = findNavController().getBackStackEntry(currentDestinationId)

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(key)) {
            val result = navBackStackEntry.savedStateHandle.get<T>(key)
            result?.run { resultEvent.invoke(this) }

            //remove live data to prevent the resultEvent() trigger every time when the app
            //moves into the ON_RESUME state
            navBackStackEntry.savedStateHandle.remove<T>(key)
        }
    }

    //add observer to the fragment back stack
    navBackStackEntry.lifecycle.addObserver(observer)

}


//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
/////////////////////////// Set navigation result ///////////// //////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

fun <T : Any> Fragment.setNavigationResult(
    result: T,
    key: String,
    navController: NavController = findNavController()
) {
    navController.previousBackStackEntry?.savedStateHandle?.set(key, result)
}

/**
 * Send result back using the [NavController] [NavBackStackEntry] back stack and [SavedStateHandle].
 *
 * By default it sends the [data] to the previous destination on the navigation back stack using
 * the [key] or  do nothing if less than two destinations on the stack.
 *
 * NOTE: if we're want to pop back stack to some arbitrary destination after sending result
 * (not to the previous one) we should find that destination [NavBackStackEntry] because
 * [NavController.getPreviousBackStackEntry] will not send result to the right destination.
 *
 * @param arbitraryDestination any destination [IdRes] in the [NavGraph] to which we want send data back
 * @param key the key to receive data via result callbacks
 * @param data the result data to send to the caller
 */
fun <T> Fragment.setNavigationResult(
    @IdRes arbitraryDestination: Int? = null,
    key: String,
    data: T,
    navController: NavController = findNavController()
) {
    //fetch backStackEntry as per the data destination
    val backStackEntry = with(navController) {
        if (arbitraryDestination != null) {
            getBackStackEntry(arbitraryDestination)
        } else {
            previousBackStackEntry
        }
    }

    backStackEntry
        ?.savedStateHandle
        ?.set(key, data)
}

fun validateClassType(it: Any) {
    if (it is Serializable) {
        if (it is java.lang.String || it is java.lang.Boolean || it is java.lang.Integer) {
            //in some cases we receive java String here. Ignore this case
            return
        }
        throwExceptionInDebug("Serializable is not supported. Please use Parcelable")
    }
}