package ua.gov.diia.ui_base.views.common

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import ua.gov.diia.ui_base.R
import java.lang.RuntimeException
import java.lang.StringBuilder

class MaskedInputField : AppCompatEditText, TextWatcher {
    private var mask: String? = null
    private var charRepresentation = 0.toChar()
    private var keepHint = false
    private var rawToMask: IntArray = intArrayOf()
    private var rawText: RawTextMask? = null
    private var editingBefore = false
    private var editingOnChanged = false
    private var editingAfter = false
    private var maskToRaw: IntArray = intArrayOf()
    private var selectionPostion = 0
    private var initialized = false
    private var ignore = false
    private var maxRawLength = 0
    private var lastValidMaskPosition = 0
    private var selectionChanged = false
    private var focusChangeListener: OnFocusChangeListener? = null
    private var allowedChars: String? = null
    private var deniedChars: String? = null
    private var maskHint: String? = "ддммрррр"
    private var currentFieldMode: Int = 0

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.MaskedInputField)
        maskHint = attributes.getString(R.styleable.MaskedInputField_addressMaskHint)
        mask = attributes.getString(R.styleable.MaskedInputField_addressMask)
        allowedChars = attributes.getString(R.styleable.MaskedInputField_address_allowed_chars)
        deniedChars = attributes.getString(R.styleable.MaskedInputField_address_denied_chars)
        val representation = attributes.getString(R.styleable.MaskedInputField_address_char_representation)
        charRepresentation = if (representation == null) {
            '#'
        } else {
            representation[0]
        }
        keepHint = attributes.getBoolean(R.styleable.MaskedInputField_address_keep_hint, false)
        cleanUp()

        // Ignoring enter key presses
        setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                else -> true
            }
        }
        attributes.recycle()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superParcelable = super.onSaveInstanceState()
        val state = Bundle()
        state.putParcelable("super", superParcelable)
        state.putString("text", getRawText())
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        super.onRestoreInstanceState(state.getParcelable("super"))
        val text = bundle.getString("text")
        setText(text)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }


    /** @param listener - its onFocusChange() method will be called before performing MaskedEditText operations,
     * related to this event.
     */
    override fun setOnFocusChangeListener(listener: OnFocusChangeListener?) {
        focusChangeListener = listener
    }

    private fun cleanUp() {
        initialized = false
        generatePositionArrays()
        rawText = RawTextMask()
        selectionPostion = rawToMask[0]
        editingBefore = true
        editingOnChanged = true
        editingAfter = true
        this.setText(makeMaskedText())

        editingBefore = false
        editingOnChanged = false
        editingAfter = false
        maxRawLength = maskToRaw[previousValidPosition(mask!!.length - 1)] + 1
        lastValidMaskPosition = findLastValidMaskPosition()
        initialized = true
        super.setOnFocusChangeListener { v, hasFocus ->
            if (focusChangeListener != null) {
                focusChangeListener!!.onFocusChange(v, hasFocus)
            }
            if (hasFocus()) {
                selectionChanged = false
                this@MaskedInputField.setSelection(lastValidPosition())
            }
        }
    }

    private fun findLastValidMaskPosition(): Int {
        for (i in maskToRaw.indices.reversed()) {
            if (maskToRaw[i] != -1) return i
        }
        throw RuntimeException("Mask must contain at least one representation char")
    }

    private fun hasHint(): Boolean {
        return maskHint != null
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
        init()
    }

    fun setCurrentFieldMode(mode: Int?) {
        this.currentFieldMode = mode ?: 0
    }

    fun setMaskHint(maskHint: String?) {
        maskHint?.let {
            this.maskHint = maskHint
            cleanUp()
        }
    }

    fun setMask(mask: String?) {
        mask?.let{
            this.mask = it
            cleanUp()
        }
    }

    fun getMask(): String? {
        return mask
    }

    fun getRawText(): String {
        return rawText!!.text
    }

    fun setCharRepresentation(charRepresentation: Char) {
        this.charRepresentation = charRepresentation
        cleanUp()
    }

    fun getCharRepresentation(): Char {
        return charRepresentation
    }

    /**
     * Generates positions for values characters. For instance:
     * Input data: mask = "+38(###)###-##-##
     * After method execution:
     * rawToMask = [3, 4, 5, 6, 8, 9, 11, 12, 14, 15]
     * maskToRaw = [-1, -1, -1, 0, 1, 2, -1, 3, 4, 5, -1, 6, 7, -1, 8, 9]
     * charsInMask = "+38()- " (and space, yes)
     */
    private fun generatePositionArrays() {
        val aux = IntArray(mask!!.length)
        maskToRaw = IntArray(mask!!.length)
        var charsInMaskAux = ""
        var charIndex = 0
        for (i in 0 until mask!!.length) {
            val currentChar = mask!![i]
            if (currentChar == charRepresentation) {
                aux[charIndex] = i
                maskToRaw[i] = charIndex++
            } else {
                val charAsString = Character.toString(currentChar)
                if (!charsInMaskAux.contains(charAsString)) {
                    charsInMaskAux = charsInMaskAux + charAsString
                }
                maskToRaw[i] = -1
            }
        }
        if (charsInMaskAux.indexOf(' ') < 0) {
            charsInMaskAux = charsInMaskAux + SPACE
        }
        val charsInMask = charsInMaskAux.toCharArray()
        rawToMask = IntArray(charIndex)
        for (i in 0 until charIndex) {
            rawToMask[i] = aux[i]
        }
    }

    private fun init() {
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
        if (!editingBefore) {
            editingBefore = true
            if (start > lastValidMaskPosition) {
                ignore = true
            }
            var rangeStart = start
            if (after == 0) {
                rangeStart = erasingStart(start)
            }
            val range = calculateRange(rangeStart, start + count)
            if (range.start != -1) {
                rawText!!.subtractFromString(range)
            }
            if (count > 0) {
                selectionPostion = previousValidPosition(start)
            }
        }
    }

    private fun erasingStart(start: Int): Int {
        var start = start
        while (start > 0 && maskToRaw[start] == -1) {
            start--
        }
        return start
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        var count = count
        if (!editingOnChanged && editingBefore) {
            editingOnChanged = true
            if (ignore) {
                return
            }
            if (count > 0) {
                val startingPosition = maskToRaw[nextValidPosition(start)]
                val addedString = s.subSequence(start, start + count).toString()
                count = rawText!!.addToString(clear(addedString), startingPosition, maxRawLength)
                if (initialized) {
                    val currentPosition: Int
                    currentPosition =
                        if (startingPosition + count < rawToMask.size) rawToMask[startingPosition + count] else lastValidMaskPosition + 1
                    selectionPostion = nextValidPosition(currentPosition)
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable) {
        if (!editingAfter && editingBefore && editingOnChanged) {
            editingAfter = true

            setText(makeMaskedText())

            selectionChanged = false
            setSelection(selectionPostion)
            editingBefore = false
            editingOnChanged = false
            editingAfter = false
            ignore = false
        }
    }

    fun isKeepHint(): Boolean {
        return keepHint
    }

    fun setKeepHint(keepHint: Boolean) {
        this.keepHint = keepHint
        setText(getRawText())
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        // On Android 4+ this method is being called more than 1 time if there is a hint in the EditText, what moves the cursor to left
        // Using the boolean var selectionChanged to limit to one execution
        var selStart = selStart
        var selEnd = selEnd
        if (initialized) {
            if (!selectionChanged) {
                selStart = fixSelection(selStart)
                selEnd = fixSelection(selEnd)

                // exactly in this order. If getText.length() == 0 then selStart will be -1
                if (selStart > text!!.length) selStart = text!!.length
                if (selStart < 0) selStart = 0

                // exactly in this order. If getText.length() == 0 then selEnd will be -1
                if (selEnd > text!!.length) selEnd = text!!.length
                if (selEnd < 0) selEnd = 0
                setSelection(selStart, selEnd)
                selectionChanged = true
            } else {
                //check to see if the current selection is outside the already entered text
                if (selStart > rawText!!.length() - 1) {
                    val start = fixSelection(selStart)
                    val end = fixSelection(selEnd)
                    if (start >= 0 && end < text!!.length) {
                        setSelection(start, end)
                    }
                }
            }
        }
        super.onSelectionChanged(selStart, selEnd)
    }

    private fun fixSelection(selection: Int): Int {
        return if (selection > lastValidPosition()) {
            lastValidPosition()
        } else {
            nextValidPosition(selection)
        }
    }

    private fun nextValidPosition(currentPosition: Int): Int {
        var currentPosition = currentPosition
        while (currentPosition < lastValidMaskPosition && maskToRaw[currentPosition] == -1) {
            currentPosition++
        }
        return if (currentPosition > lastValidMaskPosition) lastValidMaskPosition + 1 else currentPosition
    }

    private fun previousValidPosition(currentPosition: Int): Int {
        var currentPosition = currentPosition
        while (currentPosition >= 0 && maskToRaw[currentPosition] == -1) {
            currentPosition--
            if (currentPosition < 0) {
                return nextValidPosition(0)
            }
        }
        return currentPosition
    }

    private fun lastValidPosition(): Int {
        return if (rawText!!.length() == maxRawLength) {
            rawToMask[rawText!!.length() - 1] + 1
        } else nextValidPosition(rawToMask[rawText!!.length()])
    }

    private fun makeMaskedText(): String {
        val maskedTextLength: Int
        maskedTextLength = if (rawText!!.length() < rawToMask.size) {
            rawToMask[rawText!!.length()]
        } else {
            mask!!.length
        }
        val maskedText =
            CharArray(maskedTextLength) //mask.replace(charRepresentation, ' ').toCharArray();
        for (i in maskedText.indices) {
            val rawIndex = maskToRaw[i]
            if (rawIndex == -1) {
                maskedText[i] = mask!![i]
            } else {
                maskedText[i] = rawText!!.charAt(rawIndex)
            }
        }
        return String(maskedText)
    }

    private fun calculateRange(start: Int, end: Int): RangeMask {
        val range = RangeMask()
        var i = start
        while (i <= end && i < mask!!.length) {
            if (maskToRaw[i] != -1) {
                if (range.start == -1) {
                    range.start = maskToRaw[i]
                }
                range.end = maskToRaw[i]
            }
            i++
        }
        if (end == mask!!.length) {
            range.end = rawText!!.length()
        }
        if (range.start == range.end && start < end) {
            val newStart = previousValidPosition(range.start - 1)
            if (newStart < range.start) {
                range.start = newStart
            }
        }
        return range
    }

    private fun clear(string: String): String {
        var string = string
        if (deniedChars != null) {
            for (c in deniedChars!!.toCharArray()) {
                string = string.replace(Character.toString(c), "")
            }
        }
        if (allowedChars != null) {
            val builder = StringBuilder(string.length)
            for (c in string.toCharArray()) {
                if (allowedChars!!.contains(c.toString())) {
                    builder.append(c)
                }
            }
            string = builder.toString()
        }
        return string
    }

    companion object {
        const val SPACE = " "
    }
}

/**
 * Raw text, another words TextWithout mask characters
 */
class RawTextMask {
    var text = ""
        private set

    /**
     * text = 012345678, range = 123 =&gt; text = 0456789
     * @param range given range
     */
    fun subtractFromString(range: RangeMask) {
        var firstPart = ""
        var lastPart = ""
        if (range.start > 0 && range.start <= text.length) {
            firstPart = text.substring(0, range.start)
        }
        if (range.end >= 0 && range.end < text.length) {
            lastPart = text.substring(range.end, text.length)
        }
        text = firstPart + lastPart
    }

    /**
     *
     * @param newStringValue New String to be added
     * @param start Position to insert newString
     * @param maxLength Maximum raw text length
     * @return Number of added characters
     */
    fun addToString(newStringValue: String?, start: Int, maxLength: Int): Int {
        var newString = newStringValue
        var firstPart = ""
        var lastPart = ""
        if (newString == null || newString == "") {
            return 0
        } else require(start >= 0) { "Start position must be non-negative" }
        require(start <= text.length) { "Start position must be less than the actual text length" }
        var count = newString.length
        if (start > 0) {
            firstPart = text.substring(0, start)
        }
        if (start >= 0 && start < text.length) {
            lastPart = text.substring(start, text.length)
        }
        if (text.length + newString.length > maxLength) {
            count = maxLength - text.length
            newString = newString.substring(0, count)
        }
        text = firstPart + newString + lastPart
        return count
    }

    fun length(): Int {
        return text.length
    }

    fun charAt(position: Int): Char {
        return text[position]
    }
}

data class RangeMask(
    var start: Int = -1,
    var end: Int = -1
)
