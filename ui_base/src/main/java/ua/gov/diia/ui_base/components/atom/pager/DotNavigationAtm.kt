package ua.gov.diia.ui_base.components.atom.pager

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.Honeydew

@Composable
fun DotNavigationAtm(
    modifier: Modifier = Modifier,
    data: DotNavigationAtmData
) {
    BaseViewPagerIndicator(
        modifier = modifier,
        activeDotIndex = data.activeDotIndex,
        totalDotsCounter = data.totalCount,
        activeDotColor = Black,
        inactiveDotColor = Honeydew,
    )
}

data class DotNavigationAtmData(
    val activeDotIndex: Int,
    val totalCount: Int
)

@Preview
@Composable
fun DotNavigationAtm_2() {
    val data = DotNavigationAtmData(activeDotIndex = 0, totalCount = 2)
    DotNavigationAtm(data = data)
}

@Preview
@Composable
fun DotNavigationAtm_3() {
    val data = DotNavigationAtmData(activeDotIndex = 0, totalCount = 3)
    DotNavigationAtm(data = data)
}

@Preview
@Composable
fun DotNavigationAtm_4() {
    val data = DotNavigationAtmData(activeDotIndex = 0, totalCount = 4)
    DotNavigationAtm(data = data)
}

@Preview
@Composable
fun DotNavigationAtm_5() {
    val data = DotNavigationAtmData(activeDotIndex = 0, totalCount = 5)
    DotNavigationAtm(data = data)
}

@Preview
@Composable
fun DotNavigationAtm_6() {
    val data = DotNavigationAtmData(activeDotIndex = 0, totalCount = 6)
    DotNavigationAtm(data = data)
}