package ua.gov.diia.ui_base.views.common.progress

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.bindBase64
import ua.gov.diia.core.util.extensions.context.getDrawableSafe

class DiiaProgressTextWithImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

//    private val borderBack: View
    private val progressIndicator: View
    private val imageIcon: ImageView
    private val title: TextView

    init {
        inflate(context, R.layout.view_progress_text_with_image, this)

//        borderBack = findViewById(R.id.v_border_back)
        progressIndicator = findViewById(R.id.progress_indicator)
        imageIcon = findViewById(R.id.iv_icon_start)
        title = findViewById(R.id.tv_progress_text_title)

        context.obtainStyledAttributes(attrs, R.styleable.DiiaProgressTextWithImage).apply {
            try {
                getBoolean(R.styleable.DiiaProgressTextWithImage_isLoading, false).run(::setIsLoading)
                getString(R.styleable.DiiaProgressTextWithImage_title).run(::setTitle)
                getResourceId(R.styleable.DiiaProgressTextWithImage_titleTextSize, R.dimen.text_body2).run(::setTitleTextSize)
                getResourceId(R.styleable.DiiaProgressTextWithImage_iconStart, R.drawable.ic_document).run(::setIcon)
                getResourceId(R.styleable.DiiaProgressTextWithImage_innerPaddingVertical, R.dimen.large).run(::innerPaddingVertical)
                getString(R.styleable.DiiaProgressTextWithImage_iconStartBase64).run(::setIconStartBase64)
                getDimension(R.styleable.DiiaProgressTextWithImage_iconStartSize, context.resources.getDimension(R.dimen.xlarge)).run(::setIconSize)
            } finally {
                recycle()
            }
        }

        setBackgroundResource(R.drawable.shape_outlined_box_primary)
    }

    private fun innerPaddingVertical(@DimenRes res: Int?) {
        if (res != null && res != 0) {
            setPadding(
                paddingStart,
                context.resources.getDimension(res).toInt(),
                paddingEnd,
                context.resources.getDimension(res).toInt()
            )
        }
    }

    fun setIcon(@DrawableRes res: Int?) {
        imageIcon.setImageDrawable(context.getDrawableSafe(res))
    }

    fun setIconBase64(base64: String?) {
        bindBase64(imageIcon, base64)
    }

    fun setTitleTextSize(@DimenRes res: Int?) {
        if (res != null && res != 0) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(res))
        }
    }

    fun setTitle(string: String?) {
        if (string != null) {
            title.text = string
            adjustConstraints()
        }
    }

    fun setTitle(@StringRes res: Int?) {
        if (res != null && res != 0) {
            val receivedTitle = context.getString(res)
            title.text = receivedTitle
            adjustConstraints()
        }
    }

    fun setIsLoading(loading: Boolean?) {
        if (loading == true) {
            imageIcon.visibility = View.GONE
            progressIndicator.visibility = View.VISIBLE
        } else {
            imageIcon.visibility = View.VISIBLE
            progressIndicator.visibility = View.GONE
        }
    }

    fun setIsEnabled(enabled: Boolean?) {
        isEnabled = enabled == true
    }

    fun setBackground(@DrawableRes res: Int?) {
        setBackgroundResource(res ?: 0)
    }

    fun setIconStartBase64(imgBase64: String?) {
        bindBase64(imageIcon, imgBase64)
    }

    fun setIconSize(size: Float) {
        val s = size.toInt()
        imageIcon.updateLayoutParams<ViewGroup.LayoutParams> {
            width = s
            height = s
        }
        progressIndicator.updateLayoutParams<ViewGroup.LayoutParams> {
            width = s
            height = s
        }
    }

    private fun adjustConstraints() {
        title.doOnLayout {
            val constraintSet = ConstraintSet()
            constraintSet.clone(this)
            if (title.lineCount > 1) {
                constraintSet.setVerticalBias(R.id.iv_icon_start, 0f)
                constraintSet.setVerticalBias(R.id.progress_indicator, 0f)
            } else {
                constraintSet.setVerticalBias(R.id.iv_icon_start, 0.5f)
                constraintSet.setVerticalBias(R.id.progress_indicator, 0.5f)
            }
            constraintSet.applyTo(this)
        }
    }
}