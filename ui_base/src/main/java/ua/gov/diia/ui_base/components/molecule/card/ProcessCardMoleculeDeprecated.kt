package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePaginationCard
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.AzureishWhite
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ProcessCardMoleculeDeprecated(
    modifier: Modifier = Modifier,
    data: ProcessCardMoleculeDataDeprecated,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .shadow(
                elevation = 4.dp, shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            ), horizontalAlignment = Alignment.Start
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .conditional(data.primaryButton == null && data.alternativeButton == null && data.value == null) {
                padding(bottom = 20.dp)
            }
        ) {
            Row(modifier = Modifier
                .conditional(data.chip != null) {
                    padding(vertical = 16.dp)
                }
                .conditional(data.chip == null) {
                    padding(top = 16.dp)
                }) {
                data.chip?.let {
                    ChipStatusAtm(
                        data = data.chip
                    )
                } ?: data.title?.let {
                    Text(
                        text = data.title, style = DiiaTextStyle.t1BigText, color = Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                data.label?.let {
                    Text(
                        text = data.label, style = DiiaTextStyle.t2TextDescription, color = Black
                    )
                }
            }
            if (data.chip != null && data.title != null) {
                Text(
                    text = data.title, style = DiiaTextStyle.t1BigText, color = Black
                )
            }

            if (!data.subtitle.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                data.subtitle.forEach {
                    val (image, text) = it
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.size(18.dp),
                            text = image,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            modifier = Modifier,
                            text = text,
                            style = DiiaTextStyle.t2TextDescription
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }


            if (data.iconBase64String != null || data.supportText != null || data.description != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    /**
                     * Only 3 cases available:
                     * 1. Supporting text
                     * 2. Icon + Supporting text
                     * 3. Description
                     */

                    data.iconBase64String?.let {
                        IconBase64Subatomic(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(16.dp), base64Image = data.iconBase64String
                        )
                    }
                    data.supportText?.let {
                        Text(
                            text = data.supportText,
                            style = DiiaTextStyle.t2TextDescription,
                            color = Black
                        )
                    }
                    data.description?.let {
                        Text(
                            text = data.description,
                            style = DiiaTextStyle.t2TextDescription,
                            color = BlackAlpha30
                        )
                    }
                }
            }
        }

        if (data.value != null || data.primaryButton != null || data.alternativeButton != null) {

            if (data.ticker != null) {
                TickerAtm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    data = data.ticker,
                    onUIAction = onUIAction
                )
            } else {
                DividerSlimAtom(modifier = Modifier.padding(vertical = 16.dp), color = BlackSqueeze)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                data.value?.let {
                    Text(
                        text = data.value, style = DiiaTextStyle.t1BigText, color = Black
                    )
                }

                data.alternativeButton?.let {
                    ButtonStrokeAdditionalAtom(
                        modifier = Modifier.conditional(data.primaryButton != null && data.value == null) {
                            weight(1f)
                        },
                        data = data.alternativeButton,
                        progressIndicator = progressIndicator,
                        onUIAction = onUIAction
                    )
                }
                data.primaryButton?.let {
                    BtnPrimaryAdditionalAtm(
                        data = data.primaryButton,
                        progressIndicator = progressIndicator,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

data class ProcessCardMoleculeDataDeprecated(
    override val id: String,
    val chip: ChipStatusAtmData? = null,
    val label: String? = null,
    val title: String? = null,
    val iconBase64String: String? = null,
    val supportText: String? = null,
    val description: String? = null,
    val ticker: TickerAtomData? = null,
    val value: String? = null,
    val subtitle: List<Pair<String, String>>? = null,
    val primaryButton: BtnPrimaryAdditionalAtmData? = null,
    val alternativeButton: ButtonStrokeAdditionalAtomData? = null
) : UIElementData, SimplePaginationCard

@Composable
@Preview
fun ProcessCardMoleculePreview_FullState() {
    val state = ProcessCardMoleculeDataDeprecated(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = "label",
        title = "Card title example",
        iconBase64String = PreviewBase64Icons.apple,
        supportText = "Supporting text",
        description = "Description text",
        ticker = TickerAtomData( title = "Ticker text!", type = TickerType.SMALL_NEUTRAL),
        value = "5 000 000 uah",
        primaryButton = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryButton",
            id = "primaryId",
            title = UiText.DynamicString("Details"),
            interactionState = UIState.Interaction.Enabled
        ),
        alternativeButton = ButtonStrokeAdditionalAtomData(
            actionKey = "alternativeButton",
            id = "alternativeId",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        ),
        subtitle = listOf(
            "\uD83D\uDCCD" to "Київ, вул. Олександра Довженка 14 (вул. Молдавська 3А)",
            "\uD83D\uDD52" to "Цілодобово"
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(modifier = Modifier.padding(16.dp), data = state) {

        }
    }
}

@Composable
@Preview
fun ProcessCardMoleculePreview_OnlyTopPart() {
    val data = ProcessCardMoleculeDataDeprecated(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = "label",
        title = "Card title example",
        iconBase64String = PreviewBase64Icons.apple,
        supportText = "Supporting text",
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(
            modifier = Modifier.padding(16.dp), data = data
        ) {

        }
    }
}

@Composable
@Preview
fun ProcessCardMoleculePreview_OneAlternativeButton() {
    val data = ProcessCardMoleculeDataDeprecated(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = "label",
        title = "Card title example",
        iconBase64String = PreviewBase64Icons.apple,
        supportText = "Supporting text",
        ticker = TickerAtomData( title = "Ticker text!", type = TickerType.SMALL_NEUTRAL),
        alternativeButton = ButtonStrokeAdditionalAtomData(
            actionKey = "altKey",
            id = "altId",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(
            modifier = Modifier.padding(16.dp), data = data
        ) {

        }
    }
}


@Composable
@Preview
fun ProcessCardMoleculePreview_PrimaryButtonAndValue() {
    val data = ProcessCardMoleculeDataDeprecated(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ), label = "label", title = "Card title example",
        description = "Description", value = "5 000 000 uah",
        primaryButton = BtnPrimaryAdditionalAtmData(
            actionKey = "positiveAction",
            id = "posID",
            title = UiText.DynamicString("Details"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(
            modifier = Modifier.padding(16.dp), data = data
        ) {

        }
    }
}

@Composable
@Preview
fun ProcessCardMoleculePreview_BothButtons() {
    val state = ProcessCardMoleculeDataDeprecated(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = "label",
        title = "Card title example",
        iconBase64String = PreviewBase64Icons.apple,
        supportText = "Supporting text",
        primaryButton = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryAction",
            id = "primaryId",
            title = UiText.DynamicString("Details"),
            interactionState = UIState.Interaction.Enabled
        ),
        alternativeButton = ButtonStrokeAdditionalAtomData(
            actionKey = "",
            id = "altId",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(
            modifier = Modifier.padding(16.dp), data = state
        ) {

        }
    }
}


@Composable
@Preview
fun ProcessCardMoleculePreview_ChipIsAbsent() {
    val state = ProcessCardMoleculeDataDeprecated(
        id = "123",
        label = "label",
        title = "Card title example",
        iconBase64String = PreviewBase64Icons.apple,
        supportText = "Supporting text",
        primaryButton = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryAction",
            id = "primaryId",
            title = UiText.DynamicString("Details"),
            interactionState = UIState.Interaction.Enabled
        ),
        alternativeButton = ButtonStrokeAdditionalAtomData(
            actionKey = "",
            id = "altId",
            title = UiText.DynamicString("Label"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(
            modifier = Modifier.padding(16.dp), data = state
        ) {

        }
    }
}

@Composable
@Preview
fun ProcessCardMoleculePreview_ChipIsAbsent2() {
    val state = ProcessCardMoleculeDataDeprecated(
        id = "123",
        title = "Пункт незламності ДСНС",
        description = "Аптечка, інтернет, відпочинок, тепло, світло, звʼязок.",
        primaryButton = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryAction",
            id = "primaryId",
            title = UiText.DynamicString("Маршрут"),
            interactionState = UIState.Interaction.Enabled
        ),
        value = "",
        subtitle = listOf(
            "\uD83D\uDCCD" to "Київ, вул. Олександра Довженка 14 (вул. Молдавська 3А)",
            "\uD83D\uDD52" to "Цілодобово"
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        ProcessCardMoleculeDeprecated(
            modifier = Modifier.padding(16.dp), data = state
        ) {

        }
    }
}
