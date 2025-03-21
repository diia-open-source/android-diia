package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.card.CardMlcV2
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconUrlAtm
import ua.gov.diia.ui_base.components.atom.icon.IconUrlAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.toUiModel
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardMlcV2(
    modifier: Modifier = Modifier,
    data: CardMlcV2Data,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .background(
                color = White, shape = RoundedCornerShape(16.dp)
            )
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            data.chip?.let {
                ChipStatusAtm(data = data.chip)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .conditional(data.chip != null) {
                        padding(top = 16.dp)
                    },
                verticalAlignment = Alignment.Top
            ) {
                data.iconUrl?.let {
                    IconUrlAtm(data = it)
                }
                Column(modifier = Modifier
                    .weight(1f)
                    .conditional(data.iconUrl != null) {
                        padding(start = 16.dp)
                    }
                    .conditional(data.smallIconAtm != null) {
                        padding(end = 16.dp)
                    }) {
                    data.label?.let {
                        Text(
                            modifier = Modifier,
                            text = data.label.asString(),
                            style = DiiaTextStyle.t1BigText,
                            color = Black
                        )
                    }
                    data.descriptions?.forEach {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            text = it,
                            style = DiiaTextStyle.t2TextDescription,
                            color = BlackAlpha30
                        )
                    }
                    data.chips?.let {
                        FlowRow(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)

                        ) {
                            data.chips.forEach {
                                ChipStatusAtm(
                                    data = it
                                )
                            }
                        }
                    }
                }
                data.smallIconAtm?.let {
                    Box(modifier = Modifier.align(Alignment.Bottom)) {
                        SmallIconAtm(data = it)
                    }
                }
            }
        }
    }
}

data class CardMlcV2Data(
    val actionKey: String = UIActionKeysCompose.CARD_MLC_V2,
    val componentId: UiText? = null,
    override val id: String,
    val chip: ChipStatusAtmData? = null,
    val label: UiText? = null,
    val chips: List<ChipStatusAtmData>? = null,
    val smallIconAtm: SmallIconAtmData? = null,
    val iconUrl: IconUrlAtmData? = null,
    val descriptions: List<String>? = null,
    val action: DataActionWrapper? = null
) : SimplePagination

fun CardMlcV2.toUIModel(): CardMlcV2Data {
    var result = mutableListOf<ChipStatusAtmData>()
    chips?.let { ch ->
        ch.forEach { item ->
            result.add(
                ChipStatusAtmData(
                componentId = item.chipStatusAtm?.componentId?.let { cId -> UiText.DynamicString(cId) },
                type = StatusChipType.WHITE,
                title = item.chipStatusAtm?.name
            ))
        }
    }
    return CardMlcV2Data(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        id = this.componentId ?: "",
        chip = this.chipStatusAtm?.let {
            ChipStatusAtmData(
                componentId = it.componentId?.let { cId -> UiText.DynamicString(cId) },
                type = when (it.type) {
                    ChipType.success.toString() -> StatusChipType.POSITIVE
                    ChipType.pending.toString() -> StatusChipType.PENDING
                    ChipType.fail.toString() -> StatusChipType.NEGATIVE
                    ChipType.neutral.toString() -> StatusChipType.NEUTRAL
                    else -> StatusChipType.NEUTRAL
                },
                title = it.name
            )
        },
        chips = if (result.isEmpty()) null else result,
        label = this.label?.let { UiText.DynamicString(it) },
        iconUrl = this.iconUrlAtm?.toUiModel(),
        action = action?.toDataActionWrapper(),
        descriptions = this.descriptions
    )
}

@Composable
@Preview
fun CardMlcV2Preview_FullState() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1", "description2"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        chips = listOf(chipWhite, chipWhite, chipWhite, chipWhite, chipWhite, chipWhite)
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_WithoutStatus() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        ),
        chips = listOf(chipWhite, chipWhite, chipWhite, chipWhite)

    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_WhithoutChips() {
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        iconUrl = IconUrlAtmData(
            id = "1",
            url = "https://diia.data.gov.ua/assets/img/pages/home/hero/diia.svg",
            accessibilityDescription = "Button"
        ),
        descriptions = listOf("description1"),
        smallIconAtm = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.PLACEHOLDER.code,
        )
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_WithoutIcon() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        descriptions = listOf("description1", "description2"),
        chips = listOf(chipWhite, chipWhite, chipWhite)
    )
    CardMlcV2(data = state) {
    }
}

@Composable
@Preview
fun CardMlcV2Preview_LabelChips() {
    val chipWhite = ChipStatusAtmData(
        type = StatusChipType.WHITE, title = "Label"
    )
    val state = CardMlcV2Data(
        id = "123",
        label = UiText.DynamicString("Label"),
        chips = listOf(chipWhite, chipWhite, chipWhite)
    )
    CardMlcV2(data = state) {
    }
}