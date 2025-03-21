package ua.gov.diia.opensource.util.settings_action

import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.settings_action.SettingsActionExecutor
import ua.gov.diia.opensource.di.actions.GlobalActionForceAppUpdate
import javax.inject.Inject

class ForceUpdateActionExecutor @Inject constructor(
    @GlobalActionForceAppUpdate private val globalActionForceUpdate: MutableStateFlow<UiDataEvent<Boolean>>
) : SettingsActionExecutor {

    override val actionKey = "forceUpdate"

    override suspend fun executeAction() {
        globalActionForceUpdate.emit(UiDataEvent(true))
    }
}