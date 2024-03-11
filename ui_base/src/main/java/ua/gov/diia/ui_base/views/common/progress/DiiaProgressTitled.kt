package ua.gov.diia.ui_base.views.common.progress

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import ua.gov.diia.ui_base.R

class DiiaProgressTitled @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val title: TextView

    init {
        inflate(context, R.layout.view_progress_titled, this)

        title = findViewById(R.id.progress_title)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaProgressTitled,
            defStyleAttr,
            0
        ).apply {
            try {
                getString(R.styleable.DiiaProgressTitled_title).run(::setTitle)
                getBoolean(R.styleable.DiiaProgressTitled_isTitleVisible, true).run(::setIsTitleVisible)
            } finally {
                recycle()
            }
        }
    }

    fun setIsTitleVisible(visible: Boolean? = true) {
        title.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    fun setTitle(string: String?) {
        if (string != null) {
            title.text = string
        }
    }

    fun setTitle(@StringRes res: Int?) {
        if (res != null && res != 0) {
            title.text = context.getString(res)
        }
    }
}