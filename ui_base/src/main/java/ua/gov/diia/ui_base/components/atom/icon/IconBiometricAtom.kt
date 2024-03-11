package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun IconBiometricAtom(
    modifier: Modifier = Modifier,
    onUIAction: (UIAction) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    IconButton(
        modifier = modifier.background(
            color = if (isPressed) Black else Color.Transparent, shape = CircleShape
        ),
        interactionSource = interactionSource,
        onClick = { onUIAction.invoke(UIAction(UIActionKeysCompose.TOUCH_ID_BUTTON)) }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_touch_id_large),
            contentDescription = stringResource(id = R.string.accessibility_num_tile_biometric_button),
            tint = if (isPressed) White else Black
        )
    }
}

@Preview
@Composable
fun IconBiometricAtomPreview() {
    IconBiometricAtom {}
}