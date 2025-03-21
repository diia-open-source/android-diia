package ua.gov.diia.ui_base.views.common.card_item

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.core.util.extensions.context.serviceInput
import ua.gov.diia.core.util.extensions.data.formatWithSpaces
import ua.gov.diia.core.util.extensions.data.toPrice
import ua.gov.diia.core.util.extensions.validateResource
import ua.gov.diia.core.util.filter.DecimalDigitsInputFilter
import ua.gov.diia.core.util.filter.MoneyValueFilter
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.adapters.binding.srcFromRes
import ua.gov.diia.ui_base.views.common.MaskedEditText
import ua.gov.diia.ui_base.views.common.MaskedInputField

class DiiaCardInputField @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defAttrStyle: Int = 0
) : ConstraintLayout(context, attr, defAttrStyle) {

    enum class FieldMode(val v: Int) {
        BUTTON(0),
        EDITABLE(1),
        EDITABLE_PHONE_NUMBER(2),
        MASKED(3)
    }

    enum class FieldInputMode(val mode: Int) {
        SINGLE_LINE(0),
        MULTI_LINE(1),
        PHONE_NUMBER(2),
        PRICE(3),
        DECIMAL(4),
        EMAIL(5)
    }

    enum class FieldTextGravityMode(val mode: Int) {
        START(0),
        CENTER(1),
        END(2)
    }

    private val title: TextView
    private val text: TextView
    private val errorText: TextView
    private val attentionText: TextView
    private val inputField: EditText
    private val textCounter: TextView
    private val inputPhoneNumberField: MaskedEditText
    private val maskedInputField: MaskedInputField
    private val arrow: AppCompatImageView
    private val selectableBackGround: View
    private val delimiter: View
    private val descriptionTv: TextView

    private var currentFieldMode: FieldMode = FieldMode.BUTTON
    private var currentFieldInputMode: FieldInputMode = FieldInputMode.MULTI_LINE
    private var currentFieldTextGravityMode: FieldTextGravityMode = FieldTextGravityMode.START
    private val textCounterPattern = context.getString(R.string.text_length_counter)
    private var counterTextWatcher: TextWatcher? = null
    private var capitalize = false

    private var offset = 0

    init {
        inflate(context, R.layout.view_input_field, this)

        title = findViewById(R.id.title)
        inputField = findViewById(R.id.inputField)
        inputPhoneNumberField = findViewById(R.id.inputFieldPhone)
        maskedInputField = findViewById(R.id.ifMasked)
        arrow = findViewById(R.id.arrow)
        text = findViewById(R.id.text)
        textCounter = findViewById(R.id.textCounter)
        errorText = findViewById(R.id.textError)
        attentionText = findViewById(R.id.textAttention)
        selectableBackGround = findViewById(R.id.selectable)
        delimiter = findViewById(R.id.delimiter)
        descriptionTv = findViewById(R.id.description)

        setOnClickListener {
            when (currentFieldMode) {
                FieldMode.BUTTON -> selectableBackGround.performClick()
                FieldMode.EDITABLE -> inputField.requestFocus()
                FieldMode.EDITABLE_PHONE_NUMBER -> inputPhoneNumberField.requestFocus()
                FieldMode.MASKED -> maskedInputField.requestFocus()
            }
        }

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.DiiaInputField,
            defAttrStyle,
            0
        ).apply {
            try {
                offset = getIndex(R.styleable.DiiaInputField_amountOffset)
                capitalize = getBoolean(R.styleable.DiiaInputField_fieldCapitalize, capitalize)

                getInt(R.styleable.DiiaInputField_fieldInputMode, 0)
                    .run(::setFieldInputMode)

                getInt(R.styleable.DiiaInputField_fieldMode, 0)
                    .run(::setFieldMode)

                getBoolean(R.styleable.DiiaInputField_fieldError, false)
                    .run(::setFieldError)
                getBoolean(R.styleable.DiiaInputField_fieldErrorWhenFocusChanged, false)
                    .run(::setFieldError)

                getString(R.styleable.DiiaInputField_fieldErrorText)
                    .run(::setFieldErrorText)

                getBoolean(R.styleable.DiiaInputField_fieldAttention, false)
                    .run(::setFieldAttention)

                getString(R.styleable.DiiaInputField_fieldAttentionText)
                    .run(::setFieldAttentionText)

                getResourceId(R.styleable.DiiaInputField_fieldTextSize, R.dimen.lm)
                    .run(::setFieldTextSize)

                getResourceId(
                    R.styleable.DiiaInputField_fieldTitleSize,
                    R.dimen.diia_card_input_field_text_size
                )
                    .run(::setFieldTitleSize)

                getString(R.styleable.DiiaInputField_fieldText)
                    .run(::setFieldText)

                getString(R.styleable.DiiaInputField_fieldHint)
                    .run(::setFieldHint)

                getString(R.styleable.DiiaInputField_fieldMask)
                    .run(::setFieldMask)

                getString(R.styleable.DiiaInputField_fieldTitle)
                    .run(::setFieldTitle)

                getResourceId(R.styleable.DiiaInputField_fieldTitleColor, R.color.text_clickable)
                    .run(::setTitleColor)

                getBoolean(R.styleable.DiiaInputField_fieldTitleVisible, true)
                    .run(::setFieldTitleVisible)

                getBoolean(R.styleable.DiiaInputField_fieldForceHideArrow, false)
                    .run(::setFieldForceHideArrow)

                getInt(R.styleable.DiiaInputField_fieldMaxLength, -1)
                    .run(::setFieldMaxLength)

                getString(R.styleable.DiiaInputField_fieldAllowedSymbols)
                    .run(::setFieldAllowedSymbols)

                getInt(R.styleable.DiiaInputField_fieldTextGravity, 0)
                    .run(::setFieldTextGravityMode)

                getBoolean(R.styleable.DiiaInputField_fieldDelimiterGone, false)
                    .run(::setFieldTextDelimiterGone)

                getBoolean(R.styleable.DiiaInputField_saveStateEnabled, true)
                    .run(::setSaveStateEnabled)

                getString(R.styleable.DiiaInputField_description)
                    .run(::setDescription)

                setFieldCounter(
                    getBoolean(R.styleable.DiiaInputField_fieldCounterEnabled, false),
                    getInt(R.styleable.DiiaInputField_fieldCounterMaxLength, -1)
                )
            } finally {
                recycle()
            }
        }

        inputPhoneNumberField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                context.serviceInput?.hideSoftInputFromWindow(windowToken, 0)
            }
            false
        }
    }

    fun setSaveStateEnabled(isEnabled: Boolean?) {
        inputField.isSaveEnabled = isEnabled == true
        maskedInputField.isSaveEnabled = isEnabled == true
        inputPhoneNumberField.isSaveEnabled = isEnabled == true
    }

    fun setFieldMaxLength(length: Int?) {
        if (length != null && length > 1) {
            inputField.filters = arrayOf<InputFilter>(LengthFilter(length))
        }
    }

    private fun setFieldAllowedSymbols(string: String?) {
        string?.also {
            val allowedSymbolsFilter =
                InputFilter { source, start, end, dest, dstart, dend ->
                    var keepOriginal = true
                    var filtered = String(charArrayOf())
                    for (i in start until end) {
                        val character = source[i]
                        if (it.contains(character)) {
                            setFieldAttention(false)
                            filtered += character
                        } else {
                            keepOriginal = false
                            setFieldAttention(true)
                        }
                    }
                    return@InputFilter if (keepOriginal) {
                        null
                    } else {
                        filtered
                    }
                }
            inputField.filters += arrayOf(allowedSymbolsFilter)
        }
    }

    fun getCurrentFieldMode(): FieldMode = currentFieldMode

    fun setFieldText(string: String?) {
        text.text = string
        inputField.setText(string)
        maskedInputField.setText(string)
    }

    fun getFieldText(): String? {
        return inputField.text?.toString()
    }

    fun setFieldText(@StringRes res: Int?) {
        if (res != null && res != 0) {
            text.setText(res)
            inputField.setText(res)
        }
    }

    fun setFieldHint(string: String?) {
        text.hint = string
        inputField.hint = string
        maskedInputField.hint = string
    }

    fun setFieldMask(mask: String?) {
        mask?.let {
            maskedInputField.setMask(it)
        }
    }

    fun setFieldHint(@StringRes res: Int?) {
        if (res != null && res != 0) {
            text.setHint(res)
            inputField.setHint(res)
        }
    }

    fun setInputFieldTextColor(@ColorRes res: Int?) {
        res.validateResource {
            inputField.setTextColor(it)
        }
    }

    fun setFieldTitle(string: String?) {
        title.text = string
    }

    fun setTitleColor(@ColorRes res: Int?) {
        res.validateResource { colorRes ->
            val resolvedColor = context.getColorCompat(colorRes)
            title.setTextColor(resolvedColor)
        }
    }

    fun setFieldTitle(@StringRes res: Int?) {
        if (res != null && res != 0) {
            title.setText(res)
        }
    }

    fun setFieldErrorText(string: String?) {
        errorText.text = string
    }

    fun setFieldErrorText(@StringRes res: Int?) {
        if (res != null && res != 0) {
            errorText.setText(res)
        }
    }

    fun setFieldAttentionText(string: String?) {
        attentionText.text = string
    }

    fun setFieldAttentionText(@StringRes res: Int?) {
        if (res != null && res != 0) {
            attentionText.setText(res)
        }
    }

    fun setFieldForceHideArrow(show: Boolean) {
        if (currentFieldMode == FieldMode.BUTTON) {
            arrow.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    fun setFieldError(enable: Boolean?) {
        //Show error text
        errorText.visibility = if (enable == true) View.VISIBLE else View.GONE

        //Adjust color schema
        @ColorInt val textColor = (if (enable == true) R.color.state_rejected else R.color.black)
            .run(context::getColorCompat)

        @ColorInt val delimiterColor =
            (if (enable == true) R.color.state_rejected else R.color.black_alpha_30)
                .run(context::getColorCompat)

        delimiter.setBackgroundColor(delimiterColor)

        when (currentFieldMode) {
            FieldMode.BUTTON -> text.setTextColor(textColor)
            FieldMode.EDITABLE -> inputField.setTextColor(textColor)
            FieldMode.EDITABLE_PHONE_NUMBER -> inputPhoneNumberField.setTextColor(textColor)
            FieldMode.MASKED -> maskedInputField.setTextColor(textColor)
        }
    }

    fun setFieldErrorWhenFocusChanged(enable: Boolean?) {
        inputField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                errorText.isVisible = enable == true
                @ColorInt val textColor =
                    (if (enable == true) R.color.state_rejected else R.color.black)
                        .run(context::getColorCompat)

                @ColorInt val delimiterColor =
                    (if (enable == true) R.color.state_rejected else R.color.black_alpha_30)
                        .run(context::getColorCompat)

                delimiter.setBackgroundColor(delimiterColor)

                when (currentFieldMode) {
                    FieldMode.BUTTON -> text.setTextColor(textColor)
                    FieldMode.EDITABLE -> inputField.setTextColor(textColor)
                    FieldMode.EDITABLE_PHONE_NUMBER -> inputPhoneNumberField.setTextColor(textColor)
                    FieldMode.MASKED -> maskedInputField.setTextColor(textColor)
                }
            }
        }
    }

    fun setFieldAttention(enable: Boolean?) {
        attentionText.visibility = if (enable == true) View.VISIBLE else View.GONE
    }

    fun setFieldMode(value: Int?) {
        currentFieldMode = fieldModeFromInt(value ?: 0)
        maskedInputField.setCurrentFieldMode(currentFieldMode.v) //
        when (currentFieldMode) {
            FieldMode.EDITABLE -> {
                arrow.visibility = GONE
                selectableBackGround.visibility = GONE
                text.visibility = GONE
                inputField.visibility = VISIBLE
                inputPhoneNumberField.visibility = GONE
                maskedInputField.visibility = GONE
            }

            FieldMode.EDITABLE_PHONE_NUMBER -> {
                arrow.visibility = GONE
                selectableBackGround.visibility = GONE
                text.visibility = GONE
                inputField.visibility = GONE
                inputPhoneNumberField.visibility = VISIBLE
                maskedInputField.visibility = GONE
            }

            FieldMode.BUTTON -> {
                arrow.visibility = VISIBLE
                selectableBackGround.visibility = VISIBLE
                text.visibility = VISIBLE
                inputField.visibility = GONE
                inputPhoneNumberField.visibility = GONE
                maskedInputField.visibility = GONE
            }

            FieldMode.MASKED -> {
                arrow.visibility = GONE
                selectableBackGround.visibility = GONE
                text.visibility = GONE
                inputField.visibility = GONE
                inputPhoneNumberField.visibility = GONE
                maskedInputField.visibility = VISIBLE
            }
        }
    }

    fun setFieldTextSize(@DimenRes size: Int?) {
        if (size != null && size != -1) {
            when (currentFieldMode) {
                FieldMode.BUTTON -> text.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(size)
                )

                FieldMode.EDITABLE -> inputField.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(size)
                )

                FieldMode.EDITABLE_PHONE_NUMBER -> inputPhoneNumberField.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(size)
                )

                FieldMode.MASKED -> maskedInputField.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(size)
                )
            }
        }
    }

    fun setFieldTitleSize(@DimenRes size: Int?) {
        if (size != null && size != -1) {
            title.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(size)
            )
        }
    }

    private fun fieldModeFromInt(v: Int): FieldMode = when (v) {
        FieldMode.BUTTON.v -> FieldMode.BUTTON
        FieldMode.EDITABLE_PHONE_NUMBER.v -> FieldMode.EDITABLE_PHONE_NUMBER
        FieldMode.MASKED.v -> FieldMode.MASKED
        else -> FieldMode.EDITABLE
    }

    fun setFieldTitleVisible(visible: Boolean?) {
        if (visible == null) return
        title.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setFieldInputMode(value: Int?) {
        currentFieldInputMode = fieldInputModeFromInt(value ?: 0)

        when (currentFieldInputMode) {
            FieldInputMode.MULTI_LINE -> {
                inputField.apply {
                    isSingleLine = false
                    imeOptions = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                }
            }

            FieldInputMode.SINGLE_LINE -> {
                inputField.apply {
                    isSingleLine = true
                    imeOptions = InputType.TYPE_CLASS_TEXT
                }
            }

            FieldInputMode.PHONE_NUMBER -> {
            }

            FieldInputMode.PRICE -> {
                inputField.apply {
                    isSingleLine = false
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                }
                inputField.filters = arrayOf<InputFilter>(MoneyValueFilter(digits = offset))
            }

            FieldInputMode.DECIMAL -> {
                inputField.apply {
                    isSingleLine = false
                    inputType = EditorInfo.TYPE_CLASS_NUMBER + EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
                    keyListener = DigitsKeyListener.getInstance("0123456789.,")
                    inputField.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 2))
                    doAfterTextChanged {
                        keyListener =
                            if (inputField.text.contains(".") || inputField.text.contains(",")) {
                                DigitsKeyListener.getInstance("0123456789")
                            } else {
                                DigitsKeyListener.getInstance("0123456789.,")
                            }
                    }
                }
            }

            FieldInputMode.EMAIL -> {
                inputField.apply {
                    isSingleLine = true
                    imeOptions = InputType.TYPE_CLASS_TEXT
                    imeOptions = EditorInfo.IME_ACTION_DONE
                }
            }
        }
        applyCapitalizationFlag()
    }

    private fun fieldInputModeFromInt(v: Int): FieldInputMode = when (v) {
        FieldInputMode.SINGLE_LINE.mode -> FieldInputMode.SINGLE_LINE
        FieldInputMode.PHONE_NUMBER.mode -> FieldInputMode.PHONE_NUMBER
        FieldInputMode.PRICE.mode -> FieldInputMode.PRICE
        FieldInputMode.DECIMAL.mode -> FieldInputMode.DECIMAL
        FieldInputMode.EMAIL.mode -> FieldInputMode.EMAIL
        else -> FieldInputMode.MULTI_LINE
    }

    fun setSelectableClickListener(listener: () -> Unit) {
        selectableBackGround.setOnClickListener { listener.invoke() }
    }

    fun setCursorToEnd() {
        inputField.setSelection(inputField.length())
    }

    fun setClearFocus() {
        inputField.clearFocus()
    }

    fun setArrowButtonVisible(visible: Boolean?) {
        arrow.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    fun setInputTextAllCaps(allCaps: Boolean) {
        inputField.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
    }

    fun setInputPaddingBottom() {
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f, resources
                .displayMetrics
        ).toInt()
        inputField.setPadding(0, 0, 0, padding)
    }


    private fun setFieldCounter(isEnabled: Boolean, maxLength: Int) {
        counterTextWatcher?.let(inputField::removeTextChangedListener)
        if (isEnabled && maxLength > 0) {
            setFieldMaxLength(maxLength)
            counterTextWatcher = inputField.doAfterTextChanged {
                val length = it?.length ?: 0
                textCounter.isVisible = true
                textCounter.text = textCounterPattern.format(
                    (maxLength - length).coerceAtLeast(0)
                )
            }.also { it.afterTextChanged(inputField.text) }
        } else {
            textCounter.isVisible = false
        }
    }

    private fun applyCapitalizationFlag() {
        inputField.inputType = if (capitalize) {
            inputField.inputType or EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES
        } else {
            inputField.inputType and EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES.inv()
        }
    }

    private fun setFieldTextGravityMode(value: Int?) {
        currentFieldTextGravityMode = fieldTextGravityModeFromInt(value ?: 0)

        when (currentFieldTextGravityMode) {
            FieldTextGravityMode.START -> {
                inputField.gravity = Gravity.START
            }

            FieldTextGravityMode.CENTER -> {
                inputField.gravity = Gravity.CENTER
            }

            FieldTextGravityMode.END -> {
                inputField.gravity = Gravity.END
            }
        }
    }

    fun setIsSelectionEnabled(enabled: Boolean) {
        selectableBackGround.isEnabled = enabled == true
    }

    private fun fieldTextGravityModeFromInt(v: Int): FieldTextGravityMode = when (v) {
        FieldTextGravityMode.START.mode -> FieldTextGravityMode.START
        FieldTextGravityMode.CENTER.mode -> FieldTextGravityMode.CENTER
        else -> FieldTextGravityMode.END
    }

    private fun setFieldTextDelimiterGone(visibility: Boolean?) {
        if (visibility == null) return
        if (visibility) {
            delimiter.visibility = View.GONE
        } else {
            delimiter.visibility = View.VISIBLE
        }
    }

    fun setIsEnabled(enabled: Boolean?) {
        inputField.isEnabled = enabled == true
        maskedInputField.isEnabled = enabled == true
        selectableBackGround.isEnabled = enabled == true

        @ColorInt val textColor = (if (enabled == true) R.color.black else R.color.black_alpha_30)
            .run(context::getColorCompat)
        title.setTextColor(textColor)
        inputField.setTextColor(textColor)
        maskedInputField.setTextColor(textColor)
        text.setTextColor(textColor)
        if (enabled == true) arrow.srcFromRes(R.drawable.ic_arrow) else arrow.srcFromRes(R.drawable.ic_arrow_disabled)
    }

    fun setFieldClickable(clickable: Boolean?) {
        inputField.isEnabled = clickable == true
        maskedInputField.isEnabled = clickable == true
        selectableBackGround.isEnabled = clickable == true

        @ColorInt val textColor = (if (clickable == true) R.color.black else R.color.black_alpha_30)
            .run(context::getColorCompat)
        inputField.setTextColor(textColor)
        maskedInputField.setTextColor(textColor)
        text.setTextColor(textColor)
        if (clickable == true) arrow.srcFromRes(R.drawable.ic_arrow) else arrow.srcFromRes(R.drawable.ic_arrow_disabled)
    }

    fun doOnTextChanged(action: (String) -> Unit) {
        inputField.doOnTextChanged { text, _, _, _ ->
            action(text?.toString().orEmpty())
        }
        maskedInputField.doOnTextChanged { text, _, _, _ ->
            action(text?.toString().orEmpty())
        }
    }

    fun setImeOptions(imeOptions: Int) {
        inputField.imeOptions = imeOptions
    }

    fun setTextInputType(type: Int) {
        inputField.inputType = type
    }

    fun setTextInputSingleLine(single: Boolean) {
        inputField.isSingleLine = single
    }

    fun setDescription(description: String?) {
        descriptionTv.isVisible = description != null
        descriptionTv.text = description
    }

    fun configureDelimiter() {
        delimiter.setBackgroundColor(context.getColor(R.color.black_alpha_30))
        inputField.setOnFocusChangeListener { _, hasFocus ->
            delimiter.setBackgroundColor(
                context.getColor(
                    if (hasFocus) R.color.black else R.color.black_alpha_30
                )
            )
        }
    }
}

@BindingAdapter("fieldCleanUp")
fun DiiaCardInputField.cleanUpText(event: UiEvent?) {
    event?.notHandedYet?.let { notHandled ->
        if (notHandled) {
            event.handle()
            setFieldText(string = null)
        }
    }
}

@BindingAdapter("isEnabledField")
fun DiiaCardInputField.setEnabledField(enabled: Boolean?) {
    setIsSelectionEnabled(enabled ?: true)
}

@BindingAdapter("arrowButtonVisible")
fun DiiaCardInputField.setArrowButtonVisible(visible: Boolean?) {
    setArrowButtonVisible(visible)
}

@BindingAdapter("setInputTextAllCaps")
fun DiiaCardInputField.setInputTextCaps(allCaps: Boolean) {
    setInputTextAllCaps(allCaps)
}

@BindingAdapter("fieldTextTwoWay")
fun DiiaCardInputField.setFieldTextTwoWay(string: String?) {
    when (getCurrentFieldMode()) {
        DiiaCardInputField.FieldMode.EDITABLE_PHONE_NUMBER -> {
            val inputField = findViewById<MaskedEditText>(R.id.inputFieldPhone)
            val oldValue = inputField.getRawText()
            if (string != null && string != oldValue) {
                inputField.setText(string)
            }
        }

        DiiaCardInputField.FieldMode.MASKED -> {
            val inputField = findViewById<MaskedInputField>(R.id.ifMasked)
            val oldValue = inputField.getRawText()
            if (string != null && string != oldValue) {
                inputField.setText(string)
            }
        }

        else -> {
            val inputField = findViewById<EditText>(R.id.inputField)
            val oldValue = inputField.text?.toString()
            if (string != null && oldValue != string) {
                inputField.setText(string)
            }
            if (!oldValue.isNullOrBlank() && string == null) {
                inputField.text = string
            }
        }
    }
}

@BindingAdapter("fieldTextPriceWithSpaces")
fun DiiaCardInputField.setFieldTextPriceWithSpaces(string: String?) {
    val inputField = findViewById<EditText>(R.id.inputField)
    val oldValue = inputField.text?.toString()
    if (!string.isNullOrBlank() && oldValue != string) {
        inputField.setText(string.toPrice().formatWithSpaces())
        inputField.setSelection(inputField.length())
    }
}

@InverseBindingAdapter(attribute = "fieldTextPriceWithSpaces")
fun DiiaCardInputField.getFieldTextPriceWithSpaces(): String? {

    val value = findViewById<EditText>(R.id.inputField).text?.toString()
    val formattedValue = value?.toPrice().formatWithSpaces()

    return if (value.isNullOrBlank()) null else formattedValue
}

@InverseBindingAdapter(attribute = "fieldTextTwoWay")
fun DiiaCardInputField.getFieldTextTwoWay(): String? {

    val value = when (getCurrentFieldMode()) {
        DiiaCardInputField.FieldMode.EDITABLE_PHONE_NUMBER -> findViewById<MaskedEditText>(R.id.inputFieldPhone)?.getRawText()
        DiiaCardInputField.FieldMode.MASKED -> findViewById<MaskedInputField>(R.id.ifMasked)?.getRawText()
        else -> findViewById<EditText>(R.id.inputField).text?.toString()
    }

    return if (value.isNullOrBlank()) null else value
}

@BindingAdapter(value = ["fieldTextTwoWayAttrChanged"])
fun DiiaCardInputField.setTextWatcher(textAttrChanged: InverseBindingListener?) {

    val inputFiled = when (getCurrentFieldMode()) {
        DiiaCardInputField.FieldMode.EDITABLE_PHONE_NUMBER -> findViewById<MaskedEditText>(R.id.inputFieldPhone)
        DiiaCardInputField.FieldMode.MASKED -> findViewById<MaskedInputField>(R.id.ifMasked)
        else -> findViewById<EditText>(R.id.inputField)
    }

    val newValue: TextWatcher? = if (textAttrChanged == null) {
        null
    } else {
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

                textAttrChanged.onChange()
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
    }

    val oldValue = androidx.databinding.adapters.ListenerUtil.trackListener<TextWatcher?>(
        this,
        newValue,
        R.id.textWatcher
    )

    if (oldValue != null) inputFiled.removeTextChangedListener(oldValue)
    if (newValue != null) inputFiled.addTextChangedListener(newValue)
}

@BindingAdapter(value = ["fieldTextPriceWithSpacesAttrChanged"])
fun DiiaCardInputField.setTextPriceWithSpacesWatcher(textAttrChanged: InverseBindingListener?) {

    val inputFiled = findViewById<EditText>(R.id.inputField)

    val newValue: TextWatcher? = if (textAttrChanged == null) {
        null
    } else {
        object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

                textAttrChanged.onChange()
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
    }

    val oldValue = androidx.databinding.adapters.ListenerUtil.trackListener<TextWatcher?>(
        this,
        newValue,
        R.id.textWatcher
    )

    if (oldValue != null) inputFiled.removeTextChangedListener(oldValue)
    if (newValue != null) inputFiled.addTextChangedListener(newValue)
}