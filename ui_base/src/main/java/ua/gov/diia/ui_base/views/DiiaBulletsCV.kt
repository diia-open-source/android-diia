package ua.gov.diia.ui_base.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ua.gov.diia.ui_base.R
import kotlin.math.roundToInt

class DiiaBulletsCV @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var numOfBullets = 0
    private var bulletSize = DEFAULT_BULLET_SIZE
    private var bulletMargin = DEFAULT_BULLET_MARGIN

    @ColorInt
    private var bulletsColor = Color.BLACK

    init {
        orientation = HORIZONTAL
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiiaBulletsCV)
            numOfBullets = if (typedArray.hasValue(R.styleable.DiiaBulletsCV_num_of_bullets)) {
                typedArray.getInt(R.styleable.DiiaBulletsCV_num_of_bullets, 0)
            } else {
                0
            }
            bulletsColor = if (typedArray.hasValue(R.styleable.DiiaBulletsCV_num_of_bullets)) {
                typedArray.getColor(R.styleable.DiiaBulletsCV_bullets_color, Color.BLACK)
            } else {
                Color.BLACK
            }
            bulletSize = if (typedArray.hasValue(R.styleable.DiiaBulletsCV_bullet_size)) {
                typedArray.getDimension(R.styleable.DiiaBulletsCV_bullet_size, DEFAULT_BULLET_SIZE)
            } else {
                DEFAULT_BULLET_SIZE
            }
            bulletMargin = if (typedArray.hasValue(R.styleable.DiiaBulletsCV_bullet_margin)) {
                typedArray.getDimension(
                    R.styleable.DiiaBulletsCV_bullet_margin,
                    DEFAULT_BULLET_MARGIN
                )
            } else {
                DEFAULT_BULLET_MARGIN
            }
            typedArray.recycle()
        }
        setNumOfBullets(numOfBullets)
    }

    fun setPosition(position: Int) {
        for (i in 0 until numOfBullets) {
            val bullet = getChildAt(i)
            if (i + 1 <= position) {
                if (bullet is ImageView) {
                    bullet.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_full_ellipse))
                }
            } else {
                if (bullet is ImageView) {
                    bullet.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_empty_ellipse))
                }
            }
        }
    }

    fun setNumOfBullets(numOfBullets: Int) {
        this.numOfBullets = numOfBullets
        removeAllViews()
        for (i in 0 until numOfBullets) {
            val bullet = ImageView(context)
            bullet.setColorFilter(bulletsColor, android.graphics.PorterDuff.Mode.SRC_IN)
            val layoutParams = LayoutParams(bulletSize.roundToInt(), bulletSize.roundToInt())
            layoutParams.setMargins(bulletMargin.toInt(), 0, bulletMargin.toInt(), 0)
            bullet.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_empty_ellipse))
            addView(bullet, i, layoutParams)
        }
    }


    companion object {
        const val DEFAULT_BULLET_SIZE = 8.0f
        const val DEFAULT_BULLET_MARGIN = 4.0f
    }
}