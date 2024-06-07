package ua.gov.diia.ui_base.components.organism.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.SpacerAtmType
import ua.gov.diia.core.models.common_compose.org.bottom.BottomGroupOrg
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWhiteAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeWhiteAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.text.LinkAtm
import ua.gov.diia.ui_base.components.atom.text.LinkAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrg
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.toUIModel

@Composable
fun BottomGroupOrg(
    modifier: Modifier = Modifier,
    data: BottomGroupOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        data.primaryButton?.let {
            BtnPrimaryDefaultAtm(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                data = data.primaryButton,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
        data.primaryWideButton?.let {
            BtnPrimaryWideAtm(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                data = data.primaryWideButton,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
        data.secondaryButton?.let {
            BtnPlainAtm(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                data = it,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
        data.strokeButton?.let {
            BtnStrokeDefaultAtm(
                data = data.strokeButton,
                onUIAction = onUIAction
            )
        }
        data.strokeButtonWhite?.let {
            BtnStrokeWhiteAtm(
                data = data.strokeButtonWhite,
                onUIAction = onUIAction
            )
        }
        data.link?.let {
            LinkAtm(
                data = data.link,
                onUIAction = onUIAction
            )
        }
        data.checkboxBtnOrg?.let {
            CheckboxBtnOrg(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                data = data.checkboxBtnOrg,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
        if (data.checkboxBtnOrg == null) {
            SpacerAtm(
                data = SpacerAtmData(
                    if (data.secondaryButton != null)
                        SpacerAtmType.SPACER_16 else SpacerAtmType.SPACER_32
                )
            )
        }
    }
}

data class BottomGroupOrgData(
    val primaryButton: BtnPrimaryDefaultAtmData? = null,
    val secondaryButton: BtnPlainAtmData? = null,
    val strokeButton: BtnStrokeDefaultAtmData? = null,
    val strokeButtonWhite: BtnStrokeWhiteAtmData? = null,
    val primaryWideButton: BtnPrimaryWideAtmData? = null,
    val link: LinkAtmData? = null,
    val checkboxBtnOrg: CheckboxBtnOrgData? = null,
    val componentId: UiText? = null,
) : UIElementData {

    fun changeStateByValidation(state: UIState.Interaction): BottomGroupOrgData {
        return this.copy(
            primaryButton = this.primaryButton?.copy(
                interactionState = state
            )
        )
    }
    fun activateButtonsIgnoreCheckbox(condition: Boolean): BottomGroupOrgData {
        return this.copy(
            primaryButton = this.primaryButton?.copy(
                interactionState = when (condition) {
                    true -> UIState.Interaction.Enabled
                    false -> UIState.Interaction.Disabled
                }
            ),
            secondaryButton = this.secondaryButton?.copy(
                interactionState = when (condition) {
                    true -> UIState.Interaction.Enabled
                    false -> UIState.Interaction.Disabled
                }
            )
        )
    }
    fun activateCheckbox(optionId: String): BottomGroupOrgData {
        return this.copy(
            checkboxBtnOrg = this.checkboxBtnOrg?.onOptionsCheckChanged(optionId)
        )
    }
}

fun BottomGroupOrg.toUiModel(): BottomGroupOrgData {
    return BottomGroupOrgData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        primaryButton = btnPrimaryDefaultAtm?.toUIModel(),
        secondaryButton = btnPlainAtm?.toUIModel(),
        strokeButton = btnStrokeDefaultAtm?.toUIModel(),
        checkboxBtnOrg = checkboxBtnOrg?.toUIModel(),
        strokeButtonWhite = btnStrokeWhiteAtm?.toUIModel(),
        primaryWideButton = btnPrimaryWideAtm?.toUIModel(),
    )
}

@Preview
@Composable
fun BottomGroupOrganismPreviewCheckboxAndPrimary() {
    val data = BottomGroupOrgData(
        primaryButton = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreviewFull() {

    val data = BottomGroupOrgData(
        primaryButton = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        ),
        secondaryButton = BtnPlainAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreviewOnlyPrimaryButton() {
    val data = BottomGroupOrgData(
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
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreview_PrimaryButtonAndLink() {

    val data = BottomGroupOrgData(
        primaryButton = BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        ),
        link = LinkAtmData(
            link = "https://u24.gov.ua/",
            text = UiText.DynamicString("Details")
        )
    )
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreview_Stroke() {

    val data = BottomGroupOrgData(
        strokeButton = BtnStrokeDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreview_Stroke_White() {
    val data = BottomGroupOrgData(
        strokeButtonWhite = BtnStrokeWhiteAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    )
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreview_Stroke_Wide() {
    val data = BottomGroupOrgData(
        primaryWideButton = BtnPrimaryWideAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        ),
    )
    BottomGroupOrg(data = data) {}
}

@Preview
@Composable
fun BottomGroupOrganismPreview_CheckboxBtnOrg() {
    val data = BottomGroupOrgData(
        checkboxBtnOrg = CheckboxBtnOrgData(
            id = "",
            options = SnapshotStateList<CheckboxSquareMlcData>().apply {
                add(
                    CheckboxSquareMlcData(
                        id = "1",
                        title = UiText.DynamicString("Надаю дозвіл на перевірку кредитної історії"),
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected
                    )
                )
                add(
                    CheckboxSquareMlcData(
                        id = "2",
                        title = UiText.DynamicString("Дозволяю передати мої персональні дані банку для обробки"),
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected
                    )
                )
                add(
                    CheckboxSquareMlcData(
                        id = "3",
                        title = UiText.DynamicString("Дозволяю банку телефонувати мені для уточнення даних"),
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected
                    )
                )
            },
            buttonData = BtnPrimaryDefaultAtmData(
                title = UiText.DynamicString("Далі"),
                id = "",
                interactionState = UIState.Interaction.Disabled
            )
        )
    )

    BottomGroupOrg(data = data) {}
}