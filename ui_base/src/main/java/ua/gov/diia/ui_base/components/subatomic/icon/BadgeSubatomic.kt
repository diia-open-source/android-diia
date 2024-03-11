package ua.gov.diia.ui_base.components.subatomic.icon

import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeSubatomic(
    modifier: Modifier = Modifier,
    value: Int
) {
    Badge(
        modifier = modifier,
        containerColor = Black,
        contentColor = White
    ) {
        Text(
            text = formateBudgeText(value),
            style = DiiaTextStyle.t4TextSmallDescription
        )
    }
}

private fun formateBudgeText(value: Int): String {
    return when (value) {
        in 0..99 -> value.toString()
        else -> "99+"
    }
}


@Composable
@Preview
fun BadgeSubatomicPreview() {
    BadgeSubatomic(value = 1)
}

@Composable
@Preview
fun BadgeSubatomicPreview_Medium() {
    BadgeSubatomic(value = 12)
}

@Composable
@Preview
fun BadgeSubatomicPreview_Big() {
    BadgeSubatomic(value = 999)
}

