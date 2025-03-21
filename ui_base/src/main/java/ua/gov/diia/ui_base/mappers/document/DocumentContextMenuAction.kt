package ua.gov.diia.ui_base.mappers.document

import ua.gov.diia.core.models.document.DocumentContextMenuAction
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

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
