package ua.gov.diia.ui_base.adapters.binding

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ua.gov.diia.core.util.extensions.context.getColorCompatSafe
import ua.gov.diia.core.util.extensions.context.getDrawableSafe

@BindingAdapter("srcFromUri")
fun ImageView.srcFromUri(uri: String?){
    if (uri == null) return

    Glide
        .with(context)
        .load(uri.toUri())
        .into(this)
}

@BindingAdapter("srcFromUrl")
fun ImageView.srcFromUrl(url: String?){
    if (url == null) return

    Glide
        .with(context)
        .load(url)
        .into(this)
}

@BindingAdapter("srcResCompat")
fun ImageView.srcFromRes(@DrawableRes res: Int){
    setImageDrawable(context.getDrawableSafe(res))
}

@BindingAdapter("vectorTintFromRes")
fun ImageView.tintFromRes(@ColorRes res: Int?){
    val colorCompatRes = context.getColorCompatSafe(res) ?: return
    setColorFilter(colorCompatRes, android.graphics.PorterDuff.Mode.SRC_IN)
}

@BindingAdapter("rotateRes")
fun ImageView.rotateRes(rotate: Boolean?){
    rotation = if (rotate == true) 180f else 0f
}

@BindingAdapter(value = ["srcUrlWithPlaceholder", "placeholder"], requireAll = false)
fun ImageView.srcUrlWithPlaceholder(url: String?, placeholder: Int?) {
    val glide = Glide.with(this)
    if (url.isNullOrEmpty()) {
        if (placeholder == null) {
            glide.clear(this)
        } else {
            glide.load(placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    } else {
        glide.load(url)
            .error(context.getDrawableSafe(placeholder))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}
