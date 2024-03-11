package ua.gov.diia.ui_base.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import ua.gov.diia.ui_base.R

class DiiaMenuIconCV @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Checkable {

    private var _label: String? = null
    var label: String?
        get() = _label
        set(value) { findViewById<TextView>(R.id.menu_label).text = value }

    private var isChecked = false

    private var _icon: Drawable? = null

    var icon: Drawable?
        get() = _icon
        set(value) {_icon = value
        invalidateIcon()}

    private var iconSelected: Drawable? = null

    init {
        inflate(context, R.layout.view_diia_menu_icon, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiiaMenuIconCV)
            if (typedArray.hasValue(R.styleable.DiiaMenuIconCV_menuTitle)) {
                _label = typedArray.getString(R.styleable.DiiaMenuIconCV_menuTitle)
                _label?.let {
                    findViewById<TextView>(R.id.menu_label).text = _label
                }
            }
            if (typedArray.hasValue(R.styleable.DiiaMenuIconCV_icon)) {
                val drawableId: Int = typedArray.getResourceId(R.styleable.DiiaMenuIconCV_icon, 0)
                if (drawableId != 0) {
                    _icon = VectorDrawableCompat.create(resources, drawableId, null)
                }
            }
            if (typedArray.hasValue(R.styleable.DiiaMenuIconCV_icon_selected)) {
                val drawableId: Int =
                    typedArray.getResourceId(R.styleable.DiiaMenuIconCV_icon_selected, 0)
                if (drawableId != 0) {
                    iconSelected = VectorDrawableCompat.create(resources, drawableId, null)
                }
            }
            typedArray.recycle()
        }
        invalidateIcon()
    }

    private fun invalidateIcon() {
        val iv = findViewById<ImageView>(R.id.iv_menu_icon)
        if (isChecked) {
            iv.setImageDrawable(iconSelected)
        } else {
            iv.setImageDrawable(icon)
        }
    }

    fun setBadgeVisible(badgeVisible: Boolean) {
        if (badgeVisible) {
            findViewById<View>(R.id.v_badge).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.v_badge).visibility = View.GONE
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        isChecked = !isChecked
        invalidateIcon()
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        invalidateIcon()
    }

}