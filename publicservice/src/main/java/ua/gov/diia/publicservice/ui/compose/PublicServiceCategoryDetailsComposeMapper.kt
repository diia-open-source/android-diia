package ua.gov.diia.publicservice.ui.compose

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.publicservice.R
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData

interface PublicServiceCategoryDetailsComposeMapper {

    fun List<PublicService>.toComposeListItemGroupOrg(): ListItemGroupOrgData

}

class PublicServiceCategoryDetailsComposeMapperImpl :
    PublicServiceCategoryDetailsComposeMapper {

    private fun PublicService.toComposeListItemGroupOrgItem(): ListItemMlcData {
        return ListItemMlcData(
            label = UiText.DynamicString(this.name),
            id = this.code,
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
            action = DataActionWrapper(
                type = this.code
            )
        )
    }

    override fun List<PublicService>.toComposeListItemGroupOrg(): ListItemGroupOrgData {
        this.let {
            return ListItemGroupOrgData(
                itemsList = SnapshotStateList<ListItemMlcData>().apply {
                    addAll(it.map { it.toComposeListItemGroupOrgItem() })
                },
                componentId = UiText.StringResource(R.string.home_subcategories_list_test_tag))
        }
    }
}