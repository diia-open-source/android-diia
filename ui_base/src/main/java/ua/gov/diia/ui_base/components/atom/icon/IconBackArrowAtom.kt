package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black

@Composable
fun IconBackArrowAtom(
    modifier: Modifier = Modifier,
    tintColor: Color = Black,
    contentDescription: UiText? = UiText.StringResource(R.string.arrow_back)
) {
    Icon(
        modifier = modifier.size(28.dp, 28.dp),
        painter = painterResource(id = R.drawable.diia_back_arrow),
        contentDescription = contentDescription?.asString(),
        tint = tintColor
    )
}

@Preview
@Composable
fun IconBackArrowAtomPreview() {
    IconBackArrowAtom()
}