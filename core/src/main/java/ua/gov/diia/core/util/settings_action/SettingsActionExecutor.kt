package ua.gov.diia.core.util.settings_action


interface SettingsActionExecutor {

    val actionKey: String

    suspend fun executeAction()

}