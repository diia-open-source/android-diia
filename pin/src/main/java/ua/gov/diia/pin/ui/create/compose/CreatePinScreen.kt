package ua.gov.diia.pin.ui.create.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import ua.gov.diia.pin.ui.asSnapshotStateList
import ua.gov.diia.pin.ui.getPinTestData
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganism
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId


@Composable
fun CreatePinScreen(
    modifier: Modifier = Modifier,
    data: SnapshotStateList<UIElementData>,
    onUIAction: (UIAction) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.bg_blue_yellow_gradient),
                contentScale = ContentScale.FillBounds
            )
            .safeDrawingPadding()
            .provideTestTagsAsResourceId()
    ) {
        val title = createRef()
        val numButton = createRef()
        val descriptionText = createRef()
        data.forEach { item ->
            if (item is TopGroupOrgData) {
                TopGroupOrg(
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                    data = item,
                    onUIAction = onUIAction
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
            if (item is NumButtonTileOrganismData) {
                NumButtonTileOrganism(
                    modifier = Modifier
                        .constrainAs(numButton) {
                            linkTo(
                                top = descriptionText.bottom,
                                bottom = parent.bottom,
                                bias = 0.3f
                            )
                        },
                    data = item,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

private val testData = getPinTestData(
    topGroupText = "Повторіть код з 4 цифр",
    textWithParameters = "Цей код ви будете вводити для входу у застосунок Дія.",
    pinLength = 5
)

@Composable
@Preview
fun CreatePinScreenPreview() {
    val uiData = testData.asSnapshotStateList()

    CreatePinScreen(data = uiData, onUIAction = { })
}

@Composable
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
fun CreatePinScreenPreview_small_screen() {
    val uiData = testData.asSnapshotStateList()

    CreatePinScreen(data = uiData, onUIAction = { })
}