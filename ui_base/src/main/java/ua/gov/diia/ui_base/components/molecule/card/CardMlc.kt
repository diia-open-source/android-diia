package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.mlc.card.CardMlc
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
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
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.AzureishWhite
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha15
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CardMlc(
    modifier: Modifier = Modifier,
    data: CardMlcData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .shadow(
                elevation = 6.dp, shape = RoundedCornerShape(8.dp), spotColor = BlackAlpha15
            )
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
            .testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(modifier = Modifier) {
                data.chip?.let {
                    ChipStatusAtm(
                        data = data.chip
                    )
                } ?: data.title?.let {
                    Text(
                        text = data.title.asString(),
                        style = DiiaTextStyle.t1BigText,
                        color = Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                data.label?.let {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        text = data.label.asString(),
                        style = DiiaTextStyle.t2TextDescription,
                        color = Black
                    )
                }
            }
            if (data.chip != null && data.title != null) {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = data.title.asString(),
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp),

                verticalAlignment = Alignment.Top
            ) {
                data.icon?.let {
                    when (data.icon) {
                        is UiIcon.DynamicIconBase64 -> {
                            IconBase64Subatomic(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(16.dp)
                                    .noRippleClickable {
                                        onUIAction(
                                            UIAction(
                                                actionKey = data.actionKey,
                                                data = data.id
                                            )
                                        )
                                    }, base64Image = data.icon.value
                            )
                        }

                        is UiIcon.DrawableResource -> {
                            Image(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(16.dp),
                                painter = painterResource(
                                    id = DiiaResourceIcon.getResourceId(data.icon.code)
                                ),
                                contentDescription = stringResource(
                                    id = DiiaResourceIcon.getContentDescription(
                                        data.icon.code
                                    )
                                ),
                                colorFilter = ColorFilter.tint(Black)
                            )
                        }

                        is UiIcon.PlainString -> {
                            Text(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(16.dp),
                                text = data.icon.value,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }

                        else -> {

                        }
                    }
                }
                data.subtitle?.let {
                    Text(
                        modifier = Modifier,
                        text = data.subtitle.asString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = DiiaTextStyle.t2TextDescription
                    )
                }
            }

            data.subtitles?.let { subtitleItems ->
                subtitleItems.forEach{ item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 8.dp),

                            verticalAlignment = Alignment.Top
                        ) {
                            item.icon?.let { uIcon ->
                                when (uIcon) {
                                    is UiIcon.DynamicIconBase64 -> {
                                        IconBase64Subatomic(
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(16.dp)
                                                .noRippleClickable {
                                                    onUIAction(
                                                        UIAction(
                                                            actionKey = data.actionKey,
                                                            data = data.id
                                                        )
                                                    )
                                                }, base64Image = uIcon.value
                                        )
                                    }
                                    is UiIcon.DrawableResource -> {
                                        Image(
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(16.dp),
                                            painter = painterResource(
                                                id = DiiaResourceIcon.getResourceId(uIcon.code)
                                            ),
                                            contentDescription = stringResource(
                                                id = DiiaResourceIcon.getContentDescription(
                                                    uIcon.code
                                                )
                                            ),
                                            colorFilter = ColorFilter.tint(Black)
                                        )
                                    }
                                    is UiIcon.PlainString -> {
                                        Text(
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(16.dp),
                                            text = uIcon.value,
                                            style = TextStyle(
                                                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }
                                    else -> {
                                    }
                                }
                            }
                            item.value.let {
                                Text(
                                    modifier = Modifier,
                                    text = it,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = DiiaTextStyle.t2TextDescription
                                )
                            }
                        }
                    }
            }
            data.description?.let {
                Text(
                    modifier = Modifier.conditional(data.subtitle != null || data.icon != null) {
                        padding(top = 8.dp)
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = data.description.asString(),
                    style = DiiaTextStyle.t2TextDescription,
                    color = BlackAlpha30
                )
            }
        }

        if (data.ticker != null) {
            TickerAtm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                data = data.ticker,
                onUIAction = onUIAction
            )
        } else {
            if (data.botLabel == null && data.btnPrimary == null && data.btnStroke == null) {
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                DividerSlimAtom(modifier = Modifier.padding(top = 16.dp), color = BlackSqueeze)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            data.botLabel?.let {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 8.dp)
                        .weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = data.botLabel.asString(),
                    style = DiiaTextStyle.t1BigText, color = Black
                )
            }

            data.btnStroke?.let {
                BtnStrokeAdditionalAtm(
                    modifier = Modifier.padding(top = 16.dp),
                    data = data.btnStroke,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
            if (data.btnStroke != null && data.btnPrimary != null) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            data.btnPrimary?.let {
                BtnPrimaryAdditionalAtm(
                    modifier = Modifier.conditional(data.btnStroke != null && data.botLabel == null) {
                        weight(1f)
                    },
                    data = data.btnPrimary,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
        }
    }
}


data class CardMlcData(
    val actionKey: String = UIActionKeysCompose.CARD_MLC,
    val componentId: UiText? = null,
    val id: String,
    val chip: ChipStatusAtmData? = null,
    val label: UiText? = null,
    val title: UiText? = null,
    val icon: UiIcon? = null,
    val subtitle: UiText? = null,
    val subtitles: List<CMSubtitle>? = null,
    val description: UiText? = null,
    val ticker: TickerAtomData? = null,
    val botLabel: UiText? = null,
    val btnPrimary: BtnPrimaryAdditionalAtmData? = null,
    val btnStroke: ButtonStrokeAdditionalAtomData? = null
) : UIElementData {
    data class CMSubtitle(
        val icon: UiIcon?,
        val value: String
    )
}

fun CardMlc.toUIModel(): CardMlcData {
    val subList: MutableList<CardMlcData.CMSubtitle> = mutableListOf()
    this.subtitles?.forEach { subtitle ->
        subList.add(
            CardMlcData.CMSubtitle(
                icon = subtitle.icon?.let { UiIcon.DrawableResource(it) },
                value = subtitle.value ?: ""
            )
        )
    }
    return CardMlcData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        id = this.id,
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
        label = this.label?.let { UiText.DynamicString(it) },
        title = this.title?.let { UiText.DynamicString(it) },
        icon = this.subtitle?.icon?.let { UiIcon.DynamicIconBase64(it) },
        subtitle = this.subtitle?.value?.let { UiText.DynamicString(it) },
        subtitles = subList,
        description = this.description?.let { UiText.DynamicString(it) },
        ticker = if (this.ticker != null) {
            this.ticker?.let {
                TickerAtomData(
                    componentId = it.componentId.orEmpty(),
                    title = it.value,
                    type = when (it.type) {
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.warning -> TickerType.SMALL_WARNING
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.positive -> TickerType.SMALL_POSITIVE
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.neutral -> TickerType.SMALL_NEUTRAL
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.informative -> TickerType.SMALL_INFORMATIVE
                    }
                )
            }
        } else {
            this.tickerAtm?.let {
                TickerAtomData(
                    componentId = it.componentId.orEmpty(),
                    title = it.value,
                    type = when (it.type) {
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.warning -> TickerType.SMALL_WARNING
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.positive -> TickerType.SMALL_POSITIVE
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.neutral -> TickerType.SMALL_NEUTRAL
                        ua.gov.diia.core.models.common_compose.atm.text.TickerAtm.TickerType.informative -> TickerType.SMALL_INFORMATIVE
                    }
                )
            }
        },
        botLabel = this.botLabel?.let { UiText.DynamicString(it) },
        btnPrimary = this.btnPrimaryAdditionalAtm?.toUIModel(),
        btnStroke = this.btnStrokeAdditionalAtm?.toUIModel()
    )
}

enum class ChipType {
    success, pending, fail, neutral;
}

@Composable
@Preview
fun CardMlcPreview_FullState() {
    val state = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = UiText.DynamicString("Label"),
        title = UiText.DynamicString("Card title"),
        icon = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        subtitle = UiText.DynamicString("Subtitle"),
        description = UiText.DynamicString("Description"),
        ticker = TickerAtomData(title = "Ticker text!", type = TickerType.SMALL_NEUTRAL),
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
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}

@Composable
@Preview
fun CardMlcPreview_Divider() {
    val state = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "Confirm"
        ),
        label = UiText.DynamicString("Label"),
        title = UiText.DynamicString("Card title"),
        icon = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        subtitle = UiText.DynamicString("Subtitle"),
        description = UiText.DynamicString("Description"),
        ticker = null,
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
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}

@Composable
@Preview
fun CardMlcPreview_Poor() {
    val state = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "В ОБРОБЦІ"
        ),
        label = null,
        title = UiText.DynamicString("Заява №1234567"),
        icon = null,
        subtitle = null,
        description = UiText.DynamicString("від 31 травня 2023"),
        ticker = null,
        botLabel = null,
        btnPrimary = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryButton",
            id = "primaryId",
            title = UiText.DynamicString("Детальніше"),
            interactionState = UIState.Interaction.Enabled
        ),
        btnStroke = null
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}

@Composable
@Preview
fun CardMlcPreview_PoorV2() {
    val state = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "В ОБРОБЦІ"
        ),
        label = null,
        title = UiText.DynamicString("Заява №1234567"),
        icon = null,
        subtitle = null,
        description = UiText.DynamicString("від 31 травня 2023"),
        ticker = null,
        botLabel = null,
        btnPrimary = null,
        btnStroke = ButtonStrokeAdditionalAtomData(
            actionKey = "alternativeButton",
            id = "alternativeId",
            title = UiText.DynamicString("Верифікувати"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}

@Composable
@Preview
fun CardMlcPreview_Poor_Gradient() {
    val state = CardMlcData(
        id = "123",
        chip = null,
        label = null,
        title = UiText.DynamicString("Квартира"),
        icon = null,
        subtitle = UiText.DynamicString("м. Київ, Солом'янський район, пр-т Незалежності 12, кв.4"),
        description = null,
        ticker = TickerAtomData(title = "Ticker text!", type = TickerType.SMALL_NEUTRAL),
        botLabel = null,
        btnPrimary = BtnPrimaryAdditionalAtmData(
            actionKey = "primaryButton",
            id = "primaryId",
            title = UiText.DynamicString("Подати заяву"),
            interactionState = UIState.Interaction.Enabled
        ),
        btnStroke = ButtonStrokeAdditionalAtomData(
            actionKey = "alternativeButton",
            id = "alternativeId",
            title = UiText.DynamicString("Детальніше"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}

@Composable
@Preview
fun CardMlcPreview_LongText() {
    val state = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(
            type = StatusChipType.POSITIVE, title = "STATUS NAME"
        ),
        label = UiText.DynamicString(LoremIpsum(10).values.joinToString()),
        title = UiText.DynamicString(LoremIpsum(20).values.joinToString()),
        icon = UiIcon.DrawableResource("menu"),
        subtitle = UiText.DynamicString(LoremIpsum(40).values.joinToString()),
        description = UiText.DynamicString(LoremIpsum(40).values.joinToString()),
        ticker = null,
        botLabel = UiText.DynamicString("223 223 322 233 222 грн"),
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
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}

@Composable
@Preview
fun CardMlcPreview_Subtitles() {
    val state = CardMlcData(
        id = "123",
        chip = ChipStatusAtmData(type = StatusChipType.POSITIVE, title = "Confirm"),
        label = UiText.DynamicString("Label"),
        title = UiText.DynamicString("Card title"),
        icon = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        subtitle = UiText.DynamicString("Subtitle"),
        subtitles = listOf(
            CardMlcData.CMSubtitle(
                icon = UiIcon.DrawableResource("someDocs"),
                value = "Subtitle 1"
            ),
            CardMlcData.CMSubtitle(
                icon = UiIcon.DrawableResource("add"),
                value = "Subtitle 2"
            )
        ),
        description = UiText.DynamicString("Description"),
        ticker = null,
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
    Box(modifier = Modifier.background(AzureishWhite)) {
        CardMlc(data = state) {

        }
    }
}