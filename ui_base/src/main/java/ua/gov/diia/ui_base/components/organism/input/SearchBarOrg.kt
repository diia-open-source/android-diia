package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.input.SearchBarOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUiModel

@Composable
fun SearchBarOrg(
    modifier: Modifier = Modifier,
    data: SearchBarOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(modifier = modifier) {
        ua.gov.diia.ui_base.components.molecule.input.SearchInputMlc(
            modifier = Modifier.padding(bottom = 8.dp)
                .fillMaxWidth()
                .weight(1.0f, true),
            data = data.searchInputMlc,
            onUIAction = {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = it.data
                    )
                )
            }
        )
        ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtm(
            modifier = Modifier.padding(top = 8.dp, end = 24.dp),
            data = data.btnWhiteAdditionalIconAtm,
            onUIAction = {
                onUIAction(it)
            }
        )
    }
}

data class SearchBarOrgData(
    val actionKey: String = UIActionKeysCompose.SEARCH_INPUT,
    val componentId: UiText? = null,
    val searchInputMlc: SearchInputMlcData,
    val btnWhiteAdditionalIconAtm: BtnWhiteAdditionalIconAtmData
) : UIElementData {
    fun onChange(newValue: String?): SearchBarOrgData {
        return this.copy(
            searchInputMlc = searchInputMlc.onChange(newValue)
        )
    }

    fun onBadgeChanged(newValue: Int): SearchBarOrgData {
        return this.copy(
            btnWhiteAdditionalIconAtm = btnWhiteAdditionalIconAtm.onBadgeChanged(newValue)
        )
    }
}

fun SearchBarOrg.toUiModel(): SearchBarOrgData {
    return SearchBarOrgData(
        componentId = this.componentId.toDynamicStringOrNull(),
        searchInputMlc = searchInputMlc.toUiModel(),
        btnWhiteAdditionalIconAtm = btnWhiteAdditionalIconAtm.toUIModel()
    )
}

@Composable
@Preview
fun SearchBarOrgPreview_Empty() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            interactionState = UIState.Interaction.Enabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_With_Text() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            searchFieldValue = UiText.DynamicString("Text"),
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            interactionState = UIState.Interaction.Enabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Label() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            label = "Label".toDynamicStringOrNull(),
            interactionState = UIState.Interaction.Enabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Label_Counter() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            label = "Label".toDynamicStringOrNull(),
            badge = BadgeCounterAtmData(1),
            interactionState = UIState.Interaction.Enabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Counter() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            badge = BadgeCounterAtmData(10),
            interactionState = UIState.Interaction.Enabled
        )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

@Composable
@Preview
fun SearchInputMlcPreviewEditable_Icon_Counter_Disabled() {
    val data = SearchBarOrgData(
        componentId = UiText.DynamicString("001"),
        searchInputMlc = SearchInputMlcData(
            id = "",
            placeholder = UiText.DynamicString("Search keyword"),
            iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SEARCH.code),
            iconRight = UiIcon.DrawableResource(DiiaResourceIcon.CLOSE_RECTANGLE.code)
        ),
        btnWhiteAdditionalIconAtm = BtnWhiteAdditionalIconAtmData(
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
            badge = BadgeCounterAtmData(1),
            interactionState = UIState.Interaction.Disabled
            )
    )
    SearchBarOrg(
        modifier = Modifier,
        data = data,
        onUIAction = {
            (UIAction(actionKey = data.actionKey))
        }
    )
}

enum class ModeSearchInput(val value: Int) {
    EDITABLE(0), BUTTON(1)
}
