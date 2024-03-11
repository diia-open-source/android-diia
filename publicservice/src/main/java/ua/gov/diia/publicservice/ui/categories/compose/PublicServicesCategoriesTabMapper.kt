package ua.gov.diia.publicservice.ui.categories.compose

import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.publicservice.models.PublicServiceTab
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.tile.ServiceCardTileOrgData

interface PublicServicesCategoriesTabMapper {

    fun List<PublicServiceCategory>.toComposeServiceTileOrganism(): ServiceCardTileOrgData

    fun generateComposeChipTabBarV2(
        tabs: List<PublicServiceTab>?,
        selectedTab: String?
    ): ChipTabsOrgData?

    fun generateSearchInputMoleculeV2(
        placeholder: String,
        mode: Int,
    ): SearchInputV2Data?
}

