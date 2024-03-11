package ua.gov.diia.menu.ui


sealed class MenuAction {
    object OpenPlayMarketAction : MenuAction()
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

