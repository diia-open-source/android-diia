package ua.gov.diia.ui_base.views.common

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import ua.gov.diia.ui_base.R
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.core.util.extensions.validateString

class DiiaStatusLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttrStyle: Int = 0
) : CardView(context, attrs, defAttrStyle) {

    private val text: TextView

    init {
        inflate(context, R.layout.view_label, this)

        text = findViewById(R.id.text)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaStatusLabel,
            defAttrStyle,
            0
        ).apply {
            try {
                getString(R.styleable.DiiaStatusLabel_labelText)
                    .run(::setLabelText)

                getResourceId(R.styleable.DiiaStatusLabel_labelBackgroundColor, R.color.red)
                    .run(::setLabelBackgroundColor)

                getResourceId(R.styleable.DiiaStatusLabel_labelTextColor, R.color.white)
                    .run(::setLabelTextColor)

                setDefaultParentAttr()
            } finally {
                recycle()
            }
        }
    }

    private fun setDefaultParentAttr() {
        cardElevation = 0f
        radius = resources.getDimension(R.dimen.diia_label_corners_radius)
    }

    fun setLabelText(string: String?) {
       string.validateString { s -> text.text = s }
    }

    fun setLabelText(@StringRes string: Int?) {
       string.validateResource { stringRes -> text.setText(stringRes) }
    }

    fun setLabelBackgroundColor(@ColorRes color: Int?) {
       color.validateResource { colorRes ->  setCardBackgroundColor(context.getColorCompat(colorRes)) }
    }

    fun setLabelTextColor(@ColorRes color: Int?) {
        color.validateResource { colorRes -> text.setTextColor(context.getColorCompat(colorRes)) }
    }
}

@BindingAdapter("labelTextData", "labelTextRes", requireAll = true)
fun labelTextOrDefault(
    view: DiiaStatusLabel,
    text: String?,
    @StringRes res: Int?
) {
    if (text != null) {
        view.setLabelText(text)
    } else {
        view.setLabelText(res)
    }
}