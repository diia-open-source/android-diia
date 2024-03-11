package ua.gov.diia.home.ui

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData
import javax.inject.Inject

interface HomeScreenComposeMapper {

    fun TabItemMoleculeData.toComposeTabItemMolecule(): TabItemMoleculeData
    fun List<TabItemMoleculeData>.toComposeTabBarOrganism(): TabBarOrganismData

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
            showBadge = this.showBadge
        )
    }

    override fun List<TabItemMoleculeData>.toComposeTabBarOrganism(): TabBarOrganismData{
        return mapTabItemMoleculeDataToTabBarOrganismData(this)
    }

    fun mapTabItemMoleculeDataToTabBarOrganismData(list: List<TabItemMoleculeData>): TabBarOrganismData {
        with(list) {
            return this.let {
                return TabBarOrganismData(
                    tabs = SnapshotStateList<TabItemMoleculeData>().apply {
                        addAll(it.map { it.toComposeTabItemMolecule() })
                    })
            }
        }
    }
}