package ua.gov.diia.ps_criminal_cert.ui.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ps_criminal_cert.databinding.ItemCriminalCertBinding
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertListData.CertItem
import ua.gov.diia.ui_base.util.view.inflater

class CriminalCertListAdapter(
    private val onItemSelected: (CertItem) -> Unit
) : PagingDataAdapter<CertItem, CriminalCertListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemCriminalCertBinding.inflate(parent.inflater, parent, false),
            onItemSelected = onItemSelected
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.run(holder::bind)
    }

    class ViewHolder(
        val binding: ItemCriminalCertBinding,
        private val onItemSelected: ((CertItem) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(caseItem: CertItem) {
            binding.item = caseItem
            binding.root.setOnClickListener { onItemSelected?.invoke(caseItem) }

            binding.executePendingBindings()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CertItem>() {
        override fun areItemsTheSame(oldItem: CertItem, newItem: CertItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CertItem, newItem: CertItem): Boolean {
            return oldItem == newItem
        }
    }
}