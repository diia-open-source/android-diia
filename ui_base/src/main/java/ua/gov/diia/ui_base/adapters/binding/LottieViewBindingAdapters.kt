package ua.gov.diia.ui_base.adapters.binding

import android.animation.Animator
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("doOnAnimationEnd")
inline fun LottieAnimationView.doOnAnimationEnd(crossinline action: () -> Unit) {
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            action.invoke()
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
        }
    })
}