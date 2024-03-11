package ua.gov.diia.ui_base.adapters.binding

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import ua.gov.diia.core.util.extensions.context.getDrawableSafe
import ua.gov.diia.ui_base.views.DiiaMenuIconCV

@BindingAdapter("imgIcon")
fun setIcon(view: DiiaMenuIconCV, icon: Drawable) {
    view.icon = icon
}

@BindingAdapter("menuTitl")
fun setTitle(view: DiiaMenuIconCV, title: String) {
    view.label = title
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("isInvisible")
fun bindIsInvisible(view: View, isInVisible: Boolean) {
    view.visibility = if (isInVisible) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("constraintF")
fun setConstraint(view: Guideline, value: Float) {
    view.setGuidelinePercent(value)
}

@BindingAdapter("isHidden")
fun bindIsHidden(view: View, isHidden: Boolean) {
    view.visibility = if (isHidden) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}


@BindingAdapter("textColor")
fun textColor(view: TextView, @ColorRes color: Int) {
    view.setTextColor(ContextCompat.getColor(view.context, color))
}

@BindingAdapter("imgBase64")
fun bindBase64(imgView: ImageView, imgBase64: String?) {
    try {
        imgBase64?.let {
            val imageBytes = Base64.decode(it, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imgView.setImageBitmap(decodedImage)
        }
    } catch (e: Exception) {
    }
}

@BindingAdapter("bitmap")
fun setImageBitmap(view: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        view.setImageBitmap(it)
    }
}

@BindingAdapter("src")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("bg")
fun setImageBackground(view: View, drawable: Int) {
    view.background = ContextCompat.getDrawable(view.context, drawable)
}

@BindingAdapter("src")
fun setImageDrawableCompat(view: ImageView, @DrawableRes imageResource: Int) {
    view.setImageResource(imageResource)
}

@BindingAdapter("drawableEndSupport")
fun TextView.setImageDrawableEndCompat(imageResource: Int) {
    val drawable = context.getDrawableSafe(imageResource)
    val existingDrawables = compoundDrawablesRelative
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        existingDrawables[0],
        existingDrawables[1],
        drawable,
        existingDrawables[3]
    )
}

@BindingAdapter("drawableStartSupport")
fun TextView.setImageDrawableStartCompat(imageResource: Int) {
    val drawable = context.getDrawableSafe(imageResource)
    val existingDrawables = compoundDrawablesRelative
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        drawable,
        existingDrawables[1],
        existingDrawables[2],
        existingDrawables[3]
    )
}

@BindingAdapter("drawableStartSupport")
fun TextView.setImageDrawableStartCompat(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

@BindingAdapter("layout_constraintGuide_begin")
fun setLayoutConstraintGuideBegin(guideline: Guideline, percent: Float) {
    val params = guideline.layoutParams as ConstraintLayout.LayoutParams
    params.guidePercent = percent
    guideline.layoutParams = params
}

@BindingAdapter("android:layout_height")
fun setHeight(view: View, isMatchParent: Boolean) {
    val params = view.layoutParams
    params.height = if (isMatchParent) {
        ViewGroup.LayoutParams.MATCH_PARENT
    } else {
        ViewGroup.LayoutParams.WRAP_CONTENT
    }
    view.layoutParams = params
}

@BindingAdapter("layoutMarginBottom")
fun setLayoutMarginBottom(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("layoutMarginTop")
fun setLayoutMarginTop(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter(value = ["isGoneMarginTop", "marginGoneTop"], requireAll = false)
fun View.bindIsGoneMarginTop(isGoneMarginTop: Boolean, marginGoneTop: Float) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    if (isGoneMarginTop) {
        visibility = View.GONE
        layoutParams.topMargin = 0
    } else {
        visibility = View.VISIBLE
        layoutParams.topMargin = marginGoneTop.toInt()
    }
}