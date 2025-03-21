package ua.gov.diia.menu.ui

import ua.gov.diia.core.util.delegation.WithCrashlytics


sealed class MenuAction {
    data class OpenPlayMarketAction(val crashlytics: WithCrashlytics) : MenuAction()
    object OpenHelpAction : MenuAction()
    object OpenDiiaId : MenuAction()
    object OpenSignHistory : MenuAction()
    object OpenAppSessions : MenuAction()
    object OpenSupportAction : MenuAction()
    object OpenFAQAction : MenuAction()
    object ShareApp : MenuAction()
    object OpenSettings : MenuAction()
    object Logout : MenuAction()
    object AboutDiia : MenuAction()
    object CopyDeviceUid : MenuAction()
    object OpenPolicyLink : MenuAction()
    data class DoCopyDeviceUid(val deviceUid: String) : MenuAction()
}

