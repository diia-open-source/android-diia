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
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.CardMlc
import ua.gov.diia.ui_base.components.molecule.card.CardMlcData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun CardsListOrg(
    modifier: Modifier = Modifier,
    data: CardsListOrgData,
    lazyListState: LazyListState = rememberLazyListState(),
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 4.dp, bottom = 20.dp),
        state = lazyListState,
    ) {
        itemsIndexed(data.items) { index, item ->
            CardMlc(
                data = item,
                progressIndicator = progressIndicator,
                diiaResourceIconProvider = diiaResourceIconProvider,
                onUIAction = onUIAction
            )
        }
    }
}

data class CardsListOrgData(val items: List<CardMlcData>) : UIElementData

@Composable
@Preview
fun CardListOrganismPreview() {
    val dataData = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = UiText.DynamicString("Label"),
        title = UiText.DynamicString("Card title"),
        icon = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        subtitle = UiText.DynamicString("Subtitle"),
        description = UiText.DynamicString("Description"),
        ticker = TickerAtomData(
            title = "Ticker text!",
            type = TickerType.SMALL_NEUTRAL
        ),
        botLabel = UiText.DynamicString("5 000 грн"),
        btnPrimary = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryButton",
            id = "primaryId",
            title = UiText.DynamicString("label"),
            interactionState = UIState.Interaction.Enabled
        ),
        btnStroke = ButtonStrokeAdditionalAtomData(
            actionKey = "alternativeButton",
            id = "alternativeId",
            title = UiText.DynamicString("label"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    CardsListOrg(
        data = CardsListOrgData(
            listOf(
                dataData,
                dataData,
                dataData,
                dataData,
                dataData,
                dataData,
                dataData,
                dataData,
                dataData,
                dataData
            )
        ),
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}
