package ua.gov.diia.ui_base.views.common.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.setupHtmlParameters
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.util.extensions.isResourceValid
import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.core.util.extensions.validateString

class DiiaStatusMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val statusTitle: TextView
    private val statusSubtitle: TextView
    private val statusText: TextView
    private val emojiText: TextView

    init {
        inflate(context, R.layout.view_status_message, this)

        statusText = findViewById(R.id.text)
        statusTitle = findViewById(R.id.title)
        statusSubtitle = findViewById(R.id.subTitle)
        emojiText = findViewById(R.id.emoji)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaStatusMessage,
            defStyleAttr,
            0
        ).apply {
            try {
                getString(R.styleable.DiiaStatusMessage_msgTitle).run(::setMsgTitle)
                getString(R.styleable.DiiaStatusMessage_msgText).run(::setMsgText)
                getString(R.styleable.DiiaStatusMessage_msgEmoji).run(::setMsgEmoji)
                getString(R.styleable.DiiaStatusMessage_msgSubtitle).run (::setMsgSubtitle)
                setBackgroundResource(R.drawable.shape_status_message)
            } finally {
                recycle()
            }
        }
    }

    fun setMsgText(message: String?) {
        statusText.text = message
        statusText.visibility = if (message.isStringValid()) View.VISIBLE else View.GONE
    }

    fun setMsgText(@StringRes res: Int?) {
        statusText.visibility = if (res.isResourceValid()) View.VISIBLE else View.GONE
        res.validateResource { stringRes -> statusText.setText(stringRes) }
    }

    fun setMsgTitle(message: String?) {
        statusTitle.text = message
        statusTitle.visibility = if (message.isStringValid()) View.VISIBLE else View.GONE
        adjustConstraints()
    }

    fun setMsgTitle(@StringRes res: Int?) {
        res.validateResource { stringRes -> statusTitle.setText(stringRes) }
        statusTitle.visibility = if (res.isResourceValid()) View.VISIBLE else View.GONE
        adjustConstraints()
    }

    fun setMsgSubtitle(message: String?) {
        statusSubtitle.text = message
        statusSubtitle.visibility = if (message.isStringValid()) View.VISIBLE else View.GONE
        adjustConstraints()
    }

    fun setMsgSubtitle(@StringRes res: Int?) {
        statusSubtitle.visibility = if (res.isResourceValid()) View.VISIBLE else View.GONE
        res.validateResource { stringRes -> statusSubtitle.setText(stringRes) }
        adjustConstraints()
    }

    fun setMsgEmoji(emoji: String?) {
        emoji.validateString { e -> emojiText.text = e }
        emojiText.visibility = if (emoji.isStringValid()) View.VISIBLE else View.GONE
    }

    fun setMsgEmoji(@StringRes emojiRes: Int?) {
        emojiRes.validateResource { e -> emojiText.setText(e) }
        emojiText.visibility = if (emojiRes.isResourceValid()) View.VISIBLE else View.GONE
    }

    private fun adjustConstraints() {
        statusTitle.doOnLayout {
            val constraintSet = ConstraintSet()
            constraintSet.clone(this)
            if (statusTitle.lineCount > 1) {
                constraintSet.setVerticalBias(R.id.title, 0f)
            } else {
                constraintSet.setVerticalBias(R.id.title, 1f)
            }
            constraintSet.applyTo(this)
        }
    }
}

@BindingAdapter("text", "htmlMetadata", "linkActionListener", requireAll = true)
fun DiiaStatusMessage.setupHtmlParameters(
    displayText: String?,
    metadata: List<TextParameter>?,
    onLinkClicked: ((url: String) -> Unit)?
) {
    if (displayText.isStringValid() && !metadata.isNullOrEmpty()){
        findViewById<TextView>(R.id.text).apply {
            visibility = View.VISIBLE
            setupHtmlParameters(displayText, metadata, onLinkClicked)
        }
    }else {
        findViewById<TextView>(R.id.text).apply {
            visibility = View.VISIBLE
            text = displayText
        }
    }
}