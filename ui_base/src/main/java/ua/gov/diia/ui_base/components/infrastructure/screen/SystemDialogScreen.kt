package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.components.atom.button.ButtonSystemAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonSystemAtomData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.SystemTextColor
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun SystemDialogScreen(
    modifier: Modifier = Modifier,
    dataState: State<SystemDialogScreenData>,
    onEvent: (UIAction) -> Unit
) {
    val data = dataState.value
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (data.titleText != null) {
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 24.dp),
                    text = data.titleText.asString(),
                    color = Black,
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (data.descriptionText != null) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    text = data.descriptionText.asString(),
                    color = SystemTextColor,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp).padding(bottom = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (data.negativeButton != null) {
                    ButtonSystemAtom(data = data.negativeButton, onUIAction = onEvent)
                }
                if (data.positiveButton != null) {
                    ButtonSystemAtom(data = data.positiveButton, onUIAction = onEvent)
                }
            }
        }
    }
}

data class SystemDialogScreenData(
    val titleText: UiText? = null,
    val descriptionText: UiText? = null,
    val positiveButton: ButtonSystemAtomData? = null,
    val negativeButton: ButtonSystemAtomData? = null,
)

@Composable
@Preview
fun SystemDialogScreenPreview() {
    val uiData = remember {
        mutableStateOf(
            SystemDialogScreenData(
                UiText.DynamicString("Надати «Дії» одноразовий доступ до геолокації?"),
                UiText.DynamicString("Надайте доступ Дії до геолокації, для підтвердження місця перебування. Таким чином, Дія перевірить достовірність адреси, зазначеної як поточна. Геолокація не використовуватиметься для будь-яких інших цілей."),
                ButtonSystemAtomData(title = UiText.DynamicString("Дозволити")),
                ButtonSystemAtomData(title = UiText.DynamicString("Заборонити")),
            )
        )
    }
    SystemDialogScreen(dataState = uiData, onEvent = { })
}