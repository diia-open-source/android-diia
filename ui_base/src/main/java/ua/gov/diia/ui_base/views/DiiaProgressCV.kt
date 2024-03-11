package ua.gov.diia.ui_base.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import ua.gov.diia.ui_base.R

class DiiaProgressCV @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_diia_progress, this)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiiaProgressCV)
            typedArray.recycle()
        }
        start()
    }

    fun start(){
        findViewById<View>(R.id.qr_load_progress_inner).startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.anim_progress
            )
        )
    }

    fun finish() {
        clearAnimation()
        visibility = View.INVISIBLE
    }
}