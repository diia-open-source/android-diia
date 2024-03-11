package ua.gov.diia.ui_base.util.view

import android.view.View
import androidx.annotation.DimenRes
import androidx.viewpager2.widget.ViewPager2
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.doc.HorizontalMarginItemDecoration

enum class PagerSpacingParams(@DimenRes val spacing: Int, @DimenRes val nextItemVisible: Int) {
    LARGE(R.dimen.large, R.dimen.xlarge),
    X_LARGE(R.dimen.xlarge, R.dimen.xxlarge)
}

fun ViewPager2.setupPagerSpacingDecoration(params: PagerSpacingParams = PagerSpacingParams.LARGE) {
    offscreenPageLimit = 1
    val nextItemVisiblePx = resources.getDimension(params.nextItemVisible)
    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -nextItemVisiblePx * position
    }
    setPageTransformer(pageTransformer)
    val itemDecoration =
        HorizontalMarginItemDecoration(
            context,
            params.spacing,
            R.dimen.pages_current_item_shadow
        )
    addItemDecoration(itemDecoration)
}

val ViewPager2.hasNextPage: Boolean
    get() = currentItem < (adapter?.itemCount ?: 0) - 1

val ViewPager2.hasPreviousPage: Boolean
    get() = currentItem > 0

fun ViewPager2.scrollToNextPage() {
    if (hasNextPage) {
        setCurrentItem(currentItem + 1, true)
    }
}

fun ViewPager2.scrollToPreviousPage() {
    if (hasPreviousPage) {
        setCurrentItem(currentItem - 1, true)
    }
}