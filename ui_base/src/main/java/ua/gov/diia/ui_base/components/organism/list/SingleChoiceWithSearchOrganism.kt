package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.SearchInputMoleculeData
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlcl
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlclData
import ua.gov.diia.ui_base.components.molecule.message.EmptyStateErrorMolecule
import ua.gov.diia.ui_base.components.molecule.message.EmptyStateErrorMoleculeData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun SingleChoiceWithSearchOrganism(
    modifier: Modifier = Modifier,
    data: SingleChoiceWithSearchOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier) {
        SearchInputMolecule(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
            data = data.searchData,
            onUIAction = {
                onUIAction(it)
            })
        if (data.displayedList.options.isNotEmpty()) {
            SingleChoiceMlcl(
                data = data.displayedList,
                onUIAction = {
                    onUIAction(it)
                })
        } else {
            EmptyStateErrorMolecule(modifier = Modifier.padding(top = 8.dp),
                data = data.emptyListData,
                onUIAction = onUIAction)
        }

    }
}

data class SingleChoiceWithSearchOrganismData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_SEARCH_ORGANISM,
    val searchData: SearchInputMoleculeData,
    val fullList: SingleChoiceMlclData,
    val displayedList: SingleChoiceMlclData,
    val emptyListData: EmptyStateErrorMoleculeData
) : UIElementData {

    fun onItemSelected(selectedItemId: String?): SingleChoiceWithSearchOrganismData {
        if (selectedItemId == null) return this
        return this.copy(
            displayedList = this.fullList.onItemClick(selectedItemId),
        )
    }

    fun onSearch(request: String?): SingleChoiceWithSearchOrganismData {
        if (request == null) return this
        return this.copy(
            searchData = this.searchData.copy(
                searchFieldValue = request
            ),
            displayedList = this.fullList.copy(options = this.fullList.options.filter { item ->
                item.label.contains(request)
            })
        )
    }
}

@Composable
@Preview
fun SingleChoiceWithSearchOrganismPreview() {
    val source = mutableMapOf(
        "1" to "Option 1", "2" to "Option 2", "3" to "Option 3", "4" to "Option 4"
    )
    val singleChoiceListData = SingleChoiceMlclData(
        actionKey = "SingleChoiceMlclAction",
        id = "sinChoiceMlcl",
        options = SnapshotStateList<RadioBtnAtmData>().apply {
            source.map {
                add(
                    RadioBtnAtmData(
                        id = it.key,
                        label = it.value,
                        mode = RadioButtonMode.SINGLE_CHOICE,
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected,
                        logoLeft = PreviewBase64Icons.apple,
                        status = "Status"
                    )
                )
            }
        }
    )
    val emptyListData = EmptyStateErrorMoleculeData(
        text = TextWithParametersData(text = UiText.DynamicString(LoremIpsum(20).values.joinToString())),
        icon = "\uD83E\uDD37\u200D♂️",
        button = ButtonStrokeAdditionalAtomData(
            id = "", title = UiText.DynamicString("Next"), interactionState = UIState.Interaction.Enabled
        )
    )


    val searchData = SearchInputMoleculeData(
        actionKey = "SearchInputMoleculeAction",
        id = "",
        searchFieldValue = "",
        placeholder = UiText.DynamicString("Search keyword")
    )

    val data = SingleChoiceWithSearchOrganismData(
        searchData = searchData,
        fullList = singleChoiceListData,
        displayedList = singleChoiceListData,
        emptyListData = emptyListData
    )

    val state = remember {
        mutableStateOf(data)
    }

    SingleChoiceWithSearchOrganism(
        modifier = Modifier.fillMaxWidth(),
        data = state.value
    ) {
        when (it.actionKey) {
            "SingleChoiceMlclAction" -> {
                state.value = state.value.onItemSelected(it.data)
            }

            "SearchInputMoleculeAction" -> {
                state.value = state.value.onSearch(it.data)
            }
        }
    }

}