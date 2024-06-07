package ua.gov.diia.ui_base.components.molecule.doc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun StackMlc(
    modifier: Modifier,
    data: StackMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(modifier = modifier) {

        data.smallIconAtmData?.let {
            SmallIconAtm(
                modifier = Modifier,
                data = it,
                onUIAction = onUIAction
            )
        }

        data.amount?.let {
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                text = it.toString(),
                style = DiiaTextStyle.t2TextDescription,
                color = if(!data.isWhite) Black else White
            )
        }
    }
}

data class StackMlcData(
    val amount: Int? = null,
    val smallIconAtmData: SmallIconAtmData? = null,
    val isWhite: Boolean = false
) : UIElementData

@Preview
@Composable
fun StackMlcPreview() {
    val data = StackMlcData(
        amount = 3, smallIconAtmData = SmallIconAtmData(
            id = "1",
            code = DiiaResourceIcon.STACK.code,
            accessibilityDescription = "Button"
        )
    )
    StackMlc(modifier = Modifier, data = data) {}

}