package ua.gov.diia.ui_base.components.subatomic.border

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.Primary

fun Modifier.diiaGreyBorder() = composed {
    border(width = 1.dp, color = Primary, shape = RoundedCornerShape(8.dp))
}

@Preview
@Composable
fun DiiaGrayBorderPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(30.dp)
                .diiaGreyBorder()
        )
    }
}

