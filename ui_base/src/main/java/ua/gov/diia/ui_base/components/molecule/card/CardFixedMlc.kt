package ua.gov.diia.ui_base.components.molecule.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
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
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CardFixedMlc(
    modifier: Modifier = Modifier,
    data: CardFixedMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(313.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .background(color = White, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(modifier = modifier.padding(top = 8.dp)) {

                data.chip?.let {
                    ChipStatusAtm(data = data.chip)
                }

                data.title?.let {
                    Text(
                        text = data.title.asString(),
                        style = DiiaTextStyle.t1BigText,
                        color = Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                data.label?.let {
                    Text(
                        text = data.label.asString(),
                        style = DiiaTextStyle.t2TextDescription,
                        color = Black
                    )
                }
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


            data.description?.let {
                Text(
                    modifier = Modifier,
                    text = data.description.asString(),
                    style = DiiaTextStyle.t2TextDescription,
                    color = BlackAlpha30
                )
            }

            Spacer(modifier.weight(1f))


            DividerSlimAtom(modifier = Modifier.padding(top = 16.dp), color = BlackSqueeze)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                data.bottomLabel?.let {
                    Text(
                        text = data.bottomLabel.asString(),
                        style = DiiaTextStyle.t1BigText,
                        color = Black
                    )
                }

                data.alternativeButton?.let {
                    ButtonStrokeAdditionalAtom(
                        modifier = Modifier.weight(1f).padding(top = 16.dp),
                        data = data.alternativeButton,
                        onUIAction = onUIAction
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                data.primaryButton?.let {
                    BtnPrimaryAdditionalAtm(
                        modifier = Modifier,
                        data = data.primaryButton,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }
}

data class CardFixedMlcData(
    val id: String,
    val lat: Double,
    val lon: Double,
    val title: UiText? = null,
    val subtitle: List<Pair<String, String>>? = null,
    val description: UiText? = null,
    val bottomLabel: UiText? = null,
    val primaryButton: BtnPrimaryAdditionalAtmData? = null,
    val alternativeButton: ButtonStrokeAdditionalAtomData? = null,
    val chip: ChipStatusAtmData? = null,
    val label: UiText? = null,
)

@Composable
@Preview
fun CardFixedMlcPreview_FullState() {
    val subtitle = mutableListOf<Pair<String, String>>()
    subtitle.add("\uD83D\uDCCD" to "Київ, вул. Олександра Довженка 14 (вул. Молдавська 3А)")
    subtitle.add("\uD83D\uDD50" to "Відчинене для населення у робочий час")
    subtitle.add("\uD83D\uDC65" to "Місткість: 456 осіб")
    CardFixedMlc(
        data = CardFixedMlcData(
            id = "id",
            lat = 0.0,
            lon = 0.0,
            title = UiText.DynamicString("Укриття"),
            subtitle = subtitle,
            description = UiText.DynamicString("Відсутній доступ для маломобільних верств населення"),
            primaryButton = BtnPrimaryAdditionalAtmData(
                id = "",
                title = UiText.DynamicString("Маршрут"),
                interactionState = UIState.Interaction.Enabled
            ),
            alternativeButton = ButtonStrokeAdditionalAtomData(
                id = "altID",
                title = UiText.DynamicString("Детальнiше"),
                interactionState = UIState.Interaction.Enabled
            )
        )
    ) {}
}

@Composable
@Preview
fun CardFixedMlcPreview_FullState_chips() {
    val subtitle = mutableListOf<Pair<String, String>>()
    subtitle.add("\uD83D\uDCCD" to "Київ, вул. Олександра Довженка 14 (вул. Молдавська 3А)")
    subtitle.add("\uD83D\uDD50" to "Відчинене для населення у робочий час")
    subtitle.add("\uD83D\uDC65" to "Місткість: 456 осіб")
    CardFixedMlc(
        data = CardFixedMlcData(
            id = "id",
            lat = 0.0,
            lon = 0.0,
            chip = ChipStatusAtmData(title = "STATUS", type = StatusChipType.POSITIVE),
            label = UiText.DynamicString("label"),
            subtitle = subtitle,
            description = UiText.DynamicString("Відсутній доступ для маломобільних верств населення"),
            primaryButton = BtnPrimaryAdditionalAtmData(
                id = "",
                title = UiText.DynamicString("Маршрут"),
                interactionState = UIState.Interaction.Enabled
            ),
            alternativeButton = ButtonStrokeAdditionalAtomData(
                id = "altID",
                title = UiText.DynamicString("Детальна інформація Детальна інформація"),
                interactionState = UIState.Interaction.Enabled
            )
        )
    ) {}
}


@Composable
@Preview
fun CardFixedMlcPreview_FullState_large_text() {
    val subtitle = mutableListOf<Pair<String, String>>()
    subtitle.add("\uD83D\uDCCD" to "Київ, вул. Олександра Довженка 14 (вул. Молдавська 3А)")
    subtitle.add("\uD83D\uDD50" to "Відчинене для населення у робочий час")
    subtitle.add("\uD83D\uDC65" to "Місткість: 456 осіб")
    CardFixedMlc(
        data = CardFixedMlcData(
            id = "id",
            lat = 0.0,
            lon = 0.0,
            title = UiText.DynamicString("Укриття УкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриттяУкриття"),
            subtitle = subtitle,
            description = UiText.DynamicString("Відсутній доступ для маломобільних верств населення"),
            primaryButton = BtnPrimaryAdditionalAtmData(
                id = "",
                title = UiText.DynamicString("Маршрут"),
                interactionState = UIState.Interaction.Enabled
            ),
            alternativeButton = ButtonStrokeAdditionalAtomData(
                id = "altID",
                title = UiText.DynamicString("Детальна інформація Детальна інформація"),
                interactionState = UIState.Interaction.Enabled
            )
        )
    ) {}
}