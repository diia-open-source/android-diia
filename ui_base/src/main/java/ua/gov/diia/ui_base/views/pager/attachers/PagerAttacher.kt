package ua.gov.diia.ui_base.views.pager.attachers

import ua.gov.diia.ui_base.views.pager.ScrollingPagerIndicator

/**
 * Interface for attaching to custom pagers.
 *
 * @param <T> custom pager's class
</T> */
interface PagerAttacher<T> {
    /**
     * Here you should add all needed callbacks to track pager's item count, position and offset
     * You must call:
     * [ScrollingPagerIndicator.setDotCount] - initially and after page selection,
     * [ScrollingPagerIndicator.setCurrentPosition] - initially and after page selection,
     * [ScrollingPagerIndicator.onPageScrolled] - in your pager callback to track scroll offset,
     * [ScrollingPagerIndicator.reattach] - each time your adapter items change.
     *
     * @param indicator indicator
     * @param pager     pager to attach
     */
    fun attachToPager(indicator: ScrollingPagerIndicator, pager: T)

    /**
     * Here you should unregister all callbacks previously added to pager and adapter
     */
    fun detachFromPager()
}