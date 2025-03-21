package ua.gov.diia.ui_base.adapters.common

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ui_base.databinding.ItemPaginationListFooterBinding
import ua.gov.diia.ui_base.util.view.inflater
import javax.inject.Inject

class PaginationLoadStateAdapter @Inject constructor() :
    LoadStateAdapter<PaginationLoadStateAdapter.PaginationLoadFooterViewHolder>() {

    interface OnRetryListener {
        fun retry()
    }

    private val retryListener = object : OnRetryListener {
        override fun retry() {
            retryActionListener?.invoke()
        }

    }

    private var retryActionListener: (() -> Unit)? = null

    fun setRetryListener(listener: () -> Unit) {
        retryActionListener = listener
    }

    class PaginationLoadFooterViewHolder(
        private val binding: ItemPaginationListFooterBinding,
        private val actionListener: OnRetryListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadSate: LoadState) {
            with(binding) {
                footerBtn.setIsLoading(loadSate is LoadState.Loading)
                footerBtn.setOnButtonClickListener { actionListener.retry() }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PaginationLoadFooterViewHolder =
        PaginationLoadFooterViewHolder(
            ItemPaginationListFooterBinding.inflate(parent.inflater, parent, false),
            retryListener
        )

    override fun onBindViewHolder(holder: PaginationLoadFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}