package ua.gov.diia.ps_criminal_cert.ui.details

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.databinding.ItemCriminalCertLoadActionBinding
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType.DOWNLOAD_ARCHIVE
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType.VIEW_PDF
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails
import ua.gov.diia.ui_base.util.view.inflater

class CriminalCertLoadActionsAdapter(
    private val onItemClick: (CriminalCertDetails.LoadAction) -> Unit
) : ListAdapter<CriminalCertDetails.LoadAction, CriminalCertLoadActionsAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            binding = ItemCriminalCertLoadActionBinding.inflate(parent.inflater, parent, false),
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
        private val binding: ItemCriminalCertLoadActionBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(action: CriminalCertDetails.LoadAction) {
            with(binding) {
                val iconRes = when(action.type) {
                    DOWNLOAD_ARCHIVE -> R.drawable.ic_download
                    VIEW_PDF -> R.drawable.ic_view
                }
                btn.setIcon(iconRes)
                btn.setTitle(action.name)
                setLoading(action.isLoading)
                setEnabled(action.isEnabled)
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

    object DiffCallback : DiffUtil.ItemCallback<CriminalCertDetails.LoadAction>() {
        override fun areItemsTheSame(
            oldItem: CriminalCertDetails.LoadAction,
            newItem: CriminalCertDetails.LoadAction
        ): Boolean =
            oldItem.type == newItem.type

        override fun areContentsTheSame(
            oldItem: CriminalCertDetails.LoadAction,
            newItem: CriminalCertDetails.LoadAction
        ): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: CriminalCertDetails.LoadAction, newItem: CriminalCertDetails.LoadAction): Any {
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