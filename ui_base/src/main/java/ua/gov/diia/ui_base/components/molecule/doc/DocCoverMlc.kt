package ua.gov.diia.ui_base.components.molecule.doc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WarningYellow
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun DocCoverMlc(
    modifier: Modifier,
    data: DocCoverMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = WarningYellow,
                RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 32.dp)
            .noRippleClickable { /* swallow click on whole view */ }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (content, stackMlc) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(content) {
                        start.linkTo(parent.start)
                        end.linkTo(stackMlc.start, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.title.asString(),
                    color = Black,
                    style = DiiaTextStyle.h4ExtraSmallHeading
                )
                Text(
                    text = data.text.asString(),
                    modifier = Modifier.padding(top = 16.dp),
                    color = Black,
                    style = DiiaTextStyle.t3TextBody,
                    maxLines = 5
                )
                data.button?.let {
                    BtnStrokeAdditionalAtm(
                        modifier = Modifier.padding(top = 16.dp),
                        data = it,
                        onUIAction = onUIAction
                    )
                }
            }

            data.stackMlcData?.let {
                if (data.isStack && data.size != 1) {
                    StackMlc(
                        modifier = Modifier
                            .constrainAs(stackMlc) {
                                end.linkTo(parent.end)
                                bottom.linkTo(content.bottom)
                            },
                        data = it,
                        onUIAction = { onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_STACK)) }
                    )
                }
            }
        }
    }
}

data class DocCoverMlcData(
    val id: String? = "",
    val actionKey: String = UIActionKeysCompose.DOC_COVER_MLC,
    val title: UiText,
    val text: UiText,
    val button: ButtonStrokeAdditionalAtomData?,
    val stackMlcData: StackMlcData? = null,
    val isStack: Boolean = false,
    val size: Int? = null,
    val componentId: UiText? = null
) : UIElementData

enum class DocCoverMlcMockType {
    withNoStackButton, withStackButton, withStackNoButton;
}

fun generateDocCoverMlcMockTData(mockType: DocCoverMlcMockType): DocCoverMlcData {
    return when (mockType) {
        DocCoverMlcMockType.withNoStackButton -> DocCoverMlcData(
            title = UiText.DynamicString("Title"),
            text = UiText.DynamicString("Description"),
            button = ButtonStrokeAdditionalAtomData(
                id = "1",
                title = UiText.DynamicString("strokeAdditionalButton"),
                interactionState = UIState.Interaction.Enabled
            )
        )

        DocCoverMlcMockType.withStackButton -> DocCoverMlcData(
            title = UiText.DynamicString("Title"),
            text = UiText.DynamicString("Description"),
            button = ButtonStrokeAdditionalAtomData(
                id = "1",
                title = UiText.DynamicString("strokeAdditionalButton"),
                interactionState = UIState.Interaction.Enabled
            ),
            isStack = true,
            size = 2,
            stackMlcData = StackMlcData(
                amount = 3, smallIconAtmData = SmallIconAtmData(
                    id = "1",
                    code = DiiaResourceIcon.STACK.code,
                    accessibilityDescription = "Button"
                )
            )
        )

        DocCoverMlcMockType.withStackNoButton -> DocCoverMlcData(
            title = UiText.DynamicString("Title"),
            text = UiText.DynamicString("Description"),
            button = null,
            isStack = true,
            size = 2,
            stackMlcData = StackMlcData(
                amount = 13, smallIconAtmData = SmallIconAtmData(
                    id = "1",
                    code = DiiaResourceIcon.STACK.code,
                    accessibilityDescription = "Button"
                )
            )
        )
    }

}

@Preview
@Composable
fun DocCoverMlcPreview() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7F)
            .background(
                White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        DocCoverMlc(
            modifier = Modifier.constrainAs(createRef()) {
                bottom.linkTo(parent.bottom)
            },
            data = generateDocCoverMlcMockTData(DocCoverMlcMockType.withNoStackButton)
        ) {}
    }

}

@Preview
@Composable
fun DocCoverMlcPreview_WithStackButton() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7F)
            .background(
                White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        DocCoverMlc(
            modifier = Modifier.constrainAs(createRef()) {
                bottom.linkTo(parent.bottom)
            },
            data = generateDocCoverMlcMockTData(DocCoverMlcMockType.withStackButton)
        ) {}
    }
}

@Preview
@Composable
fun DocCoverMlcPreview_WithStackNoButton() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7F)
            .background(
                White,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        DocCoverMlc(
            modifier = Modifier.constrainAs(createRef()) {
                bottom.linkTo(parent.bottom)
            },
            data = generateDocCoverMlcMockTData(DocCoverMlcMockType.withStackNoButton)
        ) {}
    }
}