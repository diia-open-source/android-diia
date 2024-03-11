package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.theme.Black

@Composable
fun IconEllipseMenuAtom(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier.size(28.dp, 28.dp),
        painter = painterResource(id = R.drawable.diia_ellipse_menu),
        contentDescription = stringResource(R.string.context_menu),
        tint = Black
    )
}

@Preview
@Composable
fun IconEllipseMenuAtomPreview() {
    IconEllipseMenuAtom()
}