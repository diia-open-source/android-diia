package ua.gov.diia.ui_base.components.molecule.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

@Composable
fun PaginationMessageMlc(
    modifier: Modifier = Modifier,
    data: PaginationMessageMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .testTag(data.componentId?.asString().orEmpty())
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = data.description.asString(),
            fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
        data.button?.let { atomData ->
            BtnStrokeAdditionalAtm(
                modifier = Modifier
                    .padding(top = 16.dp),
                data = atomData,
                onUIAction = onUIAction
            )
        }
    }
}

data class PaginationMessageMlcData(
    val componentId: UiText? = null,
    val description: UiText,
    val button: ButtonStrokeAdditionalAtomData? = null
) : UIElementData

fun generatePaginationMessageMlcMockData() = PaginationMessageMlcData(
    description = UiText.DynamicString("Не вдалось завантажити дані, спробуйте повторити ще раз"),
    button = ButtonStrokeAdditionalAtomData(
        actionKey = UIActionKeysCompose.BUTTON_REGULAR,
        title = UiText.DynamicString("Оновити"),
        interactionState = UIState.Interaction.Enabled,
    )
)

@Preview
@Composable
fun PaginationMessageMlcPreview() {
    PaginationMessageMlc(
        data = generatePaginationMessageMlcMockData(),
        onUIAction = {
            /* no-op */
        }
    )
}