package ua.gov.diia.pin.ui.reset.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.pin.ui.asSnapshotStateList
import ua.gov.diia.pin.ui.create.compose.CreatePinScreen
import ua.gov.diia.pin.ui.getPinTestData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithUIBlocking


@Composable
fun ResetPinScreen(
    modifier: Modifier = Modifier,
    data: SnapshotStateList<UIElementData>,
    contentLoaded: Pair<String, Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CreatePinScreen(
            modifier = modifier,
            data = data,
            onUIAction = onUIAction
        )
        TridentLoaderWithUIBlocking(contentLoaded = contentLoaded)
    }
}

private val testData = getPinTestData(
    topGroupText = "Повторіть код з 4 цифр",
    textWithParameters = "Щоб впевнитися, що це ви змінюєте код для входу.",
)

@Composable
@Preview
fun ResetPinScreenPreview() {
    val uiData = testData.asSnapshotStateList()

    ResetPinScreen(data = uiData, contentLoaded = "" to true, onUIAction = { })
}

@Composable
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
fun ResetPinScreenPreview_small_screen() {
    val uiData = testData.asSnapshotStateList()

    ResetPinScreen(data = uiData, contentLoaded = "" to true, onUIAction = { })
}