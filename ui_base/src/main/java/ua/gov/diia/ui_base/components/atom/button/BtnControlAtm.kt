package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun BtnControlAtm(
    modifier: Modifier = Modifier,
    actionKey: String,
    icon: DiiaResourceIcon,
    bgColor: Color,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(bgColor, shape = CircleShape)
            .noRippleClickable {
                onUIAction(UIAction(actionKey = actionKey))
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon.drawableResourceId),
            contentDescription = stringResource(id = icon.contentDescriptionResourceId),
            tint = White
        )
    }
}

@Composable
@Preview
fun BtnControlPreview() {
    BtnControlAtm(
        actionKey = UIActionKeysCompose.CONTROL_BUTTON_MIC_MOLECULE,
        icon = DiiaResourceIcon.MIC_OFF,
        bgColor = Color.Black
    ) {}
}