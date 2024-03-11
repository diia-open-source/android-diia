package ua.gov.diia.ui_base.views.common.progress

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import ua.gov.diia.ui_base.R

class DiiaProgressWindow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.widget_progress_window, this)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaProgressWindow,
            defStyleAttr,
            0
        ).apply {
            recycle()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}