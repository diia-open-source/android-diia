package ua.gov.diia.ui_base.components.organism.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData

@Composable
fun BottomGroupOrganism(
    modifier: Modifier = Modifier,
    data: BottomGroupOrganismData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        data.primaryButton?.let {
            BtnPrimaryDefaultAtm(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                data = data.primaryButton,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
        data.strokeButton?.let {
            BtnStrokeDefaultAtm(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                data = it,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
        data.secondaryButton?.let {
            BtnPlainAtm(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                data = it,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }

    }
}

data class BottomGroupOrganismData(
    val checkbox: CheckboxSquareMlcData? = null,
    val primaryButton: BtnPrimaryDefaultAtmData?,
    val strokeButton: BtnStrokeDefaultAtmData? = null,
    val secondaryButton: BtnPlainAtmData? = null
) : UIElementData {}

@Preview
@Composable
fun BottomGroupOrganismPreview_Full() {
    val data = BottomGroupOrganismData(
        checkbox = CheckboxSquareMlcData(
            id = "123",
            title = UiText.DynamicString(LoremIpsum(10).values.joinToString()),
            interactionState = UIState.Interaction.Enabled,
            selectionState = UIState.Selection.Unselected
        ),
        primaryButton = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        ),
        secondaryButton = BtnPlainAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    )

    BottomGroupOrganism(data = data) {

    }
}

@Preview
@Composable
fun BottomGroupOrganismPreview_CheckboxAndPrimary() {

    val data = BottomGroupOrganismData(
        checkbox = CheckboxSquareMlcData(
            id = "123",
            title = UiText.DynamicString(LoremIpsum(10).values.joinToString()),
            interactionState = UIState.Interaction.Enabled,
            selectionState = UIState.Selection.Unselected
        ),
        primaryButton = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    )

    BottomGroupOrganism(data = data) {

    }
}

@Preview
@Composable
fun BottomGroupOrganismPreview_OnlyPrimaryButton() {
    val data = BottomGroupOrganismData(
        primaryButton = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    )
    BottomGroupOrganism(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreview_OnlyStrokeButton() {
    val data = BottomGroupOrganismData(
        primaryButton = null,
        strokeButton = BtnStrokeDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    BottomGroupOrganism(data = data) {}
}

