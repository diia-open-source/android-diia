package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.card.ProcessCardMoleculeDeprecated
import ua.gov.diia.ui_base.components.molecule.card.ProcessCardMoleculeDataDeprecated
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun CardsListOrgDeprecated(
    modifier: Modifier = Modifier,
    data: CardsListOrgDataDeprecated,
    lazyListState: LazyListState = rememberLazyListState(),
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 4.dp, bottom = 20.dp),
        state = lazyListState,
    ) {
        itemsIndexed(data.items) { index, item ->
            ProcessCardMoleculeDeprecated(
                data = item,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
    }
}

data class CardsListOrgDataDeprecated(val items: List<ProcessCardMoleculeDataDeprecated>) : UIElementData

@Composable
@Preview
fun CardListOrganismDeprecatedPreview() {
    val dataData = ProcessCardMoleculeDataDeprecated(
        id = "",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = "label",
        title = "Card title example",
        iconBase64String = PreviewBase64Icons.apple,
        supportText = "Supporting text",
    )
    CardsListOrgDeprecated(
        data = CardsListOrgDataDeprecated(listOf(dataData, dataData, dataData, dataData, dataData, dataData, dataData, dataData, dataData, dataData))
    ) {

    }
}
