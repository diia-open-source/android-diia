package ua.gov.diia.ui_base.components.atom.pager

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40

@Composable
fun DocDotNavigationAtm(
    modifier: Modifier = Modifier,
    data: DocDotNavigationAtmData
) {
    BaseViewPagerIndicator(
        modifier = modifier,
        activeDotIndex = data.activeDotIndex,
        totalDotsCounter = data.totalCount,
        activeDotColor = White,
        inactiveDotColor = WhiteAlpha40
    )
}

data class DocDotNavigationAtmData(
    val activeDotIndex: Int,
    val totalCount: Int
)

@Preview
@Composable
fun DocDotNavigationAtm_2() {
    val data = DocDotNavigationAtmData(totalCount = 2, activeDotIndex = 0)
    DocDotNavigationAtm(data = data)
}

@Preview
@Composable
fun DocDotNavigationAtm_3() {
    val data = DocDotNavigationAtmData(totalCount = 3, activeDotIndex = 0)
    DocDotNavigationAtm(data = data)
}

@Preview
@Composable
fun DocDotNavigationAtm_4() {
    val data = DocDotNavigationAtmData(totalCount = 4, activeDotIndex = 0)
    DocDotNavigationAtm(data = data)
}

@Preview
@Composable
fun DocDotNavigationAtm_5() {
    val data = DocDotNavigationAtmData(totalCount = 5, activeDotIndex = 0)
    DocDotNavigationAtm(data = data)
}

@Preview
@Composable
fun DocDotNavigationAtm_6() {
    val data = DocDotNavigationAtmData(totalCount = 6, activeDotIndex = 0)
    DocDotNavigationAtm(data = data)
}