package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.R

@Composable
fun IconNegativeFaceAtom(modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 36.sp
    )
) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.emoji_sad),
        style = textStyle
    )
}

@Preview
@Composable
fun IconNegativeFaceAtomPreview() {
    IconNegativeFaceAtom()
}