package ua.gov.diia.home.ui

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData

class HomeScreenComposeMapperImplTest {
    val mapper: HomeScreenComposeMapperImpl = HomeScreenComposeMapperImpl()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test mapping of TabItemMoleculeData list to TabBarOrganismData`() {
        val mutableList = mutableListOf<TabItemMoleculeData>()
        val firstObj = TabItemMoleculeData(
            actionKey = "action_first",
            id = "first_item_id",
            label = "First Item",
            iconSelected = UiText.DynamicString("iconSelected"),
            iconUnselected = UiText.DynamicString("iconUnselected"),
            iconSelectedWithBadge = UiText.DynamicString("iconSelectedWithBadge"),
            iconUnselectedWithBadge = UiText.DynamicString("iconUnselectedWithBadge"),
            showBadge = true,
            selectionState = UIState.Selection.Selected
        )
        mutableList.add(firstObj)

//        val mappedData = mapper.mapTabItemMoleculeDataToTabBarOrganismData(mutableList)
//
//        val firstMappedItem = mappedData.tabs[0]
//
//        assertEquals(firstObj.id, firstMappedItem.id)
//        assertEquals(firstObj.actionKey, firstMappedItem.actionKey)
//        assertEquals(firstObj.label, firstMappedItem.label)
//        assertEquals(firstObj.iconSelected, firstMappedItem.iconSelected)
//        assertEquals(firstObj.iconUnselected, firstMappedItem.iconUnselected)
//        assertEquals(firstObj.iconSelectedWithBadge, firstMappedItem.iconSelectedWithBadge)
//        assertEquals(firstObj.iconUnselectedWithBadge, firstMappedItem.iconUnselectedWithBadge)
//        assertEquals(firstObj.showBadge, firstMappedItem.showBadge)
//        assertEquals(UIState.Selection.Unselected, firstMappedItem.selectionState)

    }
}