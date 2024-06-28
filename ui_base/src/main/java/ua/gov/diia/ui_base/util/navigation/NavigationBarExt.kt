package ua.gov.diia.ui_base.util.navigation

import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.TOOLBAR_CONTEXT_MENU
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
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
        contextMenuAction = contextMenuAction,
        componentId = componentId.toDynamicStringOrNull()
    )
}

fun generateComposeNavigationPanel(
    backMenuAction: String = TOOLBAR_NAVIGATION_BACK,
    contextMenuAction: String = TOOLBAR_CONTEXT_MENU,
    title: String? = null,
    uiTextTitle: UiText? = null,
    isContextMenuExist: Boolean? = null,
    componentId: UiText? = null,
): NavigationPanelMlcData {
    return NavigationPanelMlcData(
        backAction = backMenuAction,
        title = uiTextTitle ?: title.toDynamicString(),
        isContextMenuExist = isContextMenuExist ?: false,
        contextMenuAction = contextMenuAction,
        componentId = componentId
    )
}