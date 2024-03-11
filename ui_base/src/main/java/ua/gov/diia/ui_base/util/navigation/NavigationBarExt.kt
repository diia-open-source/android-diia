package ua.gov.diia.ui_base.util.navigation

import ua.gov.diia.core.models.common_compose.NavigationBarMlcl
import ua.gov.diia.core.models.common_compose.org.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.TOOLBAR_CONTEXT_MENU
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData

fun NavigationPanelMlc?.toComposeNavigationPanel(
    backMenuAction: String = TOOLBAR_NAVIGATION_BACK,
    contextMenuAction: String = TOOLBAR_CONTEXT_MENU,
    title: String? = null,
    isContextMenuExist: Boolean? = null
): NavigationPanelMlcData? {
    if (this == null) return null
    return NavigationPanelMlcData(
        backAction = backMenuAction,
        title = title.toDynamicStringOrNull() ?: this.label.toDynamicString(),
        isContextMenuExist = isContextMenuExist ?: !this.ellipseMenu.isNullOrEmpty(),
        contextMenuAction = contextMenuAction
    )
}


fun NavigationBarMlcl?.toComposeNavigationBarPanel(
    backMenuAction: String = TOOLBAR_NAVIGATION_BACK,
    contextMenuAction: String = TOOLBAR_CONTEXT_MENU,
    title: String? = null,
    isContextMenuExist: Boolean? = null,
): NavigationPanelMlcData? {
    if (this == null) return null
    return NavigationPanelMlcData(
        backAction = backMenuAction,
        title = title.toDynamicStringOrNull() ?: this.label.toDynamicString(),
        isContextMenuExist = isContextMenuExist ?: !this.ellipseMenu.isNullOrEmpty(),
        contextMenuAction = contextMenuAction
    )
}

fun generateComposeNavigationPanel(
    backMenuAction: String = TOOLBAR_NAVIGATION_BACK,
    contextMenuAction: String = TOOLBAR_CONTEXT_MENU,
    title: String? = null,
    isContextMenuExist: Boolean? = null,
): NavigationPanelMlcData {
    return NavigationPanelMlcData(
        backAction = backMenuAction,
        title = title.toDynamicString(),
        isContextMenuExist = isContextMenuExist ?: false,
        contextMenuAction = contextMenuAction
    )
}