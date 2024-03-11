package ua.gov.diia.ui_base.views.common.progress

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.setTextColorCompat
import ua.gov.diia.ui_base.adapters.binding.setImageDrawableEndCompat
import ua.gov.diia.ui_base.adapters.binding.setImageDrawableStartCompat
import ua.gov.diia.core.util.extensions.validateResource

class DiiaProgressButton @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defAttrStyle: Int = 0
) : ConstraintLayout(context, attr, defAttrStyle) {


    private enum class ButtonMode(val code: Int) {
        SOLID(0),
        OUTLINED(1),
        TEXT(2),
        SUCCESS(3)
    }

    private val textButton: Button
    private val progressButtonContainer: ConstraintLayout
    private val progressButtonTitle: TextView
    private val doneIndicator: ConstraintLayout
    private val doneButtonTitle: TextView

    private var currentButtonMode: Int? = 0
    @DrawableRes
    private var successDrawableStart: Int? = null
    @DrawableRes
    private var successDrawableEnd: Int? = null

    init {
        inflate(context, R.layout.view_progress_button, this)

        textButton = findViewById(R.id.text_button)
        progressButtonContainer = findViewById(R.id.container_progressButton)
        progressButtonTitle = findViewById(R.id.title_progress_btn)
        doneIndicator = findViewById(R.id.container_done_button)
        doneButtonTitle = findViewById(R.id.title_done_btn)

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.DiiaProgressButton,
            defAttrStyle,
            0
        ).apply {
            try {
                successDrawableEnd =
                    getResourceId(R.styleable.DiiaProgressButton_successModeDrawableEnd, -1)
                successDrawableStart =
                    getResourceId(R.styleable.DiiaProgressButton_successModeDrawableStart, -1)

                getInt(R.styleable.DiiaProgressButton_buttonMode, 0).run(::setButtonMode)
                getBoolean(R.styleable.DiiaProgressButton_isLoading, false).run(::setIsLoading)
                getString(R.styleable.DiiaProgressButton_title).run(::setTitle)
                getBoolean(R.styleable.DiiaProgressButton_isEnabled, true).run(::setIsEnabled)
                getResourceId(R.styleable.DiiaProgressButton_titleTextSize, R.dimen.text_body2).run(
                    ::setTitleTextSize
                )
            } finally {
                recycle()
            }
        }
    }

    fun setSuccessModeDrawableEnd(@DrawableRes drawableRes: Int?){
        drawableRes?.validateResource { res ->
            successDrawableEnd = res
            setButtonMode(currentButtonMode)
        }
    }

    fun setSuccessModeDrawableStart(@DrawableRes drawableRes: Int?){
        drawableRes?.validateResource { res ->
            successDrawableStart = res
            setButtonMode(currentButtonMode)
        }
    }

    fun setTitleTextSize(@DimenRes res: Int?) {
        if (res != null && res != 0) {
            textButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(res))
            progressButtonTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(res))
        }
    }


    fun setButtonMode(mode: Int?) {
        when (mode) {
            ButtonMode.SOLID.code -> {
                textButton.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.line_button_black_select)
                    enableSuccessDrawables(false)
                    setTextColorCompat(R.color.white)
                }

                progressButtonContainer.background = ContextCompat.getDrawable(context, R.drawable.black_enable_button)
                progressButtonTitle.setTextColorCompat(R.color.white)
            }
            ButtonMode.OUTLINED.code -> {
                textButton.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.outlined_button_black_selector)
                    enableSuccessDrawables(false)
                    val colorList = ContextCompat.getColorStateList(context, R.color.outlined_button_text)
                    setTextColor(colorList)
                }

                progressButtonContainer.background =
                    ContextCompat.getDrawable(context, R.drawable.outlined_button_black)
                progressButtonTitle.setTextColorCompat(R.color.black)
            }
            ButtonMode.TEXT.code -> {
                textButton.apply {
                    background = null
                    enableSuccessDrawables(false)
                    val colorList = ContextCompat.getColorStateList(context, R.color.outlined_button_text)
                    setTextColor(colorList)
                }
                progressButtonContainer.background = null
                progressButtonTitle.setTextColorCompat(R.color.black)
            }
            ButtonMode.SUCCESS.code -> {
                textButton.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.line_button_green_select)
                    setTextColorCompat(R.color.white)
                    enableSuccessDrawables(true)
                }
            }
        }
        currentButtonMode = mode
    }

    private fun enableSuccessDrawables(enable: Boolean) {
        if (enable) {
            successDrawableStart.validateResource { res -> textButton.setImageDrawableStartCompat(res) }
            successDrawableEnd.validateResource { res -> textButton.setImageDrawableEndCompat(res) }
        } else {
            textButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    fun setTitle(string: String?) {
        if (string != null) {
            textButton.text = string
            progressButtonTitle.text = string
        }
    }

    fun setTitle(@StringRes res: Int?) {
        if (res != null && res != 0) {
            val title = context.getString(res)
            textButton.text = title
            progressButtonTitle.text = title
            doneButtonTitle.text = title
        }
    }

    fun setIsLoading(loading: Boolean?) {
        if (loading == true) {
            textButton.visibility = View.GONE
            progressButtonContainer.visibility = View.VISIBLE
        } else {
            textButton.visibility = View.VISIBLE
            progressButtonContainer.visibility = View.GONE
        }
    }

    fun setIsEnabled(enabled: Boolean?) {
        textButton.isEnabled = enabled == true
    }

    fun setOnButtonClickListener(listener: (() -> Unit)?) {
        textButton.setOnClickListener { listener?.invoke() }
    }

    fun setIsDone(done: Boolean) {
        if (done) doneIndicator.visibility = VISIBLE
        else doneIndicator.visibility = GONE
    }
}

@BindingAdapter("template", "textTemplate", requireAll = true)
fun DiiaProgressButton.setTitleFromTemplate(template: String?, value: Any?) {
    if (template != null && value != null) setTitle(template.format(value))
}
