package ua.gov.diia.ui_base.components.molecule.input.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import ua.gov.diia.ui_base.components.atom.button.NumButtonAtom
import ua.gov.diia.ui_base.components.atom.button.NumButtonAtomData
import ua.gov.diia.ui_base.components.atom.icon.IconBiometricAtom
import ua.gov.diia.ui_base.components.atom.icon.IconRemoveNumAtom
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction


@Composable
fun NumButtonTileMolecule(
    modifier: Modifier = Modifier,
    hasBiometric: Boolean = false,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            Modifier
                .height(75.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numOneAcb = stringResource(id = R.string.accessibility_num_tile_one)
            val numTwoAcb = stringResource(id = R.string.accessibility_num_tile_two)
            val numThreeAcb = stringResource(id = R.string.accessibility_num_tile_three)
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numOneAcb },
                data = NumButtonAtomData(id = "1", number = 1),
                onUIAction = onUIAction
            )
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 28.dp)
                    .semantics(mergeDescendants = true) { contentDescription = numTwoAcb },
                data = NumButtonAtomData(id = "2", number = 2),
                onUIAction = onUIAction
            )
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numThreeAcb },
                data = NumButtonAtomData(id = "3", number = 3),
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            Modifier
                .height(75.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numFourAcb = stringResource(id = R.string.accessibility_num_tile_four)
            val numFiveAcb = stringResource(id = R.string.accessibility_num_tile_five)
            val numSixAcb = stringResource(id = R.string.accessibility_num_tile_six)
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numFourAcb },
                data = NumButtonAtomData(id = "4", number = 4),
                onUIAction = onUIAction
            )
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 28.dp)
                    .semantics(mergeDescendants = true) { contentDescription = numFiveAcb },
                data = NumButtonAtomData(id = "5", number = 5),
                onUIAction = onUIAction
            )
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numSixAcb },
                data = NumButtonAtomData(id = "6", number = 6),
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            Modifier
                .height(75.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val numSevenAcb = stringResource(id = R.string.accessibility_num_tile_seven)
            val numEightAcb = stringResource(id = R.string.accessibility_num_tile_eight)
            val numNineAcb = stringResource(id = R.string.accessibility_num_tile_nine)
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numSevenAcb },
                data = NumButtonAtomData(id = "7", number = 7),
                onUIAction = onUIAction
            )
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 28.dp)
                    .semantics(mergeDescendants = true) { contentDescription = numEightAcb },
                data = NumButtonAtomData(id = "8", number = 8),
                onUIAction = onUIAction
            )
            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics(mergeDescendants = true) { contentDescription = numNineAcb },
                data = NumButtonAtomData(id = "9", number = 9),
                onUIAction = onUIAction
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            Modifier
                .height(75.dp)
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
                        .size(75.dp)
                        .semantics(mergeDescendants = true) { contentDescription = biometricAcb },
                    onUIAction = onUIAction
                )
            } else {
                //for proper line arrangement
                Spacer(modifier = Modifier.size(75.dp))
            }


            NumButtonAtom(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 28.dp)
                    .semantics(mergeDescendants = true) { contentDescription = numZeroAcb },
                data = NumButtonAtomData(id = "0", number = 0),
                onUIAction = onUIAction
            )

            IconRemoveNumAtom(
                modifier = Modifier
                    .size(75.dp)
                    .semantics(mergeDescendants = true) { contentDescription = backBtnAcb },
                onUIAction = onUIAction
            )
        }
    }
}


@Composable
@Preview
fun NumButtonTileMoleculePreview() {
    Box(modifier = Modifier.wrapContentSize()) {
        NumButtonTileMolecule() {}
    }
}