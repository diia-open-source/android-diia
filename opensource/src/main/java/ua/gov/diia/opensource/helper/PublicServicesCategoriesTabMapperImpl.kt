package ua.gov.diia.opensource.helper

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.opensource.R
import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.publicservice.models.PublicServiceTab
import ua.gov.diia.publicservice.ui.categories.compose.PublicServicesCategoriesTabMapper
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.ServiceCardMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeDataV2
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.tile.ServiceCardTileOrgData
import javax.inject.Inject

class PublicServicesCategoriesTabMapperImpl @Inject constructor() :
    PublicServicesCategoriesTabMapper {

    override fun List<PublicServiceCategory>.toComposeServiceTileOrganism(): ServiceCardTileOrgData {
        return ServiceCardTileOrgData(
            items = SnapshotStateList<ServiceCardMlcData>().apply {
                addAll(this@toComposeServiceTileOrganism.map { it.toComposeServiceCardMolecule() })
            },
            componentId = UiText.StringResource(R.string.home_service_tile_test_tag)
        )
    }

    override fun generateComposeChipTabBarV2(
        tabs: List<PublicServiceTab>?,
        selectedTab: String?
    ): ChipTabsOrgData? {
        return if (tabs?.size == 1) {
            null
        } else ChipTabsOrgData(
            tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
                if (tabs != null) {
                    addAll(tabs.map {
                        ChipTabMoleculeDataV2(
                            id = it.code,
                            title = it.name,
                            selectionState = if (it.code == selectedTab) UIState.Selection.Selected else UIState.Selection.Unselected,
                            componentId = if (it.code == "citizens")
                                UiText.StringResource(R.string.home_chip_tab_citizens_test_tag)
                            else UiText.StringResource(R.string.home_chip_tab_civil_servants_test_tag)
                        )
                    })
                }
            },
            componentId = UiText.StringResource(R.string.home_chip_tabs_services_test_tag)
        )
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

    private fun PublicServiceCategory.toComposeServiceCardMolecule(): ServiceCardMlcData {
        return ServiceCardMlcData(
            label = this.name,
            id = this.code,
            icon = UiIcon.DrawableResource(this.code)
        )
    }
}