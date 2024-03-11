package ua.gov.diia.ui_base.fragments.dialog.system

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.core.models.SystemDialog
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.ui_base.components.atom.button.ButtonSystemAtomData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.screen.SystemDialogScreenData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

class DiiaSystemDFVM : ViewModel() {

    private val _action = MutableLiveData<UiDataEvent<Action>>()
    val action: LiveData<UiDataEvent<Action>>
        get() = _action


    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()
    val uiData = mutableStateOf(SystemDialogScreenData())

    fun doInit(dialogData: SystemDialog) {
        val title = dialogData.title?.let { UiText.DynamicString(it) }
        val description = dialogData.message?.let { UiText.DynamicString(it) }
        val positiveButton = dialogData.positiveButtonTitle?.let {
            ButtonSystemAtomData(
                title = UiText.DynamicString(it),
                actionKey = UIActionKeysCompose.BUTTON_REGULAR
            )
        }
        val negativeButton = dialogData.negativeButtonTitle?.let {
            ButtonSystemAtomData(
                title = UiText.DynamicString(it),
                actionKey = UIActionKeysCompose.BUTTON_ALTERNATIVE
            )
        }

        uiData.value = uiData.value.copy(
            titleText = title,
            descriptionText = description,
            positiveButton = positiveButton,
            negativeButton = negativeButton
        )
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.BUTTON_REGULAR -> {
                onPositiveButtonClicked()
            }

            UIActionKeysCompose.BUTTON_ALTERNATIVE -> {
                onNegativeButtonClicked()
            }
        }
    }

    private fun onPositiveButtonClicked() {
        _action.value = UiDataEvent(Action.POSITIVE)
        _navigation.tryEmit(Navigation.DismissDialog)
    }

    private fun onNegativeButtonClicked() {
        _action.value = UiDataEvent(Action.NEGATIVE)
        _navigation.tryEmit(Navigation.DismissDialog)
    }

    enum class Action {
        POSITIVE, NEGATIVE
    }

    sealed class Navigation : NavigationPath {
        object DismissDialog : Navigation()
    }
}