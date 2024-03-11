package ua.gov.diia.ui_base.navigation

import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath

sealed class BaseNavigation : NavigationPath {

    object Back : BaseNavigation()

    data class ContextMenu(val contextMenuArray: Array<ContextMenuField>?) : BaseNavigation()

}