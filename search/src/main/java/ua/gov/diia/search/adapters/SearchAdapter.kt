package ua.gov.diia.search.adapters

import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.search.R
import ua.gov.diia.search.databinding.ItemRvSearchBinding
import ua.gov.diia.search.databinding.ItemRvSearchTwoLinesBinding
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.search.models.SearchableItemDoubleLine
import ua.gov.diia.ui_base.util.view.inflater

class SearchAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<SearchableItem, RecyclerView.ViewHolder>(DiffCallback) {

    private companion object {
        const val SINGLE_LINE_ITEM = 1
        const val DOUBLE_LINE_ITEM = 2
    }

    class SearchItemSingleLineVH(
        private val binding: ItemRvSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String, disabled: Boolean?) {
            with(binding) {
                tvText.text = title
                @ColorRes val titleColorRes = if (disabled != null && disabled) R.color.black_alpha_30 else R.color.black
                tvText.setTextColor(ContextCompat.getColor(itemView.context, titleColorRes))
            }
        }
    }

    class SearchItemDoubleLineVH(
        private val binding: ItemRvSearchTwoLinesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SearchableItemDoubleLine) {
            with(binding) {
                item = data
                @ColorRes val titleColorRes = if (data.isDisabled()) R.color.black_alpha_30 else R.color.black
                title.setTextColor(ContextCompat.getColor(itemView.context, titleColorRes))
                text.setTextColor(ContextCompat.getColor(itemView.context, titleColorRes))
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SearchableItemDoubleLine -> DOUBLE_LINE_ITEM
        else -> SINGLE_LINE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            DOUBLE_LINE_ITEM -> SearchItemDoubleLineVH(
                ItemRvSearchTwoLinesBinding.inflate(parent.inflater, parent, false)
            )
            else -> SearchItemSingleLineVH(
                ItemRvSearchBinding.inflate(parent.inflater, parent, false)
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SearchableItemDoubleLine -> {
                (holder as SearchItemDoubleLineVH).bind(item)
                if (!item.isDisabled()) {
                    holder.itemView.setOnClickListener { onItemClick(item.getQueryString()) }
                } else {
                    holder.itemView.setOnClickListener(null)
                }
            }

            is SearchableItem -> {
                val typedVH = holder as SearchItemSingleLineVH
                typedVH.bind(title = item.getDisplayTitle(), item.isDisabled())
                if (!item.isDisabled()) {
                    holder.itemView.setOnClickListener {
                        onItemClick(item.getQueryString())
                    }
                } else {
                    holder.itemView.setOnClickListener(null)
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<SearchableItem>() {

        override fun areItemsTheSame(oldItem: SearchableItem, newItem: SearchableItem): Boolean {
            return oldItem.getQueryString() == newItem.getQueryString()
        }

        override fun areContentsTheSame(oldItem: SearchableItem, newItem: SearchableItem): Boolean {
            return oldItem.getQueryString() == newItem.getQueryString()
        }
    }

}
