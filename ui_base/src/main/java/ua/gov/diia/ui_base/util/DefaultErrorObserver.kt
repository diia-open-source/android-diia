package ua.gov.diia.ui_base.util

import androidx.lifecycle.Observer
import androidx.navigation.NavController
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.ui_base.NavErrorDirections
import ua.gov.diia.ui_base.fragments.errordialog.DialogError
import ua.gov.diia.ui_base.fragments.errordialog.ErrorDVM

class DefaultErrorObserver(
    private val navController: NavController
) : Observer<UiDataEvent<DialogError>> {

    override fun onChanged(event: UiDataEvent<DialogError>) {
        event.getContentIfNotHandled()?.let {
            navController.navigate(NavErrorDirections.actionGlobalToErrorD(it))
        }
    }
}

class DefaultErrorActionObserver(
    private val retryAction: () -> Unit,
    private val popUpToOnFinish: Int,
    private val navController: NavController,
    private val processingDestinationId: Int
) : Observer<UiDataEvent<ErrorDVM.ErrorAction>> {

    override fun onChanged(event: UiDataEvent<ErrorDVM.ErrorAction>) {
        if (navController.previousBackStackEntry?.destination?.id == processingDestinationId) {
            event.getContentIfNotHandled()?.let { value ->
                when (value) {
                    ErrorDVM.ErrorAction.RETRY -> {
                        navController.popBackStack()
                        retryAction.invoke()
                    }
                    ErrorDVM.ErrorAction.FINISH -> {
                        navController.popBackStack(popUpToOnFinish, false)
                    }
                }
            }
        }
    }

}
