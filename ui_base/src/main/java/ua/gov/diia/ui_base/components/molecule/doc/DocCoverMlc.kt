package ua.gov.diia.ui_base.components.molecule.doc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WarningYellow

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
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.title.asString(),
            color = Black,
            style = DiiaTextStyle.h4ExtraSmallHeading
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = data.description.asString(),
            color = Black,
            style = DiiaTextStyle.t3TextBody
        )
        data.button?.let {
            BtnStrokeAdditionalAtm(
                modifier = Modifier.padding(top = 16.dp),
                data = it,
                onUIAction = onUIAction
            )
        }

    }
}

data class DocCoverMlcData(
    val id: String? = "",
    val actionKey: String = UIActionKeysCompose.DOC_COVER_MLD,
    val title: UiText,
    val description: UiText,
    val button: ButtonStrokeAdditionalAtomData?
) : UIElementData

@Preview
@Composable
fun DocCoverMlcPreview() {
    val button = ButtonStrokeAdditionalAtomData(
        id = "1",
        title = UiText.DynamicString("strokeAdditionalButton"),
        interactionState = UIState.Interaction.Enabled
    )
    val data = DocCoverMlcData(
        title = UiText.DynamicString("Title"),
        description = UiText.DynamicString("Description"),
        button = button
    )
    DocCoverMlc(modifier = Modifier, data = data) {}
}