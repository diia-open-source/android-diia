package ua.gov.diia.ps_criminal_cert.ui.steps.reason

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ps_criminal_cert.databinding.ItemCriminalCertReasonBinding
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertReasons
import ua.gov.diia.ui_base.util.view.inflater

class CriminalCertReasonsAdapter(
    private val onItemClicked: (CriminalCertReasons.Reason) -> Unit
) : ListAdapter<CriminalCertReasons.Reason, CriminalCertReasonsAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemCriminalCertReasonBinding.inflate(parent.inflater, parent, false),
            onItemClicked = {
                onItemClicked(getItem(it))
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.run(holder::bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach { list ->
                (list as List<String>).forEach { payload ->
                    if (payload == PAYLOAD_IS_SELECTED) {
                        getItem(position)?.also {
                            holder.setSelected(it.isSelected)
                        }
                    }
                }
            }
        }
    }

    class ViewHolder(
        private val binding: ItemCriminalCertReasonBinding,
        private val onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(reason: CriminalCertReasons.Reason) {
            setSelected(reason.isSelected)
            binding.nameTv.text = reason.name
        }

        fun setSelected(isSelected: Boolean) {
            binding.radioBtn.isChecked = isSelected
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CriminalCertReasons.Reason>() {
        override fun areItemsTheSame(
            oldItem: CriminalCertReasons.Reason,
            newItem: CriminalCertReasons.Reason,
        ): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(
            oldItem: CriminalCertReasons.Reason,
            newItem: CriminalCertReasons.Reason,
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CriminalCertReasons.Reason, newItem: CriminalCertReasons.Reason): Any {
            val payloads = mutableListOf<String>()
            if (oldItem.isSelected != newItem.isSelected) {
                payloads.add(PAYLOAD_IS_SELECTED)
            }
            return payloads
        }

        const val PAYLOAD_IS_SELECTED = "PAYLOAD_IS_SELECTED"
    }
}