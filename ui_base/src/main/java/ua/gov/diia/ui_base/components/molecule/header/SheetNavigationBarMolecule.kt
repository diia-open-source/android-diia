package ua.gov.diia.ui_base.components.molecule.header

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun SheetNavigationBarMolecule(
    modifier: Modifier = Modifier,
    data: SheetNavigationBarMoleculeData,
    onUIAction: (UIAction) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f),
            text = data.title.asString(),
            style = DiiaTextStyle.h1Heading
        )
        Icon(
            modifier = Modifier
                .size(32.dp)
                .noRippleClickable {
                    onUIAction(
                        UIAction(actionKey = data.actionKey)
                    )
                },
            painter = painterResource(id = R.drawable.diia_close_rounded_plain),
            contentDescription = stringResource(id = R.string.close),
            tint = Black
        )
    }
}

//45
data class SheetNavigationBarMoleculeData(
    val actionKey: String = UIActionKeysCompose.SHEET_NAVIGATION_BAR_MOLECULE,
    val title: UiText
) : UIElementData

@Composable
@Preview
fun SheetNavigationBarMoleculePreview() {
    val data =
        SheetNavigationBarMoleculeData(title = UiText.DynamicString("Отримайте договір купівлі-продажу авто"))
    SheetNavigationBarMolecule(data = data) {

    }
}