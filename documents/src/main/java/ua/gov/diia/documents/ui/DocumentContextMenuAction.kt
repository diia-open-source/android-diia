package ua.gov.diia.documents.ui

import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

interface DocumentContextMenuAction {
    val action: String
}

fun DocumentContextMenuAction.toListItemMlcData(
    label: UiText,
    iconLeft: UiIcon.DrawableResource
): ListItemMlcData {
    return ListItemMlcData(
        actionKey = this.action,
        id = this.action,
        label = label,
        iconLeft = iconLeft,
        action = DataActionWrapper(type = this.action)
    )
}
