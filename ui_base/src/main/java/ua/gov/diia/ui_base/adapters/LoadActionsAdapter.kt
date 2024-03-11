package ua.gov.diia.ui_base.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ui_base.databinding.ItemLoadActionBinding
import ua.gov.diia.core.models.common.LoadActionData
import ua.gov.diia.ui_base.util.view.inflater

class LoadActionsAdapter(
    private val onItemClick: (LoadActionData) -> Unit
) : ListAdapter<LoadActionData, LoadActionsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            binding = ItemLoadActionBinding.inflate(parent.inflater, parent, false),
            onItemClick = {
                onItemClick(getItem(it))
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).run(holder::bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach { list ->
                @Suppress("UNCHECKED_CAST")
                (list as List<String>).forEach { payload ->
                    if (payload == DiffCallback.PAYLOAD_IS_LOADING) {
                        holder.setLoading(getItem(position).isLoading)
                    } else if (payload == DiffCallback.PAYLOAD_IS_ENABLED) {
                        holder.setEnabled(getItem(position).isEnabled)
                    }
                }
            }
        }
    }

    class ViewHolder(
        private val binding: ItemLoadActionBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(action: LoadActionData) {
            with(binding) {
                data = action
                setLoading(action.isLoading)
                setEnabled(action.isEnabled)
                executePendingBindings()
            }
        }

        fun setLoading(isLoading: Boolean) {
            binding.btn.setIsLoading(isLoading)
        }

        fun setEnabled(isEnabled: Boolean) {
            binding.btn.setIsEnabled(isEnabled)
            binding.root.isEnabled = isEnabled
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<LoadActionData>() {
        override fun areItemsTheSame(oldItem: LoadActionData, newItem: LoadActionData): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: LoadActionData, newItem: LoadActionData): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: LoadActionData, newItem: LoadActionData): Any {
            val payloads = mutableListOf<String>()
            if (oldItem.isLoading != newItem.isLoading) {
                payloads.add(PAYLOAD_IS_LOADING)
            }
            if (oldItem.isEnabled != newItem.isEnabled) {
                payloads.add(PAYLOAD_IS_ENABLED)
            }
            return payloads
        }

        const val PAYLOAD_IS_LOADING = "PAYLOAD_IS_LOADING"
        const val PAYLOAD_IS_ENABLED = "PAYLOAD_IS_ENABLED"
    }
}
