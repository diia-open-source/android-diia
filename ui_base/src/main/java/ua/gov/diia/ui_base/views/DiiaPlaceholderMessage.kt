package ua.gov.diia.ui_base.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.setupHtmlParameters

class DiiaPlaceholderMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val infoText: TextView
    private val emojiText: TextView
    private val infoTitle: TextView
    private val infoDescription: TextView

    init {
        inflate(context, R.layout.view_placeholder_message, this)

        infoText = findViewById(R.id.info_text)
        emojiText = findViewById(R.id.emoji)
        infoTitle = findViewById(R.id.info_title)
        infoDescription = findViewById(R.id.info_description)


        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaPlaceholderMessage,
            defStyleAttr,
            0
        ).apply {
            try {
                getString(R.styleable.DiiaPlaceholderMessage_msgText).run(::setMsgText)
                getString(R.styleable.DiiaPlaceholderMessage_msgEmoji).run(::setMsgEmoji)
                getString(R.styleable.DiiaPlaceholderMessage_msgTitle).run(::setMsgTitle)
                getString(R.styleable.DiiaPlaceholderMessage_msgDescription).run(::setMsgDescription)

            } finally {
                recycle()
            }
        }
    }

    fun setMsgText(message: String?) {
        if (message != null) {
            infoText.text = message
        }
    }

    fun setMsgText(@StringRes message: Int?) {
        if (message != null && message != 0) {
            infoText.text = context.getString(message)
        }
    }

    fun setMsgEmoji(emoji: String?) {
        if (emoji != null) {
            emojiText.text = emoji
        }
    }

    fun setMsgEmoji(@StringRes emoji: Int?) {
        if (emoji != null && emoji != 0) {
            emojiText.text = context.getString(emoji)
        }
    }

    fun setMsgTitle(title: String?) {
        if (title != null) {
            infoTitle.text = title
        }
    }

    fun setMsgTitle(@StringRes title: Int?) {
        if (title != null && title != 0) {
            infoText.text = context.getString(title)
        }
    }

    fun setMsgDescription(@StringRes description: Int?) {
        if (description != null && description != 0) {
            infoDescription.text = context.getString(description)
        }
    }

    fun setMsgDescription(description: String?) {
        if (description != null) {
            infoDescription.text = description
        }
    }
}

@BindingAdapter("text", "htmlMetadata", "linkActionListener", requireAll = true)
fun DiiaPlaceholderMessage.setupHtmlParameters(
    displayText: String?,
    metadata: List<TextParameter>?,
    onLinkClicked: ((url: String) -> Unit)?
) {
    if (displayText.isStringValid() && !metadata.isNullOrEmpty()) {
        findViewById<TextView>(R.id.info_text).apply {
            visibility = View.VISIBLE
            setupHtmlParameters(displayText, metadata, onLinkClicked)
        }
    } else {
        findViewById<TextView>(R.id.info_text).apply {
            visibility = View.VISIBLE
            text = displayText
        }
    }
}