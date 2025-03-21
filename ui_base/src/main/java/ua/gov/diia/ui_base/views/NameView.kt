package ua.gov.diia.ui_base.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import ua.gov.diia.core.util.inputs.isPersonNameValid
import java.lang.Integer.max
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.databinding.ItemViewNameBinding
import ua.gov.diia.ui_base.databinding.ViewNameBinding
import ua.gov.diia.ui_base.util.view.inflater
import ua.gov.diia.ui_base.util.view.setEnabledRecursive

class NameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var onAddItem: (() -> Unit)? = null
    private var onRemove: ((NameModel) -> Unit)? = null
    private var onChanged: ((NameModel) -> Unit)? = null
    private var onSelect: ((NameModel) -> Unit)? = null

    private val viewHolderList = mutableListOf<ViewHolder>()

    private val binding = ViewNameBinding.inflate(inflater, this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.NameView) {
            setActionName(getString(R.styleable.NameView_actionName))
        }

        orientation = VERTICAL
        setBackgroundResource(R.drawable.bg_radius_8)
        binding.addBtn.setOnClickListener {
            onAddItem?.invoke()
        }
    }

    fun setActionName(text: String?) {
        binding.addTv.text = text
    }

    fun onAddItem(onAddItem: () -> Unit) {
        this.onAddItem = onAddItem
    }

    fun onRemove(onRemove: (NameModel) -> Unit) {
        this.onRemove = onRemove
    }

    fun onChanged(onChanged: (NameModel) -> Unit) {
        this.onChanged = onChanged
    }

    fun onSelect(onSelect: (NameModel) -> Unit) {
        this.onSelect = onSelect
    }

    fun setData(list: List<NameModel>?) {
        val max = max(list?.size ?: 0, viewHolderList.size)
        val viewHoldersToRemove = mutableListOf<ViewHolder>()
        val viewHoldersToAdd = mutableListOf<ViewHolder>()
        for (i in 0 until max) {
            val item = list?.getOrNull(i)
            val holder = viewHolderList.getOrNull(i)
            if (item != null && holder != null) {
                holder.bind(item)
            } else if (item == null) {
                viewHoldersToRemove.add(holder!!)
            } else {
                val newHolder = ViewHolder(
                    binding = ItemViewNameBinding.inflate(inflater, binding.itemsContainer, true)
                )
                viewHoldersToAdd.add(newHolder)
                newHolder.bind(item)
            }
        }

        viewHoldersToRemove.forEach {
            binding.itemsContainer.removeView(it.binding.root)
            viewHolderList.remove(it)
        }
        viewHolderList.addAll(viewHoldersToAdd)

        binding.content.isVisible = !list.isNullOrEmpty()
    }

    fun setAddButtonEnabled(isEnabled: Boolean?) {
        binding.addBtn.setEnabledRecursive(isEnabled == true)
    }

    fun setAddButtonVisibility(isVisible: Boolean?) {
        binding.addBtn.isVisible = isVisible == true
    }

    fun isNameSymbolsValid(name: String): Boolean {
        return name.isEmpty() || isPersonNameValid(name)
    }

    inner class ViewHolder(
        val binding: ItemViewNameBinding,
    ) {
        private var model: NameModel? = null

        init {
            binding.nameInput.setFieldErrorText(R.string.ukraine_validation_input_error)
            binding.nameInput.setTextInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
            binding.nameInput.setImeOptions(EditorInfo.IME_ACTION_DONE)
            binding.nameInput.setSelectableClickListener {
                val name = model ?: return@setSelectableClickListener
                onSelect?.invoke(name)
            }
            binding.nameInput.doOnTextChanged { text ->
                val name = model ?: return@doOnTextChanged
                val isNameSymbolsValid = isNameSymbolsValid(text)
                binding.nameInput.setFieldError(!isNameSymbolsValid)
                onChanged?.invoke(name.copy(name = text, isValid = isNameSymbolsValid))
            }
            binding.deleteBtn.setOnClickListener {
                val name = model ?: return@setOnClickListener
                onRemove?.invoke(name)
            }
        }

        fun bind(model: NameModel) {
            this.model = model
            binding.deleteBtn.isVisible = model.withRemove
            if (binding.nameInput.getFieldText() != model.name) {
                binding.nameInput.setFieldText(model.name)
            }
            binding.nameInput.setFieldTitle(model.title)
            binding.nameInput.setFieldHint(model.hint)
            binding.nameInput.setFieldMode(model.fieldMode.v)
        }
    }
}

@BindingAdapter("setData")
fun NameView.data(list: List<NameModel>?) {
    setData(list)
}

@BindingAdapter("setAddButtonEnabled")
fun NameView.addButtonEnabled(isEnabled: Boolean?) {
    this.setAddButtonEnabled(isEnabled)
}

@BindingAdapter("withAddButton")
fun NameView.withAddButton(withAddButton: Boolean?) {
    this.setAddButtonVisibility(withAddButton)
}