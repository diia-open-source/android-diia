package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.gov.diia.ui_base.components.subatomic.icon.BadgeSubatomic

@Composable
fun BadgeCounterAtm(
    modifier: Modifier = Modifier,
    data: BadgeCounterAtmData
) {
    BadgeSubatomic(modifier = modifier, value = data.count)
}

data class BadgeCounterAtmData(val count: Int)