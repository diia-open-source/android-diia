package ua.gov.diia.ui_base.views.common.card_item

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import ua.gov.diia.ui_base.R
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.core.util.extensions.context.getDrawableSafe
import ua.gov.diia.core.util.extensions.validateResource

class DiiaCardNotifiableField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val title: TextView
    private val notificationCount: TextView
    private val arrow: AppCompatImageView
    private val delimiterTop: View
    private val delimiterBottom: View
    private val icon: AppCompatImageView
    private val clickableView: View

    @ColorRes
    private var delimiterColor: Int

    init {
        inflate(context, R.layout.widget_card_notifiable_field, this)

        title = findViewById(R.id.title)
        notificationCount = findViewById(R.id.notificationCount)
        arrow = findViewById(R.id.arrow)
        delimiterTop = findViewById(R.id.delimiter_top)
        delimiterBottom = findViewById(R.id.delimiter_bottom)
        icon = findViewById(R.id.icon)
        clickableView = findViewById(R.id.clickable_view)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaCardNotifiableField,
            defStyleAttr,
            0
        ).apply {
            try {

                delimiterColor = getResourceId(
                    R.styleable.DiiaCardNotifiableField_delimiterColor,
                    R.color.color_divider_blue
                ).also(::setDelimiterColor)

                getString(R.styleable.DiiaCardNotifiableField_title)
                    .run(::setTitle)

                getString(R.styleable.DiiaCardNotifiableField_notificationCount)
                    .run(::setNotificationCount)

                getBoolean(R.styleable.DiiaCardNotifiableField_showTopDelimiter, false)
                    .run(::setShowTopDelimiter)

                getBoolean(R.styleable.DiiaCardNotifiableField_showBottomDelimiter, false)
                    .run(::setShowBottomDelimiter)

                getBoolean(R.styleable.DiiaCardNotifiableField_isEnabled, true)
                    .run(::setIsEnabled)

                getBoolean(R.styleable.DiiaCardNotifiableField_showArrow, true)
                    .run(::setShowArrow)

                getResourceId(R.styleable.DiiaCardNotifiableField_titleTextSize, R.dimen.text_body2)
                    .run(::setTitleTextSize)

                getResourceId(R.styleable.DiiaCardNotifiableField_icon, -1)
                    .run(::setIcon)
            } finally {
                recycle()
            }
        }
    }

    fun setIcon(@DrawableRes res: Int?) {
        res.validateResource(
            valid = { drawableRes ->
                icon.visibility = View.VISIBLE
                icon.background = context.getDrawableSafe(drawableRes)
            },
            invalid = {
                icon.visibility = View.GONE
            }
        )
    }

    fun setIcon(drawable: Drawable?) {
        if (drawable != null) {
            icon.visibility = View.VISIBLE
            icon.background = drawable
        } else {
            icon.visibility = View.GONE
        }
    }

    fun setTitle(string: String?) {
        title.text = string
    }

    fun setTitle(@StringRes res: Int?) {
        if (res != null && res != 0) {
            title.setText(res)
        }
    }

    fun setIsEnabled(enabled: Boolean) {
        clickableView.visibility = if (enabled) View.VISIBLE else View.GONE

        title.isEnabled = enabled
        arrow.isEnabled = enabled

        @ColorRes val res = if (enabled) R.color.black else R.color.black_alpha_50
        arrow.setColorFilter(context.getColorCompat(res), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    fun setDelimiterColor(@ColorRes res: Int?) {
        res.validateResource { colorRes ->
            delimiterColor = colorRes

            val resolvedColor = context.getColorCompat(colorRes)
            delimiterTop.setBackgroundColor(resolvedColor)
            delimiterBottom.setBackgroundColor(resolvedColor)
        }
    }

    fun setNotificationCount(count: String?) {
        val checkValue = (count?.let { Integer.parseInt(it) } ?: 0) == 0
        if (checkValue) notificationCount.visibility = View.GONE else notificationCount.visibility = View.VISIBLE

        if (!count.isNullOrBlank()) {
            with(notificationCount) {
                text = count
                //Adjust padding for large numbers
                if (count.length > 2) setPadding(8, 0, 8, 0)
            }
        }
    }

    fun setShowTopDelimiter(show: Boolean) {
        delimiterTop.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setShowBottomDelimiter(show: Boolean) {
        delimiterBottom.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setShowArrow(show: Boolean) {
        arrow.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setFieldClickListener(listener: () -> Unit) {
        clickableView.setOnClickListener { listener.invoke() }
    }

    fun setTitleTextSize(@DimenRes res: Int?) {
        res.validateResource { sizeRes -> title.setTextSizeFromRes(sizeRes) }
    }

    private fun TextView.setTextSizeFromRes(@DimenRes res: Int) {
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(res))
    }
}