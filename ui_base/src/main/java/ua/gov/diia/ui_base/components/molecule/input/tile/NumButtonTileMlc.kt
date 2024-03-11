package ua.gov.diia.ui_base.components.molecule.input.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnNumAtm
import ua.gov.diia.ui_base.components.atom.button.BtnNumAtmData
import ua.gov.diia.ui_base.components.atom.icon.IconBiometricAtom
import ua.gov.diia.ui_base.components.atom.icon.IconRemoveNumAtom
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction


@Composable
fun NumButtonTileMlc(
    modifier: Modifier = Modifier,
    hasBiometric: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier.padding(top = 8.dp).fillMaxWidth().wrapContentHeight()) {
        Row(
            Modifier
                .height(91.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numOneAcb = stringResource(id = R.string.accessibility_num_tile_one)
            val numTwoAcb = stringResource(id = R.string.accessibility_num_tile_two)
            val numThreeAcb = stringResource(id = R.string.accessibility_num_tile_three)
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numOneAcb },
                data = BtnNumAtmData(id = "1", number = 1),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numTwoAcb },
                data = BtnNumAtmData(id = "2", number = 2),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numThreeAcb },
                data = BtnNumAtmData(id = "3", number = 3),
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            Modifier
                .height(91.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numFourAcb = stringResource(id = R.string.accessibility_num_tile_four)
            val numFiveAcb = stringResource(id = R.string.accessibility_num_tile_five)
            val numSixAcb = stringResource(id = R.string.accessibility_num_tile_six)
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numFourAcb },
                data = BtnNumAtmData(id = "4", number = 4),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numFiveAcb },
                data = BtnNumAtmData(id = "5", number = 5),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numSixAcb },
                data = BtnNumAtmData(id = "6", number = 6),
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            Modifier
                .height(91.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numSevenAcb = stringResource(id = R.string.accessibility_num_tile_seven)
            val numEightAcb = stringResource(id = R.string.accessibility_num_tile_eight)
            val numNineAcb = stringResource(id = R.string.accessibility_num_tile_nine)
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numSevenAcb },
                data = BtnNumAtmData(id = "7", number = 7),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numEightAcb },
                data = BtnNumAtmData(id = "8", number = 8),
                onUIAction = onUIAction
            )
            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numNineAcb },
                data = BtnNumAtmData(id = "9", number = 9),
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            Modifier
                .height(91.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numZeroAcb = stringResource(id = R.string.accessibility_num_tile_zero)
            val backBtnAcb = stringResource(id = R.string.accessibility_num_tile_back_button)
            val biometricAcb =
                stringResource(id = R.string.accessibility_num_tile_biometric_button)
            if (hasBiometric) {
                IconBiometricAtom(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 16.dp)
                        .size(75.dp)
                        .semantics(mergeDescendants = true) { contentDescription = biometricAcb },
                    onUIAction = onUIAction
                )
            } else {
                //for proper line arrangement
                Spacer(modifier = Modifier.size(91.dp))
            }


            BtnNumAtm(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numZeroAcb },
                data = BtnNumAtmData(id = "0", number = 0),
                onUIAction = onUIAction
            )

            IconRemoveNumAtom(
                modifier = Modifier
                    .aspectRatio(1f)
                    .semantics(mergeDescendants = true) { contentDescription = backBtnAcb },
                onUIAction = onUIAction
            )
        }
    }
}


@Composable
@Preview
fun NumButtonTileMlcPreview() {
    Box(modifier = Modifier.wrapContentSize()) {
        NumButtonTileMlc() {}
    }
}

@Composable
@Preview
fun NumButtonTileMoleculePreview_WithBiometric() {
    Box(modifier = Modifier.wrapContentSize()) {
        NumButtonTileMlc(hasBiometric = true) {}
    }
}