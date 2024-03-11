package ua.gov.diia.ui_base.views.pager.attachers

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import ua.gov.diia.ui_base.views.pager.ScrollingPagerIndicator

class ViewPager2Attacher : AbstractViewPagerAttacher<ViewPager2>() {

    private var dataSetObserver: AdapterDataObserver? = null
    private var attachedAdapter: RecyclerView.Adapter<*>? = null
    private var onPageChangeListener: OnPageChangeCallback? = null
    private var pager: ViewPager2? = null

    override fun attachToPager(indicator: ScrollingPagerIndicator, pager: ViewPager2) {
        attachedAdapter = pager.adapter
        checkNotNull(attachedAdapter) { "Set adapter before call attachToPager() method" }
        this.pager = pager
        updateIndicatorDotsAndPosition(indicator)
        dataSetObserver = object : AdapterDataObserver() {
            override fun onChanged() {
                indicator.reattach()
            }
        }
        dataSetObserver?.let {
            attachedAdapter?.registerAdapterDataObserver(it)
        }
        onPageChangeListener = object : OnPageChangeCallback() {
            var idleState = true
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixel: Int
            ) {
                updateIndicatorOnPagerScrolled(indicator, position, positionOffset)
            }

            override fun onPageSelected(position: Int) {
                updateIndicatorDotsAndPosition(indicator)
            }

            override fun onPageScrollStateChanged(state: Int) {
                idleState = state == ViewPager2.SCROLL_STATE_IDLE
            }
        }
        onPageChangeListener?.let {
            pager.registerOnPageChangeCallback(it)
        }
    }

    override fun detachFromPager() {
        dataSetObserver?.let {
            attachedAdapter?.unregisterAdapterDataObserver(it)
        }

        onPageChangeListener?.let {
            pager?.unregisterOnPageChangeCallback(it)
        }
    }

    private fun updateIndicatorDotsAndPosition(indicator: ScrollingPagerIndicator) {
        attachedAdapter?.let {
            indicator.setDotCount(it.itemCount)
        }

        pager?.let {
            indicator.setCurrentPosition(it.currentItem)
        }
    }
}