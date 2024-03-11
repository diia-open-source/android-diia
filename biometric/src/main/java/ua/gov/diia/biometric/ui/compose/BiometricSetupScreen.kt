package ua.gov.diia.biometric.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

@Composable
fun BiometricSetupScreen(
    modifier: Modifier = Modifier,
    data: SnapshotStateList<UIElementData>,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        val (title, descriptionText, iconZone, button, altButton) = createRefs()
        data.forEach { item ->
            if (item is TopGroupOrgData) {
                TopGroupOrg(
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                    data = item,
                    onUIAction = onUIAction,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                )
            }

            if (item is TextLabelMlcData) {
                TextLabelMlc(
                    modifier = modifier
                        .constrainAs(descriptionText) {
                            top.linkTo(title.bottom)
                        },
                    data = item,
                    onUIAction = onUIAction
                )
            }


            Icon(
                modifier = modifier
                    .size(height = 96.dp, width = 224.dp)
                    .constrainAs(createRef()) {
                        top.linkTo(descriptionText.bottom)
                        bottom.linkTo(button.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(id = R.drawable.ic_biometric_auth),
                contentDescription = stringResource(id = R.string.accessibility_biometric_methods_icon),
            )

            if (item is BtnPrimaryDefaultAtmData) {
                val accessabilityText =
                    stringResource(id = R.string.accessibility_biometric_methods_button)
                BtnPrimaryDefaultAtm(
                    modifier = modifier
                        .constrainAs(button) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(altButton.top)
                        }
                        .semantics(mergeDescendants = true) {
                            contentDescription = accessabilityText
                        },
                    data = item,
                    onUIAction = onUIAction
                )
            }

            if (item is BtnPlainAtmData) {
                val accessabilityText =
                    stringResource(id = R.string.accessibility_biometric_methods_alt_button)
                BtnPlainAtm(
                    modifier = modifier
                        .constrainAs(altButton) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        }
                        .semantics(mergeDescendants = true) {
                            contentDescription = accessabilityText
                        },
                    data = item,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

@Composable
@Preview
fun BiometricSetupScreenPreview() {
    val _uiData = remember { mutableStateListOf<UIElementData>() }
    val uiData: SnapshotStateList<UIElementData> = _uiData
    _uiData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Дозвольте вхід за біометричними даними"),
            )
        )
    )
    _uiData.add(
        TextLabelMlcData(
            text = UiText.DynamicString("Дозвольте Дії використовувати сканер відбитку пальця та/або розпізнавання обличчя для входу у застосунок.")
        )
    )
    _uiData.add(
        BtnPrimaryDefaultAtmData(title = UiText.DynamicString("Дозволити"))
    )
    _uiData.add(
        BtnPlainAtmData(
            actionKey = UIActionKeysCompose.BUTTON_ALTERNATIVE,
            id = "",
            title = UiText.DynamicString("Дозволю пізніше"),
            interactionState = UIState.Interaction.Enabled
        )
    )
    BiometricSetupScreen(
        data = uiData,
        onUIAction = { },
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview()
    )
}