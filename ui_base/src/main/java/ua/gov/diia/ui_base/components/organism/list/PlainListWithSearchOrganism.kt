package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData

@Composable
fun PlainListWithSearchOrganism(
    modifier: Modifier = Modifier,
    data: PlainListWithSearchOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier.padding(top = 24.dp)) {
        if (data.fullList.itemsList.isNotEmpty()) {
            SearchInputV2(
                modifier = Modifier
                    .fillMaxWidth(),
                data = data.searchData,
                onUIAction = onUIAction
            )
            ListItemGroupOrg(
                data = data.displayedList,
                onUIAction = onUIAction
            )
        } else {
            StubMessageMlc(
                data = data.emptyListData,
                onUIAction = onUIAction
            )
        }
    }
}

data class PlainListWithSearchOrganismData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_SEARCH_ORGANISM,
    val searchData: SearchInputV2Data,
    val fullList: ListItemGroupOrgData,
    val displayedList: ListItemGroupOrgData,
    val emptyListData: StubMessageMlcData
) : UIElementData {

    fun onSearch(request: String?): PlainListWithSearchOrganismData {
        if (request == null) return this
        val displayedList = this.fullList.itemsList.filter {
            val title = it.label
            if (title is UiText.DynamicString) {
                title.value.contains(request, ignoreCase = true)
            } else false
        }
        val list = SnapshotStateList<ListItemMlcData>().apply {
            addAll(displayedList)
        }
        return this.copy(
            searchData = this.searchData.copy(searchFieldValue = UiText.DynamicString(request)),
            displayedList = ListItemGroupOrgData(itemsList = list)
        )
    }
}


@Preview
@Composable
fun PlainListWithSearchOrganismPreview() {
    val list =
        ListItemGroupOrgData(itemsList = SnapshotStateList<ListItemMlcData>().apply {
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                )
            )
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                )
            )
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    description = UiText.DynamicString("Description"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                )
            )
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    description = UiText.DynamicString("Description"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.OUT_LINK.code),
                )
            )
        })
    val data = PlainListWithSearchOrganismData(
        searchData = SearchInputV2Data(placeholder = UiText.DynamicString("Пошук")),
        fullList = list,
        displayedList = list,
        emptyListData = StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDD90"),
            title = UiText.DynamicString("На жаль, сталася помилка"),
            description = TextWithParametersData(
                text = UiText.DynamicString("Перелік банків недоступний. Спробуйте трошки пізніше.")
            )
        )

    )
    PlainListWithSearchOrganism(data = data) {}
}
