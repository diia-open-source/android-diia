package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.mlc.text.StubMessageMlc
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun StubMessageMlc(
    modifier: Modifier = Modifier,
    data: StubMessageMlcData,
    useDesignTopPadding: Boolean = true,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .conditional(useDesignTopPadding) {
                padding(top = 64.dp)
            }
            .padding(horizontal = 24.dp)
            .testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.icon.asString(),
            style = TextStyle(
                fontSize = 48.sp,
                lineHeight = 48.sp
            )
        )
        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = data.title.asString(),
                textAlign = TextAlign.Center,
                style = DiiaTextStyle.h3SmallHeading
            )
        }
        data.description?.let {
            TextWithParametersAtom(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally),
                data = data.description,
                style = DiiaTextStyle.t3TextBody.copy(textAlign = TextAlign.Center),
                onUIAction = onUIAction

            )
        }
        data.button?.let {
            BtnStrokeAdditionalAtm(
                modifier = Modifier.padding(top = 16.dp),
                data = data.button,
                onUIAction = onUIAction
            )
        }
    }
}

data class StubMessageMlcData(
    val icon: UiText,
    val title: UiText? = null,
    val description: TextWithParametersData? = null,
    val button: ButtonStrokeAdditionalAtomData? = null,
    val componentId: UiText? = null,
    override val id: String = "",
) : SimplePagination

fun StubMessageMlc?.toUIModel(): StubMessageMlcData? {
    val entity: StubMessageMlc = this ?: return null
    return StubMessageMlcData(
        icon = entity.icon.toDynamicString(),
        title = entity.title?.toDynamicString(),
        description = TextWithParametersData(
            text = UiText.DynamicString(entity.description ?: ""),
            parameters = if (this.parameters != null) {
                mutableListOf<TextParameter>().apply {
                    entity.parameters?.forEach {
                        add(
                            TextParameter(
                                data = TextParameter.Data(
                                    name = it.data?.name.toDynamicStringOrNull(),
                                    resource = it.data?.resource.toDynamicStringOrNull(),
                                    alt = it.data?.alt.toDynamicStringOrNull()
                                ),
                                type = it.type
                            )
                        )
                    }
                }

            } else emptyList()
        ),
        componentId = entity.componentId.toDynamicStringOrNull(),
        button = entity.btnStrokeAdditionalAtm?.toUIModel(),
        id = entity.componentId.orEmpty(),
    )
}

enum class StubMessageMlcMockType {
    icontitle, titledescription, description, btndescription, btntitle;
}

fun generateStubMessageMlcMockData(mockType: StubMessageMlcMockType): StubMessageMlcData {
    return when (mockType) {
        StubMessageMlcMockType.icontitle -> StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDC4C"),
            title = UiText.DynamicString("У вас все добре"),
        )

        StubMessageMlcMockType.titledescription -> StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDC4C"),
            title = UiText.DynamicString("У вас все добре"),
            description = TextWithParametersData(
                text = UiText.DynamicString("Відкритих проваджень немає")
            )
        )

        StubMessageMlcMockType.btndescription -> StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDC4C"),
            title = UiText.DynamicString("У вас все добре"),
            description = TextWithParametersData(
                text = UiText.DynamicString("Відкритих проваджень немає")
            ),
            button = ButtonStrokeAdditionalAtomData(
                actionKey = UIActionKeysCompose.BUTTON_REGULAR,
                id = "1",
                title = UiText.DynamicString("Label"),
                interactionState = UIState.Interaction.Enabled
            )
        )

        StubMessageMlcMockType.btntitle -> StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDC4C"),
            title = UiText.DynamicString("У вас все добре"),
            button = ButtonStrokeAdditionalAtomData(
                actionKey = UIActionKeysCompose.BUTTON_REGULAR,
                id = "1",
                title = UiText.DynamicString("Label"),
                interactionState = UIState.Interaction.Enabled
            )
        )

        StubMessageMlcMockType.description -> StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDC4C"),
            description = TextWithParametersData(
                text = UiText.DynamicString("Відкритих проваджень немає")
            )
        )
    }
}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title() {
    StubMessageMlc(
        data = generateStubMessageMlcMockData(StubMessageMlcMockType.icontitle)
    ) {

    }
}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title_Description() {
    StubMessageMlc(
        data = generateStubMessageMlcMockData(StubMessageMlcMockType.titledescription)
    ) {

    }
}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Description() {
    StubMessageMlc(
        data = generateStubMessageMlcMockData(StubMessageMlcMockType.description)
    ) {

    }
}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title_Description_Button() {
    StubMessageMlc(
        data = generateStubMessageMlcMockData(StubMessageMlcMockType.btndescription)
    ) {}

}

@Composable
@Preview
fun StubMessageMlcPreview_Icon_Title_Button() {
    StubMessageMlc(
        data = generateStubMessageMlcMockData(StubMessageMlcMockType.btntitle)
    ) {}

}
