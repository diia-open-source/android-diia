package ua.gov.diia.home.ui

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.home.R
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData
import javax.inject.Inject

interface HomeScreenComposeMapper {

    fun TabItemMoleculeData.toComposeTabItemMolecule(): TabItemMoleculeData
    fun toComposeTabBarOrganism(list: List<TabItemMoleculeData>): TabBarOrganismData

}

class HomeScreenComposeMapperImpl @Inject constructor(): HomeScreenComposeMapper{


    override fun TabItemMoleculeData.toComposeTabItemMolecule(): TabItemMoleculeData {
        return TabItemMoleculeData(
            actionKey = this.actionKey,
            id = this.id,
            label = this.label,
            iconSelected = this.iconSelected,
            iconUnselected = this.iconUnselected,
            iconSelectedWithBadge = this.iconSelectedWithBadge,
            iconUnselectedWithBadge = this.iconUnselectedWithBadge,
            showBadge = this.showBadge,
            componentId = this.componentId
        )
    }

    override fun toComposeTabBarOrganism(list: List<TabItemMoleculeData>): TabBarOrganismData {
        with(list) {
            return this.let {
                return TabBarOrganismData(
                    componentId = UiText.StringResource(R.string.home_tab_bar_test_tag),
                    tabs = SnapshotStateList<TabItemMoleculeData>().apply {
                        addAll(it.map { it.toComposeTabItemMolecule() })

                    })
            }
        }
    }
}