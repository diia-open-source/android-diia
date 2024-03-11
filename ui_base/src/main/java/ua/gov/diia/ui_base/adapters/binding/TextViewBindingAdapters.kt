package ua.gov.diia.ui_base.adapters.binding

import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.util.Linkify
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import ua.gov.diia.ui_base.R
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.core.util.extensions.context.getStringSafe
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.core.util.html.convertToLink
import ua.gov.diia.core.util.html.convertToMail
import ua.gov.diia.core.util.html.convertToPhone

/////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// Text gravity ///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

enum class TextGravity {
    START, CENTER, END
}

@BindingAdapter("dynamicGravity")
fun TextView.setDynamicGravity(gravity: TextGravity?) {
    this.gravity = when (gravity) {
        TextGravity.START -> Gravity.START
        TextGravity.CENTER -> Gravity.CENTER
        TextGravity.END -> Gravity.END
        else -> return
    }
}

@BindingAdapter("textAllCaps")
fun TextView.setTextAllCaps(allCaps: Boolean){
    isAllCaps = allCaps
}

/////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////// Text with flags modifiers //////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

@BindingAdapter("paintUnderline")
fun TextView.painUnderline(shouldPaint: Boolean) {
    if (shouldPaint) {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////// Compound drawable control ///////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////


@BindingAdapter(
    "enabledDrawable",
    "enabledDrawableColor",
    "disabledDrawableColor",
    requireAll = false
)
fun TextView.setEnabledDrawable(
    enabled: Boolean,
    @ColorRes enabledColorRes: Int?,
    @ColorRes disabledColorRes: Int?
) {
    val enabledColor = enabledColorRes ?: R.color.black
    val disabledColor = disabledColorRes ?: R.color.black_alpha_30
    val color = (if (enabled) enabledColor else disabledColor).run(context::getColorCompat)

    compoundDrawables
        .filterNotNull()
        .forEach { drawable ->
            drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
}

/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// Text color /////// //////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

@BindingAdapter("textColorCompat")
fun TextView.setTextColorCompat(@ColorRes color: Int?) {
    color.validateResource { colorRes -> setTextColor(context.getColorCompat(colorRes)) }
}

/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// Text from resource //////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

@BindingAdapter("textRes")
fun TextView.setTextRes(@StringRes res: Int?) {
    text = context.getStringSafe(res)
}

@BindingAdapter("text", "textFallback", requireAll = true)
fun TextView.setTextWithFallback(text: CharSequence?, textFallback: CharSequence?) {
    setText(if (text.isNullOrEmpty()) textFallback else text)
}

/////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// Text templates /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Use it if we need to set text generated at the runtime, otherwise
 * use the regular [TextView.setText] attribute.
 *
 * @param value    a value which we want to put into the template
 * @param template a template which represents the desired formatted string
 *                 with the placeholder for value
 */
@BindingAdapter("template", "textTemplate", requireAll = true)
fun TextView.setTextFromTemplate(template: String?, value: Any?) {
    if (template != null && value != null) text = template.format(value)
}

@BindingAdapter("template", "intTemplate", requireAll = true)
fun TextView.setIntFromTemplate(template: String?, value: Int?) {
    if (template != null && value != null) {
        text = template.format(value)
    }
}

@BindingAdapter("templateRes", "textTemplate", requireAll = true)
fun TextView.setTextFromTemplateTemplateRes(@StringRes templateRes: Int?, textTemplate: Any?) {
    templateRes.validateResource { res -> text = context.getString(res).format(textTemplate) }
}


/**
 * Concatenates template with the string resource
 */
@BindingAdapter("template", "textTemplate", requireAll = true)
fun TextView.setTextFromTemplate(template: String?, @StringRes res: Int?) {
    if (template != null && res != null) {
        text = template.format(context.getStringSafe(res))
    }
}

@BindingAdapter("template", "textTemplate1", "textTemplate2", requireAll = true)
fun TextView.setTextFromTemplate(template: String?, value1: Any?, value2: Any?) {
    if (template != null && value1 != null && value2 != null) text = template.format(value1, value2)
}

/////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////// Text with HTML tags /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

private const val HTTP_PREFIX = "http"
private const val TEL_PREFIX = "tel"
private const val MAIL_TO_PREFIX = "mailto:"
private const val LINK = "link"
private const val PHONE = "phone"
private const val EMAIL = "email"
private const val TEXT_PLAIN = "text/plain"

@BindingAdapter("text", "htmlMetadata", "linkActionListener", requireAll = true)
fun TextView.setupHtmlParameters(
    displayText: String?,
    metadata: List<TextParameter>?,
    onLinkClicked: ((url: String) -> Unit)?
) {
    handleTextHtmlActions { url -> onLinkClicked?.invoke(url) }
    text = if (displayText == null || metadata.isNullOrEmpty()) {
        displayText
    } else {
        HtmlCompat.fromHtml(
            displayText buildWithMetadata metadata,
            HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
        )
    }
}

private fun TextView.handleTextHtmlActions(onLinkClicked: (url: String) -> Unit) {
    Linkify.addLinks(this, Linkify.ALL)
    movementMethod = BetterLinkMovementMethod.newInstance().apply {
        setOnLinkClickListener { _, url ->
            when {
                url.startsWith(HTTP_PREFIX) -> onLinkClicked(url)
                url.startsWith(TEL_PREFIX) -> {
                    val actionIntent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(actionIntent)
                }
                url.startsWith(MAIL_TO_PREFIX) -> {
                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = TEXT_PLAIN

                        val to = arrayOf(url.replace(MAIL_TO_PREFIX, ""))
                        putExtra(Intent.EXTRA_EMAIL, to)
                    }

                    context.startActivity(Intent.createChooser(emailIntent, ""))
                }
            }
            true
        }
    }
}

infix fun String.buildWithMetadata(metadata: List<TextParameter>): String {
    var result = this
    for (param in metadata) {
        when (param.type) {
            LINK -> {
                val old = "{${param.data?.name.toString()}}"
                val new = convertToLink(
                    param.data?.resource.toString(),
                    param.data?.alt.toString()
                )
                result = result.replace(old, new)
                result = result.replace("\n", "<br>")
            }
            PHONE -> {
                val old = "{${param.data?.name.toString()}}"
                val new =
                    convertToPhone(
                        param.data?.resource.toString(),
                        param.data?.alt.toString()
                    )
                result = result.replace(old, new)
                result = result.replace("\n", "<br>")
            }
            EMAIL -> {
                val old = "{${param.data?.name.toString()}}"
                val new =
                    convertToMail(
                        param.data?.resource.toString(),
                        param.data?.alt.toString()
                    )
                result = result.replace(old, new)
                result = result.replace("\n", "<br>")
            }
        }
    }
    return result
}

