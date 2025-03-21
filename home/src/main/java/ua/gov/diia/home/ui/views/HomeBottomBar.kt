package ua.gov.diia.home.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.documents.ui.gallery.ScreenDocGalleryRoute
import ua.gov.diia.feed.ScreenFeedRoute
import ua.gov.diia.menu.ui.ScreenMenuRoute
import ua.gov.diia.publicservice.ui.categories.compose.PublicServicesCategoriesRoute
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.theme.Black


@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    data: TabBarOrganismData,
    onTabClick: (action: UIAction) -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        containerColor = Black
    ) {
        data.tabs.forEach {
            HomeBottomBarItem(
                item = it,
                onTabClick = onTabClick
            )
        }
    }
}

@Composable
fun RowScope.HomeBottomBarItem(
    item: TabItemMoleculeData,
    onTabClick: (action: UIAction) -> Unit
) {
    BottomNavigationItem(
        modifier = Modifier.padding(top = 15.dp, bottom = 12.dp).height(43.dp),
        selected = item.selectionState == UIState.Selection.Selected,
        icon = {
            if (item.showBadge) {
                IconWithBadge(
                    modifier = Modifier
                        .size(24.dp),
                    image = when (item.selectionState) {
                        UIState.Selection.Selected -> item.iconSelectedWithBadge
                            ?: UiText.StringResource(resId = R.drawable.ic_tab_menu_selected_badge)

                        UIState.Selection.Unselected -> item.iconUnselectedWithBadge
                            ?: UiText.StringResource(resId = R.drawable.ic_tab_menu_unselected_badge)
                    },
                )
            } else {
                IconWithBadge(
                    modifier = Modifier
                        .size(24.dp),
                    image = when (item.selectionState) {
                        UIState.Selection.Selected -> item.iconSelected
                            ?: UiText.StringResource(resId = R.drawable.ic_tab_menu_selected)

                        UIState.Selection.Unselected -> item.iconUnselected
                            ?: UiText.StringResource(
                                resId = R.drawable.ic_tab_menu_unselected
                            )
                    },
                )
            }
        },
        label = {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = item.label ?: "",
                style = DiiaTextStyle.t5TextSmallDescription,
                color = White
            )
        },
        onClick = {
            onTabClick(UIAction(actionKey = UIActionKeysCompose.MENU_ITEM_CLICK, data = item.id))
        }
    )
}

private object HomeActions {
    const val HOME_FEED = 0
    const val HOME_DOCUMENTS = 1
    const val HOME_SERVICES = 2
    const val HOME_MENU = 3
}


@Preview
@Composable
fun HomeBottomBarPreview123() {

    val tabFeed = TabItemMoleculeData(
        label = "Стрічка",
        iconSelected = UiText.StringResource(R.drawable.ic_tab_feed_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_tab_feed_unselected),
        actionKey = HomeActions.HOME_FEED.toString(),
        id = HomeActions.HOME_FEED.toString(),
        componentId = UiText.StringResource(R.string.home_tab_feed_test_tag)
    )
    val tabDocuments = TabItemMoleculeData(
        label = "Документи",
        iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
        actionKey = HomeActions.HOME_DOCUMENTS.toString(),
        id = HomeActions.HOME_DOCUMENTS.toString(),
        componentId = UiText.StringResource(R.string.home_tab_documents_test_tag)
    )
    val tabServices = TabItemMoleculeData(
        label = "Сервіси",
        iconSelected = UiText.StringResource(R.drawable.ic_tab_services_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_tab_services_selected),
        actionKey = HomeActions.HOME_SERVICES.toString(),
        id = HomeActions.HOME_SERVICES.toString(),
        componentId = UiText.StringResource(R.string.home_tab_services_test_tag)
    )
    val tabMenu = TabItemMoleculeData(
        label = "Меню",
        iconSelected = UiText.StringResource(R.drawable.ic_tab_menu_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_tab_menu_unselected),
        iconSelectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_selected_badge),
        iconUnselectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_unselected_badge),
        actionKey = HomeActions.HOME_MENU.toString(),
        id = HomeActions.HOME_MENU.toString(),
        showBadge = true,
        componentId = UiText.StringResource(R.string.home_tab_menu_test_tag)
    )
    val tabs = listOf(tabFeed, tabDocuments, tabServices, tabMenu)
    val tabBarData = TabBarOrganismData(
        tabs = SnapshotStateList<TabItemMoleculeData>().apply {
            tabs.forEach {
                add(it)
            }
        }
    )
    val tabBarState = remember { mutableStateOf(tabBarData) }
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
//    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(bottomBar = {
        HomeBottomBar(data = tabBarState.value,
            onTabClick = { action ->
                tabBarState.value = tabBarState.value.onTabClicked(action.data)
                navController.navigate(
                    when (action.data) {
                        "${HomeActions.HOME_FEED}" -> ScreenFeedRoute
                        "${HomeActions.HOME_DOCUMENTS}" -> ScreenDocGalleryRoute
                        "${HomeActions.HOME_SERVICES}" -> PublicServicesCategoriesRoute
                        "${HomeActions.HOME_MENU}" -> ScreenMenuRoute
                        else -> ScreenFeedRoute
                    }
                ) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            })
    }, content = { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = ScreenFeedRoute
        ) {
            composable<ScreenFeedRoute> {
                TabPresentation("Feed")
            }
            composable<ScreenDocGalleryRoute> {
//                TabPresentation("Gallery")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        var text by rememberSaveable { mutableStateOf("Hello") }

                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("Label") }
                        )
                    }


                }

            }
            composable<PublicServicesCategoriesRoute> {
                TabPresentation("Services")
            }
            composable<ScreenMenuRoute> {
                TabPresentation("Menu")
            }
        }
    })
}

@Composable
fun TabPresentation(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}

