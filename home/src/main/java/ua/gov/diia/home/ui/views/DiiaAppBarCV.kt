package ua.gov.diia.home.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.home.R

class DiiaAppBarCV @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var onQrScanButtonPressed: (() -> Unit)? = null

    init {
        inflate(context, R.layout.view_diia_app_bar, this)

        findViewById<ImageView>(R.id.iv_scan_qr).setOnClickListener {
            onQrScanButtonPressed?.invoke()
        }
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiiaAppBarCV)
            if (typedArray.hasValue(R.styleable.DiiaAppBarCV_header)) {
                val title = typedArray.getString(R.styleable.DiiaAppBarCV_header)
                findViewById<TextView>(R.id.app_bar_header).text = title
            }
            typedArray.recycle()
        }
    }

    fun setHeader(header: String) {
        findViewById<TextView>(R.id.app_bar_header).text = header
    }

    fun setIcon(@DrawableRes res: Int?) {
        val ic = findViewById<ImageView>(R.id.iv_scan_qr)
        res.validateResource(
            valid = { drawableRes ->
                ic.setImageDrawable(VectorDrawableCompat.create(resources, drawableRes, null))
            },
            invalid = {
                ic.visibility = View.GONE
            }
        )
    }

    fun setIconVisible(visible: Boolean) {
        findViewById<View>(R.id.iv_scan_qr).visibility = if (visible) View.VISIBLE else View.GONE
    }

}