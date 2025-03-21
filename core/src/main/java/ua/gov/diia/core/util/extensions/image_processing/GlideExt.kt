package ua.gov.diia.core.util.extensions.image_processing

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun RequestBuilder<Drawable>.listener (
    doOnLoaded: () -> Unit = {},
    doOnFailure: () -> Unit = {}
) = listener(object : RequestListener<Drawable?> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable?>,
        isFirstResource: Boolean
    ): Boolean {
        doOnFailure.invoke()
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable?>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        doOnLoaded.invoke()
        return false
    }
})