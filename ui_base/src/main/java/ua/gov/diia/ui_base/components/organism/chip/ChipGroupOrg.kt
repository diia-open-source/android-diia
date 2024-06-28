package ua.gov.diia.ui_base.components.organism.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.chip.ChipGroupOrg
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.chip.ChipTimeMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeDataV2
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeV2
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ChipGroupOrg(
    modifier: Modifier = Modifier,
    data: ChipGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.label?.let {
            Text(
                text = it.asString(),
                color = Black,
                style = DiiaTextStyle.t3TextBody,
            )
            Spacer(modifier = Modifier.size(16.dp))
        }

        if (!data.chipTimeItems.isNullOrEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(data.chipTimeItems.size) { i ->
                    val chip = data.chipTimeItems[i]
                    ua.gov.diia.ui_base.components.molecule.chip.ChipTimeMlc(
                        modifier,
                        chip,
                        onUIAction
                    )
                }
            }
        }

        if (!data.chipMoleculeItems.isNullOrEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(data.chipMoleculeItems.size) { i ->
                    val chip = data.chipMoleculeItems[i]
                    ChipTabMoleculeV2(modifier, chip, onUIAction)
                }
            }
        }
    }
}

data class ChipGroupOrgData(
    val label: UiText? = null,
    val componentId: UiText? = null,
    val chipTimeItems: List<ChipTimeMlcData>? = null,
    val chipMoleculeItems: List<ChipTabMoleculeDataV2>? = null,
)


fun ChipGroupOrg.toUiModel(date: String): ChipGroupOrgData {
    val chipTimeItems = mutableListOf<ChipTimeMlcData>()
    val chipMoleculeItems = mutableListOf<ChipTabMoleculeDataV2>()

    this.items?.forEach {
        if (it.chipMlc != null) {
            chipMoleculeItems.add(
                ChipTabMoleculeDataV2(
                    id = it.chipMlc?.code ?: "",
                    title = it.chipMlc?.label ?: "",
                    selectionState = if (it.chipMlc?.code == preselectedCode) {
                        UIState.Selection.Selected
                    } else {
                        UIState.Selection.Unselected
                    }
                )
            )
        }
        if (it.chipTimeMlc != null) {
            ChipTimeMlcData(
                id = it.chipTimeMlc?.id,
                date = date,
                title = UiText.DynamicString(it.chipTimeMlc?.label.orEmpty()),
                code = it.chipTimeMlc?.data?.code,
                resourceId = it.chipTimeMlc?.data?.resourceId,
                componentId = UiText.DynamicString(componentId.orEmpty()),
                selection = if (it.chipTimeMlc?.active == true || it.chipTimeMlc?.data?.code == preselectedCode)
                    UIState.Selection.Selected
                else
                    UIState.Selection.Unselected,
            )
        }
    }


    return ChipGroupOrgData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        label = UiText.DynamicString(this.label.orEmpty()),
        chipMoleculeItems = chipMoleculeItems,
        chipTimeItems = chipTimeItems
    )
}

@Composable
@Preview
fun ChipGroupOrgPreview() {
    val chipTimeItems = mutableListOf<ChipTimeMlcData>()
    for (i in 0..10) {
        val data = ChipTimeMlcData(
            title = UiText.DynamicString("label " + i),
            selection = UIState.Selection.Unselected,
            date = "",
        )
        chipTimeItems.add(data)
    }

    val data = ChipGroupOrgData(
        label = UiText.DynamicString("Оберіть час:"),
        chipTimeItems = chipTimeItems,
    )

    ChipGroupOrg(Modifier, data) {}
}

@Composable
@Preview
fun ChipGroupOrgPreview_chip_tab() {
    val chipMoleculeItems = mutableListOf<ChipTabMoleculeDataV2>()
    for (i in 0..10) {
        val data = ChipTabMoleculeDataV2(
            title = "label " + i,
            selectionState = UIState.Selection.Unselected
        )
        chipMoleculeItems.add(data)
    }

    val data = ChipGroupOrgData(
        label = UiText.DynamicString("Оберіть час:"),
        chipMoleculeItems = chipMoleculeItems
    )

    ChipGroupOrg(Modifier, data) {}
}