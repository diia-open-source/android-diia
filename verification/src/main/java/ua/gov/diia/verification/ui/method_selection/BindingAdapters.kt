package ua.gov.diia.verification.ui.method_selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import com.google.android.flexbox.FlexboxLayout
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.context.getDimensionPixelSizeSafe
import ua.gov.diia.core.util.extensions.context.getDrawableSafe
import ua.gov.diia.core.util.extensions.lifecycle.consumeEvent
import ua.gov.diia.verification.R
import ua.gov.diia.verification.databinding.ItemLoginAppBinding
import ua.gov.diia.verification.model.VerificationMethodView

@BindingAdapter(
    "data",
    "doOnItemSelected",
    "enableGoneMode",
    "onGoneListener",
    requireAll = false
)
internal fun FlexboxLayout.inflateFromData(
    dataEvent: UiDataEvent<List<VerificationMethodView>>?,
    doOnItemSelected: ((code: String) -> Unit)?,
    enableGoneMode: Boolean?,
    onGoneListener: ((visible: Boolean) -> Unit)?
) {
   dataEvent?.consumeEvent { data ->
       if (enableGoneMode == true) {
           visibility = View.VISIBLE
           onGoneListener?.invoke(true)
       }

       if (!data.isNullOrEmpty() && doOnItemSelected != null) {
           removeAllViews()
           data.forEach { method ->
               val view = method.toView(context) { code ->
                   doOnItemSelected.invoke(code)
                   if (enableGoneMode == true) {
                       visibility = View.GONE
                       onGoneListener?.invoke(false)
                   }
               }
               addView(view)
           }
       }
   }
}

@BindingAdapter(
    "dataCached",
    "doOnItemSelected",
    requireAll = false
)
internal fun FlexboxLayout.inflateFromData(
    data: List<VerificationMethodView>?,
    doOnItemSelected: ((code: String) -> Unit)?
) {
    if (!data.isNullOrEmpty() && doOnItemSelected != null) {
        removeAllViews()
        data.forEach { method ->
            val view = method.toView(context) { code ->
                doOnItemSelected.invoke(code)
                /*if (enableGoneMode == true) {
                    visibility = View.GONE
                    onGoneListener?.invoke(false)
                }*/
            }
            addView(view)
        }
    }
}

private fun VerificationMethodView.toView(
    context: Context,
    doOnItemClicked: (code: String) -> Unit
): View {

    val padding = context.getDimensionPixelSizeSafe(R.dimen.middle)

    return ItemLoginAppBinding.inflate(LayoutInflater.from(context))
        .apply {
            icon = context.getDrawableSafe(iconRes)
            ivAppLogo.setOnClickListener { doOnItemClicked(code) }
        }
        .root
        .apply { updatePadding(left = padding, right = padding, bottom = padding, top = padding) }
}
