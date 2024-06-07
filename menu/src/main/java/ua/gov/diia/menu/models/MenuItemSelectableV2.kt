package ua.gov.diia.menu.models

import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

data class MenuItemSelectableV2(
    val menuItemIcon: UiText? = null,
    var menuItemTitle: UiText? = null,
    val menuAction: String,
    val description: String? = null,
    val showBadge: Boolean = false
)
