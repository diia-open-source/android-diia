package ua.gov.diia.ui_base.components.molecule.button


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnLoadPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnLoadPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

@Composable
fun BtnLoadIconPlainGroupMlc(
    modifier: Modifier = Modifier,
    data: BtnLoadIconPlainGroupMlcData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .border(
                color = PeriwinkleGray, width = 1.dp, shape = RoundedCornerShape(7.dp)
            )
            .testTag(data.componentId?.asString() ?: "")
    ) {
        data.items.forEach {
            BtnLoadPlainIconAtm(
                modifier = Modifier,
                data = it.copy(
                    interactionState = if (progressIndicator.second) {
                        if (it.id == progressIndicator.first) {
                            UIState.Interaction.Enabled
                        } else {
                            UIState.Interaction.Disabled
                        }
                    } else {
                        it.interactionState
                    }
                ),
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
    }
}

data class BtnLoadIconPlainGroupMlcData(
    val items: List<BtnLoadPlainIconAtmData>,
    val componentId: UiText? = null,
) : UIElementData

fun BtnLoadIconPlainGroupMlc.toUIModel(): BtnLoadIconPlainGroupMlcData {
    val data = this
    return BtnLoadIconPlainGroupMlcData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        items = mutableListOf<BtnLoadPlainIconAtmData>().apply {
            data.items.forEachIndexed { index, item ->
                add(
                    item.btnLoadPlainIconAtm.toUIModel(
                        item.btnLoadPlainIconAtm.id ?: index.toString()
                    )
                )
            }
        }
    )
}

enum class BtnLoadIconPlainGroupMlcMockType {
    enabled, loading, one
}

fun generateBtnLoadIconPlainGroupMlcMockData(mockType: BtnLoadIconPlainGroupMlcMockType): BtnLoadIconPlainGroupMlcData {
    when (mockType) {
        BtnLoadIconPlainGroupMlcMockType.enabled -> {
            val btn1 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id1",
                label = "Зареєструвати авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            val btn2 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id2",
                label = "Продати авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            val btn3 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id3",
                label = "Купити авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            return BtnLoadIconPlainGroupMlcData(items = listOf(btn1, btn2, btn3))
        }

        BtnLoadIconPlainGroupMlcMockType.loading -> {
            val btn1 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id1",
                label = "Зареєструвати авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            val btn2 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id2",
                label = "Продати авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            val btn3 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id3",
                label = "Купити авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            return BtnLoadIconPlainGroupMlcData(items = listOf(btn1, btn2, btn3))
        }

        BtnLoadIconPlainGroupMlcMockType.one -> {
            val btn1 = BtnLoadPlainIconAtmData(
                componentId = "component_id".toDynamicString(),
                id = "id1",
                label = "Зареєструвати авто".toDynamicString(),
                icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
                action = DataActionWrapper(
                    type = "register",
                    resource = "1234567890"
                ),
                interactionState = UIState.Interaction.Enabled
            )
            return BtnLoadIconPlainGroupMlcData(items = listOf(btn1))
        }

    }
}

@Composable
@Preview
fun BtnLoadIconPlainGroupMlcPreview() {
    BtnLoadIconPlainGroupMlc(
        Modifier,
        data = generateBtnLoadIconPlainGroupMlcMockData(BtnLoadIconPlainGroupMlcMockType.enabled)
    ) {}
}

@Composable
@Preview
fun BtnLoadIconPlainGroupMlcPreview_Loading() {

    BtnLoadIconPlainGroupMlc(
        modifier = Modifier,
        data = generateBtnLoadIconPlainGroupMlcMockData(BtnLoadIconPlainGroupMlcMockType.loading),
        progressIndicator = "id2" to true
    ) {}
}

@Composable
@Preview
fun BtnLoadIconPlainGroupMlcPreview_one() {
    BtnLoadIconPlainGroupMlc(
        Modifier,
        generateBtnLoadIconPlainGroupMlcMockData(BtnLoadIconPlainGroupMlcMockType.one)
    ) {}
}