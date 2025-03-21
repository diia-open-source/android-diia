package ua.gov.diia.ui_base.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import ua.gov.diia.ui_base.R

class DiiaSwitch @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attr, defStyleAttr) {

    private val switchBtn: SwitchCompat
    private var currentEnableState: Boolean = false

    init {
        inflate(context, R.layout.view_diia_switch, this)

        switchBtn = findViewById(R.id.cvSwitchBtn)

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.DiiaSwitch,
            defStyleAttr,
            0
        ).apply {
            try {
                getBoolean(R.styleable.DiiaSwitch_checked, false).run(::setChecked)
                getBoolean(R.styleable.DiiaSwitch_isActive, true).run(::setIsActive)
            } finally {
                recycle()
            }
        }
    }

    fun setChecked(checked: Boolean?) {
        checked?.let { state ->
            currentEnableState = state
            switchBtn.isChecked = state
        }
    }

    fun setIsActive(active: Boolean?) {
        switchBtn.apply {
            isClickable = active ?: true
            isFocusable = active ?: true
            isEnabled = active ?: true
        }
    }

    fun setOnSwitchClickListener(todo: () -> Unit) {
        switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked != currentEnableState) {
                buttonView.isChecked = currentEnableState
                todo.invoke()
            }
        }
    }
}