package ua.gov.diia.documents.ui.actions

interface DocActionsNavigationHandler {
    /**
     * Handles navigation to other features
     */
    fun handleNavigation(
        fragment: DocActionsDFCompose,
        navigation: DocActionsVMCompose.DocActions,
        args: DocActionsDFComposeArgs
    )
}