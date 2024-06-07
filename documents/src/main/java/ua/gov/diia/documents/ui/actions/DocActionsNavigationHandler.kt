package ua.gov.diia.documents.ui.actions

import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath

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