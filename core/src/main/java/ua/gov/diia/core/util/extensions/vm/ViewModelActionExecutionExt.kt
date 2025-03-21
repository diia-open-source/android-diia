package ua.gov.diia.core.util.extensions.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction

fun <T> T.executeAction(
    progressIndicator: MutableLiveData<Boolean>? = null,
    contentLoadedIndicator: MutableLiveData<Boolean>? = null,
    errorIndicator: MutableLiveData<Boolean>? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    templateKey: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
    action: suspend CoroutineScope.() -> Unit
) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction {
    progressIndicator?.postValue(true)
    contentLoadedIndicator?.postValue(false)
    errorIndicator?.postValue(false)
    viewModelScope.launch(dispatcher) {
        try {
            action.invoke(this)
            resetErrorCounter()
        } catch (e: Exception) {
            setLastAction {
                executeAction(
                    progressIndicator,
                    contentLoadedIndicator,
                    errorIndicator,
                    dispatcher,
                    templateKey,
                    action
                )
            }
            consumeException(e, templateKey)
            errorIndicator?.postValue(true)
        } finally {
            progressIndicator?.postValue(false)
            contentLoadedIndicator?.postValue(true)
        }
    }
}

fun <T> T.executeActionOnFlow(
    progressIndicator: MutableStateFlow<Boolean>? = null,
    contentLoadedIndicator: MutableStateFlow<Boolean>? = null,
    errorIndicator: MutableStateFlow<Boolean>? = null,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    templateKey: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
    action: suspend CoroutineScope.() -> Unit
) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {
    progressIndicator?.value = true
    contentLoadedIndicator?.value = false
    errorIndicator?.value = false
    viewModelScope.launch(dispatcher) {
        try {
            action.invoke(this)
            resetErrorCounter()
        } catch (e: Exception) {
            setLastAction {
                executeActionOnFlow(
                    progressIndicator,
                    contentLoadedIndicator,
                    errorIndicator,
                    dispatcher,
                    templateKey,
                    action
                )
            }
            consumeException(e, templateKey)
            errorIndicator?.value = true
        } finally {
            progressIndicator?.value = false
            contentLoadedIndicator?.value = true
        }
    }
}