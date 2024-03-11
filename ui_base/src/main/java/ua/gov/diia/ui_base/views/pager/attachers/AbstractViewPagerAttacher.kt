package ua.gov.diia.ui_base.views.pager.attachers

import ua.gov.diia.ui_base.views.pager.ScrollingPagerIndicator

abstract class AbstractViewPagerAttacher<T> : PagerAttacher<T> {
    fun updateIndicatorOnPagerScrolled(
        indicator: ScrollingPagerIndicator,
        position: Int,
        positionOffset: Float
    ) {
        val offset: Float = when {
            positionOffset < 0 -> {
                0f
            }
            positionOffset > 1 -> {
                1f
            }
            else -> {
                positionOffset
            }
        }
        indicator.onPageScrolled(position, offset)
    }
}