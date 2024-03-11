package ua.gov.diia.ui_base.views.common.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.setupHtmlParameters
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.core.util.extensions.validateString

class DiiaAttentionMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val infoTitle: TextView
    private val infoText: TextView
    private val emojiText: TextView

    init {
        inflate(context, R.layout.view_attantion_message, this)

        infoTitle = findViewById(R.id.info_title)
        infoText = findViewById(R.id.info_text)
        emojiText = findViewById(R.id.emoji)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaAttentionMessage,
            defStyleAttr,
            0
        ).apply {
            try {
                getString(R.styleable.DiiaAttentionMessage_msgTitle).run(::setMsgTitle)
                getString(R.styleable.DiiaAttentionMessage_msgText).run(::setMsgText)
                getString(R.styleable.DiiaAttentionMessage_msgEmoji).run(::setMsgEmoji)
                setBackgroundResource(R.drawable.back_penalties_paid)
            } finally {
                recycle()
            }
        }
    }

    fun setMsgTitle(message: String?) {
        message.validateString { string ->
            infoTitle.visibility = View.VISIBLE
            infoTitle.text = string
        }
    }

    fun setMsgTitle(@StringRes message: Int?) {
        message.validateResource { res ->
            infoTitle.visibility = View.VISIBLE
            infoTitle.text = context.getString(res)
        }
    }

    fun setMsgText(message: String?) {
        message.validateString { string ->
            infoText.visibility = VISIBLE
            infoText.text = string
        }
    }

    fun setMsgText(@StringRes message: Int?) {
        message.validateResource { res ->
            infoText.visibility = VISIBLE
            infoText.text = context.getString(res)
        }
    }

    fun setMsgEmoji(emoji: String?) {
        emoji.validateString { string -> emojiText.text = string }
    }

    fun setMsgEmoji(@StringRes emoji: Int?) {
        emoji.validateResource { res -> emojiText.text = context.getString(res) }
    }
}

@BindingAdapter("text", "htmlMetadata", "linkActionListener", requireAll = true)
fun DiiaAttentionMessage.setupHtmlParameters(
    displayText: String?,
    metadata: List<TextParameter>?,
    onLinkClicked: ((url: String) -> Unit)?
) {
    if (displayText.isStringValid() && !metadata.isNullOrEmpty()){
        findViewById<TextView>(R.id.info_text).apply {
            visibility = View.VISIBLE
            setupHtmlParameters(displayText, metadata, onLinkClicked)
        }
    }else {
        findViewById<TextView>(R.id.info_text).apply {
            visibility = View.VISIBLE
            text = displayText
        }
    }
}