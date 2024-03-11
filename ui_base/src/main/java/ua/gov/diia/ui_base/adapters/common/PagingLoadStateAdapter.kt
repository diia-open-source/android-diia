package ua.gov.diia.ui_base.adapters.common

import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ui_base.adapters.common.PagingLoadStateAdapter.ViewHolder
import ua.gov.diia.ui_base.databinding.ItemLoadingFooterBinding
import ua.gov.diia.ui_base.util.view.inflater

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewHolder =
        ViewHolder(
            binding = ItemLoadingFooterBinding.inflate(parent.inflater, parent, false),
            retry = retry
        )

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ViewHolder(
        private val binding: ItemLoadingFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressBar.isInvisible = loadState !is Loading
                retryBtn.isInvisible = loadState !is Error
                errorTv.isInvisible = loadState !is Error
            }
        }
    }
}