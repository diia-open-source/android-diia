package ua.gov.diia.search.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

        fun bind(title: String) {
            with(binding) {
                tvText.text = title
            }
        }
    }

    class SearchItemDoubleLineVH(
        private val binding: ItemRvSearchTwoLinesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SearchableItemDoubleLine) {
            with(binding) {
                item = data
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
                holder.itemView.setOnClickListener { onItemClick(item.getQueryString()) }
            }
            is SearchableItem -> {
                val typedVH = holder as SearchItemSingleLineVH
                typedVH.bind(title = item.getDisplayTitle())
                holder.itemView.setOnClickListener { onItemClick(item.getQueryString()) }
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
