package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.button.BtnIconPlainGroupMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Composable
fun BtnIconPlainGroupMlc(
    modifier: Modifier = Modifier,
    data: BtnIconPlainGroupMlcData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember {
        mutableStateOf(data)
    }

    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    LaunchedEffect(key1 = progressIndicator) {
        localData.value =
            localData.value.copy(listBtn = SnapshotStateList<BtnPlainIconAtmData>().apply {
                for (item in localData.value.listBtn) {
                    add(
                        item.copy(
                            interactionState = if (progressIndicator.first != "" && progressIndicator.second) {
                                UIState.Interaction.Disabled
                            } else {
                                UIState.Interaction.Enabled
                            }
                        )
                    )
                }
            })
    }

    Column(
        modifier = modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .border(
                color = PeriwinkleGray, width = 1.dp, shape = RoundedCornerShape(7.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        localData.value.listBtn.forEach {
            BtnPlainIconAtm(modifier, it, progressIndicator, onUIAction = onUIAction)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

data class BtnIconPlainGroupMlcData(
    val listBtn: List<BtnPlainIconAtmData>,
    val componentId: UiText? = null,
) : UIElementData

fun BtnIconPlainGroupMlc?.toUIModel(): BtnIconPlainGroupMlcData? {
    if (this == null) return null
    return BtnIconPlainGroupMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        listBtn = this.items?.map {
            it.btnPlainIconAtm.toUiModel()
        }.orEmpty()
    )
}

@Composable
@Preview
fun BtnIconPlainGroupMlcPreview() {
    val btn1 = BtnPlainIconAtmData(
        id = "123",
        label = UiText.DynamicString("label1 click here"),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        interactionState = UIState.Interaction.Enabled
    )
    val btn2 = BtnPlainIconAtmData(
        id = "124",
        label = UiText.DynamicString("label2 very long name for button 2"),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.TARGET_WHITE.code),
        interactionState = UIState.Interaction.Disabled
    )
    val btn3 = BtnPlainIconAtmData(
        id = "125",
        label = UiText.DynamicString("label3"),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.TARGET_WHITE.code),
        interactionState = UIState.Interaction.Enabled
    )
    val data = BtnIconPlainGroupMlcData(listBtn = listOf(btn1, btn2, btn3))
    BtnIconPlainGroupMlc(Modifier, data) {}
}

@Composable
@Preview
fun BtnIconPlainGroupMlcPreview_one() {
    val btn1 = BtnPlainIconAtmData(
        id = "123",
        label = UiText.DynamicString("label1"),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        interactionState = UIState.Interaction.Enabled
    )
    val data = BtnIconPlainGroupMlcData(listBtn = listOf(btn1))
    BtnIconPlainGroupMlc(Modifier, data) {}
}