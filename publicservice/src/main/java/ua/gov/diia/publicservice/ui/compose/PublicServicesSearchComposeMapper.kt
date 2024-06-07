package ua.gov.diia.publicservice.ui.compose

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.publicservice.R
import ua.gov.diia.publicservice.models.PublicServiceView
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData

interface PublicServicesSearchComposeMapper {

    fun List<PublicServiceView>.toComposeListItemGroupOrgData(): ListItemGroupOrgData

    fun generateSearchInputMoleculeV2(
        placeholder: String,
        mode: Int,
    ): SearchInputV2Data?

    fun StubMessageMlcData.toComposeEmptyStateErrorMoleculeData(): StubMessageMlcData

}

class PublicServicesSearchComposeMapperImpl :
    PublicServicesSearchComposeMapper {
    private fun PublicServiceView.toComposeListItemMlcData(): ListItemMlcData {
        return ListItemMlcData(
            label = UiText.DynamicString(this.name),
            id = this.code,
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
            action = DataActionWrapper(
                type = this.code
            )
        )
    }

    override fun List<PublicServiceView>.toComposeListItemGroupOrgData(): ListItemGroupOrgData {
        this.let {
            return ListItemGroupOrgData(
                itemsList = SnapshotStateList<ListItemMlcData>().apply {
                    addAll(it.map { it.toComposeListItemMlcData() })
                },
                componentId = UiText.StringResource(R.string.home_services_list_test_tag))
        }
    }

    override fun generateSearchInputMoleculeV2(
        placeholder: String,
        mode: Int
    ): SearchInputV2Data {
        return SearchInputV2Data(
            placeholder = UiText.DynamicString(placeholder),
            mode = mode,
            componentId = UiText.StringResource(R.string.home_search_services_test_tag)
        )
    }

    override fun StubMessageMlcData.toComposeEmptyStateErrorMoleculeData(): StubMessageMlcData {
        return StubMessageMlcData(
            componentId = this.componentId,
            icon = this.icon,
            title = this.title
        )
    }
}