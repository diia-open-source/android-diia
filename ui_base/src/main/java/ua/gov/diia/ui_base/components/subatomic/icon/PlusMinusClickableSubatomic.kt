package ua.gov.diia.ui_base.components.subatomic.icon


import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.Black

@Composable
fun PlusMinusClickableSubatomic(modifier: Modifier = Modifier, expandState: UIState.Expand) {
    Icon(
        modifier = modifier,
        painter = painterResource(
            when (expandState) {
                UIState.Expand.Collapsed -> R.drawable.diia_icon_plus
                UIState.Expand.Expanded -> R.drawable.diia_icon_minus
            }
        ),
        tint = Black,
        contentDescription = stringResource(R.string.expand_collapse)
    )
}