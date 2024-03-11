package ua.gov.diia.ui_base.components.molecule.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun TabItemMolecule(
    modifier: Modifier = Modifier,
    data: TabItemMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = Modifier
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        states = listOf(UIState.Selection.Selected)
                    )
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (data.showBadge) {
            IconWithBadge(
                modifier = Modifier
                    .size(24.dp),
                image = when (data.selectionState) {
                    UIState.Selection.Selected -> data.iconSelectedWithBadge
                        ?: UiText.StringResource(resId = R.drawable.diia_check)

                    UIState.Selection.Unselected -> data.iconUnselectedWithBadge
                        ?: UiText.StringResource(resId = R.drawable.diia_back_arrow)
                },
            )
        } else {
            IconWithBadge(
                modifier = Modifier
                    .size(24.dp),
                image = when (data.selectionState) {
                    UIState.Selection.Selected -> data.iconSelected
                        ?: UiText.StringResource(resId = R.drawable.diia_check)

                    UIState.Selection.Unselected -> data.iconUnselected ?: UiText.StringResource(
                        resId = R.drawable.diia_back_arrow
                    )
                },
            )
        }

        data.label?.let {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = data.label,
                style = DiiaTextStyle.t5TextSmallDescription,
                color = White
            )
        }
    }
}

data class TabItemMoleculeData(
    val actionKey: String = UIActionKeysCompose.MENU_ITEM_CLICK,
    val id: String? = "",
    val label: String? = null,
    val iconSelected: UiText? = null,
    val iconUnselected: UiText? = null,
    val iconSelectedWithBadge: UiText? = null,
    val iconUnselectedWithBadge: UiText? = null,
    val showBadge: Boolean = false,
    val selectionState: UIState.Selection = UIState.Selection.Unselected
) : UIElementData

@Composable
@Preview
fun MenuItemAtomSelected() {
    val state = TabItemMoleculeData(
        label = "Label",
        iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
        selectionState = UIState.Selection.Selected
    )
    TabItemMolecule(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun MenuItemAtomUnselected() {
    val state = TabItemMoleculeData(
        label = "Label",
        iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
        selectionState = UIState.Selection.Unselected
    )
    TabItemMolecule(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun MenuItemAtomSelectedWithBadge() {
    val state = TabItemMoleculeData(
        label = "Label",
        selectionState = UIState.Selection.Selected,
        iconUnselectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_unselected_badge),
        iconSelectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_selected_badge),
        showBadge = true
    )
    TabItemMolecule(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun MenuItemAtomUnselectedWithBadge() {
    val state = TabItemMoleculeData(
        label = "Label",
        selectionState = UIState.Selection.Unselected,
        iconUnselectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_unselected_badge),
        iconSelectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_selected_badge),
        showBadge = true
    )
    TabItemMolecule(modifier = Modifier, data = state) {}
}